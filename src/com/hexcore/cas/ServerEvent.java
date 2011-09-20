package com.hexcore.cas;

import java.net.SocketAddress;

public enum ServerEvent 
{	
	FOUND_CLIENT, SHUTDOWN;
	
	public SocketAddress address = null;
}
