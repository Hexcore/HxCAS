package com.hexcore.cas.model.test;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.TriangleGrid;

import junit.framework.TestCase;

public class TestTriangleGrid extends TestCase
{
	private final TriangleGrid g = new TriangleGrid(new Vector2i(5, 10));
	
	public void test1Width()
	{
		int expectedResults = 5;
		assertEquals(expectedResults, g.getWidth());
	}
	
	public void test2Height()
	{
		int expectedResults = 10;
		assertEquals(expectedResults, g.getHeight());
	}
	
	public void test3Size()
	{
		Vector2i expectedResults = new Vector2i(5, 10);
		assertEquals(expectedResults.x, g.getSize().x);
		assertEquals(expectedResults.y, g.getSize().y);
	}
	
	public void test4GetCell()
	{
		double[] vals = {0};
		Cell c = new Cell(vals);
		assertEquals(c.getValue(0), g.getCell(new Vector2i(0, 0)).getValue(0));
	}
	
	public void test5GetNeighbours00()
	{
		int gridSize = 5;
		TriangleGrid t = new TriangleGrid(new Vector2i(gridSize, gridSize));
		int cnt = 0;
		for(int y = 0; y < gridSize; y++)
			for(int x = 0; x < gridSize; x++)
				t.getCell(new Vector2i(x, y)).setValue(0, cnt++);
		Vector2i pos = new Vector2i(0, 0);
		double[] vals = new double[12];
		
		//Wrappable test
		vals[0] = 23.0;
		vals[1] = 24.0;
		vals[2] = 20.0;
		vals[3] = 21.0;
		vals[4] = 22.0;
		vals[5] = 3.0;
		vals[6] = 4.0;
		vals[7] = 1.0;
		vals[8] = 2.0;
		vals[9] = 9.0;
		vals[10] = 5.0;
		vals[11] = 6.0;
		for(int i = 0; i < 12; i++)
			assertEquals(vals[i], t.getNeighbours(pos)[i].getValue(0));
		
		//Non-wrapping test
		t.setWrappable(false);
		vals[0] = Double.MIN_VALUE;
		vals[1] = Double.MIN_VALUE;
		vals[2] = Double.MIN_VALUE;
		vals[3] = Double.MIN_VALUE;
		vals[4] = Double.MIN_VALUE;
		vals[5] = Double.MIN_VALUE;
		vals[6] = Double.MIN_VALUE;
		vals[7] = 1.0;
		vals[8] = 2.0;
		vals[9] = Double.MIN_VALUE;
		vals[10] = 5.0;
		vals[11] = 6.0;
		for(int i = 0; i < 12; i++)
		{
			if(vals[i] == Double.MIN_VALUE)
				assertEquals(null, t.getNeighbours(pos)[i]);
			else
				assertEquals(vals[i], t.getNeighbours(pos)[i].getValue(0));
		}
	}
	
	public void test6GetNeighbours01()
	{
		int gridSize = 5;
		TriangleGrid t = new TriangleGrid(new Vector2i(gridSize, gridSize));
		int cnt = 0;
		for(int y = 0; y < gridSize; y++)
			for(int x = 0; x < gridSize; x++)
			{
				t.getCell(new Vector2i(x, y)).setValue(0, cnt++);
				//System.out.println("Cell[" + y + "][" + x + "] : " + (cnt - 1));
			}
		Vector2i pos = new Vector2i(1, 0);
		double[] vals = new double[12];
		
		//Wrappable test
		vals[0] = 20.0;
		vals[1] = 21.0;
		vals[2] = 22.0;
		vals[3] = 4.0;
		vals[4] = 0.0;
		vals[5] = 2.0;
		vals[6] = 3.0;
		vals[7] = 9.0;
		vals[8] = 5.0;
		vals[9] = 6.0;
		vals[10] = 7.0;
		vals[11] = 8.0;
		for(int i = 0; i < 12; i++)
			assertEquals(vals[i], t.getNeighbours(pos)[i].getValue(0));
		
		//Non-wrapping test
		t.setWrappable(false);
		vals[0] = Double.MIN_VALUE;
		vals[1] = Double.MIN_VALUE;
		vals[2] = Double.MIN_VALUE;
		vals[3] = Double.MIN_VALUE;
		vals[4] = 0.0;
		vals[5] = 2.0;
		vals[6] = 3.0;
		vals[7] = Double.MIN_VALUE;
		vals[8] = 5.0;
		vals[9] = 6.0;
		vals[10] = 7.0;
		vals[11] = 8.0;
		for(int i = 0; i < 12; i++)
		{
			if(vals[i] == Double.MIN_VALUE)
				assertEquals(null, t.getNeighbours(pos)[i]);
			else
				assertEquals(vals[i], t.getNeighbours(pos)[i].getValue(0));
		}
	}
	
	public void test7GetNeighbours22()
	{
		int gridSize = 5;
		TriangleGrid t = new TriangleGrid(new Vector2i(gridSize, gridSize));
		int cnt = 0;
		for(int y = 0; y < gridSize; y++)
			for(int x = 0; x < gridSize; x++)
				t.getCell(new Vector2i(x, y)).setValue(0, cnt++);
		Vector2i pos = new Vector2i(2, 2);
		double[] vals = new double[12];
		
		//Wrappable test
		vals[0] = 5.0;
		vals[1] = 6.0;
		vals[2] = 7.0;
		vals[3] = 8.0;
		vals[4] = 9.0;
		vals[5] = 10.0;
		vals[6] = 11.0;
		vals[7] = 13.0;
		vals[8] = 14.0;
		vals[9] = 16.0;
		vals[10] = 17.0;
		vals[11] = 18.0;
		for(int i = 0; i < 12; i++)
			assertEquals(vals[i], t.getNeighbours(pos)[i].getValue(0));
		
		//Non-wrapping test
		t.setWrappable(false);
		vals[0] = 5.0;
		vals[1] = 6.0;
		vals[2] = 7.0;
		vals[3] = 8.0;
		vals[4] = 9.0;
		vals[5] = 10.0;
		vals[6] = 11.0;
		vals[7] = 13.0;
		vals[8] = 14.0;
		vals[9] = 16.0;
		vals[10] = 17.0;
		vals[11] = 18.0;
		for(int i = 0; i < 12; i++)
		{
			if(vals[i] == Double.MIN_VALUE)
				assertEquals(null, t.getNeighbours(pos)[i]);
			else
				assertEquals(vals[i], t.getNeighbours(pos)[i].getValue(0));
		}
	}
}
