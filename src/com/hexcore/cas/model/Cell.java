package com.hexcore.cas.model;

public class Cell
{
	private int[] 	values;
	private int 	valueCount;
	
	public Cell(int valueCount)
	{
		values = new int[valueCount];
		this.valueCount = valueCount;
		
		for(int i = 0; i < valueCount; i++)
		{
			values[i] = 0;
		}
	}
	
	public Cell(int[] values)
	{
		valueCount = values.length;
		this.values = new int[valueCount];
		                 
		for(int i = 0; i < valueCount; i++)
		{
			this.values[i] = values[i]; 
		}
	}
	
	public Cell(Cell cell)
	{
		this.valueCount = cell.valueCount;
		this.values = new int[valueCount];
		
		for(int i = 0; i < valueCount; i++)
		{
			this.values[i] = cell.values[i];
		}
	}
	
	public int getValue(int index)
	{
		if(index < 0 || index >= valueCount)
			return 0;
		else
			return values[index];
	}
	
	public int getValueCount()
	{
		return valueCount;
	}
	
	public int[] getValues()
	{
		return values;
	}
	
	public void setValue(int index, int value)
	{
		if(index < 0 || index >= valueCount)
			return;
		else
			values[index] = value;
	}
}
