package com.hexcore.cas.control.protocol;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Class DoubleNode
 * 
 * @authors Divan Burger; Megan Duncan; Apurva Kumar
 */

public class DoubleNode extends Node
{
	private double value;
	
	public DoubleNode(double v)
	{
		value = v;
	}
	
	public double getDoubleValue()
	{
		return value;
	}
	
	public void setValue(double v)
	{
		value = v;
	}
	
	@Override
	public String toString()
	{
		return Double.toString(value);
	}
	
	@Override
	public void write(OutputStream out)
		throws IOException
	{
		ByteBuffer buf = ByteBuffer.allocate(Double.SIZE / 8);
		buf.putDouble(value);
		out.write('f');
		out.write(buf.array());
	}
}
