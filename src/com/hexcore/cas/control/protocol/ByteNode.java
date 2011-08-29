package com.hexcore.cas.control.protocol;

import java.io.IOException;
import java.io.OutputStream;

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
	
	public String toString()
	{
		return new String(values);
	}

	@Override
	public void write(OutputStream out)
		throws IOException
	{
		out.write(values.length);
		out.write(':');
		out.write(values);
	}
}
