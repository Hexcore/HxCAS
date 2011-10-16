package com.hexcore.cas;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.hexcore.cas.control.discovery.Lobby;
import com.hexcore.cas.control.discovery.LobbyListener;
import com.hexcore.cas.control.server.Simulator;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.World;
import com.hexcore.cas.model.WorldReader;
import com.hexcore.cas.model.WorldSaver;
import com.hexcore.cas.rulesystems.CALCompiler;
import com.hexcore.cas.ui.GUI;
import com.hexcore.cas.utilities.Configuration;
import com.hexcore.cas.utilities.Log;

public class Server implements LobbyListener
{
	private final static String TAG = "Server";
	public final static String VERSION = "v0.1";
		
	private AtomicBoolean 	running = new AtomicBoolean(false);
	private AtomicBoolean	activeSimulation = new AtomicBoolean(false);
	
	private Configuration 	config = null;
	private Lobby 			lobby = null;
	private GUI 			ui = null;
	
	private ClientThread	client = null;
	private Simulator 		simulate = null;
	private World			world = null;
	
	private LinkedBlockingQueue<ServerEvent>	eventQueue;
	
	private static Server instance = null;
	
	// Temp
	private Set<String> names = new TreeSet<String>();
	
	public static void main(String[] args)
	{
		 instance = new Server();
		 instance.start();
	}
	
	public void start()
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
		
		Log.information(TAG, "Starting user interface...");
		ui = new GUI(instance);
		
		try
		{
			Thread.sleep(1000);
		} 
		catch (InterruptedException e1)
		{
			e1.printStackTrace();
		}
		
		lobby.ping();
				
		running.set(true);
		while (running.get())
		{
			try
			{
				ServerEvent event = eventQueue.poll(3, TimeUnit.SECONDS);
				if (event == null) continue;
				
				switch (event.type) 
				{
					case FOUND_CLIENT:
					{
						String str = event.address.toString();
						str = str.substring(1, str.length() - 5);
						
						System.out.println(event.address);
						
						if (str.equals("127.0.0.1")) break; // Ignore localhost
						names.add(str);
						break;
					}
										
					case CREATE_WORLD:
					{						
						world = new World();
						
						Grid grid = event.gridType.create(event.size, 2);
						grid.setWrappable(event.wrappable);
	
						for(int y = 0; y < event.size.y; y++)
							for(int x = 0; x < event.size.x; x++)
								grid.getCell(x, y).setValue(0, 0.0);
												
						world.addGeneration(grid);						
						world.setRuleCode("ruleset Flash\n{\n\ttypecount 1;\n\tproperty alive;\n\ttype Land : 0\n\t{\n\t\tself.alive = 1 - self.alive;\n\t}\n}");
						
						ui.startWorldEditor(world);
						
						break;
					}
					
					case LOAD_WORLD:
					{
						world = new World();
						WorldReader reader = new WorldReader(world);
						
						try
						{
							reader.readWorld(event.filename);
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
						
						ui.startWorldEditor(world);
						
						break;
					}
					
					case SAVE_WORLD:
					{
						WorldSaver saver = new WorldSaver();
						
						try
						{
							saver.saveWorld(world);
							Log.information(TAG, "Saved world");
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}
					
					case START_SIMULATION:
					{
						if (!activeSimulation.getAndSet(true)) initSimulation();
						
						simulate.play();
						break;
					}		
					
					case STEP_SIMULATION:
					{
						if (!activeSimulation.getAndSet(true)) initSimulation();
						
						simulate.step();
						break;
					}
					
					case PAUSE_SIMULATION:
					{
						if (activeSimulation.get()) simulate.pause();
						break;
					}
					
					case STOP_SIMULATION:
					{
						if (activeSimulation.getAndSet(false))
						{
							simulate.disconnect();
							simulate = null;
							
							client.client.stop();
							client = null;
						}
						break;
					}
					
					case PING_CLIENTS:
					{
						lobby.ping();
						break;
					}
					
					case SHUTDOWN:
					{
						Log.information(TAG, "Got shutdown message");
						running.set(false);
						break;
					}
				}
			}								
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}		
		}
		
		Log.information(TAG, "Shutting down...");
		lobby.disconnect();
		if (simulate != null) simulate.disconnect();
		
		try
		{
			Thread.sleep(300);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		System.exit(0);
	}
	
	private void initSimulation() throws InterruptedException
	{
		ArrayList<String> clientList = new ArrayList<String>();
		clientList.addAll(names);
		
		if (clientList.isEmpty())
		{
			Log.information(TAG, "No network clients found, starting local client");
			
			client = new ClientThread();
			client.start();
			
			Thread.sleep(1000); // Wait for client to start
			
			clientList.add("127.0.0.1");
		}
		
		Log.information(TAG, "Compiling rule code...");
		
		CALCompiler compiler = new CALCompiler();
		compiler.compile(world.getRuleCode());
		
		if (compiler.getErrorCount() > 0)
			Log.error(TAG, "Invalid rule code");
		
		Log.information(TAG, "Starting overseer...");
		
		simulate = new Simulator(world, config.getInteger("Network.Client", "port", 3119));
		simulate.setClientNames(clientList);
		simulate.setRuleBytecode(compiler.getCode());
		simulate.start();
		simulate.pause();
		Thread.sleep(100);
		
		simulate.simulate(-1);
	}
	
	public void sendEvent(ServerEvent event)
	{
		try 
		{
			eventQueue.put(event);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void foundClient(SocketAddress address) 
	{
		ServerEvent event = new ServerEvent(ServerEvent.Type.FOUND_CLIENT);
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
	
	class ClientThread extends Thread
	{
		public Client client;
		
		@Override
		public void run()
		{
			client = new Client();
			client.start(false);
		}
	}
}
