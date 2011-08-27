package com.hexcore.cas.control.protocol;

public abstract class CAPInformationProcessor extends Thread
{
	private volatile boolean running = false;
	protected CAPMessageProtocol protocol = null;
		
	protected abstract void interpretInput(Message message);
	
	private DictNode makeHeader(String type)
	{
		DictNode header = new DictNode();
		header.addToDict("TYPE", new ByteNode(type));
		header.addToDict("VERSION", new IntNode(1));
		return header;	
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
	
	public void sendResult(byte[] bytes)
	{
		DictNode body = new DictNode();
		body.addToDict("DATA", new ByteNode(bytes));
		
		Message msg = new Message(makeHeader("RESULT"), body);
		protocol.sendMessage(msg);
	}

	public void disconnect()
	{
		running = false;
	}
	
	public void run()
	{
		running = true;
		
		protocol.start();
		
		while (running)
		{
			Message message = protocol.waitForMessage();
			if (message != null)
				interpretInput(message);
		}
		
		protocol.disconnect();
	}
}
