package com.hexcore.cas.control.client;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.hexcore.cas.math.Recti;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.rulesystems.Rule;
import com.hexcore.cas.utilities.Log;

public class ClientOverseer extends Thread
{
	private static final String TAG = "ClientOverseer";
	
	private LinkedBlockingQueue<Work> workQueue;
	private LinkedBlockingQueue<Work> completedQueue;
	
	private Rule	rule;
	
	private CAPIPClient informationProcessor;
	
	private WorkerThread[] threads;
	
	private boolean running = false;
	private boolean valid = false;
	private int gen = 0;

	public ClientOverseer(int port)
	{
		super();
		
		workQueue = new LinkedBlockingQueue<Work>();
		completedQueue = new LinkedBlockingQueue<Work>();
		
		rule = new GameOfLifeRule();
		
		try
		{
			informationProcessor = new CAPIPClient(this, port);
			valid = informationProcessor.isValid();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
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
	
	public void disconnect()
	{
		valid = false;
		running = false;
		informationProcessor.disconnect();
	}
	
	@Override
	public void start()
	{
		if (!valid)
		{
			Log.warning(TAG, "Socket not connected - Shutting down");
			disconnect();
			return;
		}
		
		Log.information(TAG, "Starting...");

		int cores = Runtime.getRuntime().availableProcessors();
		
		threads = new WorkerThread[cores];
		
		for (int i = 0; i < cores; i++) threads[i] = new WorkerThread(i);

		informationProcessor.start();
				
		Log.information(TAG, "Ready");
		
		for (int i = 0; i < cores; i++) threads[i].start();		
	
		running = true;
		super.start();
	}
	
	@Override
	public void run()
	{
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
			informationProcessor.sendResult(work.grid, work.workArea, more, work.ID, gen);
		}
		
		Log.information(TAG, "Stopping...");
		
		informationProcessor.disconnect();
		
		try
		{
			for (int i = 0; i < threads.length; i++) threads[i].quit();
			for (int i = 0; i < threads.length; i++) threads[i].join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	class GameOfLifeRule implements Rule
	{
		@Override
		public void run(Cell cell, Cell[] neighbours)
		{
			// Game of Life implementation for testing
			int sum = 0;
			
			for (Cell neighbour : neighbours)
				if (neighbour != null)
					sum += neighbour.getValue(0);
			
			if (cell.getValue(0) == 1)
			{
				if (sum < 2 || sum > 3) 
					cell.setValue(0, 0);
			}
			else
			{
				if (sum == 3)
					cell.setValue(0, 1);
			}
		}
		
		@Override
		public int getNumProperties()
		{
			return 2;
		}
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
		
		public WorkerThread(int id)
		{
			super("Worker - " + id);
		}
		
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
										
					Grid		newGrid = work.grid.getType().create(work.workArea.size, work.grid.getNumProperties());
					Vector2i	offset = work.workArea.position;
					Vector2i 	size = work.workArea.size;
										
					for (int y = 0; y < size.y; y++)
						for (int x = 0; x < size.x; x++)
						{
							Vector2i 	location = offset.add(x, y);
							Cell 		cell = new Cell(work.grid.getCell(location));
							Cell[] 		neighbours = work.grid.getNeighbours(location);
							
							rule.run(cell, neighbours);
							newGrid.setCell(x, y, cell);
						}
					
					work.grid = newGrid;
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
