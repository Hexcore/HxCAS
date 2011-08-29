package com.hexcore.cas.control.protocol;

import com.hexcore.cas.model.Grid;

public abstract class CAPInformationProcessor extends Thread
{
	protected boolean connected = false;
	private volatile boolean running = false;
	protected CAPMessageProtocol protocol = null;
		
	protected abstract void interpretInput(Message message);
	
	public CAPInformationProcessor()
	{
	}
	
	public boolean isConnected()
	{
		return connected;
	}
	
	public void disconnect()
	{
		connected = false;
		running = false;
	}
	
	private DictNode makeHeader(String type)
	{
		DictNode header = new DictNode();
		header.addToDict("TYPE", new ByteNode(type));
		header.addToDict("VERSION", new IntNode(1));
		return header;	
	}

	public void sendGrid(Grid g)
	{
		String sizeStr = "SIZE";
		ListNode sizeNode = new ListNode();
		sizeNode.addToList(new IntNode(g.getWidth()));
		sizeNode.addToList(new IntNode(g.getHeight()));
		
		String dataStr = "DATA";
		ListNode rows = new ListNode();
		for(int y = 0; y < g.getHeight(); y++)
		{
			ListNode currRow = new ListNode(); 
			for(int x = 0; x < g.getWidth(); x++)
			{
				ListNode currCell = new ListNode();
				for(int i = 0; i < g.getCell(x, y).getValueCount(); i++)
					currCell.addToList(new DoubleNode(g.getCell(x, y).getValue(i)));
				currRow.addToList(currCell);
			}
			rows.addToList(currRow);
		}
		
		DictNode d = new DictNode();
		d.addToDict(sizeStr, sizeNode);
		d.addToDict(dataStr, rows);
		sendResult(d);
	}
	
	public void sendResult(DictNode d)
	{
		DictNode body = new DictNode();
		body.addToDict("DATA", d);
		
		Message msg = new Message(makeHeader("RESULT"), body);
		System.out.println("Protocol: " + protocol);
		protocol.sendMessage(msg);
	}
	
	public void sendState(int is)
	{
		DictNode body = new DictNode();
		body.addToDict("STATE", new IntNode(is));
		
		Message msg = new Message(makeHeader("STATUS"), body);
		protocol.sendMessage(msg);
	}
	
	public void sendState(int is, String mess)
	{
		DictNode body = new DictNode();
		body.addToDict("STATE", new IntNode(is));
		body.addToDict("MSG", new ByteNode(mess));
		
		Message msg = new Message(makeHeader("STATUS"), body);
		protocol.sendMessage(msg);
	}
	
	public void setup()
	{
		
	}
	
	@Override
	public void start()
	{
		setup();
		
		if (connected) super.start();
	}
	
	@Override
	public void run()
	{
		if (!connected) return;
		
		System.out.println("Running...");
		
		running = true;
		
		protocol.start();
		
		while(running)
		{
			Message message = protocol.waitForMessage();
			if (message != null)
				interpretInput(message);
		}
		
		protocol.disconnect();
	}	
}