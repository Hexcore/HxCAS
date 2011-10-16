package com.hexcore.cas;

import java.net.SocketAddress;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.GridType;

public class ServerEvent 
{	
	public enum Type
	{
		FOUND_CLIENT, SHUTDOWN, CREATE_WORLD, LOAD_WORLD, SAVE_WORLD, 
		START_SIMULATION, PAUSE_SIMULATION, RESET_SIMULATION, STOP_SIMULATION, STEP_SIMULATION;
	}
	
	public Type			 type;
	public SocketAddress address = null;
		
	// CREATE_WORLD
	public Vector2i	size;
	public GridType	gridType;
	public boolean	wrappable;
	
	// LOAD_WORLD
	public String		filename;
	
	public ServerEvent(Type type)
	{
		this.type = type;
	}
}
