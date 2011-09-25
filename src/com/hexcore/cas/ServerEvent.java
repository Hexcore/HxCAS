package com.hexcore.cas;

import java.net.SocketAddress;

public class ServerEvent 
{	
	enum Type
	{
		FOUND_CLIENT, SHUTDOWN, SIMULATE;
	}
	
	public Type			 type;
	public SocketAddress address = null;
	
	ServerEvent(Type type)
	{
		this.type = type;
	}
}
