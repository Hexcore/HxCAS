package com.hexcore.cas.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.hexcore.cas.utilities.Log;
/**
 * Class World
 * Contains all information about a world.
 * Generations - end and first, rules, colours,
 * load and saving of a world by calling a worldLoader
 * and WorldSaver function.
 * 
 * @author Megan
 *
 */
public class World
{
	private int historyType = 1;
	private List<Grid> worldGenerations = null;
	private String worldFileName = null;
	private String ruleCode = null;
	private String colourCode = null;
	private WorldStreamer streamer = null;
	
	public World()
	{
		worldGenerations = Collections.synchronizedList(new ArrayList<Grid>());
	}
	
	public World(World w)
	{
		this.historyType = w.historyType;
		this.worldFileName= w.worldFileName;
		this.ruleCode= w.ruleCode;
		this.colourCode= w.colourCode;
		this.worldGenerations.addAll(w.worldGenerations);
		streamer.reset(this);
	}
	
	public void addGeneration(Grid gen)
	{
		worldGenerations.add(gen);
		
		if(historyType == 0 || historyType == 2)
		{
			for(int i = 0; i < worldGenerations.size() - 1; i++)
				worldGenerations.set(i, null);
		}
		
		if(historyType == 2)
		{
			streamer.streamGeneration(gen, streamer.getNumGenerations());
		}
	}
	
	public boolean clearHistory()
	{
		if(historyType != 2)
			return false;
		
		worldGenerations.clear();
		return true;
	}
	
	public boolean clearHistory(int genNumber)
	{
		if(worldGenerations.size() <= genNumber)
		{
			return false;
		}
		else
		{
			for(int i = 0; i < genNumber; i++)
				worldGenerations.remove(0);
			return true;
		}
	}
	
	public int getNumGenerations()
	{
		if(historyType != 2)
			return worldGenerations.size();
		
		return streamer.getNumGenerations();
	}
	
	public List<Grid> getGenerations()
	{
		if(historyType != 2)
			return worldGenerations;
		
		return streamer.getGenerations();
	}
	
	public Grid getGeneration(int index)
	{
		if(historyType != 2)
			return worldGenerations.get(index);
		
		return streamer.streamGeneration(index);
	}
	
	public int getHistoryType()
	{
		return historyType;
	}
	
	public Grid getLastGeneration()
	{
		if(historyType != 2)
		{
			if (worldGenerations.isEmpty()) return null;
			return worldGenerations.get(worldGenerations.size() - 1);
		}
		
		return streamer.getLastGeneration();
	}
	
	public String getRuleCode()
	{
		return ruleCode;
	}
	
	public String getColourCode()
	{
		return colourCode;
	}
	
	public Grid getInitialGeneration()
	{
		if(historyType != 2)
			return worldGenerations.get(0);
		
		return streamer.streamGeneration(0);
	}
	
	public String getFilename()
	{
		return worldFileName;
	}
	
	public String getWorldName()
	{
		if(worldFileName.lastIndexOf('/') != -1)
			return worldFileName.substring(worldFileName.lastIndexOf('/') + 1);
		else
			return worldFileName;
	}
	
	public boolean isHistoryKept()
	{
		if(historyType == 0)
			return false;
		
		return true;
	}
	
	public void reset()
	{
		Grid g = worldGenerations.get(0).clone();
		worldGenerations.clear();
		worldGenerations.add(g.clone());
		
		streamer.reset(this);
	}
	
	public void resetTo(Grid g)
	{
		Log.debug("World", "Grid: " + g.getWidth() + "x" + g.getHeight());
		
		worldGenerations.clear();
		worldGenerations.add(g.clone());
		
		streamer.reset(this);
	}
	
	public void setRuleCode(String ruleCode)
	{
		this.ruleCode = ruleCode;
	}	
	
	public void setColourCode(String colourCode)
	{
		this.colourCode = colourCode;
	}

	public void setFileName(String name)
	{
		worldFileName = name;
	}

	public void setKeepHistory(int hT)
	{
		historyType = hT;
	}
	
	public void setupStreaming()
	{
		streamer = new WorldStreamer();
		streamer.start(this);
	}
	
	public void setWorldGenerations(Grid[] w)
	{
		worldGenerations.clear();
		if(historyType != 2)
			for (Grid grid : w) worldGenerations.add(grid);
		else
			for(int i = 0; i < w.length; i++)
				streamer.streamGeneration(w[i], i);
	}
	
	public boolean load()
	{
		try
		{
			WorldReader wr = new WorldReader(this);
			return wr.readWorld(worldFileName);
		}
		catch (IOException e)
		{
			return false;
		}
	}
	
	public void save()
		throws IOException
	{
		WorldSaver ws = new WorldSaver();		
		ws.saveWorld(this);
	}
}
