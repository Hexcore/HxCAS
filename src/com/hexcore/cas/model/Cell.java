package com.hexcore.cas.model;

public class Cell
{
	private int[] 	values;
	
	public Cell(int valueCount)
	{
		values = new int[valueCount];
		
		for (int i = 0; i < valueCount; i++) values[i] = 0;
	}
	
	public Cell(int[] values)
	{
		this.values = (int[])values.clone();
	}
	
	public Cell(Cell cell)
	{
		this.values = (int[])cell.values.clone();
	}
	
	public int getValue(int index)
	{
		if ((index < 0) || (index >= values.length)) return 0;
		return values[index];
	}
	
	public int getValueCount()
	{
		return values.length;
	}
	
	public int[] getValues()
	{
		return values;
	}
	
	public void setValue(int index, int value)
	{
		if ((index < 0) || (index >= values.length)) return;
		values[index] = value;
	}
}
