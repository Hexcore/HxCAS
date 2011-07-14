package com.hexcore.cas.model.test;

import junit.framework.TestCase;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.RectangleGrid;


public class TestRectangleGrid extends TestCase
{
	public void testGetNeighboursFunction()
	{
		//test 1 x 1 grid (no neighbours)
		RectangleGrid grid = addNeighbours(1,1);
		TestRectangleGrid.assertEquals(0, grid.getCell(new Vector2i(0,0)).getValue(0));
		
		//Test square grid (2 x 2 grid)
		grid = addNeighbours(2,2);
		TestRectangleGrid.assertEquals(18, grid.getCell(new Vector2i(0,0)).getValue(0));
		TestRectangleGrid.assertEquals(14, grid.getCell(new Vector2i(0,1)).getValue(0));
		TestRectangleGrid.assertEquals(10, grid.getCell(new Vector2i(1,0)).getValue(0));
		TestRectangleGrid.assertEquals(6, grid.getCell(new Vector2i(1,1)).getValue(0));
		
		//Test vertically rectangular grid :P i.e. 2 x 3 grid
		grid = addNeighbours(2,3);
		TestRectangleGrid.assertEquals(27, grid.getCell(new Vector2i(0,0)).getValue(0));
		TestRectangleGrid.assertEquals(26, grid.getCell(new Vector2i(0,1)).getValue(0));
		TestRectangleGrid.assertEquals(25, grid.getCell(new Vector2i(0,2)).getValue(0));
		TestRectangleGrid.assertEquals(15, grid.getCell(new Vector2i(1,0)).getValue(0));
		TestRectangleGrid.assertEquals(14, grid.getCell(new Vector2i(1,1)).getValue(0));
		TestRectangleGrid.assertEquals(13, grid.getCell(new Vector2i(1,2)).getValue(0));
		
		//Test horizontally rectangular grid :P i.e. 3 x 1 grid
		grid = addNeighbours(3,1);
		TestRectangleGrid.assertEquals(9, grid.getCell(new Vector2i(0,0)).getValue(0));
		TestRectangleGrid.assertEquals(6, grid.getCell(new Vector2i(1,0)).getValue(0));
		TestRectangleGrid.assertEquals(3, grid.getCell(new Vector2i(2,0)).getValue(0));
	}
	
	private static RectangleGrid addNeighbours(int x, int y)
	{
		RectangleGrid grid = new RectangleGrid(new Vector2i(x,y));
		int count = 0;
		
		for(int i = 0; i < x; i++)
			for(int j = 0; j < y; j++)
				grid.getCell(new Vector2i(i,j)).setValue(0, count++);
		
		RectangleGrid neighbourSum = new RectangleGrid(new Vector2i(x,y));
		for(int i = 0; i < x; i++)
		{
			for(int j = 0; j < y; j++)
			{
				Cell[] neighbours = grid.getNeighbours(new Vector2i(i,j));
				int temp = 0;
				
				for(int k = 0; k < 8; k++)
				{
					if(neighbours[k] != null)
						temp = temp + neighbours[k].getValue(0);
				}
				neighbourSum.getCell(new Vector2i(i,j)).setValue(0, temp);
			}
		}
		return neighbourSum;
	}
}
