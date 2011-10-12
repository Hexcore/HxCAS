package com.hexcore.cas.model.test;

import org.junit.Test;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.GridType;

import junit.framework.TestCase;

public class TestGrid extends TestCase
{
	private class DummyGrid extends Grid
	{
		public DummyGrid(Grid grid)
		{
			super(grid);
		}
		
		public DummyGrid(Vector2i size, int numProperties)
		{
			super(size, numProperties);
		}		

		public DummyGrid(Vector2i size, Cell example)
		{
			super(size, example);
		}
		
		@Override
		public Grid clone()
		{
			return null;
		}		

		@Override
		public Cell[] getNeighbours(Vector2i pos)
		{
			return null;
		}

		@Override
		public Vector2i getNeighbourhoodRange()
		{
			return new Vector2i(1, 1);
		}

		@Override
		public GridType getType()
		{
			return GridType.RECTANGLE;
		}
	}
	
	public void testConstructorSize()
	{
		Vector2i	size = new Vector2i(7, 9);
		DummyGrid	grid = new DummyGrid(size, 3);
		
		assertEquals(9, grid.getHeight());
		assertEquals(7, grid.getWidth());
		assertTrue(grid.getSize().equals(size));
		assertEquals(3, grid.getNumProperties());
		
		// Assure a copy of the size was made
		size.x = 1;
		size.y = 2;
		
		assertEquals(9, grid.getHeight());
		assertEquals(7, grid.getWidth());
		assertTrue(grid.getSize().equals(new Vector2i(7, 9)));
		assertEquals(3, grid.getNumProperties());
	}
	
	public void testConstructorSizeInt()
	{	
		Vector2i	size = new Vector2i(7, 9);
		DummyGrid	grid = new DummyGrid(size, 3);	
		
		assertEquals(9, grid.getHeight());
		assertEquals(7, grid.getWidth());
		assertTrue(grid.getSize().equals(size));
		
		// Assure a copy of the size was made
		size.x = 1;
		size.y = 2;
		
		assertEquals(9, grid.getHeight());
		assertEquals(7, grid.getWidth());
		assertTrue(grid.getSize().equals(new Vector2i(7, 9)));
		
		// Ensure size
		assertEquals(3, grid.getCell(0, 0).getValueCount());
		assertEquals(3, grid.getCell(3, 3).getValueCount());
		assertEquals(3, grid.getCell(6, 8).getValueCount());

		// Ensure values		
		grid.setCell(0, 0, new double[]{3.0, 5.0, 7.0});
		grid.setCell(2, 2, new double[]{2.0, 6.0, 8.0});
		grid.setCell(6, 8, new double[]{1.0, 4.0, 9.0});
		
		assertEquals(3.0, grid.getCell(0, 0).getValue(0));
		assertEquals(6.0, grid.getCell(2, 2).getValue(1));
		assertEquals(9.0, grid.getCell(6, 8).getValue(2));
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
		assertEquals(5.0, grid.getCell(0, 0).getValue(0));
		assertEquals(5.0, grid.getCell(2, 2).getValue(0));
		assertEquals(5.0, grid.getCell(6, 8).getValue(0));
	}
	
	public void testConstructorGrid()
	{
		Vector2i	size = new Vector2i(7, 9);
		DummyGrid	grid = new DummyGrid(size, 1);
		grid.setWrappable(false);
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
		
		assertEquals(3.0, grid2.getCell(0, 0).getValue(0));
		assertEquals(5.0, grid2.getCell(2, 2).getValue(0));
		assertEquals(9.0, grid2.getCell(6, 8).getValue(0));
		
		assertEquals(false, grid2.getWrappable());
	}	
	
	@Test
	public void testGetCell()
	{
		Vector2i	size = new Vector2i(2, 2);
		DummyGrid	grid = new DummyGrid(size, 1);	
		grid.getCell(0, 0).setValue(0, 9);
		grid.getCell(1, 0).setValue(0, 10);
		grid.getCell(0, 1).setValue(0, 11);
		grid.getCell(1, 1).setValue(0, 5);
				
		assertEquals(9.0, grid.getCell(0, 0).getValue(0));
		assertEquals(10.0, grid.getCell(1, 0).getValue(0));
		assertEquals(5.0, grid.getCell(1, 1).getValue(0));
		
		Vector2i pos1 = new Vector2i(0, 1);
		Vector2i pos2 = new Vector2i(1, 0);
		assertEquals(11.0, grid.getCell(pos1).getValue(0));
		assertEquals(10.0, grid.getCell(pos2).getValue(0));	
	}
	
	@Test
	public void testSetCellArray()
	{
		Vector2i	size = new Vector2i(2, 2);
		DummyGrid	grid = new DummyGrid(size, 3);	
		grid.getCell(0, 0).setValue(0, 9);
		grid.getCell(1, 0).setValue(0, 10);
		grid.getCell(0, 1).setValue(0, 11);
		grid.getCell(1, 1).setValue(0, 5);
		
		double[] values = {1, 2, 3};
		grid.setCell(new Vector2i(1, 1), values);
		
		assertEquals(1.0, grid.getCell(1, 1).getValue(0));
		assertEquals(2.0, grid.getCell(1, 1).getValue(1));
		assertEquals(3.0, grid.getCell(1, 1).getValue(2));	
	}
	
	@Test
	public void testSetCell()
	{	
		Vector2i	size = new Vector2i(5, 5);
		DummyGrid	grid = new DummyGrid(size, 3);	
		
		Vector2i	pos1 = new Vector2i(2, 4);
		Vector2i	pos2 = new Vector2i(3, 3);		
		Cell cell = new Cell(new double[] {1, 5, 9});
		grid.setCell(pos1, cell);
		grid.setCell(pos2, cell);
		
		assertEquals(1.0, grid.getCell(pos1).getValue(0));
		assertEquals(5.0, grid.getCell(pos2).getValue(1));
		assertEquals(9.0, grid.getCell(pos1).getValue(2));		
	}
}
