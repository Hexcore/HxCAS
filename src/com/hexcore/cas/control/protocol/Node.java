package com.hexcore.cas.control.protocol;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Node
{
	public Node()
	{
		System.out.println("This constructor should not be used. One of the child nodes created was not given a parameter.");
	}
	
	public abstract void write(OutputStream out) throws IOException;
}
