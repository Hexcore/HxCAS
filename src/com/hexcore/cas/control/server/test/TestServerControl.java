package com.hexcore.cas.control.server.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import junit.framework.TestCase;

import com.hexcore.cas.control.server.ServerOverseer;
import com.hexcore.cas.math.Recti;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.RectangleGrid;
import com.hexcore.cas.model.ThreadWork;


public class TestServerControl extends TestCase
{
	public ServerOverseer server = null;
	
	private void testClientWork(ThreadWork[] cW)
	{
		System.out.println("Testing clientWork was set correctly");
		assertEquals(2, cW.length);
		
		assertEquals(4, cW[0].getGrid().getWidth());
		assertEquals(6, cW[0].getGrid().getHeight());
		assertEquals(0.0, cW[0].getGrid().getCell(0, 0).getValue(0));
		assertEquals(0.0, cW[0].getGrid().getCell(1, 0).getValue(0));
		assertEquals(1.0, cW[0].getGrid().getCell(2, 0).getValue(0));
		assertEquals(0.0, cW[0].getGrid().getCell(3, 0).getValue(0));
		assertEquals(0.0, cW[0].getGrid().getCell(0, 1).getValue(0));
		assertEquals(0.0, cW[0].getGrid().getCell(1, 1).getValue(0));
		assertEquals(0.0, cW[0].getGrid().getCell(2, 1).getValue(0));
		assertEquals(1.0, cW[0].getGrid().getCell(3, 1).getValue(0));
		assertEquals(0.0, cW[0].getGrid().getCell(0, 2).getValue(0));
		assertEquals(0.0, cW[0].getGrid().getCell(1, 2).getValue(0));
		assertEquals(1.0, cW[0].getGrid().getCell(2, 2).getValue(0));
		assertEquals(0.0, cW[0].getGrid().getCell(3, 2).getValue(0));
		assertEquals(0.0, cW[0].getGrid().getCell(0, 3).getValue(0));
		assertEquals(0.0, cW[0].getGrid().getCell(1, 3).getValue(0));
		assertEquals(0.0, cW[0].getGrid().getCell(2, 3).getValue(0));
		assertEquals(1.0, cW[0].getGrid().getCell(3, 3).getValue(0));
		assertEquals(0.0, cW[0].getGrid().getCell(0, 4).getValue(0));
		assertEquals(0.0, cW[0].getGrid().getCell(1, 4).getValue(0));
		assertEquals(1.0, cW[0].getGrid().getCell(2, 4).getValue(0));
		assertEquals(0.0, cW[0].getGrid().getCell(3, 4).getValue(0));
		assertEquals(0.0, cW[0].getGrid().getCell(0, 5).getValue(0));
		assertEquals(0.0, cW[0].getGrid().getCell(1, 5).getValue(0));
		assertEquals(0.0, cW[0].getGrid().getCell(2, 5).getValue(0));
		assertEquals(1.0, cW[0].getGrid().getCell(3, 5).getValue(0));
		assertEquals(1, cW[0].getWorkableArea().getPosition().y);
		assertEquals(1, cW[0].getWorkableArea().getPosition().x);
		assertEquals(4, cW[0].getWorkableArea().getSize().y);
		assertEquals(2, cW[0].getWorkableArea().getSize().x);
		
		assertEquals(4, cW[1].getGrid().getWidth());
		assertEquals(6, cW[1].getGrid().getHeight());
		assertEquals(1.0, cW[1].getGrid().getCell(0, 0).getValue(0));
		assertEquals(0.0, cW[1].getGrid().getCell(1, 0).getValue(0));
		assertEquals(0.0, cW[1].getGrid().getCell(2, 0).getValue(0));
		assertEquals(0.0, cW[1].getGrid().getCell(3, 0).getValue(0));
		assertEquals(0.0, cW[1].getGrid().getCell(0, 1).getValue(0));
		assertEquals(1.0, cW[1].getGrid().getCell(1, 1).getValue(0));
		assertEquals(0.0, cW[1].getGrid().getCell(2, 1).getValue(0));
		assertEquals(0.0, cW[1].getGrid().getCell(3, 1).getValue(0));
		assertEquals(1.0, cW[1].getGrid().getCell(0, 2).getValue(0));
		assertEquals(0.0, cW[1].getGrid().getCell(1, 2).getValue(0));
		assertEquals(0.0, cW[1].getGrid().getCell(2, 2).getValue(0));
		assertEquals(0.0, cW[1].getGrid().getCell(3, 2).getValue(0));
		assertEquals(0.0, cW[1].getGrid().getCell(0, 3).getValue(0));
		assertEquals(1.0, cW[1].getGrid().getCell(1, 3).getValue(0));
		assertEquals(0.0, cW[1].getGrid().getCell(2, 3).getValue(0));
		assertEquals(0.0, cW[1].getGrid().getCell(3, 3).getValue(0));
		assertEquals(1.0, cW[1].getGrid().getCell(0, 4).getValue(0));
		assertEquals(0.0, cW[1].getGrid().getCell(1, 4).getValue(0));
		assertEquals(0.0, cW[1].getGrid().getCell(2, 4).getValue(0));
		assertEquals(0.0, cW[1].getGrid().getCell(3, 4).getValue(0));
		assertEquals(0.0, cW[1].getGrid().getCell(0, 5).getValue(0));
		assertEquals(1.0, cW[1].getGrid().getCell(1, 5).getValue(0));
		assertEquals(0.0, cW[1].getGrid().getCell(2, 5).getValue(0));
		assertEquals(0.0, cW[1].getGrid().getCell(3, 5).getValue(0));
		assertEquals(1, cW[1].getWorkableArea().getPosition().y);
		assertEquals(1, cW[1].getWorkableArea().getPosition().x);
		assertEquals(4, cW[1].getWorkableArea().getSize().y);
		assertEquals(2, cW[1].getWorkableArea().getSize().x);
		
		System.out.println("SUCCESS - clientWork was set correctly");
	}
	
	private void testGrid(Grid grid)
	{
		System.out.println("Testing grid was set correctly");
		
		assertEquals('R', grid.getType());
		assertEquals(4, grid.getWidth());
		assertEquals(4, grid.getHeight());
		assertEquals(0.0, grid.getCell(0, 0).getValue(0));
		assertEquals(0.0, grid.getCell(0, 1).getValue(0));
		assertEquals(0.0, grid.getCell(0, 2).getValue(0));
		assertEquals(0.0, grid.getCell(0, 3).getValue(0));
		assertEquals(0.0, grid.getCell(1, 0).getValue(0));
		assertEquals(1.0, grid.getCell(1, 1).getValue(0));
		assertEquals(0.0, grid.getCell(1, 2).getValue(0));
		assertEquals(1.0, grid.getCell(1, 3).getValue(0));
		assertEquals(1.0, grid.getCell(2, 0).getValue(0));
		assertEquals(0.0, grid.getCell(2, 1).getValue(0));
		assertEquals(1.0, grid.getCell(2, 2).getValue(0));
		assertEquals(0.0, grid.getCell(2, 3).getValue(0));
		assertEquals(0.0, grid.getCell(3, 0).getValue(0));
		assertEquals(0.0, grid.getCell(3, 1).getValue(0));
		assertEquals(0.0, grid.getCell(3, 2).getValue(0));
		assertEquals(0.0, grid.getCell(3, 3).getValue(0));
		
		System.out.println("SUCCESS - grid was set correctly");
	}
	
	private void testNameList(ArrayList<String> nameList)
	{
		System.out.println("Testing nameList was set correctly");
		
		for(int i = 0; i < nameList.size(); i++)
			assertEquals("localhost", nameList.get(i));
		
		System.out.println("SUCCESS - nameList was set correctly");
	}
	
	private void testWorkables(Recti[] w, int NOC)
	{
		System.out.println("Testing workables was set correctly");
		
		System.out.println("--numOfclients");
		assertEquals(1, NOC);
		
		System.out.println("--clientWorkables");
		assertEquals(0, w[0].getPosition().x);
		assertEquals(0, w[0].getPosition().y);
		assertEquals(2, w[1].getPosition().x);
		assertEquals(0, w[1].getPosition().y);
		assertEquals(2, w[0].getSize().x);
		assertEquals(4, w[0].getSize().y);
		assertEquals(2, w[1].getSize().x);
		assertEquals(4, w[1].getSize().y);
		
		System.out.println("SUCCESS - workables was set correctly");
	}
	
	public void testServer()
	{
		ServerSocket clientSocket = null;
		
		try
		{
			clientSocket = new ServerSocket(3119);
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
			return;
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			return;
		}
		
		System.out.println("===============================================");
		System.out.println("TESTING DISTRIBUTION SYSTEM WITH RECTANGLE GRID");
		System.out.println("===============================================");
		RectangleGrid g = new RectangleGrid(new Vector2i(4, 4), new Cell(1));
		for(int y = 0; y < 4; y++)
			for(int x = 0; x < 4; x++)
				g.getCell(x, y).setValue(0, 0.0);
		g.getCell(2, 0).setValue(0, 1.0);
		g.getCell(1, 1).setValue(0, 1.0);
		g.getCell(2, 2).setValue(0, 1.0);
		g.getCell(1, 3).setValue(0, 1.0);
		/*
		 * [0.0][0.0][1.0][0.0]
		 * [0.0][1.0][0.0][0.0]
		 * [0.0][0.0][1.0][0.0]
		 * [0.0][1.0][0.0][0.0]
		 */
		{	
			server = new ServerOverseer(g);
			
			Grid grid = server.getGrid();
			testGrid(grid);
		}
		
		{
			ArrayList<String> nameList = new ArrayList<String>();
			nameList.add("localhost");
			server.setClientNames(nameList);
			
			nameList = server.getClientNames();
			testNameList(nameList);
		}
		
		server.start();
		
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e1)
		{
			e1.printStackTrace();
		}
		
		{
			Recti[] workables = new Recti[2];
			workables[0] = new Recti(new Vector2i(0, 0), new Vector2i(2, 4));
			workables[1] = new Recti(new Vector2i(2, 0), new Vector2i(2, 4));
			server.setClientWorkables(workables);
			
			int NOC = server.getNumberOfClients();
			Recti[] w = server.getClientWorkables();
			testWorkables(w, NOC);
		
			ThreadWork[] cW = server.getClientWork();
			testClientWork(cW);
		}
		/*		
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
		
		clientProtocol.disconnect();*/
		
		System.out.println("Testing Distribution System complete");
		
		server.disconnect();
	}
}
