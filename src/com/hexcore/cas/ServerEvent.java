package com.hexcore.cas;

import java.net.InetSocketAddress;
import java.util.List;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.GridType;

public class ServerEvent 
{	
	public enum Type
	{
		SHUTDOWN, CREATE_WORLD, LOAD_WORLD, SAVE_WORLD, READY_SIMULATION, 
		START_SIMULATION, PAUSE_SIMULATION, RESET_SIMULATION, STOP_SIMULATION, STEP_SIMULATION,
		PING_CLIENTS, CLEAR_HISTORY;
	}
	
	public Type		type;
	
	// READY_SIMULATION
	public List<InetSocketAddress>	clients;
		
	// CREATE_WORLD
	public Vector2i	size;
	public GridType	gridType;
	public boolean	wrappable;
	
	// LOAD_WORLD
	public String		filename;
	
	// CLEAR_HISTORY
	public int genNumber;
	
	public ServerEvent(Type type)
	{
		this.type = type;
	}
}
