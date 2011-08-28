package com.hexcore.cas.model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class WorldSaver
{
	private String worldFileName = null;
	private Grid[] world = null;
	private String rulesAndColours = null;
	private ArrayList<Grid> listWorld = new ArrayList<Grid>();
	
	public WorldSaver(String name)
	{
		worldFileName = name;
	}
	
	public void addGeneration(Grid w)
	{
		listWorld.add(w);
	}
	
	//Used for test cases only.
	public int getListWorldSize()
	{
		return listWorld.size();
	}
	
	public String getRulesAndColours()
	{
		return rulesAndColours;
	}
	
	public String getWorldName()
	{
		return worldFileName.substring(worldFileName.indexOf('/') + 1);
	}
	
	public void saveWorld()
		throws IOException
	{
		char type = listWorld.get(0).getType();
		switch(type)
		{
			case 'r':
			case 'R':
				world = new RectangleGrid[listWorld.size()];
				listWorld.toArray(world);
				break;
			case 'h':
			case 'H':
				world = new HexagonGrid[listWorld.size()];
				listWorld.toArray(world);
				break;
			case 't':
			case 'T':
				world = new TriangleGrid[listWorld.size()];
				listWorld.toArray(world);
				break;
			default:
				System.out.println("Unable to create a grid with no type.");
				return;	
		}
		int y = world[0].getHeight();
		int x = world[0].getWidth();
		int n = world[0].getCell(0, 0).getValueCount();
		
		/*
		 * Creates a ZIP file to persist the world, it's configuration,
		 * it's rule set and it's generations. 
		 */
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(worldFileName));
		
		ZipEntry config = new ZipEntry("config.cac");
		out.putNextEntry(config);
		String configStr = x + " " + y + "\r\n";
		configStr += type + "\r\n";
		configStr += n + "\r\n";
		out.write(configStr.getBytes());
		out.closeEntry();
		
		ZipEntry RAC = new ZipEntry("rules.car");
		out.putNextEntry(RAC);
		out.write(rulesAndColours.getBytes());
		out.closeEntry();
		
		for(int i = 0; i < listWorld.size(); i++)
		{
			ZipEntry caw = new ZipEntry(i + ".cag");
			out.putNextEntry(caw);
			String str = "";
			for(int rows = 0; rows < y; rows++)
			{
				for(int cols = 0; cols < x; cols++)
				{
					for(int index = 0; index < (n - 1); index++)
						str += world[i].getCell(cols, rows).getValue(index) + " ";
					str += world[i].getCell(cols, rows).getValue(n - 1) + "\r\n";
				}
				str += "\r\n";
			}
			out.write(str.getBytes());
			out.closeEntry();
		}
		
		out.close();
	}
	
	public void setRulesAndColours(String RAC)
	{
		rulesAndColours = RAC;
	}
	
	public void setWorldName(String name)
	{
		worldFileName = name;
	}
}
