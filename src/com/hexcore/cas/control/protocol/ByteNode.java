package com.hexcore.cas.control.protocol;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Class ByteNode
 * 
 * @authors Divan Burger; Megan Duncan; Apurva Kumar
 */

public class ByteNode extends Node
{
	private byte[] values = null;
	
	public ByteNode(byte[] b)
	{
		values = b.clone();
	}
	
	public ByteNode(String s)
	{
		values = s.getBytes().clone();
	}
	
	public byte[] getByteValues()
	{
		return values;
	}
	
	@Override
	public String toString()
	{
		return new String(values);
	}

	@Override
	public void write(OutputStream out)
		throws IOException
	{
		out.write(Integer.toString(values.length).getBytes());
		out.write(':');
		out.write(values);
	}
}
