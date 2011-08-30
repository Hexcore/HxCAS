package com.hexcore.cas.control.protocol;

import com.hexcore.cas.model.Grid;

public abstract class CAPInformationProcessor extends Thread
{
	protected final static int PROTOCOL_VERSION = 1;
	
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
	
	public void setup()
	{
		
	}
	
	@Override
	public void start()
	{
		setup();
		
		if(connected)
			super.start();
	}	
}