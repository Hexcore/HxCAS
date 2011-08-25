package com.hexcore.cas.control.protocol;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import com.hexcore.cas.control.client.Overseer;

public class CAPIPServer extends CAPInformationProcessor
{
	private ArrayList<CAPInterface> capInterfaces = null;
	private ArrayList<Socket> clients = null;
	
	public CAPIPServer(Overseer o)
		throws IOException
	{
		super();
		capInterfaces = new ArrayList<CAPInterface>();
		clients = new ArrayList<Socket>();
	}

	protected void interpretInput()
	{
	}

	public void setCurrentInformation(byte[] in)
	{
	}

	public void start()
		throws IOException
	{
		
	}

}
