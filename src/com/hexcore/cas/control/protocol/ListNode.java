package com.hexcore.cas.control.protocol;

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
	
	public ArrayList<Node> getListValues()
	{
		return values;
	}
}
