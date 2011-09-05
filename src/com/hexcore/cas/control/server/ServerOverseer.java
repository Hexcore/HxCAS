package com.hexcore.cas.control.server;

import java.util.ArrayList;

import com.hexcore.cas.control.protocol.Overseer;
import com.hexcore.cas.math.Recti;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.HexagonGrid;
import com.hexcore.cas.model.RectangleGrid;
import com.hexcore.cas.model.ThreadWork;
import com.hexcore.cas.model.TriangleGrid;

public class ServerOverseer extends Overseer
{
	private ArrayList<String> clientNames = null;
	//private Grid[] clientGrids = null;
	private int numOfClients = 0;
	//private Recti[] alteredClientWorkables = null;
	private Recti[] clientWorkables = null;
	private ThreadWork[] clientWork = null;
	
	public ServerOverseer(Grid g)
	{
		super(g);
	}
	/*
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
	
	public Grid[] getClientGrids()
	{
		return clientGrids;
	}
	*/
	public ThreadWork[] getClientWork()
	{
		return clientWork;
	}
	
	public ArrayList<String> getClientNames()
	{
		return clientNames;
	}
	/*
	public Recti[] getClientWorkablesAltered()
	{
		return alteredClientWorkables;
	}
	*/
	public Recti[] getClientWorkables()//getClientWorkablesUnaltered()
	{
		//return unalteredClientWorkables;
		return clientWorkables;
	}
	
	public Grid getGrid()
	{
		return grid;
	}
	
	public int getNumberOfClients()
	{
		return numOfClients;
	}
	
	public void makeNonwrappableGrids()
	{
		int numOfClientWorks = clientWorkables.length;
		clientWork = new ThreadWork[numOfClientWorks];
		
		for(int i = 0; i < numOfClientWorks; i++)
		{
			Grid workingGrid = null;
			int width = 2;
			int height = 2;

			Recti w = clientWorkables[i];
			if(w.getPosition().x == 0)
				width--;
			if(w.getPosition().y == 0)
				height--;
			if(w.getSize().x == grid.getWidth())
				width--;
			if(w.getSize().y == grid.getHeight())
				height--;
			Vector2i size = new Vector2i(w.getSize().x + width, w.getSize().y + height);
			Cell cell = new Cell(grid.getCell(0, 0).getValueCount());

			switch(grid.getType())
			{
				case 'h':
				case 'H':
					workingGrid = new HexagonGrid(size, cell);
					break;
				case 'r':
				case 'R':
					workingGrid = new RectangleGrid(size, cell);
					break;
				case 't':
				case 'T':
					width += 2;
					if(w.getPosition().x - 1 < 0)
						width--;
					if(w.getSize().x + width >= grid.getWidth())
						width--;
					size = new Vector2i(w.getSize().x + width, w.getSize().y + height);
					workingGrid = new TriangleGrid(size, cell);
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
					if(xx < 0 || y < 0)
						continue;
					for(int j = 0; j < grid.getCell(xx, yy).getValueCount(); j++)
						workingGrid.getCell(gXPos, gYPos).setValue(j, grid.getCell(xx, yy).getValue(j));
					gXPos++;
					if(gXPos >= w.getSize().x + (width * 2))
						gXPos = 0;
				}
				gYPos++;
			}
			//WAS HERE
			Recti aw = new Recti(new Vector2i(1, 1), w.getSize());
			switch(workingGrid.getType())
			{
				case 't':
				case 'T':
					aw.setPosition(new Vector2i(2, 1));
					break;
			}

			clientWork[i] = new ThreadWork(workingGrid, aw);
		}
	}
	
	public void makeWrappableGrids()
	{
		int numOfClientWorks = clientWorkables.length;
		clientWork = new ThreadWork[numOfClientWorks];
		//clientGrids = new Grid[numOfClients];
		
		//for(int i = 0; i < numOfClients; i++)
		for(int i = 0; i < numOfClientWorks; i++)
		{
			//Grid clientGrid = null;
			Grid workingGrid = null;
			int width = 1;
			int height = 1;

			//Recti w = unalteredClientWorkables[i];
			Recti w = clientWorkables[i];
			Vector2i size = new Vector2i(w.getSize().x + (2 * width), w.getSize().y + (2 * height));
			Cell cell = new Cell(grid.getCell(0, 0).getValueCount());

			switch(grid.getType())
			{
				case 'h':
				case 'H':
					//clientGrid = new HexagonGrid(size, cell);
					workingGrid = new HexagonGrid(size, cell);
					break;
				case 'r':
				case 'R':
					//clientGrid = new RectangleGrid(size, cell);
					workingGrid = new RectangleGrid(size, cell);
					break;
				case 't':
				case 'T':
					width += 1;
					size = new Vector2i(w.getSize().x + (2 * width), w.getSize().y + (2 * height));
					//clientGrid = new TriangleGrid(size, cell);
					workingGrid = new TriangleGrid(size, cell);
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
					{
						//clientGrid.getCell(gXPos, gYPos).setValue(j, grid.getCell(xx, yy).getValue(j));
						workingGrid.getCell(gXPos, gYPos).setValue(j, grid.getCell(xx, yy).getValue(j));
					}
					gXPos++;
					if(gXPos >= w.getSize().x + (width * 2))
						gXPos = 0;
				}
				gYPos++;
			}
			
			Recti aw = new Recti(new Vector2i(1, 1), w.getSize());
			switch(workingGrid.getType())
			{
				case 't':
				case 'T':
					aw.setPosition(new Vector2i(2, 1));
					break;
			}

			//clientGrids[i] = clientGrid;
			clientWork[i] = new ThreadWork(workingGrid, aw);
		}
	}
	
	public void rebuildGrid()
	{
		for(int i = 0; i < clientWorkables.length; i++)
		{
			/*Grid g = clientGrids[i];
			Recti aw = alteredClientWorkables[i];
			Recti uw = unalteredClientWorkables[i];*/
			Grid g = clientWork[i].getGrid();
			Recti aw = clientWork[i].getWorkableArea();
			Recti uw = clientWorkables[i];
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
	
	public void send()
	{
		//System.out.println("-- SENDING GRIDS TO CLIENTS -- SO");
		((CAPIPServer)capIP).setClientWork(clientWork);
		/*
		((CAPIPServer)capIP).setClientGrids(clientGrids);
		((CAPIPServer)capIP).setClientWorkables(alteredClientWorkables);
		*/
		((CAPIPServer)capIP).sendGrids();
		//System.out.println("-- SEND GRIDS RETURNED -- SO");
	}
	
	//Called from CAPIPServer to pass up work done
	//public void setClientGrids(Grid[] grids)
	public void setClientWork(ArrayList<ThreadWork> CW)
	{
		//System.out.println("-- CLIENT WORK RECEIVED BACK FROM BEEN COMPUTED -- SO");
		int size = CW.size();
		clientWork = new ThreadWork[size];
		for(int i = 0; i < size; i++)
			clientWork[i] = CW.get(i).clone();
		/*clientGrids = new Grid[grids.length];
		for(int i = 0; i < grids.length; i++)
			clientGrids[i] = grids[i].clone();
		//Displaying grid in console
		for(int y = 0; y < clientGrids[0].getSize().y; y++)
		{
			for(int x = 0; x < clientGrids[0].getSize().x; x++)
			{
				System.out.print("[" + clientGrids[0].getCell(x, y).getValue(0) + "]");
			}
			System.out.println();
		}*/
		rebuildGrid();
		if(grid.getWrappable())
			makeWrappableGrids();
		else
			makeNonwrappableGrids();
	}

	public void setClientNames(ArrayList<String> names)
	{
		clientNames = names;
		numOfClients = clientNames.size();
	}
	
	public void setClientWorkables(Recti[] cW)
	{
		//unalteredClientWorkables = new Recti[cW.length];
		clientWorkables = new Recti[cW.length];
		for(int i = 0; i < cW.length; i++)
			clientWorkables[i] = new Recti(cW[i].getPosition(), cW[i].getSize());
			//unalteredClientWorkables[i] = new Recti(cW[i].getPosition(), cW[i].getSize());
		//alterClientWorkables();
		if(grid.getWrappable())
			makeWrappableGrids();
		else
			makeNonwrappableGrids();
	}

	@Override
	public void start()
	{
	}
	
	@Override
	public void run()
	{
		capIP = new CAPIPServer(this, clientNames);
		capIP.start();
	}
}
