package com.hexcore.cas.rulesystems;

import java.util.ArrayList;

import com.hexcore.cas.model.Cell;

/**
 * Class StdLib

 * @authors Karl Zoller
 */

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
	
	public static double sin(double value)
	{
		return Math.sin(value);
	}
	
	public static double cos(double value)
	{
		return Math.cos(value);
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
	
	public static double random(double val)
	{
		return Math.random() * val;
	}
	
	public static int round(double val)
	{
		return (int) Math.round(val);
	}
	
	public static boolean exists(Cell c)
	{
		return c == null ? false : true;
	}
	
	public static double[] generatePropertyArray(Cell[] cells, int propertyIndex)
	{
		double[] values = new double[cells.length];
		
		for(int i = 0; i < cells.length; i++)
			if(cells[i] != null)
				values[i] = cells[i].getValue(propertyIndex);
		
		return values;
	}
	
	public static void move(boolean active, double target, int step, Cell cell, Cell[] neighbours)
	{
		if(!active)
		{
			cell.setPrivateProperty("target", -1);
			return;
		}
		
		if(step == 0)
		{
			cell.setPrivateProperty("target", target);
		}
		else if(step == 1)
		{
			cell.setPrivateProperty("parent", -1);
		}
		else if(step == 2)
		{
			if(active)
			{
				for(int i = 0; i < neighbours.length; i++)
				{
					int j = neighbours.length - (i+1);
					if(neighbours[i] != null && neighbours[i].getPrivateProperty("parent") == j)
					{
						cell.setValue(0, neighbours[i].getValue(0));
						cell.setPrivateProperty("target", -1);
					}
				}
			}
		}
		
	}
	
	/**
	 * Behaviour function. Cell acceptor for movement engine.
	 * @param active Indicates if the behaviour is currently active for this cell
	 * @param step The current movementStep
	 * @param cell 
	 * @param neighbours
	 */
	public static void accept(boolean active, int step, Cell cell, Cell[] neighbours)
	{
		if(!active)
		{
			return;
		}
		
		if(step == 0)
		{
			cell.setPrivateProperty("target", -1);
		}
		else if(step == 1)
		{
			boolean done = false;
			for(int i = 0; i < neighbours.length; i++)
			{
				
				int j = neighbours.length - (i+1);
				if(neighbours[i] != null && neighbours[i].getPrivateProperty("target") == j)
				{
					cell.setPrivateProperty("parent", i);
					done = true;
				}
			}
			
			if(!done)
				cell.setPrivateProperty("parent", -1);
		}
		else if(step == 2)
		{
			if(cell.getPrivateProperty("parent") != -1)
			{
				cell.setValue(0, neighbours[(int)cell.getPrivateProperty("parent")].getValue(0));
			}
			cell.setPrivateProperty("parent", -1);
		}
		
	}
	
	public static void propagate(boolean active, double index, Cell cell, Cell[] neighbours)
	{
		if(!active)
			return;
		
		//Hehe ;)
		double x = StdLib.max(StdLib.generatePropertyArray(neighbours, (int)index));
		
		if((x > cell.getValue((int)index)) && (cell.getPrivateProperty("sat1") <= 0) && (cell.getPrivateProperty("sat2") <= 0))
		{
			cell.setValue((int)index, x-1);
			cell.setPrivateProperty("sat1", 1);
		}
		else if(cell.getPrivateProperty("sat1") == 1)
		{
			cell.setValue((int)index, 0);
			
			if(cell.getPrivateProperty("sat2") == 1)
			{
				cell.setPrivateProperty("sat2", 0);
				cell.setPrivateProperty("sat1", 0);
			}
			else
				cell.setPrivateProperty("sat2", 1);
		}
	}
	
	public static void pulsar(boolean active, double index, double strength, double interval, Cell cell, Cell[] neighbours)
	{
		if(!active)
			return;
		
		double time = cell.getPrivateProperty("time");
		cell.setPrivateProperty("time", time+1);
		
		if(time >= interval)
		{
			if((int)index < cell.getValueCount())
				cell.setValue((int)index, strength);
			
			cell.setPrivateProperty("time", 0);
		}
		else
		{
			if((int)index < cell.getValueCount())
				cell.setValue((int)index, 0);
		}
	}
	
	public static double[] append(double[] values, double x)
	{
		double[] temp = new double[values.length];
		
		for(int i = 0; i < temp.length; i++)
			temp[i] = values[i];
		
		values = new double[values.length+1];
		
		for(int i = 0; i < temp.length; i++)
			values[i] = temp[i];
		
		values[values.length-1] = x;
		
		return values;
	}
	
	public static double[] insert(double[] values, double x, double index)
	{
		
		if(index >= values.length)
		{
			append(values, x);
		}
		else
		{
			if(index < 0)
				index = 0;
			
			double[] temp = new double[values.length];			
			for(int i = 0; i < temp.length; i++)
				temp[i] = values[i];			
			values = new double[values.length+1];
			
			for(int i = 0; i < index; i++)
				values[i] = temp[i];
			
			values[(int)index] = x;
			
			for(int i = (int)index+1; i < values.length; i++)
				values[i] = temp[i-1];
		}
	
		return values;
	}
	
	public static double[] delete(double[] values, double index)
	{
		if(index < 0)
			index = 0;
		else if(index >= values.length)
			index = values.length-1;
		
		double[] temp = new double[values.length];
		
		for(int i = 0; i < values.length; i++)
			temp[i] = values[i];
		
		values = new double[values.length-1];
		
		for(int i = 0; i < index; i++)
			values[i] = temp[i];
		
		for(int i = (int)index+1; i < temp.length; i++)
			values[i-1] = temp[i];
		
		return values;
	}
	
	public static double[] deleteAll(double[] values, double x)
	{
		ArrayList<Double> newArray = new ArrayList<Double>();
		
		for(int i = 0; i < values.length; i++)
		{
			if(values[i] != x)
				newArray.add(values[i]);
		}
		
		values = new double[newArray.size()];
		
		for(int i = 0; i < newArray.size(); i++)
			values[i] = newArray.get(i);
		
		return values;
	}
	
}
