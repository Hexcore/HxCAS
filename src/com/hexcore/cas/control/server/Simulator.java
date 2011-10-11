package com.hexcore.cas.control.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.hexcore.cas.control.protocol.Overseer;
import com.hexcore.cas.math.Recti;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.HexagonGrid;
import com.hexcore.cas.model.RectangleGrid;
import com.hexcore.cas.model.ThreadWork;
import com.hexcore.cas.model.TriangleGrid;
import com.hexcore.cas.model.World;
import com.hexcore.cas.utilities.Log;

public class Simulator extends Overseer
{
	private static final String TAG = "Server";
	
	private String[] clientNames = null;
	
	private volatile AtomicBoolean isFinishedGenerations = new AtomicBoolean(false);
	private volatile AtomicBoolean paused = new AtomicBoolean(false);
	private volatile AtomicBoolean reset = new AtomicBoolean(false);
	
	private volatile int currGen = 0;
	private volatile int numOfClients = 0;
	private volatile int numOfGenerations = 0;
	private int threadWorkID = 0;
	
	private Recti[] clientWorkables = null;
	private ThreadWork[] clientWork = null;
	private World world = null;
	
	private CAPIPServer informationProcessor = null;
	private int clientPort;
	
	public Simulator(World world, int clientPort)
	{
		this.clientPort = clientPort;
		this.world = world;
	}
	
	/*
	 * Function done by Abby Kumar
	 */
	public static Recti[] divideToClients(Vector2i sizeOfGrid, int splittingFactor)
	{
		int temp;
		//calculate how many splits needed:
		if (sizeOfGrid.x > sizeOfGrid.y)
			temp = 1;
		else
			temp = -1;
		
		int x = 1, y = 1;
		int product = x * y;
		
		while(product < splittingFactor)
		{
			if (temp > 0)
			{
				x += x;
				temp = temp * (-1);
			}
			else if(temp < 0)
			{
				y += y;
				temp = temp * (-1);
			}
			product = x * y;
		}
		
		int splitx[] = new int [x+1];
		int splity[] = new int [y+1];
		
		for(int a = 0; a < x; a++)
		{
			splitx[a] = 0;
		}
		for(int a = 0; a < y; a++)
		{
			splity[a] = 0;
		}
		int i = 1, j = 1;
		
		double px = (double)sizeOfGrid.x/x;
		double py = (double)sizeOfGrid.y/y;
		
		int posx = 0;
		int posy = 0;
		
		double cx = 0.0;
		double cy = 0.0;
		
		while(i < splitx.length)
		{
			while((posx-cx) < px)
			{
					posx++;
			}
			splitx[i] = posx;
			i++;
			cx += px;
		}
		
		while(j < splity.length)
		{
			while((posy-cy) < py)
			{
					posy++;
			}
			splity[j] = posy;
			j++;
			cy += py;
		}
		Recti[] split = new Recti[product];
		int c = 0;
		
		for(int a = 0; a < splity.length-1; a++)
		{
			for(int b = 0; b < splitx.length-1; b++)
			{
				int widthx = splitx[b+1] - splitx[b];
				int widthy = splity[a+1] - splity[a];
				split[c++] = new Recti(new Vector2i(splitx[b],splity[a]), new Vector2i(widthx, widthy));
			}
		}
		
		return split;
	}
	
	public ThreadWork[] getClientWork()
	{
		return clientWork;
	}
	
	public String[] getClientNames()
	{
		return clientNames;
	}
	
	public Recti[] getClientWorkables()
	{
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
	
	public boolean isFinished()
	{
		return isFinishedGenerations.get();
	}
	
	public void calculateSplits()
	{
		Recti[] splits = divideToClients(grid.getSize(), informationProcessor.getTotalCoreAmount() * 2);
		clientWorkables = new Recti[splits.length];
		
		for(int i = 0; i < splits.length; i++)
			clientWorkables[i] = new Recti(splits[i].getPosition(), splits[i].getSize());
	}
	
	public void splitGrids()
	{
		if(grid.getWrappable())
			splitWrappableGrids();
		else
			splitNonwrappableGrids();
	}
	
	private void splitNonwrappableGrids()
	{
		int numOfClientWorks = clientWorkables.length;
		clientWork = new ThreadWork[numOfClientWorks];
		
		for(int i = 0; i < numOfClientWorks; i++)
		{
			Vector2i borderSize = grid.getNeighbourhoodRange();
			
			int left = borderSize.x;
			int right = borderSize.x;
			int top = borderSize.y;
			int bottom = borderSize.y;

			Recti workArea = clientWorkables[i];
			
			if(workArea.getPosition().x - left < 0)
				left = workArea.getPosition().x;
			if(workArea.getPosition().y - top < 0)
				top = workArea.getPosition().y;
			if(workArea.getSize().x + workArea.getPosition().x >= grid.getWidth())
				right = workArea.getSize().x + workArea.getPosition().x - grid.getWidth() - 1;
			if(workArea.getSize().y + workArea.getPosition().y >= grid.getHeight())
				bottom = workArea.getSize().y + workArea.getPosition().y - grid.getHeight() - 1;
			
			Vector2i size = new Vector2i(workArea.getSize().x + left + right, workArea.getSize().y + top + bottom);
			Grid workingGrid = grid.getType().create(size, grid.getNumProperties());
	
			int gYPos = 0;
			int gXPos = 0;
			
			for(int y = workArea.getPosition().y - top; y < workArea.getPosition().y + workArea.getSize().y + bottom; y++, gYPos++)
				for(int x = workArea.getPosition().x - left; x < workArea.getPosition().x + workArea.getSize().x + right; x++)
				{
					workingGrid.setCell(gXPos, gYPos, grid.getCell(x, y));
					gXPos++;
					if(gXPos >= workArea.getSize().x) gXPos = 0;
				}
			
			Recti aw = new Recti(new Vector2i(left, top), workArea.getSize());
			clientWork[i] = new ThreadWork(threadWorkID++, workingGrid, aw, currGen);
		}
	}
	
	private void splitWrappableGrids()
	{
		int numOfClientWorks = clientWorkables.length;
		clientWork = new ThreadWork[numOfClientWorks];
		
		for(int i = 0; i < numOfClientWorks; i++)
		{
			Vector2i borderSize = grid.getNeighbourhoodRange();
			Recti workArea = clientWorkables[i];
			Vector2i size = new Vector2i(workArea.getSize().x + (2 * borderSize.x), workArea.getSize().y + (2 * borderSize.y));
			
			Grid workingGrid = grid.getType().create(size, grid.getNumProperties());
	
			int gYPos = 0;
			int gXPos = 0;
			for(int y = workArea.getPosition().y - borderSize.y; y < workArea.getPosition().y + workArea.getSize().y + borderSize.y; y++)
			{
				for(int x = workArea.getPosition().x - borderSize.x; x < workArea.getPosition().x + workArea.getSize().x + borderSize.x; x++)
				{
					int xx = (grid.getWidth() + x) % grid.getWidth();
					int yy = (grid.getHeight() + y) % grid.getHeight();
					for(int j = 0; j < grid.getCell(xx, yy).getValueCount(); j++)
					{
						workingGrid.getCell(gXPos, gYPos).setValue(j, grid.getCell(xx, yy).getValue(j));
					}
					gXPos++;
					if(gXPos >= workArea.getSize().x + (borderSize.x * 2))
						gXPos = 0;
				}
				gYPos++;
			}
			
			Recti aw = new Recti(borderSize, workArea.getSize());
			clientWork[i] = new ThreadWork(threadWorkID++, workingGrid, aw, currGen);
		}
	}
	
	public void pause()
	{
		paused.set(true);
	}
	
	public void play()
	{		
		numOfGenerations = -1;
		
		if (paused.getAndSet(false)) 
			startGeneration();
	}
	
	public void step()
	{		
		numOfGenerations = 1;
		
		if (paused.getAndSet(false)) 
			startGeneration();
	}	
	
	public void rebuildGrid()
	{
		Log.debug(TAG, "Merging work done by clients");
		for(int i = 0; i < clientWorkables.length; i++)
		{
			Grid g = clientWork[i].getGrid();
			Recti aw = clientWork[i].getWorkableArea();
			Recti uw = clientWorkables[i];
			for(int y = uw.getPosition().y, cy = aw.getPosition().y; y < uw.getPosition().y + uw.getSize().y; y++, cy++)
				for(int x = uw.getPosition().x, cx = aw.getPosition().x; x < uw.getPosition().x + uw.getSize().x; x++, cx++)
					grid.setCell(x, y, g.getCell(cx, cy));
		}
	}
	
	public void requestStatuses()
	{
		Log.information(TAG, "Requesting client statuses");
		informationProcessor.updateClientStatus();
	}
	
	public void forceConnect(int index)
	{
		informationProcessor.forceConnect(index);
	}
	
	public void reset()
	{
		reset.set(true);
		numOfGenerations = 0;
		world.reset();
		informationProcessor.setGeneration(currGen);
	}

	//Called from CAPIPServer to pass up work done
	public void setClientWork(List<ThreadWork> completedWork)
	{
		clientWork = completedWork.toArray(clientWork);
		threadWorkID = 0;
		
		rebuildGrid();
		world.addGeneration(grid);
		
		splitGrids();
		
		Log.information(TAG, "Finished generation: " + currGen);
		
		if (reset.getAndSet(false))
		{
			paused.set(true);
			setGrid(world.getGenerationZero());
			calculateSplits();
			splitGrids();
		}
		
		if (!paused.get()) startGeneration();
	}

	public void setClientNames(ArrayList<String> names)
	{
		int size = names.size();
		clientNames = new String[size];
		names.toArray(clientNames);
		numOfClients = size;
	}
		
	public void simulate(int gN)
	{
		Log.information(TAG, "Waiting for clients to send connect...");
		while (informationProcessor.getConnectedAmount() != clientNames.length) {}

		Log.information(TAG, "Starting simulation...");
		super.setGrid(world.getLastGeneration());
		numOfGenerations = gN;
		
		calculateSplits();
		splitGrids();
		
		paused.set(false);
		isFinishedGenerations.set(false);
		currGen = 0;
		
		startGeneration();
	}
	
	public void startGeneration()
	{
		isFinishedGenerations.set(false);
		
		currGen++;
		GenerationThread generationThread = new GenerationThread();
		generationThread.start();		
	}
	
	public void disconnect()
	{
		informationProcessor.disconnect();
	}
	
	@Override
	public void run()
	{
		informationProcessor = new CAPIPServer(this, clientPort);
		informationProcessor.connectClients(clientNames);
		informationProcessor.start();
	}
	
	class GenerationThread extends Thread
	{
		@Override
		public void run()
		{			
			if (numOfGenerations == 0)
			{
				System.out.println("Finished!");
				isFinishedGenerations.set(true);
				paused.set(true);
				return;
			}
			
			if (numOfGenerations > 0) numOfGenerations--;
			
			informationProcessor.setClientWork(clientWork, currGen);
			informationProcessor.sendInitialGrids();
		}
	}
}
