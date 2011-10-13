package com.hexcore.cas.control.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.hexcore.cas.control.protocol.ByteNode;
import com.hexcore.cas.control.protocol.CAPMessageProtocol;
import com.hexcore.cas.control.protocol.DictNode;
import com.hexcore.cas.control.protocol.DoubleNode;
import com.hexcore.cas.control.protocol.IntNode;
import com.hexcore.cas.control.protocol.ListNode;
import com.hexcore.cas.control.protocol.Message;
import com.hexcore.cas.control.protocol.Node;
import com.hexcore.cas.math.Recti;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.utilities.Log;

public class CAPIPServer
{
	private static final String TAG = "Server";
	private final static int PROTOCOL_VERSION = 1;
	
	private ArrayList<ClientInfo> clients = null;
	
	private boolean running = false;
	
	private Simulator parent = null;
	private int currGen = 0;
	private int gridsDone = 0;
	private int totalGrids = -1;
	private LinkedBlockingQueue<ThreadWork> workForClients = null;
	private Map<Integer, ThreadWork> sentWork;

	private Grid currentGrid;
	private Lock workLock;
		
	private int clientPort;

	public CAPIPServer(Simulator simulator, int clientPort)
	{
		super();
		this.clientPort = clientPort;
		
		this.clients = new ArrayList<ClientInfo>();
				
		workForClients = new LinkedBlockingQueue<ThreadWork>();
		sentWork = new HashMap<Integer, ThreadWork>();
		
		parent = simulator;
		
		workLock = new ReentrantLock();
	}
	
	protected DictNode makeHeader(String type)
	{
		DictNode header = new DictNode();
		header.addToDict("TYPE", new ByteNode(type));
		header.addToDict("VERSION", new IntNode(PROTOCOL_VERSION));
		return header;
	}
	
	public void disconnect()
	{
		Log.information(TAG, "Disconnecting clients");
		running = false;
		for (ClientInfo client : clients) client.disconnect();
	}
	
	public int getTotalCoreAmount()
	{
		int cores = 0;
		for (ClientInfo client : clients)
			cores += client.cores;
		return cores;
	}
	
	public int getConnectedAmount()
	{
		int connectedNum = 0;
		for (ClientInfo client : clients)
			if(client.accepted)
				connectedNum++;
		return connectedNum;
	}

	protected void interpretInput(Message message, String host)
	{
		Log.information(TAG, "Interpreting received message");
		
		ClientInfo fromClient = null;
		for (ClientInfo client : clients)
			if (client.protocol.getSocket().getInetAddress().getHostName().compareTo(host) == 0)
			{
				fromClient = client;
				break;
			}
		
		if (fromClient == null)
		{
			Log.error(TAG, "Host name was not found as a client");
			return;
		}
		
		DictNode header = message.getHeader();
		DictNode body = (DictNode)message.getBody();
		
		Map<String, Node> map = header.getDictValues();
		if(map.containsKey("TYPE"))
		{
			if(map.get("TYPE").toString().compareTo("ACCEPT") == 0)
			{
				fromClient.accepted = true;
				
				TreeMap<String, Node> bodyMap = body.getDictValues();
				if(bodyMap.containsKey("CORES"))
				{
					fromClient.cores = ((IntNode)bodyMap.get("CORES")).getIntValue();
				}
				else
				{
					Log.error(TAG, "ACCEPT message type does not have a CORES field");
					return;
				}
				
				if(currGen != 0 && currGen != -1)
				{
					for(int i = 0; i < fromClient.cores; i++)
						sendGrid(fromClient);
				}
			}
			else if(map.get("TYPE").toString().compareTo("REJECT") == 0)
			{
				fromClient.accepted = false;
			}
			else if(map.get("TYPE").toString().compareTo("STATUS") == 0)
			{
				TreeMap<String, Node> bodyMap = body.getDictValues();
				if(bodyMap.containsKey("STATE"))
				{
					int state = ((IntNode)bodyMap.get("STATE")).getIntValue();
					switch(state)
					{
						case 0:
							Log.information(TAG, "Client is idle");
							break;
						case 1:
							Log.information(TAG, "Client is busy");
							break;
						case 2:
							String err = "Client is in error";
							if(bodyMap.containsKey("MSG"))
								err += ": " + ((ByteNode)bodyMap.get("MSG")).toString();
							Log.error(TAG, err);
							break;
						default:
							Log.error(TAG, "STATE value is not valid");
							break;
					}
					fromClient.status = state;
				}
				else
				{
					Log.error(TAG, "STATUS message is missing the STATE field");
					return;
				}
			}
			else if(map.get("TYPE").toString().compareTo("RESULT") == 0)
			{
				if (!fromClient.accepted)
				{
					Log.error(TAG, "ACCEPT message has not been received from client yet");
					return;
				}
				else if (body == null)
				{
					Log.error(TAG, "RESULT message is missing a body");
					return;
				}
								
				if(!body.has("MORE"))
				{
					Log.error(TAG, "RESULT message is missing the MORE field");
					return;
				}
				else if(!body.has("DATA"))
				{
					Log.error(TAG, "RESULT message is missing the DATA field");
					return;
				}
				else if(!body.has("ID"))
				{
					Log.error(TAG, "RESULT message is missing the ID field");
					return;
				}
				else if(!body.has("GENERATION"))
				{
					Log.error(TAG, "RESULT message is missing the GENERATION field");
					return;
				}
				
				TreeMap<String, Node> gi = body.getDictValues();
				Recti area = null;
				int ID = ((IntNode)gi.get("ID")).getIntValue();
				int gen = ((IntNode)gi.get("GENERATION")).getIntValue();
								
				workLock.lock();
				ThreadWork orig = sentWork.get(ID);
				workLock.unlock();
				
				if (orig == null)
				{
					Log.error(TAG, "RESULT message redundant - work already complete.\r\n");
					return;
				}
				else if (gen != currGen)
				{
					Log.error(TAG, "RESULT message considered garbage - generation not the same.");
					return;
				}
				
				area = orig.getWorkableArea();
				Grid grid = orig.getGrid().getType().create(area.getSize(), 1);
				
				ArrayList<Node> rows = ((ListNode)gi.get("DATA")).getListValues();
				for(int y = 0; y < rows.size(); y++)
				{
					ArrayList<Node> currRow = ((ListNode)rows.get(y)).getListValues(); 
					for(int x = 0; x < currRow.size(); x++)
					{
						ArrayList<Node> currCell = ((ListNode)currRow.get(x)).getListValues();
						Cell cell = new Cell(currCell.size());
						for(int i = 0; i < currCell.size(); i++)
						{
							cell.setValue(i, ((DoubleNode)currCell.get(i)).getDoubleValue());
						}
						currentGrid.setCell(orig.getPosition().add(x, y), cell);
					}
				}
				
				workLock.lock();
				sentWork.remove(ID);
				gridsDone++;
				workLock.unlock();

				if(gridsDone == totalGrids)
				{
					parent.finishedGeneration();
					gridsDone = 0;
				}
				else
				{
					int more = ((IntNode)gi.get("MORE")).getIntValue();
					
					for (int i = 0; i < more; i++) sendGrid(fromClient);
				}
			}
			else
			{
				Log.error(TAG, "The message type is not recognised");
				return;
			}
		}
		else
		{
			Log.error(TAG, "The message type field if not found");
			return;
		}
	}
	
	public void sendInitialGrids()
	{
		Log.information(TAG, "Sending grids - initial procedure");
		
		int target = getTotalCoreAmount() + 1;
		int sent = 0;
		
		while (!workForClients.isEmpty() && target > sent)
			for (ClientInfo client : clients)
			{
				sent++;
				sendGrid(client);
			}
	}

	public void sendGrid(ClientInfo client)
	{
		Log.information(TAG, "Sending grids - follow-up procedure");
		
		workLock.lock();
		
		ThreadWork work = null;
		
		if (workForClients.isEmpty())
		{			
			// Resend work that hasn't been completed yet
			long now = System.nanoTime();
			for (ThreadWork curWork : sentWork.values())
				if (now > 30L * 1000000000L + curWork.getStartTime()) // Resend if the result hasn't returned in 10 seconds
				{
					Log.warning(TAG, "Work had to be resent : " + now + " " + curWork.getStartTime());
					work = curWork;
					break;
				}
		}
		else
			work = workForClients.poll();
				
		if (work != null) sendWork(work, client);
		
		workLock.unlock();
	}
	
	public void sendWork(ThreadWork work, ClientInfo client)
	{
		DictNode header = makeHeader("GRID");
		
		DictNode grid = new DictNode();
		ListNode size = new ListNode();
		size.addToList(new IntNode(work.getGrid().getSize().x));
		size.addToList(new IntNode(work.getGrid().getSize().y));
		grid.addToDict("SIZE", size);
		
		ListNode area = new ListNode();
		area.addToList(new IntNode(work.getWorkableArea().getPosition().x));
		area.addToList(new IntNode(work.getWorkableArea().getPosition().y));
		area.addToList(new IntNode(work.getWorkableArea().getSize().x));
		area.addToList(new IntNode(work.getWorkableArea().getSize().y));
		grid.addToDict("AREA", area);
		
		grid.addToDict("PROPERTIES", new IntNode(work.getGrid().getCell(0, 0).getValueCount()));
		
		char[] c = new char[1];
		c[0] = work.getGrid().getTypeSymbol();
		grid.addToDict("GRIDTYPE", new ByteNode(new String(c)));
		
		ListNode rows = new ListNode();
		for(int y = 0; y < work.getGrid().getHeight(); y++)
		{
			ListNode currRow = new ListNode(); 
			for(int x = 0; x < work.getGrid().getWidth(); x++)
			{
				ListNode currCell = new ListNode();
				for(int j = 0; j < work.getGrid().getCell(x, y).getValueCount(); j++)
				{
					currCell.addToList(new DoubleNode(work.getGrid().getCell(x, y).getValue(j)));
				}
				currRow.addToList(currCell);
			}
			rows.addToList(currRow);
		}
		grid.addToDict("DATA", rows);
		
		IntNode ID = new IntNode(work.getID());
		grid.addToDict("ID", ID);
		
		IntNode genNum = new IntNode(currGen);
		grid.addToDict("GENERATION", genNum);
		
		Message msg = new Message(header, grid);
		client.protocol.sendMessage(msg);
		
		workLock.lock();
		
		work.setStartTime(System.nanoTime());
		sentWork.put(work.getID(), work);
		
		workLock.unlock();
	}
		
	public void updateClientStatus()
	{
		for (ClientInfo client : clients)
		{
			DictNode header = this.makeHeader("QUERY");
			Message msg = new Message(header);
			client.protocol.sendMessage(msg);
		}
	}
	
	public void forceConnect(int index)
	{
		System.out.println(clients.size());
		ClientInfo client = clients.get(index);
		DictNode header = makeHeader("CONNECT");
		Message msg = new Message(header);
		client.protocol.sendMessage(msg);
	}
	
	public void connectClients(String[] names)
	{
		for (ClientInfo client : clients) client.disconnect();
				
		clients.clear();
				
		for (String name : names) 
		{
			Log.debug(TAG, "Connecting client");
			ClientInfo client = new ClientInfo(name);			
			client.connect();
			clients.add(client);
		}
		
		System.out.println("Connected: " + clients.size());
	}
	
	public void setClientWork(Grid grid, ThreadWork[] TW, int cG)
	{
		workLock.lock();
		
		workForClients.clear();
		sentWork.clear();
		
		currentGrid = grid;
		
		currGen = cG;
		gridsDone = 0;
		for(int i = 0; i < TW.length; i++)
			workForClients.add(TW[i]);
		totalGrids = TW.length;
				
		workLock.unlock();
	}
	
	public void setGeneration(int cG)
	{
		currGen = cG;
		gridsDone = 0;
	}

	public void start()
	{	
		Log.information(TAG, "Running...");
		running = true;
		
		for (ClientInfo client : clients)
			client.start();
				
		try
		{
			for (ClientInfo client : clients)
				client.join();
		}
		catch(InterruptedException e)
		{
			
		}
	}
	
	private class ClientInfo extends Thread
	{
		String name = null;
		CAPMessageProtocol protocol = null;
		int	status = 0;
		boolean accepted = false;
		boolean running = false;
		int cores = 0;
		
		public ClientInfo(String name)
		{
			super("ClientThread-" + name);
			
			this.name = name;
			this.protocol = null;
			this.accepted = false;
			this.cores = 0;
		}
		
		@Override
		public void run()
		{
			running = true;
			while (running)
			{
				Message message = protocol.waitForMessage();
				if (!protocol.isRunning()) disconnect();
				if (message != null) interpretInput(message, protocol.getSocket().getInetAddress().getHostName());
			}
		}
		
		public void connect()
		{			
			try
			{
				protocol = new CAPMessageProtocol(new Socket(name, clientPort));
				protocol.start();
			}
			catch(IOException e)
			{
				Log.error(TAG, "Error making protocol after client additions/deletions");
				e.printStackTrace();
			}
			
			DictNode header = makeHeader("CONNECT");
			Message msg = new Message(header);
			protocol.sendMessage(msg);
		}
		
		public void disconnect()
		{
			running = false;
			
			DictNode header = makeHeader("DISCONNECT");
			Message msg = new Message(header);
			protocol.sendMessage(msg);
			
			try
			{
				Thread.sleep(1000);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
			protocol.disconnect();
		}
	}
}
