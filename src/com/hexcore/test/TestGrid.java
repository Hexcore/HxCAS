package com.hexcore.test;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.Grid;

import junit.framework.TestCase;

public class TestGrid extends TestCase
{
	private class DummyGrid extends Grid
	{
		public DummyGrid(Grid grid)
		{
			super(grid);
		}

		public DummyGrid(Vector2i size, Cell example)
		{
			super(size, example);
		}

		public DummyGrid(Vector2i size)
		{
			super(size);
		}

		@Override
		public Cell[] getNeighbours(Vector2i pos)
		{
			return null;
		}
	}
	
	public void testConstructorSize()
	{
		Vector2i	size = new Vector2i(7, 9);
		DummyGrid	grid = new DummyGrid(size);
		
		assertEquals(9, grid.getHeight());
		assertEquals(7, grid.getWidth());
		assertTrue(grid.getSize().equals(size));
		
		// Assure a copy of the size was made
		size.x = 1;
		size.y = 2;
		
		assertEquals(9, grid.getHeight());
		assertEquals(7, grid.getWidth());
		assertTrue(grid.getSize().equals(new Vector2i(7, 9)));
	}
	
	public void testConstructorSizeCell()
	{
		Cell		cell = new Cell(1);
		cell.setValue(0, 5);
		
		Vector2i	size = new Vector2i(7, 9);
		DummyGrid	grid = new DummyGrid(size, cell);
		
		assertEquals(9, grid.getHeight());
		assertEquals(7, grid.getWidth());
		assertTrue(grid.getSize().equals(size));
		
		// Assure a copy of the size was made
		size.x = 1;
		size.y = 2;
		
		assertEquals(9, grid.getHeight());
		assertEquals(7, grid.getWidth());
		assertTrue(grid.getSize().equals(new Vector2i(7, 9)));
		
		// Ensure values
		assertEquals(5, grid.getCell(0, 0).getValue(0));
		assertEquals(5, grid.getCell(2, 2).getValue(0));
		assertEquals(5, grid.getCell(6, 8).getValue(0));
	}
	
	public void testConstructorGrid()
	{
		Vector2i	size = new Vector2i(7, 9);
		DummyGrid	grid = new DummyGrid(size);
		grid.getCell(0, 0).setValue(0, 3);
		grid.getCell(1, 0).setValue(0, 10);
		grid.getCell(0, 1).setValue(0, 11);
		grid.getCell(2, 2).setValue(0, 5);
		grid.getCell(6, 8).setValue(0, 9);
				
		// Call copy constructor
		DummyGrid	grid2 = new DummyGrid(grid);
		
		// Now change original grid to ensure it made a copy and didn't just link back
		size.x = 1;
		size.y = 2;
		
		grid.getCell(0, 0).setValue(0, 61);
		grid.getCell(1, 0).setValue(0, 46);
		grid.getCell(0, 1).setValue(0, 87);
		grid.getCell(2, 2).setValue(0, 32);
		grid.getCell(6, 8).setValue(0, 94);
		
		// Now test
		assertEquals(9, grid2.getHeight());
		assertEquals(7, grid2.getWidth());
		assertTrue(grid2.getSize().equals(new Vector2i(7, 9)));
		
		assertEquals(3, grid2.getCell(0, 0).getValue(0));
		assertEquals(5, grid2.getCell(2, 2).getValue(0));
		assertEquals(9, grid2.getCell(6, 8).getValue(0));
	}	
	
	public void testSetCells()
	{
		Vector2i	size = new Vector2i(2, 2);
		DummyGrid	grid = new DummyGrid(size);	
		grid.getCell(0, 0).setValue(0, 9);
		grid.getCell(1, 0).setValue(0, 10);
		grid.getCell(0, 1).setValue(0, 11);
		grid.getCell(1, 1).setValue(0, 5);
		
		int[] values = {1, 2, 3};
		grid.setCells(new Vector2i(1, 1), values);
		
		assertEquals(1, grid.getCell(1, 1).getValue(0));
		assertEquals(2, grid.getCell(1, 1).getValue(1));
		assertEquals(3, grid.getCell(1, 1).getValue(2));
	}
	
	public void testSetGetType()
	{
		Vector2i	size = new Vector2i(2, 2);
		DummyGrid	grid = new DummyGrid(size);	
		
		grid.setType('H');
		assertEquals('H', grid.getType());
		
		grid.setType('j');
		assertEquals('j', grid.getType());
	}
}
