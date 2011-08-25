package com.hexcore.cas.model.test;

import junit.framework.TestCase;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.HexagonGrid;

public class TestHexagonGrid extends TestCase
{
	public void testGetNeighboursFunction()
	{
		HexagonGrid grid;
		
		//test a square grid (equal number of hexagons in each direction)
		grid = addNeighbours(3,3);
		
		//test an unequal number of hexagons
		grid = addNeighbours(2,3);
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
