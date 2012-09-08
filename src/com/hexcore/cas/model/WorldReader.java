package com.hexcore.cas.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.utilities.Log;
import com.javamex.classmexer.MemoryUtil;
import com.javamex.classmexer.MemoryUtil.VisibilityFilter;

/**
 * Class WorldReader
 * 	An instance of this object is used to read all details
 * 	of a world from where it is stored.
 * 
 * @author Megan Duncan
 */

public class WorldReader
{
	private static final String		TAG = "WorldReader";
	
	private World					world = null;
	
	public WorldReader(World w)
	{
		world = w;
	}
	
	public boolean readWorld(String worldFilename)
		throws IOException
	{
		/*
		 * Takes the name of the world zip file and reads in.
		 * It will specifically look for the configuration file first,
		 * then the rules set then the colours set and then all the generation files
		 * or the amount of generation files that can be held in memory.
		 */
		boolean coloursFound = false;
		boolean rulesFound = false;
		char type = 'N';
		Enumeration<? extends ZipEntry> allFiles = null;
		File cawFile = new File(worldFilename);
		Grid gen = null;
		int x = -1;
		int y = -1;
		int properties = 0;
		MemoryMXBean bean = ManagementFactory.getMemoryMXBean();
		String colourCode = null;
		String currFilename = null;
		//String ruleCode = null;
		ArrayList<String> ruleCodes = new ArrayList<String>();
		ZipFile zip = null;
		
		if(!cawFile.exists())
		{
			Log.error(TAG, "Error loading world - could not find file " + worldFilename);
			return false;
		}
		
		try
		{
			zip = new ZipFile(cawFile);
		}
		catch(ZipException e)
		{
			Log.error(TAG, "Error loading world - " + e.getMessage());
			return false;
		}
		
		allFiles = zip.entries();
		
		//Searching for the configuration file
		while(true)
		{
			if(!allFiles.hasMoreElements())
			{
				Log.error(TAG, "Error loading world - configuration file not found");
				return false;
			}
			
			ZipEntry entry = (ZipEntry)allFiles.nextElement();
			currFilename = entry.getName();

			if(currFilename.endsWith(".cac"))
			{
				String fileData = getStringFromStream(zip.getInputStream(entry));
				StringTokenizer token = new StringTokenizer(fileData);
				
				x = Integer.parseInt(token.nextToken());
				y = Integer.parseInt(token.nextToken());
				type = token.nextToken().charAt(0);
				properties = Integer.parseInt(token.nextToken());

				Vector2i gridSize = new Vector2i(x, y);
				Cell cell = new Cell(properties);
				
				switch(type)
				{
					case 'r':
					case 'R':
						gen = new RectangleGrid(gridSize, cell);
						break;
					case 'h':
					case 'H':
						gen = new HexagonGrid(gridSize, cell);
						break;
					case 't':
					case 'T':
						gen = new TriangleGrid(gridSize, cell);
						break;
					case 'v':
					case 'V':
						gen = new VonNeumannGrid(gridSize, cell);
						break;
					default:
						Log.error(TAG, "Error loading world - unable to create a grid with no type");
						return false;
				}
				
				break;
			}
		}

		//Searching for the colours file
		allFiles = zip.entries();
		while(allFiles.hasMoreElements())
		{
			ZipEntry entry = (ZipEntry)allFiles.nextElement();
			currFilename = entry.getName();
			
			if(currFilename.endsWith(".cacp"))
			{
				coloursFound = true;
				colourCode = getStringFromStream(zip.getInputStream(entry));
				break;
			}
		}

		//Set to a default empty colour set
		if(!coloursFound)
		{
			Log.warning(TAG, "Colour set not found - setting colours to default empty set");
			colourCode = "colourset colours\n{}";
		}

		//Searching for the rules files
		allFiles = zip.entries();
		while(allFiles.hasMoreElements())
		{
			ZipEntry entry = (ZipEntry)allFiles.nextElement();
			currFilename = entry.getName();
			
			if(currFilename.endsWith(".cal"))
			{
				rulesFound = true;
				
				String number = currFilename.substring(currFilename.indexOf("rules") + 5, currFilename.indexOf("."));

				if(number.compareTo("") != 0)
					ruleCodes.add(number + ":" + getStringFromStream(zip.getInputStream(entry)));
				else
				{
					ruleCodes.clear();
					ruleCodes.add(getStringFromStream(zip.getInputStream(entry)));
					break;
				}
			}
		}
		
		//Set to a default ruleset for Conway's Game of Life and change colour set to mirror this, despite if a colour set was found
		if(!rulesFound)
		{
			Log.warning(TAG, "Rule set not found - setting colours and rules to default sets for Conway's Game of Life");
			//ruleCode = "ruleset GameOfLife\n{\n\ttypecount 1;\n\tproperty alive;\n\n\ttype Land\n\t{\n\t\tvar c = sum(neighbours.alive);\n\t\tif ((c < 2) || (c > 3))\n\t\t\tself.alive = 0;\n\t\telse if (c == 3)\n\t\t\tself.alive = 1;\t\t\n\t}\n}";
			//world.setRuleCodes(1);
			colourCode = "colourset colours\n{\n\tproperty alive\n\t{\n\t\t0 - 1 : rgb(0.0, 0.25, 0.5) rgb(0.0, 0.8, 0.5);\n\t\t1 - 2 : rgb(0.0, 0.8, 0.5) rgb(0.8, 0.5, 0.3);\n\t}\n\t}\n}";
		}

		//Searching for all generation files that are to be loaded
		allFiles = zip.entries();
		int arrSize = zip.size() - 2 - ruleCodes.size();
		int gensToLoad = arrSize;
		int scale = 500;
		long generationSize = MemoryUtil.deepMemoryUsageOf(gen, VisibilityFilter.ALL);
		long maxHeap = bean.getHeapMemoryUsage().getMax();
		long toBeUsedHeap = generationSize * gensToLoad;
		
		if(toBeUsedHeap >= maxHeap - (scale * 1024 * 1024))
		{
			while(true)
			{
				gensToLoad--;
				toBeUsedHeap = generationSize * gensToLoad;
				if(toBeUsedHeap < maxHeap - (scale * 1024 * 1024))
					break;
			}
		}
		Log.information(TAG, toBeUsedHeap + " bean heap memory to be used out of a maximum of " + maxHeap + " for " + gensToLoad + " generations.");

		Grid[] gens = new Grid[gensToLoad];
		int finalGen = arrSize - 1;
		
		while(allFiles.hasMoreElements())
		{
			ZipEntry entry = (ZipEntry)allFiles.nextElement();
			currFilename = entry.getName();
			
			if(currFilename.endsWith(".cag"))
			{
				System.out.println("Doing file |" + currFilename + "|");
				
				String fileData = getStringFromStream(zip.getInputStream(entry));
				StringTokenizer token = new StringTokenizer(fileData);
				
				int index = currFilename.lastIndexOf('/');
				if(index == -1)
					index = 0;
				int fileGenIndex = currFilename.indexOf(".cag");
				int fileGenNum = 0;
				if(index != 0)
					fileGenNum = Integer.parseInt(currFilename.substring(index + 1, fileGenIndex));
				else
					fileGenNum = Integer.parseInt(currFilename.substring(index, fileGenIndex));
				
				for(int rows = 0; rows < y; rows++)
				{
					for(int cols = 0; cols < x; cols++)
					{
						double[] vals = new double[properties];

						for(int j = 0; j < properties; j++)
							vals[j] = Double.parseDouble(token.nextToken());
						
						gen.setCell(cols, rows, vals);
					}
				}

				int arrIndex = gensToLoad - (finalGen - fileGenNum) - 1;
				
				if(arrIndex < 0)
					continue;
				
				gens[arrIndex] = gen.clone();
			}
			else
				continue;
		}
		
		//world.setRuleCode(ruleCode);
		world.setRuleCodes(ruleCodes);
		world.setColourCode(colourCode);
		world.setWorldGenerations(gens);
		
		Log.information(TAG, "Number of generations from WorldReader : " + world.getNumGenerations());
		
		return true;
	}
	
	/////////////////////////////////////////////
	/// Private functions
	private String getStringFromStream(InputStream stream)
	{
		return new Scanner(stream).useDelimiter("\\A").next();
	}
}
