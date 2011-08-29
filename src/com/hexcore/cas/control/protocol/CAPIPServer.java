package com.hexcore.cas.control.protocol;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.hexcore.cas.control.client.Overseer;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Grid;

public class CAPIPServer extends CAPInformationProcessor
{
	private ArrayList<CAPMessageProtocol> clients = null;
	private boolean receivedAccept = false;
	private Overseer parent = null;
	private int numOfClients = 0;
	
	public CAPIPServer(Overseer o, int n)
	{
		super();
		parent = o;
		clients = new ArrayList<CAPMessageProtocol>();
		numOfClients = n;
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
		DictNode header = message.getHeader();
		DictNode body = (DictNode)message.getBody();
		
		Map<String, Node> map = header.getDictValues();
		if(map.containsKey("TYPE"))
		{
			if(map.get("TYPE").toString().compareTo("ACCEPT") == 0)
			{
				System.out.println("-- not sure how to handle accept yet --");
				receivedAccept = true;
			}
			else if(map.get("TYPE").toString().compareTo("REJECT") == 0)
			{
				System.out.println("-- not sure how to handle reject yet --");
				receivedAccept = false;
			}
			else if(map.get("TYPE").toString().compareTo("STATUS") == 0)
			{
				System.out.println("-- not sure how to handle status yet --");
			}
			else if(map.get("TYPE").toString().compareTo("RESULT") == 0)
			{
				System.out.println("-- not sure how to handle result EXACTLY yet --");
				if(!receivedAccept)
				{
					sendState(2, "ACCEPT MESSAGE HAS NOT BEEN RECEIVED YET");
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
					sendState(2, "GRID MISSING A SIZE");
					return;
				}
				
				Grid grid = null;//THIS MUST CHANGE
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
					sendState(2, "GRID DATA MISSING");
					return;
				}
				/*
				parent.setGrid(grid);
				parent.setWorkable(area);*/
			}
			else
			{
				sendState(2, "MESSAGE TYPE NOT RECOGNISED");
				return;
			}
		}
		else
		{
			sendState(2, "MESSAGE TYPE NOT FOUND");
			return;
		}
	}
	
	@Override
	public void start()
	{
		int startPort = 50000;
		for(int i = startPort; i < startPort + numOfClients; i++)
		{
			try
			{
				Socket clientSocket = new Socket(InetAddress.getLocalHost().getHostName(), i);
				protocol = new CAPMessageProtocol(clientSocket);
			}
			catch(IOException e)
			{
				System.out.println("Error making protocols");
				e.printStackTrace();
			}
		}
	}
}
