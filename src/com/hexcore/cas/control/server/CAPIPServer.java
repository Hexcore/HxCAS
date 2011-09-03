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
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.HexagonGrid;
import com.hexcore.cas.model.RectangleGrid;
import com.hexcore.cas.model.ThreadWork;
import com.hexcore.cas.model.TriangleGrid;

public class CAPIPServer extends CAPInformationProcessor
{
	private ArrayList<CAPMessageProtocol> clients = null;
	private ArrayList<String> clientNames = null;
	private ArrayList<ThreadWork> workDoneByClients = null;
	private boolean[] clientsConnected = null;
	private boolean[] receivedAccept = null;
	private char gridType = 'X';
	//private Grid[] clientGrids = null;
	private int gridsDone = 0;
	private int numOfClients = 0;
	private int totalGrids = 0;
	private int[] clientCoreAmounts = null;
	private LinkedBlockingQueue<ThreadWork> workForClients = null;
	//private Recti[] clientWorkables = null;
	private ServerOverseer parent = null;

	public CAPIPServer(Overseer o, ArrayList<String> names)
	{
		super();
		clients = new ArrayList<CAPMessageProtocol>();
		clientNames = new ArrayList<String>();
		for(int i = 0; i < names.size(); i++)
			clientNames.add(names.get(i));
		workDoneByClients = new ArrayList<ThreadWork>();
		clientsConnected = new boolean[numOfClients];
		receivedAccept = new boolean[numOfClients];
		numOfClients = clientNames.size();
		clientCoreAmounts = new int[numOfClients];
		workForClients = new LinkedBlockingQueue<ThreadWork>();
		
		parent = (ServerOverseer)o;
		//clientWorkables = new Recti[numOfClients];
		//clientGrids = new Grid[numOfClients];
		for(int i = 0; i < numOfClients; i++)
		{
			clientsConnected[i] = false;
			receivedAccept[i] = false;
			clientCoreAmounts[i] = 0;
			//clientWorkables[i] = null;
			//clientGrids[i] = null;
		}
	}
	
	@Override
	public void disconnect()
	{
		super.disconnect();
		for(int i = 0; i < clients.size(); i++)
			clients.get(i).disconnect();
	}
	
	@Override
	protected void interpretInput(Message message)
	{
		System.out.println("THIS INTERPRETINPUT FUNCTION IN CAPIPSERVER SHOULD NOT BE CALLED!");
		return;
	}

	protected void interpretInput(Message message, String host)
	{
		System.out.println("-- RECEIVED A MESSAGE -- CAPIPSERVER");
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
			System.out.println("HOST NAME NOT FOUND AS A CLIENT");
			return;
		}
		
		DictNode header = message.getHeader();
		DictNode body = (DictNode)message.getBody();
		
		Map<String, Node> map = header.getDictValues();
		if(map.containsKey("TYPE"))
		{
			if(map.get("TYPE").toString().compareTo("ACCEPT") == 0)
			{
				//System.out.println("-- not sure how to handle ACCEPT yet --");
				receivedAccept[hostPos] = true;
				TreeMap<String, Node> bodyMap = body.getDictValues();
				if(bodyMap.containsKey("CORES"))
				{
					clientCoreAmounts[hostPos] = ((IntNode)bodyMap.get("CORES")).getIntValue();
				}
				else
				{
					System.out.println("ACCEPT MESSAGE TYPE DOES NOT HOLD CORE AMOUNT");
					return;
				}
			}
			else if(map.get("TYPE").toString().compareTo("REJECT") == 0)
			{
				//System.out.println("-- not sure how to handle reject yet --");
				receivedAccept[hostPos] = false;
			}
			else if(map.get("TYPE").toString().compareTo("STATUS") == 0)
			{
				//System.out.println("-- not sure how to handle STATUS yet --");
				System.out.println("Status recieved from Client:");
				TreeMap<String, Node> bodyMap = body.getDictValues();
				if(bodyMap.containsKey("STATE"))
				{
					int state = ((IntNode)bodyMap.get("STATE")).getIntValue();
					switch(state)
					{
						case 0:
							System.out.println("CLIENT IS IDLE");
							break;
						case 1:
							System.out.println("CLIENT IS BUSY");
							break;
						case 2:
							System.out.print("CLIENT RAISED AN ERROR");
							if(bodyMap.containsKey("MSG"))
								System.out.println(((ByteNode)bodyMap.get("MSG")).toString());
							break;
						default:
							System.out.println("STATE VALUE NOT VALID");
							break;
					}
				}
				else
				{
					System.out.println("STATUS MESSAGE MISSING STATE");
					return;
				}
			}
			else if(map.get("TYPE").toString().compareTo("RESULT") == 0)
			{
				System.out.println("-- RECEIVED RESULT GRID -- CAIPSERVER");
				if(!receivedAccept[hostPos])
				{
					System.out.println("ACCEPT MESSAGE HAS NOT BEEN RECEIVED YET");
					return;
				}
				TreeMap<String, Node> gi = body.getDictValues();
				Vector2i size = null;
				Recti area = null;
				
				if(gi.containsKey("SIZE"))
				{
					ArrayList<Node> sizeList = ((ListNode)gi.get("SIZE")).getListValues();
					size = new Vector2i(((IntNode)sizeList.get(0)).getIntValue(), ((IntNode)sizeList.get(1)).getIntValue());
				}
				else
				{
					System.out.println("GRID MISSING A SIZE");
					return;
				}
				
				if(gi.containsKey("AREA"))
				{
					ArrayList<Node> areaList = ((ListNode)gi.get("AREA")).getListValues();
					//pos, size
					Vector2i p = new Vector2i(((IntNode)areaList.get(0)).getIntValue(), ((IntNode)areaList.get(1)).getIntValue());
					Vector2i s = new Vector2i(((IntNode)areaList.get(2)).getIntValue(), ((IntNode)areaList.get(3)).getIntValue());
					area = new Recti(p, s);
				}
				else
				{
					System.out.println("GRID MISSING AN AREA");
					return;
				}
				
				/*
				 * 			
				 */
				Grid grid = null;
				switch(gridType)
				{
					case 'h':
					case 'H':
						grid = new HexagonGrid(size);
					case 'r':
					case 'R':
						grid = new RectangleGrid(size);
						break;
					case 't':
					case 'T':
						grid = new TriangleGrid(size);
						break;
				}
				
				if(gi.containsKey("DATA"))
				{
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
								//grid.getCell(x, y).setValue(i, ((DoubleNode)currCell.get(i)).getDoubleValue());
							}
							grid.setCell(x, y, cell);
						}
					}
				}
				else
				{
					System.out.println("GRID DATA MISSING");
					return;
				}
				
				if(gi.containsKey("MORE"))
				{
					ArrayList<Node> moreList = ((ListNode)gi.get("MORE")).getListValues();
					int moreFlag = ((IntNode)moreList.get(0)).getIntValue();
					if(moreFlag == 1)
						sendAGrid(clients.get(hostPos));
				}
				else
				{
					System.out.println("GRID MISSING A MORE FIELD");
					return;
				}
				
				ThreadWork TW = new ThreadWork(grid, area);
				workDoneByClients.add(TW);
				//clientGrids[hostPos] = grid;
				gridsDone++;
			}
			else
			{
				System.out.println("MESSAGE TYPE NOT RECOGNISED");
				return;
			}
		}
		else
		{
			System.out.println("MESSAGE TYPE NOT FOUND");
			return;
		}
	}
	
	public void sendCode()
	{
		
	}
	
	public void sendConnect(int index)
	{
		System.out.println("-- SENDING CONNECT TO " + index + " -- CAPIPSERVER");
		CAPMessageProtocol CAPMP = clients.get(index);
		DictNode header = this.makeHeader("CONNECT");
		
		Message msg = new Message(header);
		CAPMP.sendMessage(msg);
	}
	
	public void sendAGrid(CAPMessageProtocol MP)
	{
		DictNode header = this.makeHeader("GRID");
		
		DictNode grid = new DictNode();
		ListNode size = new ListNode();
		//size.addToList(new IntNode(clientGrids[i].getSize().x));
		//size.addToList(new IntNode(clientGrids[i].getSize().y));
		ThreadWork TW = null;
		if(workForClients.size() != 0)
		{
			TW = workForClients.poll();
		}
		else
		{
			System.out.println("NO THREADWORK FOR CLIENT");
			return;
		}
		size.addToList(new IntNode(TW.getGrid().getSize().x));
		size.addToList(new IntNode(TW.getGrid().getSize().y));
		grid.addToDict("SIZE", size);
		
		ListNode area = new ListNode();
		//area.addToList(new IntNode(clientWorkables[i].getPosition().x));
		//area.addToList(new IntNode(clientWorkables[i].getPosition().y));
		//area.addToList(new IntNode(clientWorkables[i].getSize().x));
		//area.addToList(new IntNode(clientWorkables[i].getSize().y));
		area.addToList(new IntNode(TW.getWorkableArea().getPosition().x));
		area.addToList(new IntNode(TW.getWorkableArea().getPosition().y));
		area.addToList(new IntNode(TW.getWorkableArea().getSize().x));
		area.addToList(new IntNode(TW.getWorkableArea().getSize().y));
		grid.addToDict("AREA", area);
		
		//grid.addToDict("PROPERTIES", new IntNode(clientGrids[i].getCell(0, 0).getValueCount()));
		grid.addToDict("PROPERTIES", new IntNode(TW.getGrid().getCell(0, 0).getValueCount()));
		
		char[] c = new char[1];
		//c[0] = clientGrids[i].getType();
		c[0] = TW.getGrid().getType();
		grid.addToDict("GRIDTYPE", new ByteNode(new String(c)));
		
		ListNode rows = new ListNode();
		//for(int y = 0; y < clientGrids[i].getHeight(); y++)
		for(int y = 0; y < TW.getGrid().getHeight(); y++)
		{
			ListNode currRow = new ListNode(); 
			//for(int x = 0; x < clientGrids[i].getWidth(); x++)
			for(int x = 0; x < TW.getGrid().getWidth(); x++)
			{
				ListNode currCell = new ListNode();
				//for(int j = 0; j < clientGrids[i].getCell(x, y).getValueCount(); j++)
				for(int j = 0; j < TW.getGrid().getCell(x, y).getValueCount(); j++)
				{
					//currCell.addToList(new DoubleNode(clientGrids[i].getCell(x, y).getValue(j)));
					currCell.addToList(new DoubleNode(TW.getGrid().getCell(x, y).getValue(j)));
				}
				currRow.addToList(currCell);
			}
			rows.addToList(currRow);
		}
		grid.addToDict("DATA", rows);
		
		Message msg = new Message(header, grid);
		MP.sendMessage(msg);
	}
	
	public void sendGrids()
	{
		System.out.println("-- SENDING GRIDS TO CLIENTS -- CAPIPSERVER");
		
		for(int i = 0; i < numOfClients; i++)
		{
			System.out.println("-- SENDING TO CLIENT " + i + " --");
			CAPMessageProtocol CAPMP = clients.get(i);
			sendAGrid(CAPMP);
			//MOVED STUFF THAT WAS HERE INTO sendAGrid()
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
		gridType = TW[0].getGrid().getType();
		for(int i = 0; i < TW.length; i++)
			workForClients.add(TW[i]);
		totalGrids = TW.length;
	}
	
	/*
	public void setClientGrids(Grid[] grids)
	{
		clientGrids = new Grid[grids.length];
		for(int i = 0; i < grids.length; i++)
			clientGrids[i] = grids[i].clone();
	}
	
	public void setClientWorkables(Recti[] works)
	{
		clientWorkables = new Recti[works.length];
		for(int i = 0; i < works.length; i++)
			clientWorkables[i] = new Recti(works[i].getPosition(), works[i].getSize());
	}
	 */
	public void setup()
	{
		for(int i = 0; i < numOfClients; i++)
		{
			try
			{
				Socket clientSocket = new Socket(clientNames.get(i), 3119);
				clients.add(new CAPMessageProtocol(clientSocket));
				clientsConnected[i] = true;
			}
			catch(IOException e)
			{
				System.out.println("Error making protocols");
				e.printStackTrace();
			}
			sendConnect(i);
		}
	}

	@Override
	public void run()
	{
		setup();
		
		/*for(int i = 0; i < numOfClients; i++)
			if(!clientsConnected[i])
				return;*/
		
		System.out.println("Server Running...");
		
		running = true;
		
		for(int i = 0; i < numOfClients; i++)
			clients.get(i).start();
		
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
				System.out.println("-- SETTING CLIENTGRIDS -- CAPIPSERVER");
				//parent.setClientGrids(clientGrids);
				parent.setClientWork(workDoneByClients);
				gridsDone = 0;
			}
		}
	}
}
