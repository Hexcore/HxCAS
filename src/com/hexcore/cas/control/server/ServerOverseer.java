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
import com.hexcore.cas.model.World;
import com.hexcore.cas.utilities.Log;

public class ServerOverseer extends Overseer
{
	private ArrayList<String> clientNames = null;
	private boolean isFinishedGenerations = false;
	private volatile boolean isFinishedWork = false;
	private int numOfClients = 0;
	private int numOfGenerations = 0;
	private int threadWorkID = 0;
	private int[] clientStatuses = null;
	private Recti[] clientWorkables = null;
	private static final String TAG = "Server";
	private ThreadWork[] clientWork = null;
	private World theWorld = null;
	
	public ServerOverseer(World w)
	{
		super();
		theWorld = w;
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
	
	public ArrayList<String> getClientNames()
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
		return isFinishedGenerations;
	}
	
	//===== NEEDS REVIEWING =====\\
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
					if(xx < 0 || yy < 0)
						continue;
					for(int j = 0; j < grid.getCell(xx, yy).getValueCount(); j++)
						workingGrid.getCell(gXPos, gYPos).setValue(j, grid.getCell(xx, yy).getValue(j));
					gXPos++;
					if(gXPos >= w.getSize().x + (width * 2))
						gXPos = 0;
				}
				gYPos++;
			}
			
			int x = (w.getPosition().x != 0) ? 1 : 0;
			int y = (w.getPosition().y != 0) ? 1 : 0;
			Recti aw = new Recti(new Vector2i(x, y), w.getSize());
			switch(workingGrid.getType())
			{
				case 't':
				case 'T':
					x += (w.getPosition().x != 0 && w.getPosition().x != 1) ? 1 : 0;
					aw.setPosition(new Vector2i(x, y));
					break;
			}

			clientWork[i] = new ThreadWork(workingGrid, aw, threadWorkID++);
		}
	}
	
	public void makeWrappableGrids()
	{
		int numOfClientWorks = clientWorkables.length;
		clientWork = new ThreadWork[numOfClientWorks];
		
		for(int i = 0; i < numOfClientWorks; i++)
		{
			Grid workingGrid = null;
			int width = 1;
			int height = 1;

			Recti w = clientWorkables[i];
			Vector2i size = new Vector2i(w.getSize().x + (2 * width), w.getSize().y + (2 * height));
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
					width += 1;
					size = new Vector2i(w.getSize().x + (2 * width), w.getSize().y + (2 * height));
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

			clientWork[i] = new ThreadWork(workingGrid, aw, threadWorkID++);
		}
	}
	
	public void rebuildGrid()
	{
		for(int i = 0; i < clientWorkables.length; i++)
		{
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
	
	public void requestStatuses()
	{
		Log.information(TAG, "Requesting client statuses");
		for(int i = 0; i < numOfClients; i++)
			((CAPIPServer)capIP).sendQuery(i);
	}
	
	public void send()
	{
		isFinishedWork = false;
		((CAPIPServer)capIP).setClientWork(clientWork);
		((CAPIPServer)capIP).sendGrids();
	}
	
	//FOR TESTING PURPOSES ONLY
	public void sendManualConnect(int index)
	{
		Log.information(TAG, "Sending manual CONNECT message");
		((CAPIPServer)capIP).sendConnect(index);
	}
	
	//Called from CAPIPServer to pass up work done
	public void setClientWork(ArrayList<ThreadWork> CW)
	{
		int size = CW.size();
		clientWork = new ThreadWork[size];
		for(int i = 0; i < size; i++)
			clientWork[i] = CW.get(i).clone();
		threadWorkID = 0;
		CW.clear();
		
		rebuildGrid();
		theWorld.addGeneration(grid);
		
		if(grid.getWrappable())
			makeWrappableGrids();
		else
			makeNonwrappableGrids();
		isFinishedWork = true;
		
	}

	public void setClientNames(ArrayList<String> names)
	{
		clientNames = names;
		numOfClients = clientNames.size();
		clientStatuses = new int[numOfClients];
	}
	
	public void setClientWorkables()
	{
		Recti[] split = divideToClients(grid.getSize(), ((CAPIPServer)capIP).getTotalCoreAmount() * 2);
		clientWorkables = new Recti[split.length];
		for(int i = 0; i < split.length; i++)
			clientWorkables[i] = new Recti(split[i].getPosition(), split[i].getSize());
		if(grid.getWrappable())
			makeWrappableGrids();
		else
			makeNonwrappableGrids();
	}
	
	public void setStatus(int index, int status)
	{
		clientStatuses[index] = status;
	}

	public void simulate(Grid g, int gN)
	{
		super.setGrid(g);
		theWorld.addGeneration(g);
		numOfGenerations = gN;
		
		setClientWorkables();
		
		/*((CAPIPServer)capIP).reset();
		
		while(!((CAPIPServer)capIP).isReset())
		{
		}*/
		
		isFinishedGenerations = false;
		for(int i = 0; i < numOfGenerations; i++)
		{
			send();
			while(!isFinishedWork)
			{
			}
		}
		isFinishedGenerations = true;
	}
	
	@Override
	public void run()
	{
		capIP = new CAPIPServer(this, clientNames);
		capIP.start();
	}
}
