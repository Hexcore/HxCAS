package com.hexcore.cas.model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.utilities.Log;

public class WorldStreamer
{
	private boolean reset = false;
	private boolean started = false;
	private boolean outOpen = false;
	private int numGenerations = 0;
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
				Log.error(TAG, "Error with clearing the history - " + ex.getMessage());
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
				Log.error(TAG, "Error with clearing the history - " + ex.getMessage());
				ex.printStackTrace();
			}
		}
		
		writeInitialValues(w);
	}
	
	public Grid getGeneration(int genNum)
	{
		try
		{
			File cawFile = new File(cawFilename);
			if(!cawFile.exists())
			{
				Log.error(TAG, "Error with the world : The file " + cawFilename + " could not be found.");
				return null;
			}
			
			boolean configFound = false;
			boolean genFound = false;
			char type = 'N';
			Grid gen = null;
			int properties = 0;
			int x = -1;
			int y = -1;
			ZipEntry entry;
			ZipInputStream zipIn = new ZipInputStream(new BufferedInputStream(new FileInputStream(cawFile)));
			
			while((entry = zipIn.getNextEntry()) != null)
			{
				String name = entry.getName();
				String ext = name.substring(name.lastIndexOf(".") + 1);
				
				String entryData = "";
				
				byte[] data = new byte[1];
				while((data[0] = (byte)zipIn.read()) != -1)
				{
					entryData += new String(data);
				}
				
				StringTokenizer token = new StringTokenizer(entryData);
				
				if(ext.compareTo("cac") == 0)
				{
					configFound = true;

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
						default:
							Log.error(TAG, "Unable to create a grid with no type.");
							return null;
					}
				}
				else if(ext.compareTo("cag") == 0)
				{
					if(!configFound)
					{
						Log.error(TAG, "Configuration file not found.");
						return null;
					}
					
					int index = name.lastIndexOf('/');
					if(index == -1)
						index = 0;
					
					if(name.substring(index).compareTo(genNum + ".cag") == 0)
					{
						genFound = true;

						for(int rows = 0; rows < y; rows++)
						{
							for(int cols = 0; cols < x; cols++)
							{
								double[] vals = new double[properties];

								for(int i = 0; i < properties; i++)
									vals[i] = Double.parseDouble(token.nextToken());
								
								gen.setCell(cols, rows, vals);
							}
						}
					}
				}
			}
			
			if(!genFound)
			{
				Log.error(TAG, "Generation file " + genNum + " not found.");
				return null;
			}

			zipIn.close();
			return gen;
		}
		catch(IOException ex)
		{
			Log.error(TAG, "Error retrieving generation - " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}
	}
	
	public List<Grid> getGenerations(World w)
	{
		List<Grid> generations = Collections.synchronizedList(new ArrayList<Grid>());
		
		try
		{
			File cawFile = new File(cawFilename);
			if(!cawFile.exists())
			{
				Log.error(TAG, "Error with the world : The file " + cawFilename + " could not be found.");
				return null;
			}
			
			boolean configFound = false;
			char type = 'N';
			Grid gen = null;
			int properties = 0;
			int x = -1;
			int y = -1;
			ZipEntry entry;
			ZipInputStream zipIn = new ZipInputStream(new BufferedInputStream(new FileInputStream(cawFile)));
			
			while((entry = zipIn.getNextEntry()) != null)
			{
				String name = entry.getName();
				String ext = name.substring(name.lastIndexOf(".") + 1);
				
				String entryData = "";
				
				byte[] data = new byte[1];
				while((data[0] = (byte)zipIn.read()) != -1)
				{
					entryData += new String(data);
				}
				
				StringTokenizer token = new StringTokenizer(entryData);
				
				if(ext.compareTo("cac") == 0)
				{
					configFound = true;
					
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
						default:
							Log.error(TAG, "Unable to create a grid with no type.");
							return null;
					}
				}
				else if(ext.compareTo("cag") == 0)
				{
					if(!configFound)
					{
						Log.error(TAG, "Configuration file not found.");
						return null;
					}
					
					for(int genNum = 0; genNum < numGenerations; genNum++)
					{
						int index = name.lastIndexOf('/');
						if(index == -1)
							index = 0;
						
						if(name.substring(index).compareTo(genNum + ".cag") != 0)
							Log.error(TAG, "Generation " + genNum + " file not found.");
						
						for(int rows = 0; rows < y; rows++)
						{
							for(int cols = 0; cols < x; cols++)
							{
								double[] vals = new double[properties];

								for(int i = 0; i < properties; i++)
									vals[i] = Double.parseDouble(token.nextToken());
								
								gen.setCell(cols, rows, vals);
							}
						}
						
						generations.add(gen.clone());
					}
				}
			}

			zipIn.close();
			return generations;
		}
		catch(IOException ex)
		{
			Log.error(TAG, "Error retrieving all generations - " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}
	}
	
	public Grid getLastGeneration()
	{
		return getGeneration(numGenerations - 1);
	}
	
	public int getNumGenerations()
	{
		return numGenerations;
	}
	
	public boolean hasStarted()
	{
		return started;
	}
	
	public void openOut()
	{
		Log.debug(TAG, "ZipOutputStream was closed. Reopening....");
		
		File theFile = new File(cawFilename);
		
		try
		{
			out = new ZipOutputStream(new FileOutputStream(theFile, true));
			outOpen = true;
		}
		catch(IOException e)
		{
			Log.error(TAG, "Error getting streams to the world - " + e.getMessage());
			e.printStackTrace();
			return;
		}
	}
	
	public void reset(World w)
	{
		if(started)
		{
			reset = true;
			
			stop();
			start(w);
		}
		else
		{
			reset = false;
			
			start(w);
		}
	}
	
	public void start(World w)
	{
		File caw = null;
		if(w.getFilename() == null)
			w.setFileName("worlds/newWorld.caw");
		
		int cawIndex = w.getFilename().indexOf(".caw");
		
		String name = (cawIndex != -1 ? w.getFilename() : w.getFilename() + ".caw");
		
		if(cawFilename == null)
			cawFilename = name;
		
		if(!reset)
		{
			caw = new File(name);
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
			catch (IOException ex)
			{
				Log.error(TAG, "Error generating new world - " + ex.getMessage());
				ex.printStackTrace();
			}
		}
		else
		{
			caw = new File(cawFilename);
			reset = false;
		}
		
		try
		{
			out = new ZipOutputStream(new FileOutputStream(caw));
			outOpen = true;
		}
		catch(IOException ex)
		{
			Log.error(TAG, "Error getting output stream to the world - " + ex.getMessage());
			ex.printStackTrace();
		}
		
		numGenerations = 0;
		
		writeInitialValues(w);
		
		started = true;
	}
	
	public void stop()
	{
		if(outOpen)
		{
			try
			{
				out.close();
				out = null;
				outOpen = false;
				
				started = false;
			}
			catch(IOException ex)
			{
				Log.error(TAG, "Error stopping the output stream to the world - " + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}
	
	public void streamGeneration(Grid gen)
	{
		if(!outOpen)
			openOut();
		
		try
		{
			int genNum = numGenerations;
			
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
			
			numGenerations++;
		}
		catch(IOException ex)
		{
			Log.error(TAG, "Error streaming generation to the world - " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	public void writeInitialValues(World w)
	{
		w.setKeepHistory(1);
		
		try
		{
			Grid genZero = w.getInitialGeneration();
			char type = genZero.getTypeSymbol();
			int height = genZero.getHeight();
			int props = genZero.getNumProperties();
			int width = genZero.getWidth();
			
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
			
			numGenerations = gens.size();
			
			w.clearHistory();
		}
		catch(IOException ex)
		{
			Log.error(TAG, "Error writing all initial values to the world - " + ex.getMessage());
			ex.printStackTrace();
		}
		
		w.setKeepHistory(2);
	}
	
	public String getCawFilename()
	{
		return cawFilename;
	}
}