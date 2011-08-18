package com.hexcore.cas.model;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.rulesystems.HexcoreVM;

public class World
{
	private Grid[] world = null;
	private String rulesAndColours = null;
	
	public World()
	{
	}
	
	public void setRulesAndColours(String RAC)
	{
		rulesAndColours = RAC;
	}
	
	public void setWorld(Grid[] w)
	{
		char type = w[0].getType();
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
		for(int pos = 0; pos < len; pos++)
		{
			for(int rows = 0; rows < w[pos].getHeight(); rows++)
			{
				for(int cols = 0; cols < w[pos].getWidth(); cols++)
				{
					int n = w[pos].getCell(cols, rows).getValueCount();
					for(int i = 0; i < n; i++)
						world[pos].getCell(cols, rows).setValue(i, w[pos].getCell(cols, rows).getValue(i));
				}
			}
		}
	}
	
	public String getRulesAndColours()
	{
		return rulesAndColours;
	}
	
	public Grid[] getWorld()
	{
		return world;
	}
	
	public void sendRulesAndColours()
	{
		HexcoreVM h = new HexcoreVM();
		//h.loadRules(rulesAndColours);
	}
}
