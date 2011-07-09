package com.hexcore.test;
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
		int[] vals = {0};
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
		int[] vals = new int[12];
		vals[0] = 23;
		vals[1] = 24;
		vals[2] = 20;
		vals[3] = 21;
		vals[4] = 22;
		vals[5] = 3;
		vals[6] = 4;
		vals[7] = 1;
		vals[8] = 2;
		vals[9] = 9;
		vals[10] = 5;
		vals[11] = 6;
		cnt = 0;
		for(int i = 0; i < 12; i++)
			assertEquals(vals[i], t.getNeighbours(pos)[i].getValue(0));
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
				System.out.println("Cell[" + y + "][" + x + "] : " + (cnt - 1));
			}
		Vector2i pos = new Vector2i(1, 0);
		int[] vals = new int[12];
		vals[0] = 20;
		vals[1] = 21;
		vals[2] = 22;
		vals[3] = 4;
		vals[4] = 0;
		vals[5] = 2;
		vals[6] = 3;
		vals[7] = 9;
		vals[8] = 5;
		vals[9] = 6;
		vals[10] = 7;
		vals[11] = 8;
		cnt = 0;
		for(int i = 0; i < 12; i++)
			assertEquals(vals[i], t.getNeighbours(pos)[i].getValue(0));
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
		int[] vals = new int[12];
		vals[0] = 5;
		vals[1] = 6;
		vals[2] = 7;
		vals[3] = 8;
		vals[4] = 9;
		vals[5] = 10;
		vals[6] = 11;
		vals[7] = 13;
		vals[8] = 14;
		vals[9] = 16;
		vals[10] = 17;
		vals[11] = 18;
		cnt = 0;
		for(int i = 0; i < 12; i++)
			assertEquals(vals[i], t.getNeighbours(pos)[i].getValue(0));
	}
}
