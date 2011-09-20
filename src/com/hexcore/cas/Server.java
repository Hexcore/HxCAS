package com.hexcore.cas;

import java.net.SocketAddress;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.hexcore.cas.control.discovery.Lobby;
import com.hexcore.cas.control.discovery.LobbyListener;
import com.hexcore.cas.utilities.Configuration;
import com.hexcore.cas.utilities.Log;

public class Server implements LobbyListener
{
	private final static String TAG = "Server";
	public final static String VERSION = "v0.1";
	
	private static Server instance = null;
	
	private volatile boolean running = false;
	private Configuration config = null;
	private Lobby lobby = null;
	
	private LinkedBlockingQueue<ServerEvent>	eventQueue;
	
	public static void main(String[] args)
	{
		instance = new Server();
	}
	
	public static void sendEvent(ServerEvent event)
	{
		if (instance == null)
		{
			Log.error(TAG, "No instance for Server yet");
			return;
		}
		
		try 
		{
			instance.eventQueue.put(event);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	public Server()
	{
		System.out.println("== Hexcore CAS Server - " + VERSION + " ==");
		
		Log.information(TAG, "Loading configuration...");
		config = new Configuration("data/config.txt");
		
		Log.information(TAG, "Starting lobby...");
		int beaconPort = config.getInteger("Network.Beacon", "port", 3118);
		int beaconReplyPort = config.getInteger("Network.Beacon", "replyPort", 3117);
		lobby = new Lobby(beaconPort, beaconReplyPort);
		lobby.start();
		
		eventQueue = new LinkedBlockingQueue<ServerEvent>();
		
		running = true;
		while (running)
		{
			try
			{
				ServerEvent event = eventQueue.poll(1, TimeUnit.SECONDS);
				
				switch (event) 
				{
					case FOUND_CLIENT:
						
						break;
					
					case SHUTDOWN:
						running = false;
						break;
				}
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}		
		}
		
		lobby.disconnect();
		
		Log.information(TAG, "Shutting down...");
	}
	
	@Override
	public void foundClient(SocketAddress address) 
	{
		ServerEvent event = ServerEvent.FOUND_CLIENT;
		event.address = address;
		
		try 
		{
			eventQueue.put(event);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
}
