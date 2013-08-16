package com.hexcore.cas.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Class Cell:
 * 	The basic building block of the CA.
 * 	Has an array of doubles called values
 * 	that contains its properties.
 * 
 * @author Apurva Kumar, Karl Zoller
 */

public class Cell
{
	/*
	 * Values linked and defined by CAL properties
	 */
	private double[] values;
	
	/*
	 * Ad-hoc values used when needed by CAL behaviour engines.
	 */
	private HashMap<String, Double> privateProperties;
	
	private Cell()
	{
		privateProperties = new HashMap<String, Double>();
	}
	
	public Cell(int valueCount)
	{
		this();
		values = new double[valueCount];
		
		for(int i = 0; i < valueCount; i++)
			values[i] = 0.0;
	}
	
	public Cell(double[] values)
	{
		this();
		this.values = (double[])values.clone();
	}
	
	@SuppressWarnings("unchecked")
	public Cell(Cell cell)
	{
		this();
		this.values = (double[])cell.values.clone();
		
		Set<String> keys = cell.privateProperties.keySet();
		Iterator<String> iter = keys.iterator();
		
		while(iter.hasNext())
		{
			String current = iter.next();
			this.privateProperties.put(current, cell.privateProperties.get(current));
		}
		//this.privateProperties = (HashMap<String,Double>)cell.privateProperties.clone();
	}
	
	public double getValue(int index)
	{
		if((index < 0) || (index >= values.length))
			return 0.0;
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
		if((index < 0) || (index >= values.length))
			return;
		values[index] = value;
	}
	
	public void setPrivateProperty(String key, double value)
	{
		privateProperties.put(key, value);
	}
	
	public double getPrivateProperty(String key)
	{
		return privateProperties.get(key) == null ? -1.0 : privateProperties.get(key);
	}
	
	public Map<String, Double> getPrivateProperties()
	{
		return privateProperties;
	}
}
