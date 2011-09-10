package com.hexcore.cas.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class WorldSaver
{
	public WorldSaver()
	{
	}
	
	public void saveWorld(String worldFileName, Grid[] world, String rulesAndColours)
		throws IOException
	{
		char type = world[0].getType();
		int y = world[0].getHeight();
		int x = world[0].getWidth();
		int n = world[0].getCell(0, 0).getValueCount();
		
		/*
		 * Creates a ZIP file to persist the world, it's configuration,
		 * it's rule set and it's generations. 
		 */
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(new File(worldFileName)));
		
		ZipEntry config = new ZipEntry("config.cac");
		out.putNextEntry(config);
		String configStr = x + " " + y + "\r\n";
		configStr += type + "\r\n";
		configStr += n + "\r\n";
		out.write(configStr.getBytes());
		out.closeEntry();
		
		ZipEntry RAC = new ZipEntry("rules.car");
		out.putNextEntry(RAC);
		out.write(rulesAndColours.getBytes());
		out.closeEntry();
		
		for(int i = 0; i < world.length; i++)
		{
			ZipEntry caw = new ZipEntry(i + ".cag");
			out.putNextEntry(caw);
			String str = "";
			for(int rows = 0; rows < y; rows++)
			{
				for(int cols = 0; cols < x; cols++)
				{
					for(int index = 0; index < (n - 1); index++)
						str += world[i].getCell(cols, rows).getValue(index) + " ";
					str += world[i].getCell(cols, rows).getValue(n - 1) + "\r\n";
				}
				str += "\r\n";
			}
			out.write(str.getBytes());
			out.closeEntry();
		}
		
		out.close();
	}
}
