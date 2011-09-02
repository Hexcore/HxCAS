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
	
	//IN TEST - SERVER
	public boolean isConnected()
	{
		return connected;
	}
	
	//IN TEST - SERVER
	public boolean isRunning()
	{
		return running;
	}
	
	//IN TEST IMPLICITLY - SERVER
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