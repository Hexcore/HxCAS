package com.hexcore.cas.control.client;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.hexcore.cas.control.protocol.Overseer;
import com.hexcore.cas.math.Recti;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.utilities.Log;

public class ClientOverseer extends Overseer
{
	private static final String TAG = "ClientOverseer";
	
	private LinkedBlockingQueue<Work> workQueue;
	
	private boolean running = false;

	public ClientOverseer()
	{
		super();
		
		workQueue = new LinkedBlockingQueue<Work>();
		
		try
		{
			capIP = new CAPIPClient(this);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public int checkState()
	{
		if(workQueue.isEmpty())
			return 0;
		else
			return 1;
	}
	
	public void setRules(byte[] b)
	{
		//vm.loadRules(b);
	}
	
	public void addGrid(Grid grid, Recti workArea)
	{
		Log.information(TAG, "Got work");
		
		Work work = new Work();
		work.grid = grid;
		work.workArea = workArea;
		try
		{
			workQueue.put(work);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void disconnect()
	{
		running = false;
	}
	
	@Override
	public void run()
	{
		Log.information(TAG, "Starting...");

		int cores = Runtime.getRuntime().availableProcessors();
		
		WorkerThread[] thread = new WorkerThread[cores];
		
		for (int i = 0; i < cores; i++)
		{
			thread[i] = new WorkerThread();
			thread[i].start();
		}
		
		capIP.start();
		
		Log.information(TAG, "Ready");
		
		running = true;
		while (running)
		{
			try
			{
				Thread.sleep(500);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		Log.information(TAG, "Stopping...");
		
		capIP.disconnect();
		
		try
		{
			for (int i = 0; i < cores; i++) thread[i].quit();
			for (int i = 0; i < cores; i++) thread[i].join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	private void calculateCell(Cell cell, Cell[] neighbours)
	{
		// Game of Life implementation for testing
		int sum = 0;
		
		for (Cell neighbour : neighbours)
			sum += neighbour.getValue(0);
		
		if(sum < 2 || sum > 3)
			cell.setValue(0, 0);
		else if(sum == 3)
			cell.setValue(0, 1);			
	}
	
	class Work
	{
		Grid	grid;
		Recti	workArea;
	}
	
	class WorkerThread extends Thread
	{
		boolean running = false;
		
		public void quit()
		{
			running = false;
		}
				
		@Override
		public void run()
		{
			while (running)
			{
				try
				{
					Work work = workQueue.poll(3, TimeUnit.SECONDS);
				
					if (work == null) continue;
					
					Vector2i end = work.workArea.position.add(work.workArea.size);
					
					for (int y = work.workArea.position.y; y < end.y; y++)
						for (int x = work.workArea.position.x; x < end.x; x++)
							calculateCell(grid.getCell(x, y), grid.getNeighbours(new Vector2i(x, y)));
	
					((CAPIPClient)capIP).sendResult(work.grid, work.workArea, !workQueue.isEmpty());
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}