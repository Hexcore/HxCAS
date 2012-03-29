package com.hexcore.cas.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.hexcore.cas.math.Vector2i;

public class WorldStreamer
{
	private ZipFile world;
	private ZipOutputStream out;
	
	public WorldStreamer()
	{
	}
	
	public void reset(World w)
	{
		stop();
		start(w);
	}
	
	public void start(World w)
	{
		int exindex = w.getFilename().indexOf(".caw");
		File file = null;
		if(exindex != -1)
			file = new File(w.getFilename());
		else
			file = new File(w.getFilename() + ".caw");
		try
		{
			world = new ZipFile(file);
			out = new ZipOutputStream(new FileOutputStream(file));
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		writeInitialValues(w);
	}
	
	public void stop()
	{
		try
		{
			out.close();
			world.close();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void streamGeneration(Grid gen, int genNum)
	{
		try
		{
			ZipEntry cag = new ZipEntry(genNum + ".cag");
			out.putNextEntry(cag);
			String str = "";
			for(int rows = 0; rows < gen.getHeight(); rows++)
			{
				for(int cols = 0; cols < gen.getWidth(); cols++)
				{
					for(int index = 0; index < gen.getNumProperties() - 1; index++)
						str += gen.getCell(cols, rows).getValue(index) + " ";
					str += gen.getCell(cols, rows).getValue(gen.getNumProperties() - 1) + "\n";
				}
				str += "\n";
			}
			out.write(str.getBytes());
			out.closeEntry();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public Grid streamGeneration(int genNum)
	{
		try
		{
			Enumeration<? extends ZipEntry> generationFiles = world.entries();
			
			int x = -1;
			int y = -1;
			char type = 'N';
			int properties = 0;
			while(true)
			{
				if(!generationFiles.hasMoreElements())
				{
					System.out.println("Configuration file not found.");
					return null;
				}
				ZipEntry config = (ZipEntry)generationFiles.nextElement();
				if (config.getName().endsWith(".cac"))
				{
					BufferedReader in = new BufferedReader(new InputStreamReader(world.getInputStream(config)));
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
			
			Grid gen = null;
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
				default:
					System.out.println("Unable to create a grid with no type.");
					return null;
			}
			
			generationFiles = world.entries();
			while(true)
			{
				if(!generationFiles.hasMoreElements())
				{
					System.out.println("Generation file not found.");
					return null;
				}
				else
				{
					ZipEntry file = (ZipEntry)generationFiles.nextElement();
					String name = file.getName();
					if(name == genNum + ".cag")
					{
						BufferedReader in = new BufferedReader(new InputStreamReader(world.getInputStream(file)));
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
										return null;
									
									vals[i] = Double.parseDouble(line.substring(prevIndex + 1, currIndex));
									prevIndex = currIndex;
								}
								vals[properties - 1] = Double.parseDouble(line.substring(prevIndex + 1));
								
								gen.setCell(cols, rows, vals);
							}
							line = in.readLine();
						}
						return gen;
					}
				}
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public void writeInitialValues(World w)
	{
		try
		{
			Grid gen0 = w.getInitialGeneration();
			char type = gen0.getTypeSymbol();
			int height = gen0.getHeight();
			int props = gen0.getNumProperties();
			int width = gen0.getWidth();
			
			ZipEntry config = new ZipEntry("config.cac");
			out.putNextEntry(config);
			String configStr = width + " " + height + "\n";
			configStr += type + "\n";
			configStr += props + "\n";
			out.write(configStr.getBytes());
			out.closeEntry();
			
			ZipEntry ruleCodeEntry = new ZipEntry("rules.car");
			out.putNextEntry(ruleCodeEntry);
			out.write(w.getRuleCode().getBytes());
			out.closeEntry();
			
			ZipEntry colourCodeEntry = new ZipEntry("colours.cacp");
			out.putNextEntry(colourCodeEntry);
			out.write(w.getColourCode().getBytes());
			out.closeEntry();
			
			List<Grid> gens = w.getGenerations();
			for (int i = 0; i < gens.size(); i++)
			{
				Grid grid = gens.get(i);
				ZipEntry caw = new ZipEntry(i + ".cag");
				out.putNextEntry(caw);
				String str = "";
				for(int rows = 0; rows < height; rows++)
				{
					for(int cols = 0; cols < width; cols++)
					{
						for(int index = 0; index < props - 1; index++)
							str += grid.getCell(cols, rows).getValue(index) + " ";
						str += grid.getCell(cols, rows).getValue(props - 1) + "\n";
					}
					str += "\n";
				}
				out.write(str.getBytes());
				out.closeEntry();
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	//For JUnit test only
	public boolean isZipWorldOpen()
	{
		try
		{
			int size = world.size();
			return true;
		}
		catch(IllegalStateException ex)
		{
			return false;
		}
	}
}