package com.hexcore.cas;

import java.io.IOException;

import com.hexcore.cas.control.discovery.Beacon;

public class Client
{
	public final static String version = "v0.1";
	
	private Beacon beacon = null;
	
	public static void main(String[] args)
	{
		new Client();
	}
	
	public Client()
	{
		System.out.println("== Hexcore CAS Client - " + version + " ==");
		
		System.out.println("Setting up beacon...");
		beacon = new Beacon();
		beacon.start();
		
		System.out.println("\nCAS Client ready");
		System.out.println(" * Press 'q' to shutdown");
		
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
		
		System.out.println("Shutting down...");
		beacon.disconnect();
	}
}
