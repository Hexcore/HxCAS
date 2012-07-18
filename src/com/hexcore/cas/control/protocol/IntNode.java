package com.hexcore.cas.control.protocol;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Class IntNode
 * 
 * @authors Divan Burger; Megan Duncan; Apurva Kumar
 */

public class IntNode extends Node
{
	private int value;
	
	public IntNode(int v)
	{
		value = v;
	}
	
	public int getIntValue()
	{
		return value;
	}
	
	public void setValue(int v)
	{
		value = v;
	}
	
	@Override
	public String toString()
	{
		return Integer.toString(value);
	}
	
	@Override
	public void write(OutputStream out)
		throws IOException
	{
		out.write('i');
		out.write(Integer.toString(value).getBytes());
		out.write('e');
	}
}
