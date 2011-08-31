package com.hexcore.cas.control.protocol;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.hexcore.cas.control.client.Overseer;
import com.hexcore.cas.control.client.ServerOverseer;
import com.hexcore.cas.math.Recti;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.HexagonGrid;
import com.hexcore.cas.model.RectangleGrid;
import com.hexcore.cas.model.TriangleGrid;

public class CAPIPServer extends CAPInformationProcessor
{
	private ArrayList<CAPMessageProtocol> clients = null;
	private boolean[] receivedAccept = null;
	private ServerOverseer parent = null;
	private int numOfClients = 0;
	private ArrayList<String> clientNames = null;
	private Recti[] clientWorkables = null;
	private Grid[] clientGrids = null;
	private boolean[] clientsConnected = null;
	private int gridsDone = 0;
	
	public CAPIPServer(Overseer o, ArrayList<String> names)
	{
		super();
		parent = (ServerOverseer)o;
		clients = new ArrayList<CAPMessageProtocol>();
		clientNames = names;
		numOfClients = clientNames.size();
		receivedAccept = new boolean[numOfClients];
		clientWorkables = new Recti[numOfClients];
		clientGrids = new Grid[numOfClients];
		clientsConnected = new boolean[numOfClients];
		for(int i = 0; i < numOfClients; i++)
		{
			receivedAccept[i] = false;
			clientWorkables[i] = null;
			clientGrids[i] = null;
			clientsConnected[i] = false;
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
				System.out.println("-- not sure how to handle accept yet --");
				receivedAccept[hostPos] = true;
			}
			else if(map.get("TYPE").toString().compareTo("REJECT") == 0)
			{
				System.out.println("-- not sure how to handle reject yet --");
				receivedAccept[hostPos] = false;
			}
			else if(map.get("TYPE").toString().compareTo("STATUS") == 0)
			{
				System.out.println("-- not sure how to handle status yet --");
			}
			else if(map.get("TYPE").toString().compareTo("RESULT") == 0)
			{
				System.out.println("-- not sure how to handle result EXACTLY yet --");
				if(!receivedAccept[hostPos])
				{
					System.out.println("ACCEPT MESSAGE HAS NOT BEEN RECEIVED YET");
					return;
				}
				TreeMap<String, Node> gi = body.getDictValues();
				Vector2i size = null;
				
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
				
				Grid grid = clientGrids[hostPos];//makeGrid(clientWorkables[hostPos]);
				if(gi.containsKey("DATA"))
				{
					ArrayList<Node> rows = ((ListNode)gi.get("DATA")).getListValues();
					for(int y = 0; y < rows.size(); y++)
					{
						ArrayList<Node> currRow = ((ListNode)rows.get(y)).getListValues(); 
						for(int x = 0; x < currRow.size(); x++)
						{
							ArrayList<Node> currCell = ((ListNode)currRow.get(x)).getListValues();
							for(int i = 0; i < currCell.size(); i++)
							{
								grid.getCell(x, y).setValue(i, ((DoubleNode)currCell.get(i)).getDoubleValue());
							}
						}
					}
				}
				else
				{
					System.out.println("GRID DATA MISSING");
					return;
				}
				clientGrids[hostPos] = grid;
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
	
	public void setClientGrids(Grid[] grids)
	{
		clientGrids = grids.clone();
	}
	
	public void setClientWorkables(Recti[] works)
	{
		clientWorkables = works.clone();
	}
	
	@Override
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
		}
	}
	
	@Override
	public void run()
	{
		for(int i = 0; i < numOfClients; i++)
			if(!clientsConnected[i])
				return;
		
		System.out.println("Running...");
		
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
			if(gridsDone == numOfClients)
			{
				//parent.setClientWorkables(clientWorkables);
				parent.setClientGrids(clientGrids);
				gridsDone = 0;
			}
		}
	}
}
