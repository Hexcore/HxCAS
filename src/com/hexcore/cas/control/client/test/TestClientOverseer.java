package com.hexcore.cas.control.client.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Test;

import com.hexcore.cas.control.client.CAPIPClient;
import com.hexcore.cas.control.client.ClientOverseer;
import com.hexcore.cas.control.protocol.ByteNode;
import com.hexcore.cas.control.protocol.CAPMessageProtocol;
import com.hexcore.cas.control.protocol.DictNode;
import com.hexcore.cas.control.protocol.IntNode;
import com.hexcore.cas.control.protocol.Message;
import com.hexcore.cas.math.Recti;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.RectangleGrid;
import com.hexcore.cas.utilities.Log;

public class TestClientOverseer
{
	@Test
	public void testClientOverseer()
	{				
		// Create client overseer
		ClientOverseer client = new ClientOverseer();
			
		// Test work queue and status
		assertEquals(0, client.checkState());
		
		Grid grid = new RectangleGrid(new Vector2i(10, 10));
		client.addGrid(grid, new Recti(new Vector2i(1, 1), new Vector2i(8, 8)));
		
		assertEquals(1, client.checkState());
				
		client.start();
		
		// Wait for client to get ready
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		// Create dummy server
		ServerThread server = new ServerThread();
		server.start();
		
		try
		{
			Thread.sleep(3000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		client.disconnect();
	}
	
	private class ServerThread extends Thread
	{
		@Override
		public void run()
		{
			Socket socket = null;
			
			try
			{
				socket = new Socket("localhost", 3119);
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
			
			// Send disconnect
			header = new DictNode();
			header.addToDict("TYPE", new ByteNode("DISCONNECT"));
			header.addToDict("VERSION", new IntNode(CAPIPClient.PROTOCOL_VERSION));
			serverProtocol.sendMessage(new Message(header));
			
			serverProtocol.disconnect();
		}
	}
}
