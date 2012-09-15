package com.hexcore.cas.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.utilities.Log;
import com.javamex.classmexer.MemoryUtil;
import com.javamex.classmexer.MemoryUtil.VisibilityFilter;

/**
 * Class WorldStreamer
 * 	The class that streams all world generations to disk as simulation
 * 	runs when hard disk memory is selected.
 * 
 * @authors Megan Duncan
 */

public class WorldStreamer
{
	private static final String		TAG = "WorldStreamer";
	
	private boolean					reset = false;
	private volatile boolean		started = false;
	
	private int						numGenerations = 0;
	
	private String					cawFilename = null;
	private String					tmpDir = null;
	
	/**
	 * WorldStreamer default constructor.
	 */
	public WorldStreamer()
	{
	}
	
	/**
	 * Returns the CAW file name as given by the file name of the world.
	 * 
	 * @return - the CAW file name of the world
	 */
	public String getCawFilename()
	{
		return cawFilename;
	}
	
	/**
	 * Looks for the generation with the corresponding given generation number on disk and reads in.
	 * 
	 * Returns null during the following conditions:
	 * 	No configuration file is found;
	 * 	Configuration file is found but has not grid type symbol; or
	 * 	No generation with the given number.
	 * 
	 * Otherwise returns the generation with all its information read in.
	 * 
	 * @param genNum - the generation number that has been requested
	 * @return - the complete generation
	 */
	public Grid getGeneration(int genNum)
	{
		Log.debug(TAG, "Streamer getGeneration(" + genNum + ") called.");
		
		boolean configFound = false;
		boolean genFound = false;
		char type = 'N';
		File currFile = null;
		File folder = new File(tmpDir);
		File[] listOfFiles = folder.listFiles();
		Grid gen = null;
		InputStream in = null;
		int properties = 0;
		int x = -1;
		int y = -1;
		String currFilename = null;
		
		try
		{
			Log.debug(TAG, "Looking for configuration file");
			//Searching for the configuration file
			for(int i = 0; i < listOfFiles.length; i++)
			{
				if(listOfFiles[i].isFile())
				{
					currFilename = listOfFiles[i].getName();
					currFile = new File(tmpDir + "/" + currFilename);
					in = new BufferedInputStream(new FileInputStream(currFile));
					
					String fileData = "";
					
					if(currFilename.endsWith(".cac"))
					{
						configFound = true;
						
						byte[] data = new byte[1024];
						int len = 1024;
						while((len = in.read(data)) > 0)
						{
							fileData += new String(data);
						}

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
								Log.error(TAG, "Error retrieving generaion - unable to create a grid with no type");
								return null;
						}
						
						break;
					}
					else
						continue;
				}
			}
			
			if(!configFound)
			{
				Log.error(TAG, "Error retrieving generaion - configuration file not found");
				return null;
			}
			
			Log.debug(TAG, "Looking for generation file");
			//Searching for the generation file
			for(int i = 0; i < listOfFiles.length; i++)
			{
				if(listOfFiles[i].isFile())
				{
					currFilename = listOfFiles[i].getName();
					currFile = new File(tmpDir + "/" + currFilename);
					in = new BufferedInputStream(new FileInputStream(currFile));
					
					String fileData = "";
					
					int index = currFilename.lastIndexOf('/');
					if(index == -1)
						index = 0;
					
					if(currFilename.substring(index).compareTo(genNum + ".cag") == 0)
					{
						genFound = true;
						
						byte[] data = new byte[1024];
						int len = 1024;
						while((len = in.read(data)) > 0)
						{
							fileData += new String(data);
						}
						
						StringTokenizer token = new StringTokenizer(fileData);
						
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
						
						break;
					}
				}
			}
			
			if(!genFound)
			{
				Log.error(TAG, "Error retrieving generation - generation file " + genNum + " not found");
				return null;
			}
			
			in.close();
			in = null;
			
			return gen;
		}
		catch(IOException ex)
		{
			Log.error(TAG, "Error retrieving generation - " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Looks for all generations on disk and reads them in one by one. Only reads in the total number
	 * of generations that the system can handle with an extra gap of 500 generations.
	 * 
	 * Returns null during the following conditions:
	 * 	No configuration file is found; or
	 * 	Configuration file is found but has not grid type symbol.
	 * 
	 * Otherwise returns the list of generations.
	 * 
	 * Any missing generation file will result in a Grid that is null in that spot in the returned list.
	 * 
	 * @return - the list of complete generations with 500 gap
	 */
	public List<Grid> getGenerations()
	{
		boolean configFound = false;
		char type = 'N';
		File currFile = null;
		File folder = new File(tmpDir);
		File[] listOfFiles = folder.listFiles();
		Grid gen = null;
		InputStream in = null;
		int arrSize = listOfFiles.length - 3;
		int properties = 0;
		int x = -1;
		int y = -1;
		List<Grid> generations = Collections.synchronizedList(new ArrayList<Grid>());
		MemoryMXBean bean = ManagementFactory.getMemoryMXBean();
		String currFilename = null;
		
		try
		{
			//Searching for the configuration file 
			for(int i = 0; i < listOfFiles.length; i++)
			{
				if(listOfFiles[i].isFile())
				{
					currFilename = listOfFiles[i].getName();
					currFile = new File(tmpDir + "/" + currFilename);
					in = new BufferedInputStream(new FileInputStream(currFile));
					
					String fileData = "";
					
					if(currFilename.endsWith(".cac"))
					{
						configFound = true;
						
						byte[] data = new byte[1024];
						int len = 1024;
						while((len = in.read(data)) > 0)
						{
							fileData += new String(data);
						}
						
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
								Log.error(TAG, "Error retrieving all generations - unable to create a grid with no type");
								return null;
						}
						
						break;
					}
					else
						continue;
				}
			}
			
			if(!configFound)
			{
				Log.error(TAG, "Error retrieving all generations - configuration file not found");
				return null;
			}
			
			int gensToLoad = arrSize;
			int scale = 500;
			long generationSize = MemoryUtil.deepMemoryUsageOf(gen, VisibilityFilter.ALL);;
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
			
			//Searching for all generation files that are to be loaded
			for(int i = 0; i < listOfFiles.length; i++)
			{
				if(listOfFiles[i].isFile())
				{
					currFilename = listOfFiles[i].getName();
					currFile = new File(tmpDir + "/" + currFilename);
					in = new BufferedInputStream(new FileInputStream(currFile));
					
					String fileData = "";
					
					if(currFilename.endsWith(".cag"))
					{
						byte[] data = new byte[1024];
						int len = 1024;
						while((len = in.read(data)) > 0)
						{
							fileData += new String(data);
						}
						
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
			}
			
			in.close();
			in = null;
			
			for(int i = 0; i < gensToLoad; i++)
				generations.add(gens[i]);
			
			return generations;
		}
		catch(IOException ex)
		{
			Log.error(TAG, "Error retrieving all generations - " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Returns the last generation that was written to disk by using the getGeneration function.
	 * 
	 * @return - the last generation written
	 */
	public Grid getLastGeneration()
	{
		return getGeneration(numGenerations - 1);
	}
	
	/**
	 * Returns true if the streamer has been started and false if the streamer has not been started.
	 * 
	 * @return - the boolean on the started status
	 */
	public boolean hasStarted()
	{
		return started;
	}
	
	/**
	 * Resets to the given world.
	 * 
	 * If the streamer has already started, then the reset is a soft reset to Generation Zero.
	 * Otherwise, it is a hard reset to a completely new world.
	 * 
	 * @param w - the world that is to be reset to
	 */
	public void reset(World w)
	{
		if(started)
		{
			reset = true;
			start(w);
		}
		else
		{
			reset = false;
			start(w);
		}
	}
	
	/**
	 * Sets the CAW file name of the streamer to the given file name.
	 * 
	 * @param name - the file name that must be set to
	 */
	public void setWorldFilename(String name)
	{
		cawFilename = name;
	}
	
	/**
	 * Starts the streamer up with the given world.
	 * 
	 * If it is not a soft reset, a new temporary folder is created and set as the current CAW file name.
	 * 
	 * Regardless, the streamer sets the generation counter to 0, re-writes the details from the
	 * world and starts.
	 * 
	 * @param w - the world that the streamer is set to
	 */
	public void start(World w)
	{
		if(!reset)
		{
			File caw = null;
			if(w.getFilename() == null)
				w.setFileName("worlds/newWorld.caw");
			
			int cawIndex = w.getFilename().indexOf(".caw");
			
			String name = (cawIndex != -1 ? w.getFilename() : w.getFilename() + ".caw");
			
			if(cawFilename == null)
				cawFilename = name;
			
			caw = new File(name);
			int worldNum = 1;
			
			while(caw.exists())
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
			
			int backIndex = cawFilename.lastIndexOf("\\");
			int forwardIndex = cawFilename.lastIndexOf("/");
			
			if(backIndex != -1)
				tmpDir = "worlds/[tmp]" + cawFilename.substring(backIndex + 1, cawFilename.lastIndexOf("."));
			else if(forwardIndex != -1)
				tmpDir = "worlds/[tmp]" + cawFilename.substring(forwardIndex + 1, cawFilename.lastIndexOf("."));
			boolean res = (new File(tmpDir).mkdirs());
			
			if(!res)
			{
				String[] files = (new File(tmpDir)).list();
				for(int i = 0; i < files.length; i++)
				{
					String currName = tmpDir + "/" + files[i];
					File currFile = new File(currName);
					if(!currFile.delete())
						Log.error(TAG, "Problem clearing out temporary file " + currName);
				}
			}
			
			Log.debug(TAG, "cawFilename : " + cawFilename);
			Log.debug(TAG, "tmpDir : " + tmpDir);
		}
		
		numGenerations = 0;
		
		writeInitialValues(w);
		
		started = true;
	}
	
	/**
	 * Stops the streamer and streams all information from the temporary folders into a permanent
	 * zipped CAW file.
	 */
	public void stop()
	{
		Log.information(TAG, "The program is streaming the world to disk. Please be patient.");
		
		File folder = new File(tmpDir);
		folder.deleteOnExit();
		
		File caw = new File(cawFilename);
		File[] listOfFiles = folder.listFiles();
		
		try
		{
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(caw)));
		
			for(int i = 0; i < listOfFiles.length; i++)
			{
				if(listOfFiles[i].isFile())
				{
					String currFilename = listOfFiles[i].getName();
					
					File currFile = new File(tmpDir + "/" + currFilename);
					currFile.deleteOnExit();
					
					FileInputStream in = new FileInputStream(currFile);
					ZipEntry entry = new ZipEntry(currFilename);
					
					out.putNextEntry(entry);
					byte[] data = new byte[1024];
					int len = 1024;
					while((len = in.read(data)) > 0)
					{
						out.write(data, 0, len);
					}
					out.closeEntry();
					
					in.close();
				}
			}
			
			out.close();
		}
		catch(IOException ex)
		{
			Log.error(TAG, "Error in stopping - " + ex.getMessage());
			ex.printStackTrace();
		}
		
		started = false;
	}
	
	/**
	 * Streams the given generation to disk in the temporary folder.
	 * 
	 * @param gen - the generation that must be streamed to disk
	 */
	public void streamGeneration(Grid gen)
	{
		OutputStream out = null;
		File genFile = null;
		String data = "";
		
		try
		{
			int genNum = numGenerations;
			
			genFile = new File(tmpDir + "/" + genNum + ".cag");
			if(!genFile.createNewFile())
				Log.warning(TAG, "Error streaming generation to world - could not create new temporary generation " + genNum + " file. Overwriting.");
			out = new BufferedOutputStream(new FileOutputStream(genFile));
			
			for(int rows = 0; rows < gen.getHeight(); rows++)
			{
				for(int cols = 0; cols < gen.getWidth(); cols++)
				{
					for(int index = 0; index < gen.getNumProperties() - 1; index++)
						data += gen.getCell(cols, rows).getValue(index) + " ";
					data += gen.getCell(cols, rows).getValue(gen.getNumProperties() - 1) + "\n";
				}
				data += "\n";
			}
			
			out.write(data.getBytes());
			out.close();
			
			numGenerations++;
		}
		catch(IOException ex)
		{
			Log.error(TAG, "Error streaming generation to the world - " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	/**
	 * Requests all the information from the given world and streams everything into the
	 * temporary folder.
	 * 
	 * @param w - the world that the streamer is set to
	 */
	private void writeInitialValues(World w)
	{
		OutputStream out = null;
		File currFile = null;
		String data = "";
		
		w.setKeepHistory(1);
		
		try
		{
			Grid genZero = w.getInitialGeneration();
			char type = genZero.getTypeSymbol();
			int height = genZero.getHeight();
			int props = genZero.getNumProperties();
			int width = genZero.getWidth();
			
			currFile = new File(tmpDir + "/config.cac");
			out = new BufferedOutputStream(new FileOutputStream(currFile));
			data = width + " " + height + "\n";
			data += type + "\n";
			data += props + "\n";
			out.write(data.getBytes());
			out.close();
			
			currFile = null;
			data = "";
			out = null;
			
			if(w.getStepAmount() == 1)
			{
				currFile = new File(tmpDir + "/rules.cal");
				out = new BufferedOutputStream(new FileOutputStream(currFile));
				
				out.write(w.getRuleCodes().get(0).getBytes());
				out.close();

				currFile = null;
				data = "";
				out = null;
			}
			else
			{
				ArrayList<String> rulesets = w.getRuleCodes();
				for(int i = 0; i < rulesets.size(); i++)
				{
					currFile = new File(tmpDir + "/rules" + (i + 1) + ".cal");
					out = new BufferedOutputStream(new FileOutputStream(currFile));
					out.write(rulesets.get(i).getBytes());
					out.close();
					
					currFile = null;
					data = "";
					out = null;
				}
			}
			
			currFile = new File(tmpDir + "/colours.cacp");
			out = new BufferedOutputStream(new FileOutputStream(currFile));
			out.write(w.getColourCode().getBytes());
			out.close();
			
			List<Grid> gens = w.getGenerations();
			for (int i = 0; i < gens.size(); i++)
			{
				Grid grid = gens.get(i);
				currFile = new File(tmpDir + "/" + i + ".cag");
				out = new BufferedOutputStream(new FileOutputStream(currFile));
				
				for(int rows = 0; rows < height; rows++)
				{
					for(int cols = 0; cols < width; cols++)
					{
						for(int index = 0; index < props - 1; index++)
							data += grid.getCell(cols, rows).getValue(index) + " ";
						data += grid.getCell(cols, rows).getValue(props - 1) + "\n";
					}
					data += "\n";
				}
				out.write(data.getBytes());
				out.close();
				
				currFile = null;
				data = "";
				out = null;
			}
			
			numGenerations = gens.size();
			
			w.setGenAmount(numGenerations);
		}
		catch(IOException ex)
		{
			Log.error(TAG, "Error writing all initial values to the world - " + ex.getMessage());
			ex.printStackTrace();
		}
		
		w.setKeepHistory(2);
	}
}