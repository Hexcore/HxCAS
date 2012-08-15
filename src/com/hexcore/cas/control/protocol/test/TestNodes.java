package com.hexcore.cas.control.protocol.test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.TreeMap;

import org.junit.Test;

import com.hexcore.cas.control.protocol.ByteNode;
import com.hexcore.cas.control.protocol.DictNode;
import com.hexcore.cas.control.protocol.DoubleNode;
import com.hexcore.cas.control.protocol.IntNode;
import com.hexcore.cas.control.protocol.ListNode;
import com.hexcore.cas.control.protocol.Node;

public class TestNodes
{
	private IntNode iN = new IntNode(1);
	private DoubleNode fN = new DoubleNode(1.00);
	private ByteNode bN = new ByteNode("TEST");
	private ListNode lN = new ListNode();
	private DictNode dN = new DictNode();
	
	@Test
	public void testIntNode1Getter()
	{
		assertEquals(1, iN.getIntValue());
	}
	
	@Test
	public void testIntNode2Setter()
	{
		iN.setValue(5);
		assertEquals(5, iN.getIntValue());
	}
	
	@Test
	public void testDoubleNode1Getter()
	{
		assertEquals(1.00, fN.getDoubleValue(), 0.0);
	}
	
	@Test
	public void testDoubleNode2Setter()
	{
		fN.setValue(5.00);
		assertEquals(5.00, fN.getDoubleValue(), 0.0);
	}
	
	@Test
	public void testDoubleNode3()
	{
		OutputStream out = new ByteArrayOutputStream();
		
		try 
		{
			fN.write(out);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		for (byte b : out.toString().getBytes())
			System.out.print(":" + b);
		
		System.out.println();
	}
	
	@Test
	public void testByteNode1Getter()
	{
		byte[] b = bN.getByteValues();
		assertEquals("TEST", new String(b));
	}
	
	@Test
	public void testByteNode2ByteConstructor()
	{
		byte[] b = new byte[4];
		for(int i = 0; i < 4; i++)
			b[i] = (byte)i;
		ByteNode bn = new ByteNode(b);
		b = bn.getByteValues().clone();
		for(int i = 0; i < 4; i++)
			assertEquals(i, b[i]);
	}
	
	@Test
	public void testByteNode3ToString()
	{
		assertEquals("TEST", bN.toString());
	}
	
	@Test
	public void testDictNodeAddToDictANDGetter()
	{
		dN.addToDict("iN", iN);
		TreeMap<String, Node> TM = dN.getDictValues();
		assertTrue(TM.containsKey("iN"));
		assertEquals(iN, TM.get("iN"));
	}
	
	@Test
	public void testListNodeAddToListANDGetter()
	{
		lN.addToList(iN);
		ArrayList<Node> l = lN.getListValues();
		assertEquals(iN, l.get(0));
	}
	
	@Test
	public void testWrite()
	{
		lN.addToList(iN);
		lN.addToList(bN);
		//lN.addToList(fN);
		dN.addToDict("LIST", lN);
		
		OutputStream out = new ByteArrayOutputStream();
		try
		{
			dN.write(out);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		String output = out.toString();
		
		assertTrue(output.equals("d4:LISTli1e4:TESTee"));
	}
}
