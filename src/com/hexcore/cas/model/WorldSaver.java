package com.hexcore.cas.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WorldSaver
{
	private String worldFileName = null;
	private Grid world = null;
	
	public WorldSaver(String name, Grid w)
	{
		worldFileName = name;
		world = w;
	}
	
	public void saveWorld()
		throws IOException
	{
		FileWriter out = new FileWriter(new File(worldFileName));
		int x = world.getWidth();
		int y = world.getHeight();
		String str = "";
		str = x + " " + y + "\r\n" + world.getType() + "\r\n";
		out.write(str);
		for(int row = 0; row < y; row++)
		{
			for(int col = 0; col < x; col++)
			{
				Cell cell = world.getCell(col, row);
				int n = cell.getValueCount();
				str = n + "";
				out.write(str);
				for(int i = 0; i < n; i++)
				{
					str = " " + cell.getValue(i);
					out.write(str);
				}
				out.write("\r\n");
			}
			out.write("\r\n");
		}
		out.close();
	}
	
	public String getWorldName()
	{
		return worldFileName.substring(worldFileName.indexOf('/') + 1);
	}
	
	public void setWorldName(String name)
	{
		worldFileName = name;
	}
	
	public void setWorld(Grid w)
	{
		world = w;
	}
}
