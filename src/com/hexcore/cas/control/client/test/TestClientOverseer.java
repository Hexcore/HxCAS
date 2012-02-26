package com.hexcore.cas.control.client.test;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import junit.framework.TestCase;

import com.hexcore.cas.control.client.CAPIPClient;
import com.hexcore.cas.control.client.ClientOverseer;
import com.hexcore.cas.control.protocol.ByteNode;
import com.hexcore.cas.control.protocol.CAPMessageProtocol;
import com.hexcore.cas.control.protocol.DictNode;
import com.hexcore.cas.control.protocol.DoubleNode;
import com.hexcore.cas.control.protocol.IntNode;
import com.hexcore.cas.control.protocol.ListNode;
import com.hexcore.cas.control.protocol.Message;
import com.hexcore.cas.utilities.Log;

public class TestClientOverseer extends TestCase
{
	private static final int TEST_CLIENT_PORT = 3229;
	
	
	public void testClientOverseer()
	{				
		// Create client overseer
		ClientOverseer client = new ClientOverseer(TEST_CLIENT_PORT);
		client.start();
				
		System.out.println("Starting server");
		
		// Create dummy server
		ServerThread server = new ServerThread();
		server.start();
		
		try
		{
			server.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			Thread.sleep(3000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	private class ServerThread extends Thread
	{
		@Override
		public void run()
		{
			// Wait for client to get ready
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			// Connect to client
			Socket socket = null;
			
			try
			{
				socket = new Socket("localhost", TEST_CLIENT_PORT);
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
			
			// Startup message protocol
			CAPMessageProtocol serverProtocol = new CAPMessageProtocol(socket);
			serverProtocol.start();
			
			DictNode header, body;
			
			// Send connect
			header = new DictNode();
			header.addToDict("TYPE", new ByteNode("CONNECT"));
			header.addToDict("VERSION", new IntNode(CAPIPClient.PROTOCOL_VERSION));
			serverProtocol.sendMessage(new Message(header));
			
			// Wait for accept
			Message message = serverProtocol.waitForMessage();
			ByteNode typeNode = (ByteNode)message.getHeader().get("TYPE");
			Log.debug("TestServer", "Got message: " + typeNode.toString());
			assertEquals("ACCEPT", typeNode.toString());

			// Send a test grid
			header = new DictNode();
			header.addToDict("TYPE", new ByteNode("GRID"));
			header.addToDict("VERSION", new IntNode(CAPIPClient.PROTOCOL_VERSION));
			
			ListNode sizeNode = new ListNode();
			sizeNode.addToList(new IntNode(10));
			sizeNode.addToList(new IntNode(10));
			
			ListNode areaNode = new ListNode();
			areaNode.addToList(new IntNode(1));
			areaNode.addToList(new IntNode(1));	
			areaNode.addToList(new IntNode(8));
			areaNode.addToList(new IntNode(8));	
			
			ListNode data = new ListNode();
			
			for (int y = 0; y < 10; y++)
			{
				ListNode row = new ListNode();
				for (int x = 0; x < 10; x++)
				{
					ListNode cell = new ListNode();
					
					if ((x <= 6) && (x >= 4) && (y <= 6) && (y >= 4))
						cell.addToList(new DoubleNode(1.0));
					else
						cell.addToList(new DoubleNode(0.0));
					
					row.addToList(cell);
				}
				data.addToList(row);
			}
			
			body = new DictNode();
			body.addToDict("SIZE", sizeNode);
			body.addToDict("AREA", areaNode);
			body.addToDict("PROPERTIES", new IntNode(1));
			body.addToDict("GRIDTYPE", new ByteNode("R"));
			body.addToDict("ID", new IntNode(1));
			body.addToDict("GENERATION", new IntNode(1));
			body.addToDict("DATA", data);
			
			serverProtocol.sendMessage(new Message(header, body));
			
			// Wait for result
			message = serverProtocol.waitForMessage();
			typeNode = (ByteNode)message.getHeader().get("TYPE");
			Log.debug("TestServer", "Got message: " + typeNode.toString());
			assertEquals("RESULT", typeNode.toString());
			
			// Send disconnect
			header = new DictNode();
			header.addToDict("TYPE", new ByteNode("DISCONNECT"));
			header.addToDict("VERSION", new IntNode(CAPIPClient.PROTOCOL_VERSION));
			serverProtocol.sendMessage(new Message(header));
			
			serverProtocol.disconnect();
		}
	}
}
