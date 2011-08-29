package com.hexcore.cas.control.protocol;

import java.io.IOException;
import java.io.OutputStream;

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
	public void write(OutputStream out)
		throws IOException
	{
		out.write('i');
		out.write(value);
		out.write('e');
	}
}
