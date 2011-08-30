package com.hexcore.cas.control.protocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import com.hexcore.cas.control.client.ClientOverseer;
import com.hexcore.cas.control.client.Overseer;
import com.hexcore.cas.math.Recti;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.HexagonGrid;
import com.hexcore.cas.model.RectangleGrid;
import com.hexcore.cas.model.TriangleGrid;

public class CAPIPClient extends CAPInformationProcessor
{
	private ClientOverseer parent = null;
	private ServerSocket sock = null;
	private boolean sentAccept = false;
	private CAPMessageProtocol protocol = null;
	
	public CAPIPClient(Overseer o)
		throws IOException
	{
		super();
		
		System.out.println("Creating Client...");
		
		parent = (ClientOverseer)o;
		try
		{
			sock = new ServerSocket(3119);
			System.out.println("Sock listening on " + sock.getLocalPort());
		}
		catch(IOException ex)
		{
			System.out.println("Error connecting sock to port 3119");
			ex.printStackTrace();
		}
	}
	
	@Override
	public void disconnect()
	{
		super.disconnect();
		try
		{
			sock.close();
		}
		catch(IOException e)
		{
			System.out.println("Error closing ServerSocket");
			e.printStackTrace();
		}
	}

	public void sendGrid(Grid g)
	{
		String sizeStr = "SIZE";
		ListNode sizeNode = new ListNode();
		sizeNode.addToList(new IntNode(g.getWidth()));
		sizeNode.addToList(new IntNode(g.getHeight()));
		
		String dataStr = "DATA";
		ListNode rows = new ListNode();
		for(int y = 0; y < g.getHeight(); y++)
		{
			ListNode currRow = new ListNode(); 
			for(int x = 0; x < g.getWidth(); x++)
			{
				ListNode currCell = new ListNode();
				for(int i = 0; i < g.getCell(x, y).getValueCount(); i++)
					currCell.addToList(new DoubleNode(g.getCell(x, y).getValue(i)));
				currRow.addToList(currCell);
			}
			rows.addToList(currRow);
		}
		
		DictNode d = new DictNode();
		d.addToDict(sizeStr, sizeNode);
		d.addToDict(dataStr, rows);
		sendResult(d);
	}
	
	public void sendResult(DictNode d)
	{
		DictNode body = new DictNode();
		body.addToDict("DATA", d);
		
		Message msg = new Message(makeHeader("RESULT"), body);
		System.out.println("Protocol: " + protocol);
		protocol.sendMessage(msg);
	}
	
	public void sendState(int is)
	{
		DictNode body = new DictNode();
		body.addToDict("STATE", new IntNode(is));
		
		Message msg = new Message(makeHeader("STATUS"), body);
		protocol.sendMessage(msg);
	}
	
	public void sendState(int is, String mess)
	{
		DictNode body = new DictNode();
		body.addToDict("STATE", new IntNode(is));
		body.addToDict("MSG", new ByteNode(mess));
		
		Message msg = new Message(makeHeader("STATUS"), body);
		protocol.sendMessage(msg);
	}

	@Override
	protected void interpretInput(Message message)
	{
		DictNode header = message.getHeader();
		DictNode body = (DictNode)message.getBody();
		
		Map<String, Node> map = header.getDictValues();
		if(map.containsKey("TYPE"))
		{
			if(map.get("TYPE").toString().compareTo("CODE") == 0)
			{
				if(!sentAccept)
				{
					sendState(2, "CONNECT MESSAGE HAS NOT BEEN RECEIVED YET");
					return;
				}
				Map<String, Node> codeInfo = body.getDictValues();
				if(codeInfo.containsKey("DATA"))
				{
					//parent.setRules(((ByteNode)codeInfo.get("DATA")).getByteValues());
					System.out.println("-- not sure how to handle code yet --");
				}
				else
				{
					sendState(2, "DATA MISSING FOR CODE MESSAGE TYPE");
					return;
				}
			}
			else if(map.get("TYPE").toString().compareTo("CONNECT") == 0)
			{
				if(sentAccept)
				{
					sendState(2, "CONNECT MESSAGE HAS ALREADY BEEN RECEIVED");
					return;
				}
				if(map.containsKey("VERSION"))
				{
					if(PROTOCOL_VERSION == ((IntNode)map.get("VERSION")).getIntValue())
					{
						DictNode h = new DictNode();
						h.addToDict("TYPE", new ByteNode("ACCEPT"));
						h.addToDict("VERSION", new IntNode(PROTOCOL_VERSION));
						
						DictNode b = new DictNode();
						Message msg = new Message(h, b);
						protocol.sendMessage(msg);
						sentAccept = true;
					}
					else
					{
						DictNode h = new DictNode();
						h.addToDict("TYPE", new ByteNode("REJECT"));
						h.addToDict("VERSION", new IntNode(PROTOCOL_VERSION));
						
						DictNode b = new DictNode();
						b.addToDict("MSG", new ByteNode("VERSIONS INCOMPATIBLE"));
						Message msg = new Message(h, b);
						protocol.sendMessage(msg);
					}
				}
				else
				{
					sendState(2, "VERSION MISSING");
					return;
				}
			}
			else if(map.get("TYPE").toString().compareTo("DISCONNECT") == 0)
			{
				if(!sentAccept)
				{
					sendState(2, "CONNECT MESSAGE HAS NOT BEEN RECEIVED YET");
					return;
				}
				protocol.disconnect();
				sentAccept = false;
			}
			else if(map.get("TYPE").toString().compareTo("GRID") == 0)
			{
				if(!sentAccept)
				{
					sendState(2, "CONNECT MESSAGE HAS NOT BEEN RECEIVED YET");
					return;
				}
				Map<String, Node> gi = body.getDictValues();
				Vector2i size = null;
				Recti area = null;
				int n = -1;
				char type = 'X';
				Grid grid = null;
				
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
				
				if(gi.containsKey("AREA"))
				{
					ArrayList<Node> sizeList = ((ListNode)gi.get("AREA")).getListValues();
					area = new Recti(new Vector2i(((IntNode)sizeList.get(2)).getIntValue(), ((IntNode)sizeList.get(3)).getIntValue()), size);
				}
				else
				{
					sendState(2, "GRID MISSING AN AREA");
					return;
				}
				
				if(gi.containsKey("PROPERTIES"))
				{
					n = ((IntNode)gi.get("PROPERTIES")).getIntValue();
				}
				else
				{
					sendState(2, "GRID MISSING THE PROPERTY AMOUNT");
					return;
				}
				
				if(gi.containsKey("GRIDTYPE"))
				{
					type = gi.get("GRIDTYPE").toString().charAt(0);
				}
				else
				{
					sendState(2, "GRID MISSING THE GRID TYPE");
					return;
				}
				
				switch(type)
				{
					case 'h':
					case 'H':
						grid = new HexagonGrid(size, new Cell(n));
						break;
					case 't':
					case 'T':
						grid = new TriangleGrid(size, new Cell(n));
						break;
					case 'r':
					case 'R':
						grid = new RectangleGrid(size, new Cell(n));
						break;
					default:
						sendState(2, "GRID TYPE INVALID");
						return;
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
				
				parent.setGrid(grid);
				parent.setWorkable(area);
			}
			else if(map.get("TYPE").toString().compareTo("QUERY") == 0)
			{
				if(!sentAccept)
				{
					sendState(2, "CONNECT MESSAGE HAS NOT BEEN RECEIVED YET");
					return;
				}
				sendState(parent.checkState());
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
	public void setup()
	{
		System.out.println("Waiting for server...");
		
		try
		{
			Socket clientSocket = sock.accept();
			protocol = new CAPMessageProtocol(clientSocket);
			connected = true;
		}
		catch(IOException e)
		{
			System.out.println("Error making protocol");
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		if (!connected) return;
		
		System.out.println("Running...");
		
		running = true;
		
		protocol.start();
		
		while(running)
		{
			Message message = protocol.waitForMessage();
			if (message != null)
				interpretInput(message);
		}
		
		protocol.disconnect();
	}
}
