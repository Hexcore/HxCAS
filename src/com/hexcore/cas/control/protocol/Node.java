package com.hexcore.cas.control.protocol;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Node
{
	public Node()
	{
		System.out.println("This constructor should not be used. One of the child nodes created was not given a parameter.");
	}

	public ArrayList<Node> getListValues()
	{
		return null;
	}
	
	public int getIntValue()
	{
		return -1;
	}
	
	public double getDoubleValue()
	{
		return -1.0;
	}
	
	public HashMap<String, Node> getDictValues()
	{
		return null;
	}
	
	public byte[] getByteValues()
	{
		return null;
	}
}
