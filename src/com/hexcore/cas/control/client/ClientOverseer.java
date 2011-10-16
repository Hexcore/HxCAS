package com.hexcore.cas.control.client;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.hexcore.cas.math.Recti;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.rulesystems.Rule;
import com.hexcore.cas.rulesystems.test.GameOfLifeRule;
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
	private boolean busy = false;
	private boolean valid = false;
	private int port = -1;

	public ClientOverseer(int port)
	{
		super();
		
		workQueue = new LinkedBlockingQueue<Work>();
		completedQueue = new LinkedBlockingQueue<Work>();
		
		rule = new GameOfLifeRule();
		
		this.port = port;
		//setup();
	}
	
	public int checkState()
	{
		if(workQueue.isEmpty())
			return 0;
		else
			return 1;
	}
	
	public void addGrid(Grid grid, Recti workArea, int id, int generation)
	{
		Work work = new Work();
		work.generation = generation;
		work.id = id;
		work.grid = grid.clone();
		work.workArea = new Recti(workArea);
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
		busy = false;
		informationProcessor.disconnect();
	}
	
	public void setRule(Rule rule)
	{
		Log.information(TAG, "Loaded new rule code");
		this.rule = rule;
	}
	
	public void stopRunning()
	{
		disconnect();
		running = false;
	}
	
	public void setup()
	{
		try
		{
			informationProcessor = new CAPIPClient(this, port);
			valid = informationProcessor.isValid();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		int cores = Runtime.getRuntime().availableProcessors();
		
		threads = new WorkerThread[cores];
		
		for (int i = 0; i < cores; i++) threads[i] = new WorkerThread(i);
				
		Log.information(TAG, "Ready");
		
	}
	
	@Override
	public void start()
	{
		setup();
		if (!valid)
		{
			Log.warning(TAG, "Socket not connected - Shutting down");
			disconnect();
			return;
		}
		
		Log.information(TAG, "Starting...");

		super.start();
	}
	
	@Override
	public void run()
	{
		running = true;
		while(running)
		{
			informationProcessor.start();	
			
			for (int i = 0; i < threads.length; i++) threads[i].start();
			
			busy = true;
			
			while (busy)
			{
				Work work = null;
				
				try
				{
					work = completedQueue.poll(500, TimeUnit.MILLISECONDS);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				
				if (work == null) continue;
				
				Log.information(TAG, "Sending completed work");
				int more = Math.max(Runtime.getRuntime().availableProcessors() + 1 - workQueue.size(), 0);			
				informationProcessor.sendResult(work.grid, work.workArea, more, work.id, work.generation);
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
			
			if(!running)
				break;
			
			setup();
		}
	}
	
	public void reset()
	{
		workQueue.clear();
		completedQueue.clear();
	}
	
	class Work
	{
		int		generation;
		int		id;
		Grid	grid;
		Recti	workArea;
	}
	
	class WorkerThread extends Thread
	{
		boolean running = false;
		
		public WorkerThread(int id)
		{
			super("Worker-" + id);
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
					Work work = workQueue.poll(1, TimeUnit.SECONDS);
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
