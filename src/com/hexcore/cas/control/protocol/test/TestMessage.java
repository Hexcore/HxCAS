package com.hexcore.cas.control.protocol.test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

import com.hexcore.cas.control.protocol.DictNode;
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
}
