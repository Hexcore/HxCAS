package com.hexcore.cas.rulesystems;

import com.hexcore.cas.model.Cell;

public class StdLib
{
	public static double max(double[] values)
	{
		double highest = -Double.MAX_VALUE;
		
		for(double x : values)
		{
			if(x > highest)
				highest = x;
		}
		
		return highest;
	}
	
	public static double min(double[] values)
	{
		double lowest = Double.MAX_VALUE;
		
		for(double x : values)
		{
			if(x < lowest)
				lowest = x;
		}
		
		return lowest;
	}
	
	/* Log base 10*/
	public static double log(double value)
	{
		return Math.log10(value);
	}
	
	/* Natural log*/
	public static double ln(double value)
	{
		return Math.log(value);
	}
	
	public static double sum(double[] values)
	{
		double sum = 0;
		
		for(double x : values)
			sum += x;
				
		return sum;
	}
	
	public static int count(double[] values)
	{
		return values.length;
	}
	
	public int exists(Cell c)
	{
		return c == null ? 0 : 1;
	}
	
	public static double[] generatePropertyArray(Cell[] cells, int propertyIndex)
	{
		double[] values = new double[cells.length];
		
		for(int i = 0; i < cells.length; i++)
			if(cells[i] != null)
				values[i] = cells[i].getValue(propertyIndex);
		
		return values;
	}
}
