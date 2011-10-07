package com.hexcore.cas;

import java.net.SocketAddress;

import com.hexcore.cas.math.Vector2i;

public class ServerEvent 
{	
	public enum Type
	{
		FOUND_CLIENT, SHUTDOWN, CREATE_WORLD, START_SIMULATION, PAUSE_SIMULATION, RESET_SIMULATION;
	}
	
	public Type			 type;
	public SocketAddress address = null;
	
	// CREATE_WORLD
	public Vector2i		size;
	public char			gridType;
	public boolean		wrappable;
	
	public ServerEvent(Type type)
	{
		this.type = type;
	}
}
