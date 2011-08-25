package com.hexcore.cas.control.protocol;

public class DoubleNode extends Node
{
	private double value;
	
	public DoubleNode(double v)
	{
		value = v;
	}
	
	public void setValue(double v)
	{
		value = v;
	}
	
	public double getDoubleValue()
	{
		return value;
	}
}
