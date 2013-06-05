package com.hexcore.cas.model.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.HexagonGrid;

public class TestHexagonGrid
{
	@Test
	public void testGetNeighboursFunction()
	{
		HexagonGrid grid;
		
		//test a square grid (equal number of hexagons in each direction)
		//wrap-around
		grid = addNeighbours(3,3, true);
		assertEquals(27.0, 	grid.getCell(0, 0).getValue(0), 0.0);
		assertEquals(21.0, 	grid.getCell(1, 0).getValue(0), 0.0);
		assertEquals(27.0, 	grid.getCell(2, 0).getValue(0), 0.0);
		
		assertEquals(18.0, 	grid.getCell(0, 1).getValue(0), 0.0);
		assertEquals(30.0, 	grid.getCell(1, 1).getValue(0), 0.0);
		assertEquals(18.0, 	grid.getCell(2, 1).getValue(0), 0.0);
		
		assertEquals(27.0, 	grid.getCell(0, 2).getValue(0), 0.0);
		assertEquals(21.0, 	grid.getCell(1, 2).getValue(0), 0.0);
		assertEquals(27.0, 	grid.getCell(2, 2).getValue(0), 0.0);
		
		//not wrap-around
		grid = addNeighbours(3,3, false);
		assertEquals(4.0, 	grid.getCell(0, 0).getValue(0), 0.0);
		assertEquals(14.0, 	grid.getCell(1, 0).getValue(0), 0.0);
		assertEquals(6.0, 	grid.getCell(2, 0).getValue(0), 0.0);
		
		assertEquals(11.0, 	grid.getCell(0, 1).getValue(0), 0.0);
		assertEquals(30.0, 	grid.getCell(1, 1).getValue(0), 0.0);
		assertEquals(15.0, 	grid.getCell(2, 1).getValue(0), 0.0);
		
		assertEquals(14.0, 	grid.getCell(0, 2).getValue(0), 0.0);
		assertEquals(18.0, 	grid.getCell(1, 2).getValue(0), 0.0);
		assertEquals(16.0, 	grid.getCell(2, 2).getValue(0), 0.0);
	}

	private static HexagonGrid addNeighbours(int x, int y, boolean wrap)
	{
		HexagonGrid grid = new HexagonGrid(new Vector2i(x,y), 1);
		grid.setWrappable(wrap);
		int count = 0;
		for(int i = 0; i < y; i++)
			for(int j = 0; j < x; j++)
				grid.getCell(new Vector2i(j,i)).setValue(0, count++);
		
		HexagonGrid neighbourSum = new HexagonGrid(new Vector2i(x,y), 1);
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
					//System.out.println(neighbours[k].getValue(0));
				}
				neighbourSum.getCell(new Vector2i(j,i)).setValue(0, temp);
				//System.out.println();
				//System.out.println();
			}
		}
		return neighbourSum;
	}
	
}
