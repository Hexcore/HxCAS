package com.hexcore.cas.model.test;

import com.hexcore.cas.model.Cell;

import junit.framework.TestCase;


public class TestCell extends TestCase
{
	private Cell cell;
	
	public void test1CellConstructors()
	{
		cell = new Cell(3);
		assertEquals(3, cell.getValueCount());
		assertEquals(0, cell.getValue(0));
		assertEquals(0, cell.getValue(1));
		assertEquals(0, cell.getValue(2));
		
		// Test initialising with an array
		int[] testValues = {10,5,2};
		cell =  new Cell(testValues);
		assertEquals(3, cell.getValueCount());
		assertEquals(10, cell.getValue(0));
		assertEquals(5, cell.getValue(1));
		assertEquals(2, cell.getValue(2));
		assertEquals(0, cell.getValue(-1));
		assertEquals(0, cell.getValue(3));
		
		// Test copy constructor
		Cell cell2 = new Cell(cell);
		assertEquals(3, cell2.getValueCount());
		assertEquals(10, cell2.getValue(0));
		assertEquals(5, cell2.getValue(1));
		assertEquals(2, cell2.getValue(2));
		assertEquals(0, cell2.getValue(-1));
		assertEquals(0, cell2.getValue(3));
		
		// Ensure a proper copy was made
		cell.setValue(0, 99);
		cell.setValue(1, 98);
		cell.setValue(2, 97);
		
		assertEquals(10, cell2.getValue(0));
		assertEquals(5, cell2.getValue(1));
		assertEquals(2, cell2.getValue(2));	
	}
	
	public void test2Values()
	{
		cell = new Cell(2);
		cell.setValue(0, 1);
		cell.setValue(1, 3);
		
		assertEquals(2, cell.getValueCount());
		assertEquals(1, cell.getValue(0));
		assertEquals(3, cell.getValue(1));
		assertEquals(0, cell.getValue(-1));
		assertEquals(0, cell.getValue(3));
		
		int[] testVals = cell.getValues();
		assertEquals(testVals[0], 1);
		assertEquals(testVals[1], 3);
	}
}
