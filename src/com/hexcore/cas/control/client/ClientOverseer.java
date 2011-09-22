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
	private LinkedBlockingQueue<Work> completedQueue;
	
	private boolean running = false;
	private boolean valid = false;
	private int gen = 0;

	public ClientOverseer(int port)
	{
		super();
		
		workQueue = new LinkedBlockingQueue<Work>();
		completedQueue = new LinkedBlockingQueue<Work>();
		
		try
		{
			capIP = new CAPIPClient(this, port);
			valid = ((CAPIPClient)capIP).isValid();
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
	
	public void addGrid(Grid grid, Recti workArea, int id, int g)
	{
		Log.information(TAG, "Got work");
		gen = g;
		
		Work work = new Work();
		work.grid = grid.clone();
		work.workArea = new Recti(workArea);
		work.ID = id;
		try
		{
			workQueue.put(work);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean isValid()
	{
		return valid;
	}
	
	@Override
	public void disconnect()
	{
		valid = false;
		running = false;
	}
	
	@Override
	public void run()
	{
		if (!valid)
		{
			Log.warning(TAG, "Socket not connected - Shutting down");
			disconnect();
			return;
		}
		
		Log.information(TAG, "Starting...");

		int cores = Runtime.getRuntime().availableProcessors();
		
		WorkerThread[] thread = new WorkerThread[cores];
		
		for (int i = 0; i < cores; i++) thread[i] = new WorkerThread();

		capIP.start();
				
		Log.information(TAG, "Ready");
		
		for (int i = 0; i < cores; i++) thread[i].start();
		
		running = true;
		while (running)
		{
			Work work = null;
			
			try
			{
				work = completedQueue.poll(1000, TimeUnit.MILLISECONDS);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			if (work == null) continue;
			
			Log.information(TAG, "Sending completed work");
			int more = Math.max(Runtime.getRuntime().availableProcessors() - workQueue.size(), 0);			
			((CAPIPClient)capIP).sendResult(work.grid, work.workArea, more, work.ID, gen);
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
		int		ID;
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
			running = true;
			while (running)
			{
				try
				{
					Work work = workQueue.poll(3, TimeUnit.SECONDS);
				
					if (work == null) continue;
					
					Log.information(TAG, "Doing work");
					
					Vector2i end = work.workArea.position.add(work.workArea.size);
					
					for (int y = work.workArea.position.y; y < end.y; y++)
						for (int x = work.workArea.position.x; x < end.x; x++)
						{
							Cell cell = work.grid.getCell(x, y);
							Cell[] neighbours = work.grid.getNeighbours(new Vector2i(x, y));
							calculateCell(cell, neighbours);
						}
					
					completedQueue.put(work);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
