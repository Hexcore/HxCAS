package com.hexcore.cas.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.utilities.Log;

public class WorldReader
{
	private static final String TAG = "WorldReader";
	
	private World world = null;
	
	public WorldReader(World w)
	{
		world = w;
	}
	
	public boolean readWorld(String worldFileName)
		throws IOException
	{
		/*
		 * Takes the name of the world zip file and reads in.
		 * It will specifically look for the configuration file first,
		 * then the rules set and then all the generation files.
		 */
		String ruleCode = null;
		String colourCode = null;	
		
		File f = new File(worldFileName);
		if (!f.exists())
		{
			Log.error(TAG, "Error loading world - Could not find file: " + worldFileName);
			return false;
		}
		
		ZipFile zip = null;
		try
		{
			zip = new ZipFile(f);
		}
		catch (ZipException e)
		{
			Log.error(TAG, "Error loading world - " + e.getMessage());
			return false;
		}
		
		Enumeration<? extends ZipEntry> generationFiles = zip.entries();
		
		int x = -1;
		int y = -1;
		char type = 'N';
		int properties = 0;
		while(true)
		{
			if(!generationFiles.hasMoreElements())
			{
				System.out.println("Configuration file not found.");
				return false;
			}
			ZipEntry config = (ZipEntry)generationFiles.nextElement();
			if (config.getName().endsWith(".cac"))
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(zip.getInputStream(config)));
				String line;
				line = in.readLine();
				if(line.indexOf(" ") != -1)
				{
					x = Integer.parseInt(line.substring(0, line.indexOf(" ")));
					y = Integer.parseInt(line.substring(line.indexOf(" ") + 1));
				}
				else
				{
					x = Integer.parseInt(line);
					line = in.readLine();
					y = Integer.parseInt(line);
				}
				
				line = in.readLine();
				type = line.charAt(0);
				line = in.readLine();
				properties = Integer.parseInt(line);
				break;
			}
		}
		
		Grid[] worldGenerations = null;
		Vector2i gridSize = new Vector2i(x, y);
		Cell cell = new Cell(properties);
		switch(type)
		{
			case 'r':
			case 'R':
				worldGenerations = new RectangleGrid[zip.size() - 2];
				for(int i = 0; i < zip.size() - 2; i++)
					worldGenerations[i] = new RectangleGrid(gridSize, cell);
				break;
			case 'h':
			case 'H':
				worldGenerations = new HexagonGrid[zip.size() - 2];
				for(int i = 0; i < zip.size() - 2; i++)
					worldGenerations[i] = new HexagonGrid(gridSize, cell);
				break;
			case 't':
			case 'T':
				worldGenerations = new TriangleGrid[zip.size() - 2];
				for(int i = 0; i < zip.size() - 2; i++)
					worldGenerations[i] = new TriangleGrid(gridSize, cell);
				break;
			default:
				System.out.println("Unable to create a grid with no type.");
				return false;
		}
		
		generationFiles = zip.entries();
		while (generationFiles.hasMoreElements())
		{
			ZipEntry config = (ZipEntry)generationFiles.nextElement();
			
			if (config.getName().endsWith(".car"))
				ruleCode = getStringFromStream(zip.getInputStream(config));
			else if (config.getName().endsWith(".cacp"))
				colourCode = getStringFromStream(zip.getInputStream(config));	
		}
		
		if (ruleCode == null)
		{
			System.out.println("Rule file not found.");
			return false;
		}
		
		if (colourCode == null)
		{
			System.out.println("Colour file not found.");
			return false;
		}	
		
		int worldPos = -1;
		generationFiles = zip.entries();
		while(generationFiles.hasMoreElements())
		{
			ZipEntry file = (ZipEntry)generationFiles.nextElement();
			String name = file.getName();
			long size = file.getSize();
			if (name.endsWith(".cag"))
			{
				if(size > 0)
				{
					worldPos = Integer.parseInt(name.substring(0, name.indexOf(".cag")));
					BufferedReader in = new BufferedReader(new InputStreamReader(zip.getInputStream(file)));
					String line;
					for(int rows = 0; rows < y; rows++)
					{
						for(int cols = 0; cols < x; cols++)
						{
							line = in.readLine();
							if (line.isEmpty()) continue;
							
							double[] vals = new double[properties];
							
							int prevIndex = -1;
							for(int i = 0; i < properties - 1; i++)
							{
								int currIndex = line.indexOf(" ", prevIndex + 1);

								if (currIndex <= 0)
								{
									Log.error(TAG, "Invalid file format");
									return false;
								}
								
								vals[i] = Double.parseDouble(line.substring(prevIndex + 1, currIndex));
								prevIndex = currIndex;
							}
							vals[properties - 1] = Double.parseDouble(line.substring(prevIndex + 1));
							
							worldGenerations[worldPos].setCell(cols, rows, vals);
						}
						line = in.readLine();
					}
				}
			}
		}
		
		world.setRuleCode(ruleCode);
		world.setColourCode(colourCode);
		world.setWorldGenerations(worldGenerations);
		
		return true;
	}
	
	private String getStringFromStream(InputStream stream)
	{
		return new Scanner(stream).useDelimiter("\\A").next();
	}
}
