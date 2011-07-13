package com.hexcore.cas.model;

import java.io.File;
import java.io.FileOutputStream;
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
	
	public Grid saveWorld()
		throws IOException
	{
		FileOutputStream out = new FileOutputStream(new File(worldFileName));
		int x = world.getWidth();
		int y = world.getHeight();
		out.write(x + ' ');
		out.write(y);
		out.write("\r\n".getBytes());
		out.write(world.getType());
		out.write("\r\n".getBytes());
		for(int row = 0; row < y; row++)
		{
			for(int col = 0; col < x; col++)
			{
				Cell cell = world.getCell(col, row);
				int n = cell.getValueCount();
				out.write(n);
				for(int i = 0; i < n; i++)
				{
					out.write(' ' + cell.getValue(i));
				}
				out.write("\r\n".getBytes());
			}
			out.write("\r\n".getBytes());
		}
		return world;
	}
	
	public String getWorldName()
	{
		return worldFileName;
	}
	
	public void setWorldName(String name)
	{
		worldFileName = name;
	}
}
