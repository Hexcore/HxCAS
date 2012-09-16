package com.hexcore.cas.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Class WorldSaver
 * 	An instance of this object is used to write all details of a world to where it is to be stored
 * 	according to the file name given by the world.
 * 
 * @author Megan Duncan
 */

public class WorldSaver
{
	private static final String		TAG = "WorldSaver";
	
	/**
	 * WorldSaver default constructor.
	 */
	public WorldSaver()
	{
	}
	
	/**
	 * Saves the world to the file name that the world has.
	 * 
	 * This is done by creating a zipped CAW file to persist the world to that is named by the
	 * file name given by the world. Into this zipped file goes a configuration file, at least
	 * one rule set file, a colour set file and at least one generation file.
	 * 
	 * @param world - the world that needs to be saved
	 * 
	 * @throws IOException
	 */
	public void saveWorld(World world)
		throws IOException
	{
		Grid generationZero = world.getInitialGeneration();
		
		char type = generationZero.getTypeSymbol();
		int gridHeight = generationZero.getHeight();
		int gridWidth = generationZero.getWidth();
		int properties = generationZero.getNumProperties();
		
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
		
		if(world.getStepAmount() == 1)
		{
			ZipEntry ruleCodeEntry = new ZipEntry("rules.cal");
			out.putNextEntry(ruleCodeEntry);
			out.write(world.getRuleCode().getBytes());
			out.closeEntry();
		}
		else
		{
			ArrayList<String> rulesets = world.getRuleCodes();
			for(int i = 0; i < rulesets.size(); i++)
			{
				ZipEntry ruleCodeEntry = new ZipEntry("rules" + (i + 1) + ".cal");
				out.putNextEntry(ruleCodeEntry);
				out.write(rulesets.get(i).getBytes());
				out.closeEntry();
			}
		}
		
		ZipEntry colourCodeEntry = new ZipEntry("colours.cacp");
		out.putNextEntry(colourCodeEntry);
		out.write(world.getColourCode().getBytes());
		out.closeEntry();
		
		int differenceGenerationCounter = 0;
		
		List<Grid> generations = world.getGenerations();
		for(int i = 0; i < generations.size(); i++)
		{
			Grid grid = generations.get(i);
			ZipEntry caw = new ZipEntry(i + ".cag");
			out.putNextEntry(caw);
			String str = "";
			
			List<CoordinatedCell> differentCells = getDifferenceCells(i - 1, i, generations);
			
			if(differenceGenerationCounter >= 20)
			{
				differentCells.clear();
				differenceGenerationCounter = 0;
			}
			
			if(differentCells.size() == 0)
			{
				str += "E\n";
				
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
				
				differenceGenerationCounter = 0;
			}
			else
			{
				str += "D\n";
				
				for(int j = 0; j < differentCells.size(); j++)
				{
					CoordinatedCell cell = differentCells.get(j);
					str += cell.x + " " + cell.y + " ";
					
					for(int index = 0; index < properties - 1; index++)
						str += cell.getValue(index) + " ";
					str += cell.getValue(properties - 1) + "\n";
				}
				
				differenceGenerationCounter++;
			}
			
			out.write(str.getBytes());
			out.closeEntry();
		}
		
		out.close();
	}
	
	/////////////////////////////////////////////
	/// Private functions
	/**
	 * Gets a list of the cells that are different between the current generation and the previous
	 * generation.
	 * 
	 * If the current generation is Generation Zero, then an empty list is returned.
	 * 
	 * If the number of differences between the generations is larger than the threshold,
	 * an empty list is returned.
	 * 
	 * Otherwise returns the list of difference cells.
	 * 
	 * @param prev - the index of the previous generation
	 * @param curr - the index of the current generation
	 * @param generations - the list of generations for the world
	 * 
	 * @return - the list of difference cells
	 */
	private List<CoordinatedCell> getDifferenceCells(int prev, int curr, List<Grid> generations)
	{
		if(curr == 0)
			return new ArrayList<CoordinatedCell>();
		else
		{
			List<CoordinatedCell> differentCells = new ArrayList<CoordinatedCell>();
			
			Grid generationZero = generations.get(0);
			Grid prevGen = generations.get(prev);
			Grid currGen = generations.get(curr);
			
			int gridHeight = generationZero.getHeight();
			int gridWidth = generationZero.getWidth();
			int properties = generationZero.getNumProperties();

			int totalProperties = gridHeight * gridWidth * properties;
			int diff = 0;
			double threshold = (double)properties / (double)(properties + 2);
			double percent = 1.0;
			
			for(int rows = 0; rows < gridHeight; rows++)
				for(int cols = 0; cols < gridWidth; cols++)
					for(int props = 0; props < properties; props++)
						if(prevGen.getCell(cols, rows).getValue(props) != currGen.getCell(cols, rows).getValue(props))
						{
							diff++;
							differentCells.add(new CoordinatedCell(currGen.getCell(cols, rows), cols, rows));
						}
			
			percent = (double)diff / (double)totalProperties;
			
			if(percent < threshold)
				return differentCells;
			else
				return new ArrayList<CoordinatedCell>();
		}
	}
	
	/////////////////////////////////////////////
	/// Inner classes
	private class CoordinatedCell extends Cell
	{
		public int x;
		public int y;
		
		public CoordinatedCell(Cell cell, int x, int y)
		{
			super(cell);
			this.x = x;
			this.y = y;
		}
	}
}
