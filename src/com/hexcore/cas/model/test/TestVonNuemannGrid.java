package com.hexcore.cas.model.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.VonNuemannGrid;


public class TestVonNuemannGrid
{
	private final VonNuemannGrid g = new VonNuemannGrid(new Vector2i(5, 10), 1);
	private final int size = 4;
	
	@Test
	public void testWidth()
	{
		int expectedResults = 5;
		assertEquals(expectedResults, g.getWidth());
	}
	
	@Test
	public void testHeight()
	{
		int expectedResults = 10;
		assertEquals(expectedResults, g.getHeight());
	}
	
	@Test
	public void testSize()
	{
		Vector2i expectedResults = new Vector2i(5, 10);
		assertEquals(expectedResults.x, g.getSize().x);
		assertEquals(expectedResults.y, g.getSize().y);
	}
	
	@Test
	public void testGetCell()
	{
		double[] vals = {0};
		Cell c = new Cell(vals);
		assertEquals(c.getValue(0), g.getCell(new Vector2i(0, 0)).getValue(0), 0.0);
	}
	
	@Test
	public void testGetNeighbours00()
	{
		int gridSize = 5;
		VonNuemannGrid t = new VonNuemannGrid(new Vector2i(gridSize, gridSize), 1);
		int cnt = 0;
		for(int x = 0; x < gridSize; x++)
			for(int y = 0; y < gridSize; y++)
				t.getCell(new Vector2i(x, y)).setValue(0, cnt++);
		Vector2i pos = new Vector2i(0, 0);
		double[] vals = new double[size];
		
		//Wrappable test
		vals[0] = 4.0;
		vals[1] = 20.0;
		vals[2] = 5.0;
		vals[3] = 1.0;
		for(int i = 0; i < size; i++)
			assertEquals(vals[i], t.getNeighbours(pos)[i].getValue(0), 0.0);
		
		//Non-wrapping test
		t.setWrappable(false);
		vals[0] = Double.MIN_VALUE;
		vals[1] = Double.MIN_VALUE;
		vals[2] = 5.0;
		vals[3] = 1.0;
		for(int i = 0; i < size; i++)
		{
			if(vals[i] == Double.MIN_VALUE)
				assertEquals(null, t.getNeighbours(pos)[i]);
			else
				assertEquals(vals[i], t.getNeighbours(pos)[i].getValue(0), 0.0);
		}
	}
	
	@Test
	public void testGetNeighbours01()
	{
		int gridSize = 5;
		VonNuemannGrid t = new VonNuemannGrid(new Vector2i(gridSize, gridSize), 1);
		int cnt = 0;
		for(int x = 0; x < gridSize; x++)
			for(int y = 0; y < gridSize; y++)
				t.getCell(new Vector2i(x, y)).setValue(0, cnt++);
		Vector2i pos = new Vector2i(1, 0);
		double[] vals = new double[size];
		
		//Wrappable test
		vals[0] = 9.0;
		vals[1] = 0.0;
		vals[2] = 10.0;
		vals[3] = 6.0;
		for(int i = 0; i < size; i++)
			assertEquals(vals[i], t.getNeighbours(pos)[i].getValue(0), 0.0);
		
		//Non-wrapping test
		t.setWrappable(false);
		vals[0] = Double.MIN_VALUE;
		vals[1] = 0.0;
		vals[2] = 10.0;
		vals[3] = 6.0;
		for(int i = 0; i < size; i++)
		{
			if(vals[i] == Double.MIN_VALUE)
				assertEquals(null, t.getNeighbours(pos)[i]);
			else
				assertEquals(vals[i], t.getNeighbours(pos)[i].getValue(0), 0.0);
		}
	}
	
	@Test
	public void testGetNeighbours22()
	{
		int gridSize = 5;
		VonNuemannGrid t = new VonNuemannGrid(new Vector2i(gridSize, gridSize), 1);
		int cnt = 0;
		for(int x = 0; x < gridSize; x++)
			for(int y = 0; y < gridSize; y++)
				t.getCell(new Vector2i(x, y)).setValue(0, cnt++);
		Vector2i pos = new Vector2i(2, 2);
		double[] vals = new double[size];
		
		//Wrappable test
		vals[0] = 11.0;
		vals[1] = 7.0;
		vals[2] = 17.0;
		vals[3] = 13.0;
		for(int i = 0; i < size; i++)
			assertEquals(vals[i], t.getNeighbours(pos)[i].getValue(0), 0.0);
		
		//Non-wrapping test
		t.setWrappable(false);
		vals[0] = 11.0;
		vals[1] = 7.0;
		vals[2] = 17.0;
		vals[3] = 13.0;
		for(int i = 0; i < size; i++)
		{
			if(vals[i] == Double.MIN_VALUE)
				assertEquals(null, t.getNeighbours(pos)[i]);
			else
				assertEquals(vals[i], t.getNeighbours(pos)[i].getValue(0), 0.0);
		}
	}
}
