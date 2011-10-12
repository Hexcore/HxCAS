package com.hexcore.cas.model.test;

import junit.framework.TestCase;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.RectangleGrid;


public class TestRectangleGrid extends TestCase
{
	public void testGetNeighboursFunction()
	{
		RectangleGrid grid;
		
		grid = addNeighbours(2,2, true);
		assertEquals(18.0, 	grid.getCell(0, 0).getValue(0));
		assertEquals(14.0, 	grid.getCell(0, 1).getValue(0));
		assertEquals(10.0, 	grid.getCell(1, 0).getValue(0));
		assertEquals(6.0, 	grid.getCell(1, 1).getValue(0));
		
		grid = addNeighbours(2,2, false);
		assertEquals(6.0, 	grid.getCell(0, 0).getValue(0));
		assertEquals(5.0, 	grid.getCell(0, 1).getValue(0));
		assertEquals(4.0, 	grid.getCell(1, 0).getValue(0));
		assertEquals(3.0, 	grid.getCell(1, 1).getValue(0));
		
		grid = addNeighbours(2,3, true);
		assertEquals(27.0, 	grid.getCell(0, 0).getValue(0));
		assertEquals(26.0, 	grid.getCell(0, 1).getValue(0));
		assertEquals(25.0, 	grid.getCell(0, 2).getValue(0));
		assertEquals(15.0, 	grid.getCell(1, 0).getValue(0));
		assertEquals(14.0, 	grid.getCell(1, 1).getValue(0));
		assertEquals(13.0, 	grid.getCell(1, 2).getValue(0));
		
		grid = addNeighbours(2,3, false);
		assertEquals(8.0, 	grid.getCell(0, 0).getValue(0));
		assertEquals(14.0, 	grid.getCell(0, 1).getValue(0));
		assertEquals(10.0, 	grid.getCell(0, 2).getValue(0));
		assertEquals(5.0, 	grid.getCell(1, 0).getValue(0));
		assertEquals(11.0, 	grid.getCell(1, 1).getValue(0));
		assertEquals(7.0, 	grid.getCell(1, 2).getValue(0));
		
		grid = addNeighbours(3,2,true);
		assertEquals(24.0, 	grid.getCell(0, 0).getValue(0));
		assertEquals(20.0, 	grid.getCell(0, 1).getValue(0));
		assertEquals(22.0, 	grid.getCell(1, 0).getValue(0));
		assertEquals(18.0, 	grid.getCell(1, 1).getValue(0));
		assertEquals(20.0, 	grid.getCell(2, 0).getValue(0));
		assertEquals(16.0, 	grid.getCell(2, 1).getValue(0));
		
		grid = addNeighbours(3,2, false);
		assertEquals(6.0, 	grid.getCell(0, 0).getValue(0));
		assertEquals(5.0, 	grid.getCell(0, 1).getValue(0));
		assertEquals(13.0, 	grid.getCell(1, 0).getValue(0));
		assertEquals(12.0, 	grid.getCell(1, 1).getValue(0));
		assertEquals(10.0, 	grid.getCell(2, 0).getValue(0));
		assertEquals(9.0, 	grid.getCell(2, 1).getValue(0));
	}
	
	private static RectangleGrid addNeighbours(int x, int y, boolean wrap)
	{
		RectangleGrid grid = new RectangleGrid(new Vector2i(x,y), 1);
		grid.setWrappable(wrap);
		int count = 0;
		
		for(int i = 0; i < x; i++)
			for(int j = 0; j < y; j++)
				grid.getCell(new Vector2i(i,j)).setValue(0, count++);
		
		RectangleGrid neighbourSum = new RectangleGrid(new Vector2i(x,y), 1);
		for(int i = 0; i < x; i++)
		{
			for(int j = 0; j < y; j++)
			{
				Cell[] neighbours = grid.getNeighbours(new Vector2i(i,j));
				double temp = 0.0;
				
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
