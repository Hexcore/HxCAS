package com.hexcore.cas.control.protocol;

public class IntNode extends Node
{
	private int value;
	
	public IntNode(int v)
	{
		value = v;
	}
	
	public void setValue(int v)
	{
		value = v;
	}
	
	public int getIntValue()
	{
		return value;
	}
}
