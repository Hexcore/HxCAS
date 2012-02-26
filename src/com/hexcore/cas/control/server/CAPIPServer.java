package com.hexcore.cas.control.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.utilities.Log;

public class CAPIPServer
{
	private static final String TAG = "Server";
	private final static int PROTOCOL_VERSION = 1;
	
	private List<ClientInfo> clients = null;
	private Lock clientListLock;
		
	private Simulator parent = null;
	private int currGen = 0;
	private int gridsDone = 0;
	private int totalGrids = -1;
	private LinkedBlockingQueue<ThreadWork> workQueue = null;

	private Grid currentGrid;
	private Lock workLock;
		
	private int clientPort;

	public CAPIPServer(Simulator simulator, int clientPort)
	{
		super();
		this.clientPort = clientPort;
		this.clients = new ArrayList<ClientInfo>();
				
		workQueue = new LinkedBlockingQueue<ThreadWork>();
		
		parent = simulator;
		
		clientListLock = new ReentrantLock();
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
		
		clientListLock.lock();
		for (ClientInfo client : clients) client.disconnect();
		clientListLock.unlock();
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
				int ID = ((IntNode)gi.get("ID")).getIntValue();
				int gen = ((IntNode)gi.get("GENERATION")).getIntValue();
								
				workLock.lock();
				ThreadWork orig = fromClient.sentWork.get(ID);
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
				fromClient.sentWork.remove(ID);
				gridsDone++;
				workLock.unlock();

				if(gridsDone == totalGrids)
				{
					parent.finishedGeneration();
					gridsDone = 0;
				}
				else
				{
					int more = ((IntNode)gi.get("MORE")).getIntValue() * 2;
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
		sendGrids(target);
	}	
	
	public void sendGrids(int target)
	{
		int sent = 0;
		
		while (!workQueue.isEmpty() && target > sent)
			for (ClientInfo client : clients)
			{
				if (!client.accepted) continue;
				
				sent++;
				sendGrid(client);
			}
	}

	public void sendGrid(ClientInfo client)
	{
		Log.information(TAG, "Sending grids - follow-up procedure");
		
		workLock.lock();
		
		ThreadWork work = null;
		
		if (!workQueue.isEmpty())
			work = workQueue.poll();
				
		if (work != null) 
			sendWork(work, client);
		
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
		client.sentWork.put(work.getID(), work);
		
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
	
	public void connectClients(List<InetSocketAddress> addresses)
	{
		for (ClientInfo client : clients) 
			client.disconnect();
				
		clients.clear();
				
		for (InetSocketAddress address : addresses) 
		{
			Log.debug(TAG, "Connecting client");
			ClientInfo client = new ClientInfo(address);			
			boolean result = client.connect();
			if(result)
				clients.add(client);
		}
		
		System.out.println("Connected: " + clients.size());
	}
	
	public void setClientWork(Grid grid, ThreadWork[] TW, int cG)
	{
		workLock.lock();
		
		workQueue.clear();
		
		for (ClientInfo client : clients) 
			client.sentWork.clear();
		
		currentGrid = grid;
		
		currGen = cG;
		gridsDone = 0;
		for(int i = 0; i < TW.length; i++)
			workQueue.add(TW[i]);
		totalGrids = TW.length;
				
		workLock.unlock();
	}
	
	public void setGeneration(int cG)
	{
		currGen = cG;
		gridsDone = 0;
	}

	public void sendByteCode(byte[] bytes)
	{
		Log.information(TAG, "Sending bytecode...");
		for (ClientInfo client : clients)
			client.sendByteCode(bytes);
	}
	
	public void start()
	{	
		Log.information(TAG, "Running...");
		
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
	
	private void clientDisconnect(ClientInfo client)
	{
		System.out.println("Client has disconnected: " + client.address.getHostName());

		int resendAmount = client.sentWork.size(); 
		
		try
		{
			for (ThreadWork work : client.sentWork.values())
				workQueue.put(work);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		sendGrids(resendAmount);
	}
	
	private class ClientInfo extends Thread
	{
		InetSocketAddress address = null;
		CAPMessageProtocol protocol = null;
		
		@SuppressWarnings("unused")
		int	status = 0;
		
		boolean accepted = false;
		boolean running = false;
		int cores = 0;
		
		private Map<Integer, ThreadWork> sentWork;
		
		public ClientInfo(InetSocketAddress address)
		{
			super("ClientThread-" + address.getHostName());
			
			this.address = address;
			this.protocol = null;
			this.accepted = false;
			this.cores = 0;
			this.sentWork = new HashMap<Integer, ThreadWork>();
		}
		
		@Override
		public void run()
		{
			running = true;
			while (running)
			{
				Message message = protocol.waitForMessage();
				if (!protocol.isRunning())
				{
					disconnect();
					clientDisconnect(this);
				}
				if (message != null) interpretInput(message, protocol.getSocket().getInetAddress().getHostName());
			}
		}
		
		public boolean connect()
		{			
			try
			{
				Log.information(TAG, "Connecting to: " + address.getHostName() + ":" + clientPort);
				protocol = new CAPMessageProtocol(new Socket(address.getHostName(), clientPort));
				protocol.start();
				
				DictNode header = makeHeader("CONNECT");
				Message msg = new Message(header);
				protocol.sendMessage(msg);
				return true;
			}
			catch(IOException e)
			{
				Log.error(TAG, "Error making protocol after client additions/deletions");
				e.printStackTrace();
				return false;
			}
		}
		
		public void disconnect()
		{
			running = false;
			accepted = false;
			
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
		
		public void sendByteCode(byte[] bytes)
		{
			DictNode header = makeHeader("CODE");
			DictNode body = new DictNode();
			body.addToDict("DATA", new ByteNode(bytes));
			Message msg = new Message(header, body);
			protocol.sendMessage(msg);	
		}
	}
}
