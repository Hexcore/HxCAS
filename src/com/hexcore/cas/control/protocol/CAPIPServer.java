package com.hexcore.cas.control.protocol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.hexcore.cas.control.client.Overseer;
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
	
	public CAPIPServer()
	{
		super();
		clients = new ArrayList<CAPMessageProtocol>();
	}

	@Override
	protected void interpretInput(Message message)
	{
		DictNode header = message.getHeader();
		DictNode body = (DictNode)message.getBody();
		
		HashMap<String, Node> map = header.getDictValues();
		if(map.containsKey("TYPE"))
		{
			if(map.get("TYPE").toString().compareTo("ACCEPT") == 0)
			{
				System.out.println("-- not sure how to handle accept yet --");
			}
			else if(map.get("TYPE").toString().compareTo("REJECT") == 0)
			{
				System.out.println("-- not sure how to handle reject yet --");
			}
			else if(map.get("TYPE").toString().compareTo("STATUS") == 0)
			{
				System.out.println("-- not sure how to handle status yet --");
			}
			else if(map.get("TYPE").toString().compareTo("RESULT") == 0)
			{
				System.out.println("-- not sure how to handle result EXACTLY yet --");
				/*HashMap<String, Node> gi = body.getDictValues();
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
}
