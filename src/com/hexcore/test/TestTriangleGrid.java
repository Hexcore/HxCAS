package com.hexcore.test;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.TriangleGrid;

import junit.framework.TestCase;

public class TestTriangleGrid extends TestCase
{
	private int[] hVals = {1, 2, 3, 4, 5};
	private final TriangleGrid g = new TriangleGrid(new Vector2i(5, 10));
	private final TriangleGrid h = new TriangleGrid(new Vector2i(4, 4), new Cell(hVals));
	
	public void test1Width()
	{
		int expectedResults = 10;
		assertEquals(expectedResults, g.getWidth());
	}
	
	public void test2Height()
	{
		int expectedResults = 5;
		assertEquals(expectedResults, g.getHeight());
	}
	
	public void test3Size()
	{
		Vector2i expectedResults = new Vector2i(5, 10);
		assertEquals(expectedResults.get(0), g.getSize().get(0));
		assertEquals(expectedResults.get(1), g.getSize().get(1));
	}
	
	public void test4GetCell()
	{
		int[] vals = {0};
		Cell c = new Cell(vals);
		assertEquals(c.getValue(0), g.getCell(new Vector2i(0, 0)).getValue(0));
	}
	
	//public void test5GetNeighbours()
	//{
		
	//}
}
