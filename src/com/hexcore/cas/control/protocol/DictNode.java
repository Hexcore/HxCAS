package com.hexcore.cas.control.protocol;

import java.util.HashMap;

public class DictNode extends Node
{
	private HashMap<String, Node> values = null;
	
	public DictNode()
	{
		values = new HashMap<String, Node>();
	}
	
	public void addToDict(String s, Node n)
	{
		values.put(s, n);
	}
	
	public HashMap<String, Node> getDictValues()
	{
		return values;
	}
}
