package com.hexcore.cas.control.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;

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
	private ArrayList<CAPMessageProtocol> clients = null;
	private ArrayList<String> clientNames = null;
	private ArrayList<ThreadWork> workDoneByClients = null;
	private ArrayList<ThreadWork> workSentToClients = null;
	private volatile boolean toReset = false;
	private volatile boolean isReset = false;
	private boolean[] clientsConnected = null;
	private boolean[] receivedAccept = null;
	private int gridsDone = 0;
	private int numOfClients = 0;
	private int totalGrids = -1;
	private int[] clientCoreAmounts = null;
	private LinkedBlockingQueue<ThreadWork> workForClients = null;
	private ServerOverseer parent = null;
	private static final String TAG = "Server";

	public CAPIPServer(Overseer o, ArrayList<String> names)
	{
		super();
		clients = new ArrayList<CAPMessageProtocol>();
		clientNames = new ArrayList<String>();
		for(int i = 0; i < names.size(); i++)
			clientNames.add(names.get(i));
		workDoneByClients = new ArrayList<ThreadWork>();
		workSentToClients = new ArrayList<ThreadWork>();
		numOfClients = clientNames.size();
		clientsConnected = new boolean[numOfClients];
		receivedAccept = new boolean[numOfClients];
		clientCoreAmounts = new int[numOfClients];
		workForClients = new LinkedBlockingQueue<ThreadWork>();
		parent = (ServerOverseer)o;
		
		for(int i = 0; i < numOfClients; i++)
		{
			clientsConnected[i] = false;
			receivedAccept[i] = false;
			clientCoreAmounts[i] = 0;
		}
	}
	
	@Override
	public void disconnect()
	{
		Log.information(TAG, "Disconnecting clients");
		
		for(int i = 0; i < clients.size(); i++)
			sendDisconnect(i);
		
		for(int i = 0; i < clients.size(); i++)
			clients.get(i).disconnect();
		
		super.disconnect();
	}
	
	public boolean isReset()
	{
		return isReset;
	}
	
	public int getTotalCoreAmount()
	{
		int cores = 0;
		for(int i = 0; i < clientCoreAmounts.length; i++)
			cores += clientCoreAmounts[i];
		return cores;
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
			if(clients.get(i).getSocket().getInetAddress().getHostName().compareTo(host) == 0)
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
				
				ID = ((IntNode)gi.get("ID")).getIntValue();
				
				ThreadWork orig = null;
				int origPos = -1;
				for(int i = 0; i < workSentToClients.size(); i++)
					if(workSentToClients.get(i).getID() == ID)
					{
						orig = workSentToClients.get(i);
						origPos = i;
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
				
				int moreFlag = ((IntNode)gi.get("MORE")).getIntValue();
				if(moreFlag == 1)
					sendAGrid(clients.get(hostPos));
				
				ThreadWork TW = new ThreadWork(grid, area, ID);
				workDoneByClients.add(TW);
				workSentToClients.remove(origPos);
				
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
	
	public void reset()
	{
		toReset = true;
	}
	
	public void sendCode()
	{
		
	}
	
	public void sendConnect(int index)
	{
		CAPMessageProtocol CAPMP = clients.get(index);
		DictNode header = this.makeHeader("CONNECT");
		
		Message msg = new Message(header);
		CAPMP.sendMessage(msg);
	}
	
	public void sendDisconnect(int index)
	{
		CAPMessageProtocol CAPMP = clients.get(index);
		DictNode header = this.makeHeader("DISCONNECT");
		
		Message msg = new Message(header);
		CAPMP.sendMessage(msg);
	}
	
	public void sendAGrid(CAPMessageProtocol MP)
	{
		Log.information(TAG, "Sending grids - follow-up procedure");
		ThreadWork TW = null;
		DictNode header = this.makeHeader("GRID");
		
		if(workForClients.size() != 0)
		{
			TW = workForClients.poll();
		}
		else
		{
			/*if(workSentToClients.size() != 0)
			{
				int len = workSentToClients.size();
				System.out.println("Injecting more work : " + len);
				for(int i = len - 1; i >= 0; i--)
				{
					workForClients.add(workSentToClients.get(i));
					workSentToClients.remove(i);
				}
			}
			else*/
			{
				Log.information(TAG, "No more grid work is available to be sent to clients");
				return;
			}
		}
		
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
		
		Message msg = new Message(header, grid);
		MP.sendMessage(msg);
		
		workSentToClients.add(TW);
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
			CAPMessageProtocol CAPMP = clients.get(i);
			if(sendToAllCores)
			{
				for(int j = 0; j < clientCoreAmounts[i]; j++)
					sendAGrid(CAPMP);
			}
			else
			{
				sendAGrid(CAPMP);
			}
		}
	}
	
	public void sendQuery(int index)
	{
		CAPMessageProtocol CAPMP = clients.get(index);
		DictNode header = this.makeHeader("QUERY");
		
		Message msg = new Message(header);
		CAPMP.sendMessage(msg);
	}
	
	public void setClientWork(ThreadWork[] TW)
	{
		for(int i = 0; i < TW.length; i++)
			workForClients.add(TW[i]);
		totalGrids = TW.length;
	}
	
	public void setup()
	{
		Log.information(TAG, "Setting up");
		for(int i = 0; i < numOfClients; i++)
		{
			try
			{
				Socket clientSocket = new Socket(clientNames.get(i), 3119);
				clients.add(new CAPMessageProtocol(clientSocket));
				clients.get(i).start();
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
				Message message = clients.get(i).waitForMessage();
				if(message != null)
					interpretInput(message, clients.get(i).getSocket().getInetAddress().getHostName());
			}
			if(gridsDone == totalGrids)
			{
				Log.debug(TAG, "Sending work done by clients");
				parent.setClientWork(workDoneByClients);
				gridsDone = 0;
			}
		}
	}
}
