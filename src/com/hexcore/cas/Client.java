package com.hexcore.cas;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import com.hexcore.cas.control.client.ClientOverseer;
import com.hexcore.cas.control.discovery.Beacon;
import com.hexcore.cas.utilities.Configuration;
import com.hexcore.cas.utilities.Log;

public class Client
{
	public final static String TAG = "Client";
	public final static String VERSION = "v0.1";
	
	private Beacon 			beacon = null;
	private Configuration 	config = null;
	private ClientOverseer 	overseer = null;
	private AtomicBoolean	running = new AtomicBoolean();
	
	public static void main(String[] args)
	{
		Client instance = new Client();
		instance.start(true);
	}
	
	public void stop()
	{
		running.set(false);
	}
	
	public void start(boolean textUserInterface)
	{
		if (textUserInterface) System.out.println("== Hexcore CAS Client - " + VERSION + " ==");
		
		Log.information(TAG, "Loading configuration...");
		config = new Configuration("data/config.txt");
		
		Log.information(TAG, "Setting up beacon...");
		beacon = new Beacon(config.getInteger("Network.Beacon", "port", 3118));
		beacon.start();
		
		Log.information(TAG, "Setting up client overseer...");
		overseer = new ClientOverseer(config.getInteger("Network.Client", "port", 3119));
		overseer.start();
		
		if (overseer.isValid())
		{
			if (textUserInterface)
			{
				System.out.println("\nCAS Client ready");
				System.out.println(" * Press 'q' to shutdown\n");
			}
			
			running.set(true);
			while (running.get())
			{
				if (textUserInterface)
				{
					int c = ' ';
					
					try
					{
						c = System.in.read();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					
					if (c == 'q') running.set(false);
				}
				else
				{
					try
					{
						Thread.sleep(1000);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		
		Log.information(TAG, "Shutting down...");
		beacon.disconnect();
		overseer.disconnect();
		
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		System.exit(0);
	}
}
