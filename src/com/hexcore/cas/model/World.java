package com.hexcore.cas.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.hexcore.cas.rulesystems.CALCompiler;
import com.hexcore.cas.rulesystems.Rule;
import com.hexcore.cas.rulesystems.RuleLoader;
import com.hexcore.cas.utilities.Log;

/**
 * Class World
 * 	Contains all information about a world, as below:
 * 		- Generations :
 * 			No history - only current generation being displayed;
 * 			Memory history - all generations that memory can hold; and
 * 			Disk history - all generations from the very beginning.
 * 		- Rule and colour sets.
 * 	Load and saving of a world by calling a WorldReader and WorldSaver.
 * 
 * @author Divan Burger; Megan Duncan; Apurva Kumar
 */

public class World
{
	private static final String		TAG = "World";
	
	private ArrayList<String>		ruleCodes;
	
	private int						currEngineStep = -1;
	private int						historyType = 1;
	private int						genAmount = 0;
	private int						lastIn = -1;
	
	private List<Grid> 				worldGenerations = null;
	
	private String					colourCode = null;
	private String					worldFileName = null;
	
	private WorldStreamer 			streamer = null;
	
	public World()
	{
		worldGenerations = Collections.synchronizedList(new ArrayList<Grid>());
		
		streamer = new WorldStreamer();
		
		ruleCodes = new ArrayList<String>();
	}
	
	public World(World w)
	{
		this.historyType = w.historyType;
		this.worldFileName = w.worldFileName;
		this.colourCode = w.colourCode;
		this.worldGenerations.clear();
		this.worldGenerations.addAll(w.worldGenerations);
		
		this.ruleCodes.clear();
		this.ruleCodes.addAll(w.ruleCodes);
		this.currEngineStep = w.currEngineStep;
		
		if(historyType == 2)
		{
			streamer = new WorldStreamer();
			streamer.start(this);
			lastIn = worldGenerations.size();
		}
	}
	
	public void addGeneration(Grid gen)
	{
		worldGenerations.add(gen);
		currEngineStep = (currEngineStep + 1) % ruleCodes.size();
		
		if(historyType == 0)
		{
			genAmount++;
			
			if(worldGenerations.size() > 1)
				for(int i = 0; i < worldGenerations.size() - 1; i++)
					worldGenerations.remove(0);
		}
		else if(historyType == 2)
		{
			genAmount++;
			lastIn = genAmount - 1;
			
			if(worldGenerations.size() > 1)
				for(int i = 0; i < worldGenerations.size() - 1; i++)
					worldGenerations.remove(0);
			
			streamer.streamGeneration(gen);
		}
	}
	
	public boolean clearHistory(int genNumber)
	{
		if(historyType == 0 || historyType == 2)
			return false;
		
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
	
	public ArrayList<String> compareRulesets()
	{
		ArrayList<String> results = new ArrayList<String>();
		
		String ruleset0 = ruleCodes.get(0);
		int ruleset0Typecount = 0;
		ArrayList<String> ruleset0Properties = new ArrayList<String>();
		ArrayList<String> ruleset0Typenames = new ArrayList<String>();
		
		//Get information on ruleset 0
		int begin = 0, end = 0;
		
		//Typecount amount
		begin = ruleset0.indexOf("typecount ");
		end = ruleset0.indexOf(";", begin);
		ruleset0Typecount = Integer.parseInt(ruleset0.substring(begin + "typecount ".length(), end));

		//Property names
		begin = end;
		while(true)
		{
			begin = ruleset0.indexOf("property ", begin);
			if(begin == -1)
				break;
			end = ruleset0.indexOf(";", begin);
			ruleset0Properties.add(ruleset0.substring(begin + "property ".length(), end));
			begin = end;
		}

		//Typenames
		while(true)
		{
			begin = ruleset0.indexOf("type ", begin);
			if(begin == -1)
				break;
			end = ruleset0.indexOf("\n", begin);
			ruleset0Typenames.add(ruleset0.substring(begin + "type ".length(), end));
			begin = end;
		}
		
		for(int i = 1; i < ruleCodes.size(); i++)
		{
			String ruleseti = ruleCodes.get(i);
			int rulesetiTypecount = 0;
			ArrayList<String> rulesetiProperties = new ArrayList<String>();
			ArrayList<String> rulesetiTypenames = new ArrayList<String>();
			
			begin = 0;
			end = 0;
			
			//Typecount amount
			begin = ruleseti.indexOf("typecount ");
			end = ruleseti.indexOf(";", begin);
			rulesetiTypecount = Integer.parseInt(ruleseti.substring(begin + "typecount ".length(), end));

			//Property names
			begin = end;
			while(true)
			{
				begin = ruleseti.indexOf("property ", begin);
				if(begin == -1)
					break;
				end = ruleseti.indexOf(";", begin);
				rulesetiProperties.add(ruleseti.substring(begin + "property ".length(), end));
				begin = end;
			}

			//Typenames
			while(true)
			{
				begin = ruleseti.indexOf("type ", begin);
				if(begin == -1)
					break;
				end = ruleseti.indexOf("\n", begin);
				rulesetiTypenames.add(ruleseti.substring(begin + "type ".length(), end));
				begin = end;
			}
			
			if(rulesetiTypecount != ruleset0Typecount)
				results.add("Ruleset " + (i + 1) + " has a different typecount to the first ruleset.[typecount of " + rulesetiTypecount + "]");
			if(rulesetiProperties.size() != ruleset0Properties.size())
				results.add("Ruleset " + (i + 1) + " has a different amount of properties to the first ruleset.[" + rulesetiProperties.size() + " properties]");
			else
			{
				for(int j = 0; j < rulesetiProperties.size(); j++)
				{
					if(rulesetiProperties.get(j).compareTo(ruleset0Properties.get(j)) != 0)
						results.add("Ruleset " + (i + 1) + " has a different property to the first ruleset.[property " + (j + 1) + ":" + rulesetiProperties.get(j) + "]");
				}
			}
			if(rulesetiTypenames.size() != ruleset0Typenames.size())
				results.add("Ruleset " + (i + 1) + " has a different amount of types specified to the first ruleset.[" + rulesetiProperties.size() + " properties]");
			else
			{
				for(int j = 0; j < rulesetiTypenames.size(); j++)
				{
					if(rulesetiTypenames.get(j).compareTo(ruleset0Typenames.get(j)) != 0)
						results.add("Ruleset " + (i + 1) + " has a different type to the first ruleset.[type " + (j + 1) + ":" + rulesetiTypenames.get(j) + "]");
				}
			}
		}
		
		return results;
	}
	
	public String getColourCode()
	{
		return colourCode;
	}
	
	public String getFilename()
	{
		return worldFileName;
	}
	
	public Grid getGeneration(int index)
	{
		if(historyType == 0 && index == 0)
			return worldGenerations.get(0);
		else if(historyType == 1)
		{
			if(index >= worldGenerations.size())
			{
				Log.error(TAG, "Generation " + index + " not found!");
				return null;
			}
			
			return worldGenerations.get(index);
		}
		else if(historyType == 2)
		{
			if(genAmount == 0)
				return null;
			
			if(index == lastIn && worldGenerations.size() > 0)
			{
				return worldGenerations.get(worldGenerations.size() - 1);
			}
			else
			{
				lastIn = index;
				worldGenerations.clear();
				worldGenerations.add(streamer.getGeneration(index));
				return worldGenerations.get(0);
			}
		}
		else
			return null;
	}
	
	public List<Grid> getGenerations()
	{
		if(historyType == 2 && streamer.hasStarted())
			return streamer.getGenerations();
		else
			return worldGenerations;
	}
	
	public int getHistoryType()
	{
		return historyType;
	}
	
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
	
	public int getNumGenerations()
	{
		if(historyType == 0 || historyType == 2)
			return genAmount;
		else if(historyType == 1)
			return worldGenerations.size();
		else
			return -1;
	}
	
	public byte[] getRuleByteCode()
	{
		String code = ruleCodes.get(currEngineStep);
		
		CALCompiler compiler = new CALCompiler();
		compiler.compile(code);
		
		return compiler.getCode();
	}
	
	public String getRuleCode()
	{
		return ruleCodes.get(currEngineStep);
	}
	
	public String getRuleCode(int index)
	{
		return ruleCodes.get(index);
	}
	
	public String getRuleCode(String firstLine)
	{
		for(int i = 0; i < ruleCodes.size(); i++)
			if(ruleCodes.get(i).indexOf(firstLine) != -1)
				return ruleCodes.get(i);
		
		return null;
	}
	
	public ArrayList<String> getRuleCodes()
	{
		return ruleCodes;
	}
	
	public int getStepAmount()
	{
		return ruleCodes.size();
	}
	
	public String getWorldName()
	{
		if(worldFileName.lastIndexOf('/') != -1)
			return worldFileName.substring(worldFileName.lastIndexOf('/') + 1);
		else
			return worldFileName;
	}
	
	public boolean hasStarted()
	{
		return streamer.hasStarted();
	}
	
	public boolean isHistoryKept()
	{
		if(historyType == 0)
			return false;
		else
			return true;
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
	
	public boolean removeRuleCode(String name)
	{
		boolean removed = false;
		
		for(int i = 0; i < ruleCodes.size(); i++)
		{
			if(ruleCodes.get(i).indexOf("ruleset " + name) != -1)
			{
				ruleCodes.remove(i);
				removed = true;
				break;
			}
		}
		
		return removed;
	}
	
	public void reset()
	{
		Log.debug(TAG, "Hard reset.");
		
		Grid g = null;
		if(historyType == 0 || historyType == 1)
			g = worldGenerations.get(0).clone();
		else
			g = streamer.getGeneration(0);
		
		worldGenerations.clear();
		worldGenerations.add(g.clone());
		
		if(historyType == 2)
		{
			genAmount = 1;
			lastIn = 0;
			streamer.reset(this);
		}
	}
	
	public void resetTo(Grid g)
	{
		Log.debug(TAG, "Resetting world to grid: " + g.getWidth() + "x" + g.getHeight());
		
		worldGenerations.clear();
		worldGenerations.add(g.clone());
		
		if(historyType == 0 || historyType == 2)
		{
			genAmount = worldGenerations.size();
			lastIn = genAmount - 1;
		}
		
		if(historyType == 2)
			streamer.reset(this);
	}
	
	public void resetTo(World w)
	{
		Log.debug(TAG, "Resetting world.");
		
		this.worldFileName = w.worldFileName;
		this.colourCode = w.colourCode;
		this.worldGenerations.clear();
		
		this.ruleCodes.clear();
		this.ruleCodes.addAll(w.ruleCodes);
		this.currEngineStep = w.currEngineStep;
		
		if(historyType != 0)
			this.worldGenerations.addAll(w.worldGenerations);
		else
			this.worldGenerations.add(w.worldGenerations.get(w.worldGenerations.size() - 1));
		
		if(historyType == 0 || historyType == 2)
		{
			genAmount = worldGenerations.size();
			lastIn = genAmount - 1;
		}
		
		if(historyType == 2)
			streamer.reset(this);
	}
	
	public void setColourCode(String colourCode)
	{
		this.colourCode = colourCode;
	}
	
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
	
	public void setGenAmount(int gA)
	{
		genAmount = gA;
	}
	
	public void setKeepHistory(int hT)
	{
		historyType = hT;
	}
	
	public void setRuleCodes(int steps)
	{
		if(ruleCodes.size() == steps)
			return;
		
		String gameOfLifeRulesBegin = "ruleset GameOfLife";
		String gameOfLifeRulesEnd = "\n{\n\ttypecount 1;\n\tproperty alive;\n\n\ttype Land\n\t{\n\t\tvar c = sum(neighbours.alive);\n\t\tif ((c < 2) || (c > 3))\n\t\t\tself.alive = 0;\n\t\telse if (c == 3)\n\t\t\tself.alive = 1;\t\t\n\t}\n}";
		
		if(ruleCodes.size() < steps)
			for(int i = ruleCodes.size(); i < steps; i++)
			{
				String code = gameOfLifeRulesBegin + i + gameOfLifeRulesEnd;
				ruleCodes.add(code);
			}
		else
			for(int i = ruleCodes.size() - 1; i >= steps; i--)
				ruleCodes.remove(i);

		currEngineStep = (currEngineStep + 1) % ruleCodes.size();
	}
	
	public void setRuleCodes(ArrayList<String> list)
	{
		if(list.size() == 0)
			setRuleCodes(1);
		
		ruleCodes.clear();
		if(list.size() == 1)
		{
			ruleCodes.addAll(list);
			currEngineStep = (currEngineStep + 1) % ruleCodes.size();
			return;
		}

		int i = 1;
		int j = 0;

		while(j < list.size())
		{
			String currCode = list.get(j);
			String numberStr = currCode.substring(0, currCode.indexOf(":"));
			int number = Integer.parseInt(numberStr);
			if(number == i)
			{
				ruleCodes.add(currCode.substring(currCode.indexOf(":") + 1));
				j++;
			}
			
			i++;
		}

		currEngineStep = (currEngineStep + 1) % ruleCodes.size();
	}
	
	public void setWorldGenerations(Grid[] w)
	{
		worldGenerations.clear();
		
		if(historyType == 0)
		{
			worldGenerations.add(w[w.length - 1]);
			return;
		}
		
		for(Grid grid : w) worldGenerations.add(grid);
		
		if(historyType == 0 || historyType == 2)
			genAmount = worldGenerations.size();
		
		if(historyType == 2 && streamer.hasStarted())
			streamer.reset(this);
	}
	
	public void save()
		throws IOException
	{
		WorldSaver ws = new WorldSaver();		
		ws.saveWorld(this);
	}
	
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
	
	public void stop()
	{
		if(historyType != 2)
			return;
		
		Log.debug(TAG, "stop() called");
		
		if(streamer.hasStarted())
			streamer.stop();
	}
	
	public void updateRuleCode(String code, int index)
	{
		ruleCodes.remove(index);
		ruleCodes.add(index, code);
	}
}
