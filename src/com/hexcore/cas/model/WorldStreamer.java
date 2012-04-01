package com.hexcore.cas.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.utilities.Log;

public class WorldStreamer
{
	private boolean outOpen = false;
	private String cawFilename = null;
	private ZipFile world = null;
	private ZipOutputStream out = null;

	private static final String TAG = "WorldStreamer";
	
	public WorldStreamer()
	{
	}
	
	public String getFilename()
	{
		return cawFilename;
	}
	
	public int getNumGenerations()
	{
		if(outOpen)
		{
			try
			{
				out.close();
				outOpen = false;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		File f = new File(cawFilename);
		if (!f.exists())
		{
			Log.error(TAG, "Error loading world - Could not find file: " + cawFilename);
			return -1;
		}
		
		int size = -1;
		
		ZipFile zip = null;
		try
		{
			zip = new ZipFile(f);

			size = zip.size() - 3;
			
			zip.close();
		}
		catch (ZipException e)
		{
			Log.error(TAG, "Error opening world - " + e.getMessage());
			e.printStackTrace();
			return -1;
		}
		catch(IOException e)
		{
			Log.error(TAG, "Error opening world - " + e.getMessage());
			e.printStackTrace();
			return -1;
		}
		
		return size;
	}
	
	public List<Grid> getGenerations()
	{
		List<Grid> gens = Collections.synchronizedList(new ArrayList<Grid>());
		
		int size = getNumGenerations();
		
		if(outOpen)
		{
			try
			{
				out.close();
				outOpen = false;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		try
		{
			File cawFile = new File(cawFilename);
			world = new ZipFile(cawFile);

			int x = -1;
			int y = -1;
			char type = 'N';
			int properties = 0;
			String line;

			ZipEntry config = world.getEntry("config.cac");
			if(config == null)
			{
				System.out.println("Configuration file not found.");
				return null;
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(world.getInputStream(config)));
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

			for(int genNum = 0; genNum < size; genNum++)
			{
				String genName = genNum + ".cag";
				ZipEntry genEntry = world.getEntry(genName);
				if(genEntry == null)
				{
					System.out.println("Generation file not found.");
					return null;
				}
				
				in = new BufferedReader(new InputStreamReader(world.getInputStream(genEntry)));
				line = null;
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
				
				gens.add(gen.clone());
			}
			
			world.close();
			world = null;
			
			return gens;
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public Grid getLastGeneration()
	{
		int size = getNumGenerations();
		
		return streamGeneration(size - 1);
	}
	
	public void openOut()
	{
		Log.debug(TAG, "ZipOutputStream was closed. Reopening.");
		
		String ruleCode = "";
		String colourCode = "";	
		
		File f = new File(cawFilename);
		if (!f.exists())
		{
			Log.error(TAG, "Error loading world - Could not find file: " + cawFilename);
			return;
		}
		
		ZipFile zip = null;
		try
		{
			zip = new ZipFile(f);
		}
		catch (ZipException e)
		{
			Log.error(TAG, "Error opening world - " + e.getMessage());
			e.printStackTrace();
			return;
		}
		catch(IOException e)
		{
			Log.error(TAG, "Error opening world - " + e.getMessage());
			e.printStackTrace();
			return;
		}
		
		int x = -1;
		int y = -1;
		char type = 'N';
		int properties = 0;
		Grid[] worldGenerations = null;
		int worldGenSize = zip.size() - 3;
		
		try
		{
			ZipEntry config = zip.getEntry("config.cac");
			
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
			
			Vector2i gridSize = new Vector2i(x, y);
			Cell cell = new Cell(properties);
			switch(type)
			{
				case 'r':
				case 'R':
					worldGenerations = new RectangleGrid[worldGenSize];
					for(int i = 0; i < worldGenSize; i++)
						worldGenerations[i] = new RectangleGrid(gridSize, cell);
					break;
				case 'h':
				case 'H':
					worldGenerations = new HexagonGrid[worldGenSize];
					for(int i = 0; i < worldGenSize; i++)
						worldGenerations[i] = new HexagonGrid(gridSize, cell);
					break;
				case 't':
				case 'T':
					worldGenerations = new TriangleGrid[worldGenSize];
					for(int i = 0; i < worldGenSize; i++)
						worldGenerations[i] = new TriangleGrid(gridSize, cell);
					break;
				default:
					Log.error(TAG, "Unable to create a grid with no type.");
					return;
			}
			
			ZipEntry rules = zip.getEntry("rules.car");
			in = new BufferedReader(new InputStreamReader(zip.getInputStream(rules)));

			line = in.readLine();
			while(line != null)
			{
				ruleCode += line;
				line = in.readLine();
			}

			ZipEntry colours = zip.getEntry("colours.cacp");
			in = new BufferedReader(new InputStreamReader(zip.getInputStream(colours)));
			
			line = in.readLine();
			while(line != null)
			{
				colourCode += line;
				line = in.readLine();
			}
			
			if(ruleCode.compareTo("") == 0)
			{
				Log.error(TAG, "Rule file not found.");
				return;
			}
			
			if(colourCode.compareTo("") == 0)
			{
				Log.error(TAG, "Colour file not found.");
				return;
			}	
			
			for(int g = 0; g < worldGenSize; g++)
			{
				String genFile = g + ".cag";
				ZipEntry gen = zip.getEntry(genFile);

				in = new BufferedReader(new InputStreamReader(zip.getInputStream(gen)));
				line = null;
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
								Log.error(TAG, "Invalid file format.");
								return;
							}
							
							vals[i] = Double.parseDouble(line.substring(prevIndex + 1, currIndex));
							prevIndex = currIndex;
						}
						vals[properties - 1] = Double.parseDouble(line.substring(prevIndex + 1));
						
						worldGenerations[g].setCell(cols, rows, vals);
					}
					line = in.readLine();
				}
			}
			
			try
			{
				zip.close();
				out = new ZipOutputStream(new FileOutputStream(new File(cawFilename)));
				outOpen = true;
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			
			config = new ZipEntry("config.cac");
			out.putNextEntry(config);
			String configStr = x + " " + y + "\n";
			configStr += type + "\n";
			configStr += properties + "\n";
			out.write(configStr.getBytes());
			out.closeEntry();
			
			ZipEntry ruleCodeEntry = new ZipEntry("rules.car");
			out.putNextEntry(ruleCodeEntry);
			out.write(ruleCode.getBytes());
			out.closeEntry();
			
			ZipEntry colourCodeEntry = new ZipEntry("colours.cacp");
			out.putNextEntry(colourCodeEntry);
			out.write(colourCode.getBytes());
			out.closeEntry();
			
			for (int i = 0; i < worldGenSize; i++)
			{
				Grid grid = worldGenerations[i];
				ZipEntry caw = new ZipEntry(i + ".cag");
				out.putNextEntry(caw);
				String str = "";
				for(int rows = 0; rows < y; rows++)
				{
					for(int cols = 0; cols < x; cols++)
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
		}
		catch(IOException e)
		{
			Log.error(TAG, "Error - " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void reset(World w)
	{
		stop();
		start(w);
	}
	
	public void start(World w)
	{
		String name = (w.getFilename().indexOf(".caw") != -1 ? w.getFilename() : w.getFilename() + ".caw");
		File caw = new File(name);
		int worldNum = 1;
		try
		{
			while(!caw.createNewFile())
			{
				int underIndex = name.indexOf("_");
				if(underIndex != -1)
					name = name.substring(0, name.indexOf("_")) + "_" + worldNum + ".caw";
				else
					name = name.substring(0, name.indexOf(".caw")) + "_" + worldNum + ".caw";
				caw = new File(name);
				worldNum++;
			}
			
			cawFilename = name;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			out = new ZipOutputStream(new FileOutputStream(caw));
			outOpen = true;
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
			outOpen = false;
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void streamGeneration(Grid gen, int genNum)
	{
		if(!outOpen)
			openOut();
		
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
		if(outOpen)
		{
			try
			{
				out.close();
				outOpen = false;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		try
		{
			File cawFile = new File(cawFilename);
			world = new ZipFile(cawFile);

			int x = -1;
			int y = -1;
			char type = 'N';
			int properties = 0;
			String line;

			ZipEntry config = world.getEntry("config.cac");
			if(config == null)
			{
				System.out.println("Configuration file not found.");
				return null;
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(world.getInputStream(config)));
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

			String genName = genNum + ".cag";
			ZipEntry genEntry = world.getEntry(genName);
			if(genEntry == null)
			{
				System.out.println("Generation file not found.");
				return null;
			}
			
			in = new BufferedReader(new InputStreamReader(world.getInputStream(genEntry)));
			line = null;
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
			
			world.close();
			world = null;
			
			return gen;
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
			if(!gens.isEmpty())
			{
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
			
			//w.clearHistory();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
}