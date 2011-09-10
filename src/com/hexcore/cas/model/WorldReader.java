package com.hexcore.cas.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.hexcore.cas.math.Vector2i;

public class WorldReader
{
	private World world = null;
	
	public WorldReader(World w)
	{
		world = w;
	}
	
	public void readWorld(String worldFileName)
		throws IOException
	{
		/*
		 * Takes the name of the world zip file and reads in.
		 * It will specifically look for the configuration file first,
		 * then the rules set and then all the generation files.
		 */
		String rulesAndColours = "";
		
		File f = new File(worldFileName);
		ZipFile zip = new ZipFile(f);
		Enumeration<? extends ZipEntry> generationFiles = zip.entries();
		
		int x = -1;
		int y = -1;
		char type = 'N';
		int n = 0;
		while(true)
		{
			if(!generationFiles.hasMoreElements())
			{
				System.out.println("Configuration file not found.");
				return;
			}
			ZipEntry config = (ZipEntry)generationFiles.nextElement();
			if(config.getName().indexOf(".cac") != -1)
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
				n = Integer.parseInt(line);
				break;
			}
		}
		
		Grid[] worldGenerations = null;
		Vector2i gridSize = new Vector2i(x, y);
		Cell cell = new Cell(n);
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
				return;
		}
		
		generationFiles = zip.entries();
		while(true)
		{
			if(!generationFiles.hasMoreElements())
			{
				System.out.println("Rules and colours file not found.");
				return;
			}
			ZipEntry config = (ZipEntry)generationFiles.nextElement();
			if(config.getName().indexOf(".car") != -1)
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(zip.getInputStream(config)));
				String line = "";
				char[] c = new char[1];
				int tmp = in.read(c);
				while(tmp != -1)
				{
					line += c[0];
					tmp = in.read(c);
				}
				rulesAndColours = line;
				break;
			}
		}
		
		int worldPos = -1;
		generationFiles = zip.entries();
		while(generationFiles.hasMoreElements())
		{
			ZipEntry file = (ZipEntry)generationFiles.nextElement();
			String name = file.getName();
			long size = file.getSize();
			if(name.indexOf(".cag") != -1)
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
							double[] vals = new double[n];
							int prevIndex = -1;
							int currIndex = 0;
							for(int i = 0; i < (n - 1); i++)
							{
								currIndex = line.indexOf(" ", prevIndex);
								vals[i] = Double.parseDouble(line.substring(prevIndex + 1, currIndex));
								prevIndex = currIndex;
							}
							vals[n - 1] = Double.parseDouble(line.substring(prevIndex + 1));
							for(int i = 0; i < n; i++)
								worldGenerations[worldPos].getCell(cols, rows).setValue(i, vals[i]);
						}
						line = in.readLine();
					}
				}
			}
			else
			{
				System.out.println("Recieved a rule set or a config file. Cannot handle right now.");
			}
		}
		
		world.setRulesAndColours(rulesAndColours);
		world.setWorldGenerations(worldGenerations);
	}
}
