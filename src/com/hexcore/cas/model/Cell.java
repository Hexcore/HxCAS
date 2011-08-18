package com.hexcore.cas.model;

public class Cell
{
	private double[] 	values;
	
	public Cell(int valueCount)
	{
		values = new double[valueCount];
		
		for (int i = 0; i < valueCount; i++) values[i] = 0.0;
	}
	
	public Cell(double[] values)
	{
		this.values = (double[])values.clone();
	}
	
	public Cell(Cell cell)
	{
		this.values = (double[])cell.values.clone();
	}
	
	public double getValue(int index)
	{
		if ((index < 0) || (index >= values.length)) return 0.0;
		return values[index];
	}
	
	public int getValueCount()
	{
		return values.length;
	}
	
	public double[] getValues()
	{
		return values;
	}
	
	public void setValue(int index, double value)
	{
		if ((index < 0) || (index >= values.length)) return;
		values[index] = value;
	}
}
