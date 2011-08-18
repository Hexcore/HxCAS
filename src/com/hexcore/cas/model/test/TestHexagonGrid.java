package com.hexcore.cas.model.test;

import junit.framework.TestCase;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.HexagonGrid;

public class TestHexagonGrid extends TestCase
{
	public void testGetNeighboursFunction()
	{
		//test 1 x 1 grid (no neighbours)
		HexagonGrid grid = addNeighbours(1,1);
		TestHexagonGrid.assertEquals(0.0, grid.getCell(new Vector2i(0,0)).getValue(0));
		
		//test a square grid (equal number of hexagons in each direction)
		grid = addNeighbours(3,3);
		TestHexagonGrid.assertEquals(31.0, grid.getCell(new Vector2i(0,0)).getValue(0));
		TestHexagonGrid.assertEquals(31.0, grid.getCell(new Vector2i(1,0)).getValue(0));
		TestHexagonGrid.assertEquals(37.0, grid.getCell(new Vector2i(2,0)).getValue(0));
		TestHexagonGrid.assertEquals(20.0, grid.getCell(new Vector2i(0,1)).getValue(0));
		TestHexagonGrid.assertEquals(26.0, grid.getCell(new Vector2i(1,1)).getValue(0));
		TestHexagonGrid.assertEquals(26.0, grid.getCell(new Vector2i(1,1)).getValue(0));
		TestHexagonGrid.assertEquals(13.0, grid.getCell(new Vector2i(0,2)).getValue(0));
		TestHexagonGrid.assertEquals(13.0, grid.getCell(new Vector2i(1,2)).getValue(0));
		TestHexagonGrid.assertEquals(19.0, grid.getCell(new Vector2i(2,2)).getValue(0));
		
		//test an unequal number of hexagons
		grid = addNeighbours(2,3);
		TestHexagonGrid.assertEquals(20.0, grid.getCell(new Vector2i(0,0)).getValue(0));
		TestHexagonGrid.assertEquals(22.0, grid.getCell(new Vector2i(1,0)).getValue(0));
		TestHexagonGrid.assertEquals(14.0, grid.getCell(new Vector2i(0,1)).getValue(0));
		TestHexagonGrid.assertEquals(16.0, grid.getCell(new Vector2i(1,1)).getValue(0));
		TestHexagonGrid.assertEquals(8.0, grid.getCell(new Vector2i(0,2)).getValue(0));
		TestHexagonGrid.assertEquals(10.0, grid.getCell(new Vector2i(1,2)).getValue(0));
	}

	private static HexagonGrid addNeighbours(int x, int y)
	{
		HexagonGrid grid = new HexagonGrid(new Vector2i(x,y));
		int count = 0;
		for(int i = 0; i < y; i++)
			for(int j = 0; j < x; j++)
				grid.getCell(new Vector2i(j,i)).setValue(0, count++);
		
		HexagonGrid neighbourSum = new HexagonGrid(new Vector2i(x,y));
		for(int i = 0; i < y; i++)
		{
			for(int j = 0; j < x; j++)
			{
				Cell[] neighbours = grid.getNeighbours(new Vector2i(j,i));
				double temp = 0.0;
				for(int k = 0; k < 6; k++)
				{
					if(neighbours[k] != null)
						temp = temp + neighbours[k].getValue(0);
				}
				neighbourSum.getCell(new Vector2i(j,i)).setValue(0, temp);
			}
		}
		return neighbourSum;
	}
	
}
