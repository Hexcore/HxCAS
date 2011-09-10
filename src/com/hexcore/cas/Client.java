package com.hexcore.cas;

import java.io.IOException;

import com.hexcore.cas.control.client.ClientOverseer;
import com.hexcore.cas.control.discovery.Beacon;
import com.hexcore.cas.utilities.Configuration;
import com.hexcore.cas.utilities.Log;

public class Client
{
	public final static String TAG = "Client";
	public final static String VERSION = "v0.1";
	
	private Beacon beacon = null;
	private Configuration config = null;
	private ClientOverseer overseer = null;
	
	public static void main(String[] args)
	{
		new Client();
	}
	
	public Client()
	{
		System.out.println("== Hexcore CAS Client - " + VERSION + " ==");
		
		Log.information(TAG, "Loading configuration...");
		config = new Configuration("data/config.txt");
		
		Log.information(TAG, "Setting up beacon...");
		beacon = new Beacon(config.getInteger("Network.Beacon", "port", 3118));
		beacon.start();
		
		Log.information(TAG, "Setting up client overseer...");
		overseer = new ClientOverseer();
		overseer.start();
		
		if (overseer.isValid())
		{
			System.out.println("\nCAS Client ready");
			System.out.println(" * Press 'q' to shutdown\n");
			
			while (true)
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
				
				if (c == 'q') break;
			}
		}
		
		Log.information(TAG, "Shutting down...");
		beacon.disconnect();
		overseer.disconnect();
	}
}
