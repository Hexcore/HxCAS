package com.hexcore.cas.control.protocol;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Class ListNode
 * 
 * @authors Divan Burger; Megan Duncan; Apurva Kumar
 */

public class ListNode extends Node
{
	private ArrayList<Node> values = null;
	
	public ListNode()
	{
		values = new ArrayList<Node>();
	}
	
	public void addToList(Node n)
	{
		values.add(n);
	}
	
	public Node get(int index)
	{
		return values.get(index);
	}
	
	public ArrayList<Node> getListValues()
	{
		return values;
	}
	
	@Override
	public String toString()
	{
		String str = "[";
		boolean first = true;
		for(Node node : values) 
		{
			if (!first) str += ", ";
			first = false;
			str += node.toString();
		}
		return str + "]";
	}	
	
	@Override
	public void write(OutputStream out)
		throws IOException
	{
		out.write('l');
		for(Node node : values)
			node.write(out);
		out.write('e');
	}
}
