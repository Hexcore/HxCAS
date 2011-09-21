package com.hexcore.cas;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.hexcore.cas.control.discovery.Lobby;
import com.hexcore.cas.control.discovery.LobbyListener;
import com.hexcore.cas.control.server.ServerOverseer;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.RectangleGrid;
import com.hexcore.cas.model.World;
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
	private ServerOverseer overseer = null;
	
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
		
		eventQueue = new LinkedBlockingQueue<ServerEvent>();
		
		Log.information(TAG, "Starting lobby...");
		int beaconPort = config.getInteger("Network.Beacon", "port", 3118);
		int beaconReplyPort = config.getInteger("Network.Beacon", "replyPort", 3117);
		lobby = new Lobby(beaconPort, beaconReplyPort);
		lobby.start();
		lobby.addListener(this);
		
		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		lobby.ping();
		
		running = true;
		while (running)
		{
			try
			{
				ServerEvent event = eventQueue.poll(1, TimeUnit.SECONDS);
				if (event == null) continue;
				
				switch (event) 
				{
					case FOUND_CLIENT:
					{
						String str = event.address.toString();
						str = str.substring(1, str.length() - 5);
						
						System.out.println(str);
						
						ArrayList<String> names = new ArrayList<String>();
						names.add(str);
						
						World world = new World();
						
						Grid grid = new RectangleGrid(new Vector2i(100, 100));
						for(int y = 0; y < 100; y++)
							for(int x = 0; x < 100; x++)
								grid.getCell(x, y).setValue(0, 1.0);
						
						overseer = new ServerOverseer(world, config.getInteger("Network.Client", "port", 3119));
						overseer.setClientNames(names);
						overseer.start();
						
						Thread.sleep(100);
						
						overseer.simulate(grid, 10);
						break;
					}
					
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
		
		if (overseer != null) overseer.disconnect();
		
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
