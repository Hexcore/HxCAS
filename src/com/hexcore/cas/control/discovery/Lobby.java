package com.hexcore.cas.control.discovery;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Lobby extends Thread
{
	public final static int LISTEN_PORT = 3117;
	public final static int BEACON_PORT = 3118;
	
	private boolean running = false;
	private SocketAddress address = null;
	private SocketAddress beaconAddress = null;
	private DatagramChannel	channel = null;
	
	public Lobby()
	{
		System.setProperty("java.net.preferIPv4Stack", "true");
		
		address = new InetSocketAddress(LISTEN_PORT);
		beaconAddress = new InetSocketAddress("255.255.255.255", BEACON_PORT);
		
		try
		{
			channel = DatagramChannel.open();
			channel.socket().setBroadcast(true);
			channel.socket().setReuseAddress(true);
			channel.socket().bind(address);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void disconnect()
	{
		running = false;
	}
	
	public void ping()
	{
		System.out.println("Pinging for clients on port " + BEACON_PORT);
		
		ByteBuffer buffer = ByteBuffer.allocate(8);
		
		try
		{
			channel.send(buffer, beaconAddress);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		running = true;
		ByteBuffer buffer = ByteBuffer.allocate(8);
		
		System.out.println("Discovery: Listening for replies on " + address);
		
		try
		{
			while (running)
			{
				SocketAddress replyAddress = channel.receive(buffer);
				System.out.println("Discovery: Got reply from " + replyAddress);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
