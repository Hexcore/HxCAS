package com.hexcore.cas.control.protocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

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
	private Overseer parent = null;
	private ServerSocket sock = null;
	private boolean sentAccept = false;
	
	public CAPIPClient(Overseer o)
		throws IOException
	{
		super();
		
		System.out.println("Creating Client...");
		
		parent = o;
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
}
