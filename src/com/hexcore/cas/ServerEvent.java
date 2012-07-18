package com.hexcore.cas;

import java.net.InetSocketAddress;
import java.util.List;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.GridType;

/**
 * Class ServerEvent
 * 	Used to send requests and actions of the program through
 * 	to the server for responses.
 * 
 * @authors Divan Burger; Karl Zoller
 */

public class ServerEvent 
{	
	public enum Type
	{
		SHUTDOWN, CREATE_WORLD, LOAD_WORLD, SAVE_WORLD, READY_SIMULATION, 
		START_SIMULATION, PAUSE_SIMULATION, RESET_SIMULATION, STOP_SIMULATION, STEP_SIMULATION,
		PING_CLIENTS, CLEAR_HISTORY, SET_PLAYBACK_SPEED;
	}
	
	public Type						type;
	
	// READY_SIMULATION
	public List<InetSocketAddress>	clients;
		
	// CREATE_WORLD
	public boolean					wrappable;
	public GridType					gridType;
	public Vector2i					size;
	
	// LOAD_WORLD
	public String					filename;
	
	// CLEAR_HISTORY
	public int						genNumber;
	
	// SET_PLAYBACK_SPEED
	public long						milliseconds;
	
	public ServerEvent(Type type)
	{
		this.type = type;
	}
}
