package com.hexcore.cas.control.protocol;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map.Entry;

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
