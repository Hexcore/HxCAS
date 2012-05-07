package com.hexcore.cas.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.utilities.Log;

public class WorldStreamer
{
	private boolean started = false;
	private boolean outOpen = false;
	private String cawFilename = null;
	private ZipOutputStream out = null;

	private static final String TAG = "WorldStreamer";
	
	public WorldStreamer()
	{
	}
	
	public void clearHistory(World w)
	{
		File caw = new File(cawFilename);
		
		if(!outOpen)
		{
			try
			{
				out = new ZipOutputStream(new FileOutputStream(caw));
				outOpen = true;
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
		else
		{
			stop();
			
			try
			{
				out = new ZipOutputStream(new FileOutputStream(caw));
				outOpen = true;
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
		
		writeInitialValues(w);
	}
	
	public Grid getGeneration(int genNum)
	{
		if(outOpen)
			stop();
		
		try
		{
			File cawFile = new File(cawFilename);
			ZipFile zip = new ZipFile(cawFile);

			int x = -1;
			int y = -1;
			char type = 'N';
			int properties = 0;
			String line;

			ZipEntry config = zip.getEntry("config.cac");
			if(config == null)
			{
				System.out.println("Configuration file not found.");
				return null;
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(zip.getInputStream(config)));
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
			ZipEntry genEntry = zip.getEntry(genName);
			if(genEntry == null)
			{
				Log.error(TAG, "Generation file " + genNum + " not found.");
				return null;
			}
			
			in = new BufferedReader(new InputStreamReader(zip.getInputStream(genEntry)));
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
			
			zip.close();
			
			return gen;
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public List<Grid> getGenerations(World w)
	{
		if(outOpen)
			stop();
		
		List<Grid> generations = Collections.synchronizedList(new ArrayList<Grid>());
		
		try
		{
			File cawFile = new File(cawFilename);
			ZipFile zip = new ZipFile(cawFile);

			int x = -1;
			int y = -1;
			char type = 'N';
			int properties = 0;
			String line;

			ZipEntry config = zip.getEntry("config.cac");
			if(config == null)
			{
				Log.error(TAG, "Configuration file not found.");
				return null;
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(zip.getInputStream(config)));
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
					Log.error(TAG, "Unable to create a grid with no type.");
					return null;
			}

			for(int genNum = 0; genNum < zip.size() - 3; genNum++)
			{
				String genName = genNum + ".cag";
				ZipEntry genEntry = zip.getEntry(genName);
				if(genEntry == null)
				{
					Log.error(TAG, "Generation " + genNum + " file not found.");
					//return null;
				}
				
				in = new BufferedReader(new InputStreamReader(zip.getInputStream(genEntry)));
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
				
				generations.add(gen.clone());
			}
			
			zip.close();
			
			return generations;
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
		
		return getGeneration(size - 1);
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
	
	public boolean hasStarted()
	{
		return started;
	}
	
	public void openOut()
	{
		Log.debug(TAG, "ZipOutputStream was closed. Reopening.");
		
		String ruleCode = "";
		String colourCode = "";	
		
		if(cawFilename == null)
			Log.debug(TAG, "cawFilename is null.");
		
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
			Log.debug(TAG, "Reading in...");
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
			
			zip.close();
			
			try
			{
				out = new ZipOutputStream(new FileOutputStream(new File(cawFilename)));
				outOpen = true;
			}
			catch(IOException e)
			{
				Log.error(TAG, "Error creating output stream - " + e.getMessage());
				e.printStackTrace();
			}

			Log.debug(TAG, "Writing out...");
			
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
		Log.debug(TAG, "reset() called");
		stop();
		start(w);
	}
	
	public void start(World w)
	{
		Log.debug(TAG, "start() called");
		if(w.getFilename() == null)
			w.setFileName("worlds/newWorld.caw");
		
		int cawIndex = w.getFilename().indexOf(".caw");
		
		String name = (cawIndex != -1 ? w.getFilename() : w.getFilename() + ".caw");
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
			Log.debug(TAG, "cawFilename : " + cawFilename);
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

		started = true;
	}
	
	public void stop()
	{
		Log.debug(TAG, "stop() called");
		
		if(outOpen)
		{
			try
			{
				out.close();
				outOpen = false;
				
				started = false;
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
			
			System.gc();
		}
	}
	
	public void streamGeneration(Grid gen)
	{
		int genNum = getNumGenerations();
		
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
			
			w.clearHistory();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public String getCawFilename()
	{
		return cawFilename;
	}
}