package com.hexcore.cas;

import java.net.SocketAddress;

public class ServerEvent 
{	
	public enum Type
	{
		FOUND_CLIENT, SHUTDOWN, SIMULATE;
	}
	
	public Type			 type;
	public SocketAddress address = null;
	
	public ServerEvent(Type type)
	{
		this.type = type;
	}
}
