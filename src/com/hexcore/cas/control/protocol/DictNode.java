package com.hexcore.cas.control.protocol;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Class DictNode
 * 
 * @authors Divan Burger; Megan Duncan; Apurva Kumar
 */

public class DictNode extends Node
{
	// Values must be in alphabetical order
	private TreeMap<String, Node> values = null;
	
	public DictNode()
	{
		values = new TreeMap<String, Node>();
	}
	
	public void addToDict(String s, Node n)
	{
		values.put(s, n);
	}
	
	public Node get(String key)
	{
		return values.get(key);
	}
	
	public TreeMap<String, Node> getDictValues()
	{
		return values;
	}
	
	public boolean has(String key)
	{
		return values.containsKey(key);
	}
	
	@Override
	public String toString()
	{
		String str = "{";
		boolean first = true;
		for(Entry<String, Node> entry : values.entrySet())
		{
			if(!first)
				str += ", ";
			first = false;				
			str += entry.getKey() + " : " + entry.getValue();
		}
		return str + "}";
	}
	
	@Override
	public void write(OutputStream out)
		throws IOException
	{
		out.write('d');
		for (Entry<String, Node> entry : values.entrySet())
		{
			ByteNode keyNode = new ByteNode(entry.getKey().getBytes());
			keyNode.write(out);
			entry.getValue().write(out);
		}
		out.write('e');
	}
}
