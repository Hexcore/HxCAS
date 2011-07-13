package com.hexcore.cas.model;

import com.hexcore.cas.math.Vector2i;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class WorldReader
{
	private String worldFileName = null;
	private Grid world = null; 
	
	public WorldReader(String name)
	{
		worldFileName = name;
	}
	
	public Grid readWorld()
		throws FileNotFoundException
	{
		Scanner scan = new Scanner(new File(worldFileName));
		int x = scan.nextInt();
		int y = scan.nextInt();
		Vector2i size = new Vector2i(x, y);
		char gridType = scan.next().charAt(0);
		switch(gridType)
		{
			case 'H':
			case 'h':
				world = new HexagonGrid(size);
				break;
			case 'R':
			case 'r':
				world = new RectangleGrid(size);
				break;
			case 'T':
			case 't':
				world = new TriangleGrid(size);
				break;
			default:
				System.out.println("Error! Grid cannot be an abstract grid type!");
				break;
		}
		for(int row = 0; row < y; row++)
		{
			for(int col = 0; col < x; col++)
			{
				int n = scan.nextInt();
				int[] vals = new int[n];
				for(int i = 0; i < n; i++)
					vals[i] = scan.nextInt();
				world.setCells(new Vector2i(col, row), vals);
			}
		}
		return world;
	}
	
	public String getWorldName()
	{
		return worldFileName.substring(worldFileName.indexOf('/') + 1);
	}
	
	public void setWorldName(String name)
	{
		worldFileName = name;
	}
}
