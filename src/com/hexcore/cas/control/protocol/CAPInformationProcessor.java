package com.hexcore.cas.control.protocol;

public abstract class CAPInformationProcessor extends Thread
{
	public final static int PROTOCOL_VERSION = 1;
	
	protected boolean connected = false;
	protected volatile boolean running = false;
		
	protected abstract void interpretInput(Message message);
	
	public CAPInformationProcessor()
	{
	}
	
	public boolean isConnected()
	{
		return connected;
	}
	
	public boolean isRunning()
	{
		return running;
	}
	
	public void disconnect()
	{
		connected = false;
		running = false;
	}
	
	protected DictNode makeHeader(String type)
	{
		DictNode header = new DictNode();
		header.addToDict("TYPE", new ByteNode(type));
		header.addToDict("VERSION", new IntNode(PROTOCOL_VERSION));
		return header;
	}
}
