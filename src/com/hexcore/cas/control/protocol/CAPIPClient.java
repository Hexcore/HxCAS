package com.hexcore.cas.control.protocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

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
	private ServerSocket sock = new ServerSocket(3119);
	private CAPInterface inter = null;
	private Overseer parent = null;
	private LinkedBlockingQueue<ArrayList<Byte>> queue = null;
	
	public CAPIPClient(Overseer o)
		throws IOException
	{
		super();
		inter = new CAPInterface(this, sock.accept());
		parent = o;
		queue = new LinkedBlockingQueue<ArrayList<Byte>>();
	}

	protected void interpretInput()
	{
		HashMap<String, Node> map = header.getDictValues();
		if(map.containsKey("TYPE"))
		{
			if(map.get("TYPE").toString().compareTo("CODE") == 0)
			{
				HashMap<String, Node> codeInfo = body.getDictValues();
				if(codeInfo.containsKey("DATA"))
				{
					parent.setRules(codeInfo.get("DATA").getByteValues());
					System.out.println("-- not sure how to handle code yet --");
				}
				else
				{
					inter.sendState(2, "DATA MISSING FOR CODE MESSAGE TYPE");
					return;
				}
			}
			else if(map.get("TYPE").toString().compareTo("CONNECT") == 0)
			{
				System.out.println("-- not sure how to handle connect yet --");
			}
			else if(map.get("TYPE").toString().compareTo("DISCONNECT") == 0)
			{
				inter.disconnect();
			}
			else if(map.get("TYPE").toString().compareTo("GRID") == 0)
			{
				HashMap<String, Node> gi = body.getDictValues();
				Vector2i size = null;
				Recti area = null;
				int n = -1;
				char type = 'X';
				Grid grid = null;
				if(gi.containsKey("SIZE"))
				{
					ArrayList<Node> sizeList = gi.get("SIZE").getListValues();
					size = new Vector2i(sizeList.get(0).getIntValue(), sizeList.get(1).getIntValue());
				}
				else
				{
					inter.sendState(2, "GRID MISSING A SIZE");
					return;
				}
				if(gi.containsKey("AREA"))
				{
					ArrayList<Node> sizeList = gi.get("AREA").getListValues();
					area = new Recti(new Vector2i(sizeList.get(2).getIntValue(), sizeList.get(3).getIntValue()), size);
				}
				else
				{
					inter.sendState(2, "GRID MISSING AN AREA");
					return;
				}
				if(gi.containsKey("PROPERTIES"))
				{
					n = gi.get("PROPERTIES").getIntValue();
				}
				else
				{
					inter.sendState(2, "GRID MISSING THE PROPERTY AMOUNT");
					return;
				}
				if(gi.containsKey("GRIDTYPE"))
				{
					type = gi.get("GRIDTYPE").toString().charAt(0);
				}
				else
				{
					inter.sendState(2, "GRID MISSING THE GRID TYPE");
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
						inter.sendState(2, "GRID TYPE INVALID");
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
							ArrayList<Node> currCell = ((DoubleNode)currRow.get(x)).getListValues();
							for(int i = 0; i < currCell.size(); i++)
							{
								grid.getCell(x, y).setValue(i, currCell.get(i).getDoubleValue());
							}
						}
					}
				}
				else
				{
					inter.sendState(2, "GRID DATA MISSING");
					return;
				}
				
				parent.setGrid(grid);
				parent.setWorkable(area);
			}
			else if(map.get("TYPE").toString().compareTo("QUERY") == 0)
			{
				if(currInBytes == null)
					inter.sendState(0);
				else
					inter.sendState(1);
			}
			else
			{
				inter.sendState(2, "MESSAGE TYPE NOT RECOGNSED");
				return;
			}
		}
		else
		{
			inter.sendState(2, "MESSAGE TYPE NOT FOUND");
			return;
		}
	}
	
	public void setCurrentInformation(byte[] in)
	{
		if(currInBytes == null)
		{
			currInBytes = in;
			int result = processInput();
			if(result == 2)
			{
				System.out.println("Error processing input.");
				inter.sendState(2, "INPUT PROCESSING ERROR");
			}
			else
			{
				interpretInput();
				parent.start();
				currInBytes = null;
				if(queue.size() == 0)
					inter.sendState(0);
			}
		}
		else
		{
			inter.sendState(1);
			ArrayList<Byte> b = new ArrayList<Byte>();
			for(int i = 0; i < in.length; i++)
				b.add(new Byte(in[i]));
			queue.add(b);
		}
		if(queue.size() != 0)
		{
			ArrayList<Byte> b = queue.remove();
			byte[] bin = new byte[b.size()];
			for(int i = 0; i < bin.length; i++)
				bin[i] = b.get(i).byteValue();
			setCurrentInformation(bin);
		}
	}

	public void start()
		throws IOException
	{
		inter.start();
	}
}
