package com.hexcore.cas.control.server;

import java.util.ArrayList;

import com.hexcore.cas.control.protocol.CAPInformationProcessor;
import com.hexcore.cas.control.protocol.Overseer;
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
	
	//IN TEST
	public ServerOverseer(Grid g)
	{
		super(g);
	}
	
	//IN TEST IMPLICITLY
	public void alterClientWorkables()
	{
		alteredClientWorkables = new Recti[unalteredClientWorkables.length];
		for(int i = 0; i < unalteredClientWorkables.length; i++)
			alteredClientWorkables[i] = new Recti(unalteredClientWorkables[i].getPosition(), unalteredClientWorkables[i].getSize());
		
		for(int i = 0; i < numOfClients; i++)
		{
			switch(grid.getType())
			{
				case 'h':
				case 'H':
				case 'r':
				case 'R':
					alteredClientWorkables[i].setPosition(new Vector2i(1, 1));
					break;
				case 't':
				case 'T':
					alteredClientWorkables[i].setPosition(new Vector2i(2, 1));
					break;
			}
		}
	}
	
	//IN TEST
	public Grid[] getClientGrids()
	{
		return clientGrids;
	}
	
	//IN TEST
	public ArrayList<String> getClientNames()
	{
		return clientNames;
	}
	
	//IN TEST
	public Recti[] getClientWorkablesAltered()
	{
		return alteredClientWorkables;
	}
	
	//IN TEST
	public Recti[] getClientWorkablesUnaltered()
	{
		return unalteredClientWorkables;
	}
	
	//IN TEST
	public Grid getGrid()
	{
		return grid;
	}
	
	//IN TEST
	public int getNumberOfClients()
	{
		return numOfClients;
	}
	
	//IN TEST IMPLICITLY
	public void makeGrids()
	{
		clientGrids = new Grid[numOfClients];
		
		for(int i = 0; i < numOfClients; i++)
		{
			Grid clientGrid = null;
			int width = 1;
			int height = 1;

			Recti w = unalteredClientWorkables[i];
			Vector2i size = new Vector2i(w.getSize().x + (2 * width), w.getSize().y + (2 * height));
			Cell cell = new Cell(grid.getCell(0, 0).getValueCount());

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
					width += 1;
					size = new Vector2i(w.getSize().x + (2 * width), w.getSize().y + (2 * height));
					clientGrid = new TriangleGrid(size, cell);
					break;
			}

			int gYPos = 0;
			int gXPos = 0;
			for(int y = w.getPosition().y - height; y < w.getPosition().y + w.getSize().y + height; y++)
			{
				for(int x = w.getPosition().x - width; x < w.getPosition().x + w.getSize().x + width; x++)
				{
					int xx = (grid.getWidth() + x) % grid.getWidth();
					int yy = (grid.getHeight() + y) % grid.getHeight();
					for(int j = 0; j < grid.getCell(xx, yy).getValueCount(); j++)
						clientGrid.getCell(gXPos, gYPos).setValue(j, grid.getCell(xx, yy).getValue(j));
					gXPos++;
					if(gXPos >= w.getSize().x + (width * 2))
						gXPos = 0;
				}
				gYPos++;
			}
			clientGrids[i] = clientGrid;
		}
	}
	
	public void rebuildGrid()
	{
		for(int i = 0; i < numOfClients; i++)
		{
			Grid g = clientGrids[i];
			Recti aw = alteredClientWorkables[i];
			Recti uw = unalteredClientWorkables[i];
			for(int y = uw.getPosition().y, yy = aw.getPosition().y; y < uw.getPosition().y + uw.getSize().y; y++, yy++)
			{
				for(int x = uw.getPosition().x, xx = aw.getPosition().x; x < uw.getPosition().x + uw.getSize().x; x++, xx++)
				{
					for(int index = 0; index < grid.getCell(x, y).getValueCount(); index++)
					{
						Cell c = g.getCell(xx, yy);
						grid.getCell(x, y).setValue(index, c.getValue(index));
					}
				}
			}
		}
	}
	
	//IN TEST
	public void send()
	{
		System.out.println("-- SENDING GRIDS TO CLIENTS -- SO");
		((CAPIPServer)capIP).setClientGrids(clientGrids);
		((CAPIPServer)capIP).setClientWorkables(alteredClientWorkables);
		((CAPIPServer)capIP).sendGrids();
		System.out.println("-- SEND GRIDS RETURNED -- SO");
	}
	
	//IN TEST IMPLICITLY
	//Called from CAPIPServer to pass up work done
	public void setClientGrids(Grid[] grids)
	{
		System.out.println("Gotten ClientGrids Back!");
		clientGrids = new Grid[grids.length];
		for(int i = 0; i < grids.length; i++)
			clientGrids[i] = grids[i].clone();
		for(int y = 0; y < clientGrids[0].getSize().y; y++)
		{
			for(int x = 0; x < clientGrids[0].getSize().x; x++)
			{
				System.out.print("[" + clientGrids[0].getCell(x, y).getValue(0) + "]");
			}
			System.out.println();
		}
		rebuildGrid();
		makeGrids();
	}
	
	//IN TEST
	public void setClientNames(ArrayList<String> names)
	{
		clientNames = names;
		numOfClients = clientNames.size();
	}
	
	//IN TEST
	public void setClientWorkables(Recti[] cW)
	{
		unalteredClientWorkables = new Recti[cW.length];
		for(int i = 0; i < cW.length; i++)
			unalteredClientWorkables[i] = new Recti(cW[i].getPosition(), cW[i].getSize());
		alterClientWorkables();
		makeGrids();
	}
	
	//IN TEST
	@Override
	public void start()
	{
		capIP = new CAPIPServer(this, clientNames);
		capIP.start();
	}
}
