package com.hexcore.cas;

import java.net.SocketAddress;

public class ServerEvent 
{	
	enum Type {FOUND_CLIENT};
	
	public Type				type;
	public SocketAddress	address;
	
	ServerEvent(Type type)
	{
		this.type = type;
	}
}
