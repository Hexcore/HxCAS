package com.hexcore.cas.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Class WorldSaver
 * 	An instance of this object is used to write all details
 * 	of a world to where it is stored.
 * 
 * @author Megan Duncan
 */

public class WorldSaver
{
	public WorldSaver()
	{
	}
	
	public void saveWorld(World world)
		throws IOException
	{
		Grid firstGrid = world.getInitialGeneration();
		
		char type = firstGrid.getTypeSymbol();
		int gridHeight = firstGrid.getHeight();
		int gridWidth = firstGrid.getWidth();
		int properties = firstGrid.getNumProperties();
		
		/*
		 * Creates a ZIP file to persist the world, it's configuration,
		 * it's rule set and it's generations. 
		 */
		int exindex = world.getFilename().indexOf(".caw");
		File file = null;
		if(exindex != -1)
			file = new File(world.getFilename());
		else
			file = new File(world.getFilename() + ".caw");
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file));
		
		ZipEntry config = new ZipEntry("config.cac");
		out.putNextEntry(config);
		String configStr = gridWidth + " " + gridHeight + "\n";
		configStr += type + "\n";
		configStr += properties + "\n";
		out.write(configStr.getBytes());
		out.closeEntry();
		
		ZipEntry ruleCodeEntry = new ZipEntry("rules.car");
		out.putNextEntry(ruleCodeEntry);
		out.write(world.getRuleCode().getBytes());
		out.closeEntry();
		
		ZipEntry colourCodeEntry = new ZipEntry("colours.cacp");
		out.putNextEntry(colourCodeEntry);
		out.write(world.getColourCode().getBytes());
		out.closeEntry();
		
		List<Grid> generations = world.getGenerations();
		for (int i = 0; i < generations.size(); i++)
		{
			Grid grid = generations.get(i);
			ZipEntry caw = new ZipEntry(i + ".cag");
			out.putNextEntry(caw);
			String str = "";
			for(int rows = 0; rows < gridHeight; rows++)
			{
				for(int cols = 0; cols < gridWidth; cols++)
				{
					for(int index = 0; index < properties - 1; index++)
						str += grid.getCell(cols, rows).getValue(index) + " ";
					str += grid.getCell(cols, rows).getValue(properties - 1) + "\n";
				}
				str += "\n";
			}
			out.write(str.getBytes());
			out.closeEntry();
		}
		
		out.close();
	}
}
