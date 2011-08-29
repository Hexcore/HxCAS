package com.hexcore.cas.control.client;

import com.hexcore.cas.control.protocol.CAPIPServer;
import com.hexcore.cas.math.Recti;
import com.hexcore.cas.model.Grid;

public class ServerOverseer extends Overseer
{
	private int numOfClients = 0;
	
	public ServerOverseer(Grid g, Recti w)
	{
		super(g, w);
	}
	
	public int getNumberOfClients()
	{
		return numOfClients;
	}
	
	public void setNumberOfClients(int num)
	{
		numOfClients = num;
		capIP = new CAPIPServer(this, numOfClients);
	}
	
	@Override
	public void start()
	{
		
	}
}
