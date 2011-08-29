package com.hexcore.cas.control.protocol.test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

import com.hexcore.cas.control.protocol.ByteNode;
import com.hexcore.cas.control.protocol.DictNode;
import com.hexcore.cas.control.protocol.IntNode;
import com.hexcore.cas.control.protocol.ListNode;
import com.hexcore.cas.control.protocol.Message;

public class TestMessage 
{
	@Test
	public void testEmptyMessage()
	{
		DictNode	header = new DictNode();
		DictNode	body = new DictNode();
		
		Message message = new Message(header, body);
		OutputStream out = new ByteArrayOutputStream();
		
		try
		{
			message.write(out);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		String output = out.toString();
		
		assertTrue(output.equals("de;de;"));
	}
	
	@Test
	public void testIntMessage()
	{
		DictNode	header = new DictNode();
		header.addToDict("num", new IntNode(10));
		
		Message message = new Message(header);
		OutputStream out = new ByteArrayOutputStream();
		
		try
		{
			message.write(out);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		String output = out.toString();
			
		assertTrue(output.equals("d3:numi10ee;;"));
	}
	
	@Test
	public void testListMessage()
	{
		ListNode	list1 = new ListNode();
		list1.addToList(new IntNode(1));
		list1.addToList(new IntNode(2));
		
		ListNode	list2 = new ListNode();
		list2.addToList(new IntNode(3));
		list2.addToList(new IntNode(4));
		
		DictNode	header = new DictNode();
		header.addToDict("num", new IntNode(10));
		header.addToDict("first", list1);
		header.addToDict("second", list2);
		
		Message message = new Message(header);
		OutputStream out = new ByteArrayOutputStream();
		
		try
		{
			message.write(out);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		String output = out.toString();
			
		assertTrue(output.equals("d5:firstli1ei2ee3:numi10e6:secondli3ei4eee;;"));
	}
	
	@Test
	public void testDictionaryIntMessage()
	{
		DictNode	header = new DictNode();
		header.addToDict("num", new IntNode(10));
		header.addToDict("name", new ByteNode("Test"));
		
		DictNode	body = new DictNode();
		body.addToDict("one", new IntNode(1));
		body.addToDict("two", new IntNode(2));
		
		Message message = new Message(header, body);
		OutputStream out = new ByteArrayOutputStream();
		
		try
		{
			message.write(out);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		String output = out.toString();
		
		assertTrue(output.equals("d4:name4:Test3:numi10ee;d3:onei1e3:twoi2ee;"));
	}	
}
