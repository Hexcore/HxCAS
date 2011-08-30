package com.hexcore.cas.control.client;

import java.util.ArrayList;

import com.hexcore.cas.control.protocol.CAPIPServer;
import com.hexcore.cas.math.Recti;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.HexagonGrid;
import com.hexcore.cas.model.RectangleGrid;
import com.hexcore.cas.model.TriangleGrid;

public class ServerOverseer extends Overseer
{
	private ArrayList<String> clientNames = null;
	private Grid[] clientGrids = null;
	private int numOfClients = 0;
	private Recti[] alteredClientWorkables = null;
	private Recti[] unalteredClientWorkables = null;
	
	public ServerOverseer(Grid g)
	{
		super(g);
	}
	
	public void alterClientWorkables()
	{		
		for(int i = 0; i < numOfClients; i++)
		{
			switch(grid.getType())
			{
				case 'h':
				case 'H':
				case 'r':
				case 'R':
					alteredClientWorkables[i].getSize().x += 2;
					alteredClientWorkables[i].getSize().y += 2;
					alteredClientWorkables[i].getPosition().x = 1;
					alteredClientWorkables[i].getPosition().y = 1;
					break;
				case 't':
				case 'T':
					alteredClientWorkables[i].getSize().x += 4;
					alteredClientWorkables[i].getSize().y += 2;
					alteredClientWorkables[i].getPosition().x = 2;
					alteredClientWorkables[i].getPosition().y = 1;
					break;
			}
		}
	}
	
	public ArrayList<String> getClientNames()
	{
		return clientNames;
	}
	
	public void makeGrids()
	{
		clientGrids = new Grid[numOfClients];
		
		for(int i = 0; i < numOfClients; i++)
		{
			Grid clientGrid = null;

			Recti w = unalteredClientWorkables[i];
			Vector2i size = new Vector2i(w.getSize());
			Cell cell = new Cell(grid.getCell(0, 0).getValueCount());

			int width = 2;
			int height = 2;
			
			switch(grid.getType())
			{
				case 'h':
				case 'H':
					clientGrid = new HexagonGrid(size, cell);
					break;
				case 'r':
				case 'R':
					clientGrid = new RectangleGrid(size, cell);
					break;
				case 't':
				case 'T':
					clientGrid = new TriangleGrid(size, cell);
					width += 2;
					break;
			}

			int gYPos = 0;
			int gXPos = 0;
			for(int y = w.getPosition().y - height; y < w.getPosition().y + w.getSize().y + height; y++)
			{
				for(int x = w.getPosition().x - width; x < w.getPosition().x + w.getSize().x + width; x++)
				{
					int xx = x % grid.getWidth();
					int yy = y % grid.getHeight();
					for(int j = 0; j < grid.getCell(xx, yy).getValueCount(); j++)
						clientGrid.getCell(gXPos, gYPos).setValue(j, grid.getCell(xx, yy).getValue(j));
					gXPos++;
					if(gXPos >= w.getSize().x + (width * 2))
						gXPos = 0;
				}
			}
		}
	}
	
	public void rebuildGrid()
	{
		
	}
	
	public void sendGrids()
	{
		((CAPIPServer)capIP).setClientGrids(clientGrids);
	}
	
	public void sendClientWorkables()
	{
		((CAPIPServer)capIP).setClientWorkables(alteredClientWorkables);
	}
	
	//Called from CAPIPServer to pass up work done
	public void setClientGrids(Grid[] grids)
	{
		clientGrids = grids.clone();
		rebuildGrid();
		makeGrids();
	}
	
	public void setClientNames(ArrayList<String> names)
	{
		clientNames = names;
		numOfClients = clientNames.size();
	}
	
	public void setClientWorkables(Recti[] cW)
	{
		unalteredClientWorkables = cW.clone();
		alterClientWorkables();
		sendClientWorkables();
		makeGrids();
		sendGrids();
	}
	
	@Override
	public void start()
	{
		capIP = new CAPIPServer(this, clientNames);
		//capIP.start();
	}
}
