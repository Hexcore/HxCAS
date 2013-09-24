package com.hexcore.cas.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.hexcore.cas.rulesystems.CALCompiler;
import com.hexcore.cas.utilities.Log;

/**
 * Class World
 * 	Contains all information about a world, as below:
 * 		- Generations :
 * 			No history - only current generation being displayed;
 * 			Memory history - all generations that memory can hold; and
 * 			Disk history - all generations from the very beginning.
 * 		- Rule and colour sets.
 * 	Loading and saving of a world is done by calling a WorldReader and WorldSaver,
 * 	or by WorldStreamer by default during disk history mode.
 * 
 * @author Divan Burger; Megan Duncan; Apurva Kumar
 */

public class World
{
	private static final String		TAG = "World";
	
	
	private int						genAmount = 0;
	private int						historyType = 1;
	private int						lastIn = -1;
	
	private List<Grid> 				worldGenerations = null;
	
	private String					colourCode = null;
	private String					ruleCode = null;
	private String					worldFileName = null;
	
	private WorldStreamer 			streamer = null;
	
	/**
	 * World default construtor.
	 */
	public World()
	{
		worldGenerations = Collections.synchronizedList(new ArrayList<Grid>());
		
		streamer = new WorldStreamer();
		
	}
	
	/**
	 * World copy constructor.
	 * 
	 * @param world - the world to be cloned
	 */
	public World(World world)
	{
		this.historyType = world.historyType;
		this.worldFileName = world.worldFileName;
		this.ruleCode = world.ruleCode;
		this.colourCode = world.colourCode;
		
		worldGenerations = Collections.synchronizedList(new ArrayList<Grid>());
		this.worldGenerations.clear();
		this.worldGenerations.addAll(world.worldGenerations);
		
		if(historyType == 2)
		{
			streamer = new WorldStreamer();
			streamer.start(this);
			lastIn = worldGenerations.size();
		}
	}
	
	/**
	 * Adds the given generation to the current list of generations.
	 * If current mode is no history, then previous generation is thrown away.
	 * If current mode is disk history, then previous generation is streamed to disk
	 * 	removed from the list.
	 * 
	 * @param gen - generation to be added
	 */
	public void addGeneration(Grid gen)
	{		
		if(historyType == 0)
		{
			genAmount++;
			
			worldGenerations.clear();
			worldGenerations.add(gen);
			/*
			if(worldGenerations.size() > 1)
				for(int i = 0; i < worldGenerations.size() - 1; i++)
					worldGenerations.remove(0);*/
		}
		else if(historyType == 1)
		{
			worldGenerations.add(gen);
		}
		else if(historyType == 2)
		{
			genAmount++;
			lastIn = genAmount - 1;
			
			Grid prevGen = (worldGenerations.size() > 0 ? worldGenerations.get(0) : null);
			streamer.streamGeneration(prevGen, gen);
			worldGenerations.clear();
			worldGenerations.add(gen);
			/*if(worldGenerations.size() == 1)
				streamer.streamGeneration(null, gen);
			else
				streamer.streamGeneration(worldGenerations.get(0), gen);
			
			if(worldGenerations.size() > 1)
				for(int i = 0; i < worldGenerations.size() - 1; i++)
					worldGenerations.remove(0);*/
		}
	}
	
	/**
	 * Clears the current list of generations up until the given generation number.
	 * If the current mode is no history or disk history, nothing is done. 
	 * 
	 * If the number of generations is less than the generation number, then the clear is unsuccessful.
	 * Otherwise, the clear is recorded as successful.
	 * 
	 * @param genNum - the generation number the must be clear up until
	 * @return - boolean value of whether the world was able to clear the history or not
	 */
	public boolean clearHistory(int genNum)
	{
		if(historyType == 0 || historyType == 2)
			return false;
		
		if(worldGenerations.size() <= genNum)
		{
			return false;
		}
		else
		{
			for(int i = 0; i < genNum; i++)
				worldGenerations.remove(0);
			return true;
		}
	}
	

	/**
	 * Returns the colour set code for the current world.
	 * 
	 * @return - a string of the colour set code
	 */
	public String getColourCode()
	{
		return colourCode;
	}
	
	/**
	 * Returns the file name for the current world.
	 * 
	 * @return - a string of the file name
	 */
	public String getFilename()
	{
		return worldFileName;
	}
	
	/**
	 * Returns the generation at the given index.
	 * 
	 * If the current mode is no history and the index is 0, the only generation stored is returned.
	 * 
	 * If the current mode is memory history:
	 * 	If the index is less than the size of the list of stored generations,
	 * 	then the generation at that index is returned otherwise null is returned.
	 * 
	 * If the current mode is disk history:
	 * 	If the number of generations of the world is 0, then null is returned.
	 * 	If the generation that is being requested is the last generation that was loaded, then
	 * 	the only generation stored is returned.
	 * 	Otherwise, the generation is read from disk and returned, which could be null if the generation
	 * 	was not found on disk.
	 * 
	 * Otherwise, null is returned.
	 * 
	 * @param genNum - the generation number
	 * @return - the generation that has the number of index
	 */
	public Grid getGeneration(int genNum)
	{
		if(genNum < 0)
			return null;
		
		if(historyType == 0 && genNum == 0)
			return worldGenerations.get(0);
		else if(historyType == 1)
		{
			if(genNum >= worldGenerations.size())
			{
				Log.error(TAG, "Error retrieving generation " + genNum + " - the generation was not found.");
				return null;
			}
			
			return worldGenerations.get(genNum);
		}
		else if(historyType == 2)
		{
			if(genAmount == 0)
				return null;
			
			if(genNum == lastIn && worldGenerations.size() > 0)
			{
				return worldGenerations.get(worldGenerations.size() - 1);
			}
			else
			{
				lastIn = genNum;
				worldGenerations.clear();
				worldGenerations.add(streamer.getGeneration(genNum));
				return worldGenerations.get(0);
			}
		}
		else
			return null;
	}
	
	/**
	 * Returns all the generations of the world.
	 * 
	 * If the current mode is disk history, all the generations are read from disk and returned.
	 * 
	 * Otherwise, the list of current generations of the world is returned, which could be a
	 * single generation is the current mode is no history or if simulation hasn't started yet.
	 * 
	 * @return - the list of generations
	 */
	public List<Grid> getGenerations()
	{
		if(historyType == 2 && streamer.hasStarted())
			return streamer.getGenerations();
		else
			return worldGenerations;
	}
	
	/**
	 * Returns the current mode of history for the world.
	 * 
	 * 0 is no history.
	 * 1 is memory history.
	 * 2 is disk history.
	 * 
	 * @return - returns the current history type
	 */
	public int getHistoryType()
	{
		return historyType;
	}
	
	/**
	 * Returns Generation Zero for the current world.
	 * 
	 * If there are currently no generations stored, null is returned.
	 * 
	 * If the current mode is disk history:
	 * 	If the number of generations for the world is 1 then the only stored generation is returned.
	 * 	Otherwise Generation Zero is read from disk and returned.
	 * 
	 * Otherwise the first stored generation in the list is returned, which for no history may not
	 * actually be Generation Zero.
	 * 
	 * @return - Generation Zero
	 */
	public Grid getInitialGeneration()
	{
		if(worldGenerations.isEmpty())
			return null;
		else if(historyType == 2 && streamer.hasStarted())
		{
			lastIn = 0;
			if(genAmount == 1)
			{
				return worldGenerations.get(0);
			}
			else
			{
				worldGenerations.clear();
				worldGenerations.add(streamer.getGeneration(0));
				return worldGenerations.get(0);
			}
		}
		else
			return worldGenerations.get(0);
	}
	
	/**
	 * Returns the last created generation for the current world.
	 * 
	 * If current mode is no history and there is a generation stored, that generation is returned.
	 * 
	 * If the current mode is memory and there is at least 1 generation stored, then the last generation
	 * in the list is returned.
	 * 
	 * If the current mode is disk history, the last written generation is read in an returned.
	 * 
	 * Otherwise null is returned.
	 * 
	 * @return - the last created generation
	 */
	public Grid getLastGeneration()
	{
		if(historyType == 0 && !worldGenerations.isEmpty())
			return worldGenerations.get(0);
		else if(historyType == 1 && !worldGenerations.isEmpty())
			return worldGenerations.get(worldGenerations.size() - 1);
		else if(historyType == 2)
		{
			lastIn = genAmount - 1;
			worldGenerations.clear();
			worldGenerations.add(streamer.getLastGeneration());
			return worldGenerations.get(0);
		}
		else
			return null;
	}
	
	/**
	 * Returns the number of generations that have been created for the current world.
	 * 
	 * If the current mode is no history or disk history, then the counter is returned.
	 * 
	 * If the current mode is memory history then the size of the list of stored generations
	 * is returned.
	 * 
	 * Otherwise -1 is returned.
	 * 
	 * @return - the number of created generations
	 */
	public int getNumGenerations()
	{
		if(historyType == 0 || historyType == 2)
			return genAmount;
		else if(historyType == 1)
			return worldGenerations.size();
		else
			return -1;
	}
	
	
	/**
	 * Returns the string of the current rule set.
	 * 
	 * @return - the rule set code
	 */
	public String getRuleCode()
	{
		return ruleCode;
	}
	
	
	
	/**
	 * Returns the name of the world as given by the file name.
	 * 
	 * @return - the string of the world name
	 */
	public String getWorldName()
	{
		if(worldFileName.lastIndexOf('/') != -1)
			return worldFileName.substring(worldFileName.lastIndexOf('/') + 1);
		else
			return worldFileName;
	}
	
	/**
	 * Returns either true if the streamer for the world has been started or false if the streamer
	 * has not been started. 
	 * 
	 * @return - the boolean on the streamer start status
	 */
	public boolean hasStarted()
	{
		return streamer.hasStarted();
	}
	
	/**
	 * Returns false is the current mode is no history, otherwise returns true.
	 * 
	 * @return - the boolean on the history mode
	 */
	public boolean isHistoryKept()
	{
		if(historyType == 0)
			return false;
		else
			return true;
	}
	
	/**
	 * Loads the world from disk using a WorldReader.
	 * 
	 * Returns true is the reader was successful in loading, otherwise returns false.
	 * 
	 * @return - the boolean on the success status
	 */
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
	
	/**
	 * Performs a hard reset on the world back to Generation Zero.
	 */
	public void reset()
	{
		Log.debug(TAG, "Hard reset.");
		
		Grid generationZero = null;
		if(historyType == 0 || historyType == 1)
			generationZero = worldGenerations.get(0).clone();
		else
			generationZero = streamer.getGeneration(0);
		
		worldGenerations.clear();
		worldGenerations.add(generationZero.clone());
		
		if(historyType == 2)
		{
			genAmount = 1;
			lastIn = 0;
			streamer.reset(this);
		}
	}
	
	/**
	 * Performs a reset on the world back to the generation given.
	 *
	 * @param gen - generation to be reset to
	 */
	public void resetTo(Grid gen)
	{
		Log.debug(TAG, "Resetting world to grid: " + gen.getWidth() + "x" + gen.getHeight());
		
		worldGenerations.clear();
		worldGenerations.add(gen.clone());
		
		if(historyType == 0 || historyType == 2)
		{
			genAmount = worldGenerations.size();
			lastIn = genAmount - 1;
		}
		
		if(historyType == 2)
			streamer.reset(this);
	}
	
	/**
	 * Performs a reset on the world to the given world.
	 * Could be considered a "clone reset".
	 * 
	 * @param world - world to be reset to
	 */
	public void resetTo(World world)
	{
		Log.debug(TAG, "Resetting world.");
		
		this.worldFileName = world.worldFileName;
		this.ruleCode = world.ruleCode;
		this.colourCode = world.colourCode;
		this.worldGenerations.clear();
		
		
		if(historyType != 0)
			this.worldGenerations.addAll(world.worldGenerations);
		else
			this.worldGenerations.add(world.worldGenerations.get(world.worldGenerations.size() - 1));
		
		if(historyType == 0 || historyType == 2)
		{
			genAmount = worldGenerations.size();
			lastIn = genAmount - 1;
		}
		
		if(historyType == 2)
			streamer.reset(this);
	}
	
	/**
	 * Sets the colour set code to the given colour set code string.
	 * 
	 * @param colourCode - the colour set code to be set to
	 */
	public void setColourCode(String colourCode)
	{
		this.colourCode = colourCode;
	}
	
	/**
	 * Sets the file name of the world to the given file name string.
	 * 
	 * @param name - the file name to be set to
	 */
	public void setFileName(String name)
	{
		int firstIndexOf = name.indexOf(".caw");
		int lastIndexOf = name.lastIndexOf(".caw");
		
		if(firstIndexOf == lastIndexOf)
			worldFileName = (name.endsWith(".caw") ? name : name + ".caw");
		else
			worldFileName = name.substring(0, firstIndexOf) + name.substring(lastIndexOf);
		
		if(historyType == 2 && streamer.hasStarted())
			streamer.setWorldFilename(worldFileName);
	}
	
	/**
	 * Sets the number of generations currently in the world to the given value.
	 * Used in modes no and disk history.
	 * 
	 * @param gA - the number of generations to be set to
	 */
	public void setGenAmount(int gA)
	{
		genAmount = gA;
	}
	
	/**
	 * Sets the current history mode to the given history type.
	 * 
	 * @param hT - the history type mode to be set to
	 */
	public void setKeepHistory(int hT)
	{
		historyType = hT;
	}
	
	public void setRuleCode(String ruleCode)
	{
		this.ruleCode = ruleCode;
	}
	

	
	/**
	 * Sets the world generations to the generations given.
	 * 
	 * @param generations - the list of generations to be added
	 */
	public void setWorldGenerations(Grid[] generations)
	{
		worldGenerations.clear();
		
		if(historyType == 0)
		{
			worldGenerations.add(generations[generations.length - 1]);
			return;
		}
		
		for(Grid grid : generations)
			worldGenerations.add(grid);
		
		if(historyType == 0 || historyType == 2)
			genAmount = worldGenerations.size();
		
		if(historyType == 2 && streamer.hasStarted())
			streamer.reset(this);
	}
	
	/**
	 * Saves the world into the file given by the file name.
	 * 
	 * @throws IOException
	 */
	public void save()
		throws IOException
	{
		WorldSaver ws = new WorldSaver();		
		ws.saveWorld(this);
	}
	
	/**
	 * Starts the world streamer for the world only if the current mode is disk history. 
	 */
	public void start()
	{
		if(historyType != 2)
			return;
		
		if(!streamer.hasStarted())
		{
			Log.debug(TAG, "Starting streamer");
			streamer.start(this);
		}
	}
	
	/**
	 * Stops the world streamer for the world only if the current mode is disk history. 
	 */
	public void stop()
	{
		if(historyType != 2)
			return;
		
		Log.debug(TAG, "stop() called");
		
		if(streamer.hasStarted())
			streamer.stop();
	}
	
}
