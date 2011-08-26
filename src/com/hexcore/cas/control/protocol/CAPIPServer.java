package com.hexcore.cas.control.protocol;

import java.util.ArrayList;

import com.hexcore.cas.control.client.Overseer;

public class CAPIPServer extends CAPInformationProcessor
{
	private ArrayList<CAPMessageProtocol> clients = null;
	
	public CAPIPServer(Overseer o)
	{
		super();
		clients = new ArrayList<CAPMessageProtocol>();
	}

	@Override
	protected void interpretInput(Message message)
	{
	}
}
