package com.hexcore.cas.control.protocol;

public class ByteNode extends Node
{
	private byte[] values = null;
	
	public ByteNode(byte[] b)
	{
		values = b.clone();
	}
	
	public byte[] getByteValues()
	{
		return values;
	}
	
	public String toString()
	{
		return new String(values);
	}
}
