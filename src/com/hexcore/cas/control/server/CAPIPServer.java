package com.hexcore.cas.control.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.hexcore.cas.control.protocol.ByteNode;
import com.hexcore.cas.control.protocol.CAPInformationProcessor;
import com.hexcore.cas.control.protocol.CAPMessageProtocol;
import com.hexcore.cas.control.protocol.DictNode;
import com.hexcore.cas.control.protocol.DoubleNode;
import com.hexcore.cas.control.protocol.IntNode;
import com.hexcore.cas.control.protocol.ListNode;
import com.hexcore.cas.control.protocol.Message;
import com.hexcore.cas.control.protocol.Node;
import com.hexcore.cas.control.protocol.Overseer;
import com.hexcore.cas.math.Recti;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.ThreadWork;
import com.hexcore.cas.utilities.Log;

public class CAPIPServer extends CAPInformationProcessor
{
	private CAPMessageProtocol[] clients = null;
	private String[] clientNames = null;
	private boolean[] clientsConnected = null;
	private boolean[] receivedAccept = null;
	private int currGen = 0;
	private int gridsDone = 0;
	private int numOfClients = 0;
	private int totalGrids = -1;
	private int[] clientCoreAmounts = null;
	private LinkedBlockingQueue<ThreadWork> workForClients = null;
	private Lock lock = null;
	private ServerOverseer parent = null;
	private static final String TAG = "Server";
	private ThreadWork[] workDoneByClients = null;
	private ThreadWork[] workSentToClients = null;

	public CAPIPServer(Overseer o)
	{
		super();
		workForClients = new LinkedBlockingQueue<ThreadWork>();
		parent = (ServerOverseer)o;
		lock = new ReentrantLock();
	}
	
	@Override
	public void disconnect()
	{
		Log.information(TAG, "Disconnecting clients");
		
		for(int i = 0; i < clients.length; i++)
			sendDisconnect(i);
		
		try
		{
			Thread.sleep(3000);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		for(int i = 0; i < clients.length; i++)
			clients[i].disconnect();
		
		super.disconnect();
	}
	
	public int getTotalCoreAmount()
	{
		int cores = 0;
		for(int i = 0; i < clientCoreAmounts.length; i++)
			cores += clientCoreAmounts[i];
		return cores;
	}
	
	public int getConnectedAmount()
	{
		int connectedNum = 0;
		for(int i = 0; i < receivedAccept.length; i++)
			if(receivedAccept[i])
				connectedNum++;
		return connectedNum;
	}
	
	@Override
	protected void interpretInput(Message message)
	{
		Log.error(TAG, "THIS INTERPRETINPUT FUNCTION IN CAPIPSERVER SHOULD NOT BE CALLED");
		return;
	}

	protected void interpretInput(Message message, String host)
	{
		Log.information(TAG, "Interpreting received message");
		int hostPos = -1;
		for(int i = 0; i < numOfClients; i++)
		{
			if(clients[i].getSocket().getInetAddress().getHostName().compareTo(host) == 0)
			{
				hostPos = i;
				break;
			}
			else
			{
				continue;
			}
		}
		
		if(hostPos == -1)
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
				receivedAccept[hostPos] = true;
				TreeMap<String, Node> bodyMap = body.getDictValues();
				if(bodyMap.containsKey("CORES"))
				{
					clientCoreAmounts[hostPos] = ((IntNode)bodyMap.get("CORES")).getIntValue();
				}
				else
				{
					Log.error(TAG, "ACCEPT message type does not have a CORES field");
					return;
				}
				
				if(currGen != 0 && currGen != -1)
				{
					for(int i = 0; i < clientCoreAmounts[hostPos]; i++)
						sendAGrid(hostPos);
				}
			}
			else if(map.get("TYPE").toString().compareTo("REJECT") == 0)
			{
				receivedAccept[hostPos] = false;
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
					parent.setStatus(hostPos, state);
				}
				else
				{
					Log.error(TAG, "STATUS message is missing the STATE field");
					return;
				}
			}
			else if(map.get("TYPE").toString().compareTo("RESULT") == 0)
			{
				if(!receivedAccept[hostPos])
				{
					Log.error(TAG, "ACCEPT message has not been received from client yet");
					return;
				}
				TreeMap<String, Node> gi = body.getDictValues();
				Recti area = null;
				int ID = -1;
				int gen = 0;
				
				if(body == null)
				{
					Log.error(TAG, "RESULT message is missing a body");
					return;
				}
				else if(!body.has("MORE"))
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
				
				ID = ((IntNode)gi.get("ID")).getIntValue();
				
				ThreadWork orig = workSentToClients[ID];
				
				if(orig == null)
				{
					Log.error(TAG, "RESULT message redundant - work already complete.\r\n");
					return;
				}
				
				gen = ((IntNode)gi.get("GENERATION")).getIntValue();
				
				if(gen != currGen)
				{
					Log.error(TAG, "RESULT message considered garbage - generation not the same.");
					return;
				}
				
				area = orig.getWorkableArea();
				Grid grid = orig.getGrid();
				
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
						grid.setCell(x, y, cell);
					}
				}
				
				ThreadWork TW = new ThreadWork(grid, area, ID, gen);
				workDoneByClients[ID] = TW.clone();
				workSentToClients[ID] = null;
				
				int moreFlag = ((IntNode)gi.get("MORE")).getIntValue();
				for(int i = 0; i < moreFlag; i++)
					sendAGrid(hostPos);
				
				gridsDone++;
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
	
	public void sendCode()
	{
		
	}
	
	public void sendConnect(int index)
	{
		CAPMessageProtocol CAPMP = clients[index];
		DictNode header = this.makeHeader("CONNECT");
		
		Message msg = new Message(header);
		CAPMP.sendMessage(msg);
	}
	
	public void sendDisconnect(int index)
	{
		CAPMessageProtocol CAPMP = clients[index];
		DictNode header = this.makeHeader("DISCONNECT");
		
		Message msg = new Message(header);
		CAPMP.sendMessage(msg);
	}
	
	public void sendAGrid(int index)
	{
		CAPMessageProtocol MP = clients[index];
		Log.information(TAG, "Sending grids - follow-up procedure");
		ThreadWork TW = null;
		DictNode header = this.makeHeader("GRID");
		
		lock.lock();
		
		if(workForClients.size() != 0)
		{
			TW = workForClients.poll();
		}
		else
		{
			try
			{
				Thread.sleep(1000);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
			int cntNoRes = 0;
			for(int i = 0; i < workSentToClients.length; i++)
				if(workSentToClients[i] != null && workDoneByClients[i] == null)
					cntNoRes++;
			if(cntNoRes > 0)
			{
				int len = workSentToClients.length;
				for(int i = len - 1; i >= 0; i--)
				{
					if(workSentToClients[i] != null)
					{
						workForClients.add(workSentToClients[i].clone());
						workSentToClients[i] = null;
					}
				}
				if(workForClients.size() != 0)
					TW = workForClients.poll();
				else
				{
					Log.information(TAG, "No more grid work is available to be sent to clients");
					return;
				}
			}
			else
			{
				Log.information(TAG, "No more grid work is available to be sent to clients");
				return;
			}
		}
		
		lock.unlock();
		
		DictNode grid = new DictNode();
		ListNode size = new ListNode();
		size.addToList(new IntNode(TW.getGrid().getSize().x));
		size.addToList(new IntNode(TW.getGrid().getSize().y));
		grid.addToDict("SIZE", size);
		
		ListNode area = new ListNode();
		area.addToList(new IntNode(TW.getWorkableArea().getPosition().x));
		area.addToList(new IntNode(TW.getWorkableArea().getPosition().y));
		area.addToList(new IntNode(TW.getWorkableArea().getSize().x));
		area.addToList(new IntNode(TW.getWorkableArea().getSize().y));
		grid.addToDict("AREA", area);
		
		grid.addToDict("PROPERTIES", new IntNode(TW.getGrid().getCell(0, 0).getValueCount()));
		
		char[] c = new char[1];
		c[0] = TW.getGrid().getType();
		grid.addToDict("GRIDTYPE", new ByteNode(new String(c)));
		
		ListNode rows = new ListNode();
		for(int y = 0; y < TW.getGrid().getHeight(); y++)
		{
			ListNode currRow = new ListNode(); 
			for(int x = 0; x < TW.getGrid().getWidth(); x++)
			{
				ListNode currCell = new ListNode();
				for(int j = 0; j < TW.getGrid().getCell(x, y).getValueCount(); j++)
				{
					currCell.addToList(new DoubleNode(TW.getGrid().getCell(x, y).getValue(j)));
				}
				currRow.addToList(currCell);
			}
			rows.addToList(currRow);
		}
		grid.addToDict("DATA", rows);
		
		IntNode ID = new IntNode(TW.getID());
		grid.addToDict("ID", ID);
		
		IntNode genNum = new IntNode(currGen);
		grid.addToDict("GENERATION", genNum);
		
		Message msg = new Message(header, grid);
		MP.sendMessage(msg);
		
		workSentToClients[TW.getID()] = TW.clone();
	}
	
	public void sendGrids()
	{
		Log.information(TAG, "Sending grids - initial procedure");
		int numOfAllCores = 0;
		for(int i = 0; i < clientCoreAmounts.length; i++)
			numOfAllCores += clientCoreAmounts[i];
		boolean sendToAllCores = false;
		if(workForClients.size() >= numOfAllCores)
			sendToAllCores = true;
		
		for(int i = 0; i < numOfClients; i++)
		{
			if(sendToAllCores)
				for(int j = 0; j < clientCoreAmounts[i]; j++)
					sendAGrid(i);
			else
				sendAGrid(i);
		}
	}
	
	public void sendQuery(int index)
	{
		CAPMessageProtocol CAPMP = clients[index];
		DictNode header = this.makeHeader("QUERY");
		
		Message msg = new Message(header);
		CAPMP.sendMessage(msg);
	}
	
	public void setClientNames(String[] names)
	{
		int size = names.length;
		if(clientNames == null)
		{
			clientNames = new String[size];
			for(int i = 0; i < size; i++)
				clientNames[i] = names[i];
			numOfClients = size;
			clients = new CAPMessageProtocol[numOfClients];
			clientsConnected = new boolean[numOfClients];
			receivedAccept = new boolean[numOfClients];
			clientCoreAmounts = new int[numOfClients];
			
			for(int i = 0; i < numOfClients; i++)
			{
				clientsConnected[i] = false;
				receivedAccept[i] = false;
				clientCoreAmounts[i] = 0;
			}
		}
		else
		{
			int currSize = clientNames.length;
			int newSize = names.length;
			ArrayList<ClientInfo> tmp = new ArrayList<ClientInfo>();
			ArrayList<String> tmpClientNames = new ArrayList<String>();
			for(int i = 0; i < currSize; i++)
				tmpClientNames.add(clientNames[i]);
			
			for(int i = 0; i < newSize; i++)
			{
				if(tmpClientNames.contains(names[i]))
				{
					int pos = tmpClientNames.indexOf(names[i]);
					tmp.add(new ClientInfo(clientNames[pos], clients[pos], clientsConnected[pos], receivedAccept[pos], clientCoreAmounts[pos]));
				}
				else
				{
					tmp.add(new ClientInfo(names[i], null, false, false, 0));
				}
			}

			clientNames = new String[newSize];
			numOfClients = newSize;
			clients = new CAPMessageProtocol[numOfClients];
			clientsConnected = new boolean[numOfClients];
			receivedAccept = new boolean[numOfClients];
			clientCoreAmounts = new int[numOfClients];
			
			for(int i = 0; i < newSize; i++)
			{
				clientNames[i] = tmp.get(i).name;
				clients[i] = tmp.get(i).mp;
				clientsConnected[i] = tmp.get(i).connected;
				receivedAccept[i] = tmp.get(i).accepted;
				clientCoreAmounts[i] = tmp.get(i).cores;
			}
			
			for(int i = 0; i < newSize; i++)
			{
				if(clientsConnected[i] == false)
				{
					try
					{
						Socket clientSocket = new Socket(clientNames[i], 3119);
						clients[i] = new CAPMessageProtocol(clientSocket);
						clients[i].start();
						clientsConnected[i] = true;
					}
					catch(IOException e)
					{
						Log.error(TAG, "Error making protocol after client additions/deletions");
						e.printStackTrace();
					}
					
					try
					{
						Thread.sleep(3000);
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
					
					sendConnect(i);
				}
				else
					continue;
			}
		}
	}
	
	public void setClientWork(ThreadWork[] TW, int cG)
	{
		currGen = cG;
		gridsDone = 0;
		for(int i = 0; i < TW.length; i++)
			workForClients.add(TW[i]);
		totalGrids = TW.length;
		workDoneByClients = new ThreadWork[TW.length];
		workSentToClients = new ThreadWork[TW.length];
		for(int i = 0; i < TW.length; i++)
		{
			workDoneByClients[i] = null;
			workSentToClients[i] = null;
		}
	}
	
	public void setGeneration(int cG)
	{
		currGen = cG;
		gridsDone = 0;
	}
	
	public void setup()
	{
		Log.information(TAG, "Setting up");
		for(int i = 0; i < numOfClients; i++)
		{
			try
			{
				Socket clientSocket = new Socket(clientNames[i], 3119);
				clients[i] = new CAPMessageProtocol(clientSocket);
				clients[i].start();
				clientsConnected[i] = true;
			}
			catch(IOException e)
			{
				Log.error(TAG, "Error making protocols");
				e.printStackTrace();
			}
		}
		
		try
		{
			Thread.sleep(3000);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		for(int i = 0; i < numOfClients; i++)
			sendConnect(i);
	}

	@Override
	public void run()
	{
		setup();
		
		Log.information(TAG, "Now running");
		
		running = true;
		while(running)
		{
			for(int i = 0; i < numOfClients; i++)
			{
				Message message = clients[i].waitForMessage();
				if(message != null)
				{
					System.out.println("GOT: " + message.toString());
					interpretInput(message, clients[i].getSocket().getInetAddress().getHostName());
				}
			}
			if(gridsDone == totalGrids)
			{
				Log.debug(TAG, "Sending work done by clients");
				ArrayList<ThreadWork> workTmp = new ArrayList<ThreadWork>();
				for(int i = 0; i < workDoneByClients.length; i++)
				{
					if(workDoneByClients[i] == null)
					{
						System.out.println("workDoneByClients[" + i + "] == null");
						continue;
					}
					workTmp.add(workDoneByClients[i].clone());
				}
				parent.setClientWork(workTmp);
				gridsDone = 0;
			}
		}
	}
	
	private class ClientInfo
	{
		String name = null;
		CAPMessageProtocol mp = null;
		boolean connected = false;
		boolean accepted = false;
		int cores = 0;
		
		public ClientInfo(String n, CAPMessageProtocol m, boolean c, boolean a, int co)
		{
			name = n;
			mp = m;
			connected = c;
			accepted = a;
			cores = co;
		}
	}
}
