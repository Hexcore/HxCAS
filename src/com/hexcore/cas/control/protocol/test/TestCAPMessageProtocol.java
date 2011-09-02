package com.hexcore.cas.control.protocol.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Test;

import com.hexcore.cas.control.protocol.CAPMessageProtocol;
import com.hexcore.cas.control.protocol.DictNode;
import com.hexcore.cas.control.protocol.IntNode;
import com.hexcore.cas.control.protocol.ListNode;
import com.hexcore.cas.control.protocol.Message;
import com.hexcore.cas.control.protocol.ByteNode;

public class TestCAPMessageProtocol 
{
	@Test
	public void testCommunication()
	{
		Socket clientSocket = null;
		
		ServerThread server = new ServerThread();
		server.start();
		
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e1)
		{
			e1.printStackTrace();
		}
		
		try
		{
			clientSocket = new Socket("127.0.0.1", 13366);
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
			return;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		
		// Test raw socket message
		byte[] buf = new byte[4];
		try
		{
			clientSocket.getInputStream().read(buf);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		System.out.println("Got: " + new String(buf));
		
		// Test message protocol
		CAPMessageProtocol clientProtocol = new CAPMessageProtocol(clientSocket);
		clientProtocol.start();
		
		Message message;
		int state = 0;
		
		while (true)
		{
			message = clientProtocol.waitForMessage();
			if (message == null) continue;
			
			ByteNode typeNode = (ByteNode)message.getHeader().getDictValues().get("TYPE");
			System.out.println("Client - Got message: " + typeNode.toString());
			
			if (typeNode.toString().equals("CONNECT"))
			{
				assertTrue("Did not get the CONNECT message first", state == 0);
				state = 1;
				
				DictNode header = new DictNode();
				header.addToDict("TYPE", new ByteNode("ACCEPT"));
				clientProtocol.sendMessage(new Message(header));
			}
			else if (typeNode.toString().equals("DISCONNECT"))
			{
				assertTrue("The DISCONNECT message should be right after the CONNECT message", state == 1);
				state = 2;
				
				assertNotNull(message.getBody());
				
				DictNode bodyNode = (DictNode)message.getBody();
				IntNode intNode = (IntNode)bodyNode.getDictValues().get("num");
				
				assertEquals(10, intNode.getIntValue());
				
				ListNode listNode = (ListNode)bodyNode.getDictValues().get("list");
				ByteNode stringNode = (ByteNode)listNode.getListValues().get(0);
				
				assertEquals("string", stringNode.toString());
				
				break;
			}
		}
		
		clientProtocol.disconnect();
	}
	
	private class ServerThread extends Thread
	{
		@Override
		public void run()
		{
			Socket socket = null;
			
			try
			{
				ServerSocket serverSocket = new ServerSocket(13366);
				serverSocket.setSoTimeout(3000);
				socket = serverSocket.accept();
			}
			catch (UnknownHostException e)
			{
				e.printStackTrace();
				return;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return;
			}
			
			// Test that socket is writing successfully
			try
			{
				socket.getOutputStream().write("test".getBytes());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			// Startup message protocol
			CAPMessageProtocol serverProtocol = new CAPMessageProtocol(socket);
			serverProtocol.start();
			
			DictNode header, body;
			
			// Send connect
			header = new DictNode();
			header.addToDict("TYPE", new ByteNode("CONNECT"));
			serverProtocol.sendMessage(new Message(header));
			
			// Wait for accept
			Message message = serverProtocol.waitForMessage();
			ByteNode typeNode = (ByteNode)message.getHeader().getDictValues().get("TYPE");
			System.out.println("Server - Got message: " + typeNode.toString());
			assertEquals("ACCEPT", typeNode.toString());
			
			// Send disconnect
			header = new DictNode();
			header.addToDict("TYPE", new ByteNode("DISCONNECT"));
			body = new DictNode();
			body.addToDict("num", new IntNode(10));
			ListNode list = new ListNode();
			list.addToList(new ByteNode("string"));
			body.addToDict("list", list);
			serverProtocol.sendMessage(new Message(header, body));	
			
			serverProtocol.disconnect();
		}
	}
}
