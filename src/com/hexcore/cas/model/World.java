package com.hexcore.cas.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.hexcore.cas.math.Vector2i;

public class World
{
	private List<Grid> worldGenerations = null;
	private Grid[] world = null;
	private String worldFileName = null;
	private String rulesAndColours = null;
	
	public World()
	{
		worldGenerations = Collections.synchronizedList(new ArrayList<Grid>());
	}
	
	public void addGeneration(Grid gen)
	{
		worldGenerations.add(gen);
	}
	
	public int getNumGenerations()
	{
		return worldGenerations.size();
	}
	
	public Grid getGeneration(int index)
	{
		return worldGenerations.get(index);
	}
	
	public Grid getLastGeneration()
	{
		if (worldGenerations.isEmpty()) return null;
		return worldGenerations.get(worldGenerations.size() - 1);
	}
	
	public String getRulesAndColours()
	{
		return rulesAndColours;
	}
	
	public Grid getGenerationZero()
	{
		return worldGenerations.get(0);
	}
	
	public Grid[] getWorld()
	{
		if(world != null)
			return world;
		
		if(worldGenerations.size() == 0)
			return null;
		
		char type = worldGenerations.get(0).getTypeSymbol();
		int len = worldGenerations.size();
		switch(type)
		{
			case 'r':
			case 'R':
				world = new RectangleGrid[len];
				break;
			case 'h':
			case 'H':
				world = new HexagonGrid[len];
				break;
			case 't':
			case 'T':
				world = new TriangleGrid[len];
				break;
			default:
				System.out.println("Unable to create a grid with no type.");
				return null;
		}
		
		worldGenerations.toArray(world);
		return world;
	}
	
	public String getWorldName()
	{
		if(worldFileName.lastIndexOf('/') != -1)
			return worldFileName.substring(worldFileName.lastIndexOf('/') + 1);
		else
			return worldFileName;
	}
	
	public void reset()
	{
		Grid g = worldGenerations.get(0).clone();
		worldGenerations.clear();
		worldGenerations.add(g.clone());
	}
	
	public void setRulesAndColours(String RAC)
	{
		rulesAndColours = RAC;
	}
	
	public void sendRulesAndColours()
	{
		//HexcoreVM h = new HexcoreVM();
		//h.loadRules(rulesAndColours);
	}

	public void setFileName(String name)
	{
		worldFileName = name;
	}

	public void setWorldGenerations(Grid[] w)
	{
		char type = w[0].getTypeSymbol();
		int len = w.length;
		switch(type)
		{
			case 'r':
			case 'R':
				world = new RectangleGrid[len];
				for(int i = 0; i < len; i++)
					world[i] = new RectangleGrid(w[i].getSize(), w[i].getCell(new Vector2i(0, 0)));
				break;
			case 'h':
			case 'H':
				world = new HexagonGrid[len];
				for(int i = 0; i < len; i++)
					world[i] = new HexagonGrid(w[i].getSize(), w[i].getCell(new Vector2i(0, 0)));
				break;
			case 't':
			case 'T':
				world = new TriangleGrid[len];
				for(int i = 0; i < len; i++)
					world[i] = new TriangleGrid(w[i].getSize(), w[i].getCell(new Vector2i(0, 0)));
				break;
			default:
				System.out.println("Unable to create a grid with no type.");
				return;
		}
		for(int i = 0; i < w.length; i++)
			world[i] = w[i].clone();
	}
	
	public void load()
		throws IOException
	{
		WorldReader wr = new WorldReader(this);
		wr.readWorld(worldFileName);
	}
	
	public void save()
		throws IOException
	{
		WorldSaver ws = new WorldSaver();
		char type = worldGenerations.get(0).getTypeSymbol();
		int len = worldGenerations.size();
		switch(type)
		{
			case 'r':
			case 'R':
				world = new RectangleGrid[len];
				for(int i = 0; i < len; i++)
					world[i] = new RectangleGrid(worldGenerations.get(i).getSize(), worldGenerations.get(i).getCell(new Vector2i(0, 0)));
				break;
			case 'h':
			case 'H':
				world = new HexagonGrid[len];
				for(int i = 0; i < len; i++)
					world[i] = new HexagonGrid(worldGenerations.get(i).getSize(), worldGenerations.get(i).getCell(new Vector2i(0, 0)));
				break;
			case 't':
			case 'T':
				world = new TriangleGrid[len];
				for(int i = 0; i < len; i++)
					world[i] = new TriangleGrid(worldGenerations.get(i).getSize(), worldGenerations.get(i).getCell(new Vector2i(0, 0)));
				break;
			default:
				System.out.println("Unable to create a grid with no type.");
				return;
		}
		worldGenerations.toArray(world);
		ws.saveWorld(worldFileName, world, rulesAndColours);
	}
}
