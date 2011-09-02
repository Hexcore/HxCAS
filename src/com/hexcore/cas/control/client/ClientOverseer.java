package com.hexcore.cas.control.client;

import java.io.IOException;
import java.util.Vector;

import com.hexcore.cas.control.protocol.Overseer;
import com.hexcore.cas.math.Recti;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.HexagonGrid;
import com.hexcore.cas.model.RectangleGrid;
import com.hexcore.cas.model.ThreadState;
import com.hexcore.cas.model.TriangleGrid;

public class ClientOverseer extends Overseer
{
	private Vector<ThreadState> threadWork = null;
	private Recti workable = null;
	
	public ClientOverseer()
		throws IOException
	{
		super();
		capIP = new CAPIPClient(this);
		capIP.start();
	}
	
	@Override
	public int checkState()
	{
		if(threadWork == null)
			return 0;
		else
			return 1;
	}
	
	public Recti getWorkable()
	{
		return workable;
	}
	
	public void setRules(byte[] b)
	{
		//vm.loadRules(b);
	}
	
	public void setWorkable(Recti r)
	{
		workable = r;
	}
	
	@Override
	public void start()
	{
		System.out.println("-- CLIENT RUNNIN -- CO");
		threadWork = new Vector<ThreadState>();
		/*
		 * Work is divided up for the CoreThreads.
		 * Each thread will have an equal number of cells to work on,
		 * with the exception of the last thread, which may have the
		 * same number of cells of the rest, else it will have more,
		 * which is the remainder cells of
		 * 		totalNumberOfCells / totalNumberOfThreads
		 */
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
		
		/*
		 * Sets all the changes made by the threads to the grid of the overseer.
		 */
		for(int i = 0; i < threadWork.size(); i++)
		{
			int cnt = 0;
			int x = threadWork.get(i).startingPosition.x;
			for(int y = threadWork.get(i).startingPosition.y; y < grid.getHeight(); y++)
			{
				for(; ; x++)
				{
					if(x >= grid.getWidth())
					{
						x = 0;
						break;
					}
					
					for(int index = 0; index < grid.getCell(x, y).getValueCount(); index++)
						grid.getCell(x, y).setValue(index, threadWork.get(i).work[cnt].getValue(index));
					
					cnt++;
					if(cnt >= threadWork.get(i).num)
						break;
				}
				if(cnt >= threadWork.get(i).num)
					break;
			}
		}
		
		Grid g = null;
		switch(grid.getType())
		{
			case 'h':
			case 'H':
				g = new HexagonGrid(grid.getSize(), new Cell(grid.getCell(0, 0).getValueCount()));
				break;
			case 'r':
			case 'R':
				g = new RectangleGrid(grid.getSize(), new Cell(grid.getCell(0, 0).getValueCount()));
				break;
			case 't':
			case 'T':
				g = new TriangleGrid(grid.getSize(), new Cell(grid.getCell(0, 0).getValueCount()));
				break;
			default:
				System.out.println("Grid in overseer had no type!");
				break;
		}
		for(int y = workable.getY(); y < (workable.getPosition().y + workable.getSize().y); y++)
			for(int x = workable.getX(); x < (workable.getPosition().x + workable.getSize().x); x++)
				for(int i = 0; i < grid.getCell(x, y).getValueCount(); i++)
					g.getCell(x, y).setValue(i, grid.getCell(x, y).getValue(i));
		threadWork = null;
		System.out.println("-- SENDING RESULT GRID -- CO");
		((CAPIPClient)capIP).sendGrid(g);
	}
	
	public class CoreThread extends Thread
	{
		private Cell[] mywork = null;
		private int myworkPos = -1;
		private int num = -1;
		private Vector2i workPos = null;
		
		public CoreThread(Vector2i p, int n)
		{
			workPos = p;
			num = n;
			mywork = new Cell[num];
			myworkPos = 0;
		}
		
		private void gameOfLife(int x, int y)
		{
			Cell[] neigh = grid.getNeighbours(new Vector2i(x, y));
			int cnt = 0;
			for(int i = 0; i < neigh.length; i++)
				if(neigh[i].getValue(0) == 1)
					cnt++;
			if(cnt < 2 || cnt > 3)
			{
				mywork[myworkPos] = new Cell(grid.getCell(x, y));
				mywork[myworkPos].setValue(0, 0);
			}
			else if(cnt == 3 && grid.getCell(x, y).getValue(0) == 0)
			{
				mywork[myworkPos] = new Cell(grid.getCell(x, y)); 
				mywork[myworkPos].setValue(0, 1);
			}
			else
				mywork[myworkPos] = new Cell(grid.getCell(x, y));
			myworkPos++;
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
					
					gameOfLife(x, y);
					/*
					 * Cell c = mywork[myworkPos++];
					 * mywork[myworkPos++] = new Cell(vm.run(c, c.getNeighbours()));
					 * 
					 */
					
					cnt++;
					if(cnt >= num)
						break;
				}
				if(cnt >= num)
					break;
			}
			threadWork.add(new ThreadState(mywork, workPos, num));
		}
	}
}