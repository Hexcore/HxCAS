package com.hexcore.cas.control.client;

import com.hexcore.cas.math.Recti;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.HexagonGrid;
import com.hexcore.cas.model.RectangleGrid;
import com.hexcore.cas.model.TriangleGrid;

public class Overseer 
{
	private Grid grid = null;
	private Recti workable = null;
	//private Object parent = null;
	
	public Overseer(Grid g, Recti w)//, Object o)
	{
		grid = g;
		workable = w;
		//parent = o;
	}
	
	public void setGrid(Grid g)
	{
		Vector2i gridSize = new Vector2i(g.getWidth(), g.getHeight());
		Cell cell = new Cell(g.getCell(0, 0));
		switch(g.getType())
		{
			case 'r':
			case 'R':
				grid = new RectangleGrid(gridSize, cell);
				break;
			case 'h':
			case 'H':
				grid = new HexagonGrid(gridSize, cell);
				break;
			case 't':
			case 'T':
				grid = new TriangleGrid(gridSize, cell);
				break;
			default:
				System.out.println("Unable to create a grid with no type.");
				return;
		}
		for(int y = 0; y < grid.getHeight(); y++)
			for(int x = 0; x < grid.getWidth(); x++)
				grid.setCell(x, y, g.getCell(x, y).getValues());
	}
	
	public void setWorkable(Recti r)
	{
		workable = r;
	}
	
	//Used for testing purposes only
	public Grid getGrid()
	{
		return grid;
	}
	
	//Used for testing purposes only
	public Recti getWorkable()
	{
		return workable;
	}
	
	public void start()
	{
		int coreNum = Runtime.getRuntime().availableProcessors();
		CoreThread[] cores = new CoreThread[coreNum];
		int totalCellNum = grid.getHeight() * grid.getWidth();
		int div = totalCellNum / coreNum;
		int rem = totalCellNum % coreNum;
		Vector2i[] startingPoints = new Vector2i[coreNum];
		int[] sizes = new int[coreNum];
		int pos = 0;
		for(int i = 0; i < (totalCellNum - rem); i += div)
		{
			startingPoints[pos] = new Vector2i(i % grid.getWidth(), i / grid.getWidth());
			sizes[pos] = (i == (totalCellNum - div - rem)) ? div + rem : div;
			if(i == (totalCellNum - div - rem))
				break;
			pos++;
		}
		
		for(int i = 0; i < coreNum; i++)
			cores[i] = new CoreThread(startingPoints[i], sizes[i]);
		for(int i = 0; i < coreNum; i++)
			cores[i].start();
		
		try
		{
			boolean allDone = false;
			while(!allDone)
			{
				for(int i = 0; i < coreNum; i++)
					cores[i].join(100);
				for(int i = 0; i < coreNum; i++)
				{
					if(!cores[i].isAlive())
						allDone = true;
					else
						allDone = false;
				}
			}
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		//o.setOverseerWork(grid);
	}
	
	public class CoreThread extends Thread
	{
		private Vector2i workPos = null;
		private int num = -1;
		
		public CoreThread(Vector2i p, int n)
		{
			workPos = p;
			num = n;
		}
		
		public void run()
		{
			int cnt = 0;
			int x = workPos.x;
			for(int y = workPos.y; y < grid.getHeight(); y++)
			{
				for(; ; x++)
				{
					if(x >= grid.getWidth())
					{
						x = 0;
						break;
					}
					for(int i = 0; i < grid.getCell(x, y).getValueCount(); i++)
					{
						int val = (grid.getCell(x, y).getValue(i) == 0) ? 1 : 0;
						grid.getCell(x, y).setValue(i, val);
					}
					cnt++;
					if(cnt >= num)
						break;
				}
				if(cnt >= num)
					break;
			}
		}
	}
}