package com.hexcore.cas.control.protocol;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

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
	public void write(OutputStream out)
		throws IOException
	{
		out.write('l');
		for (Node node : values) node.write(out);
		out.write('e');
	}
}
