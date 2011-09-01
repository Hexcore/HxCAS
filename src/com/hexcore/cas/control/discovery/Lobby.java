package com.hexcore.cas.control.discovery;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Enumeration;

public class Lobby extends Thread
{
	public final static int LISTEN_PORT = 3117;
	public final static int BEACON_PORT = 3118;
	
	private boolean running = false;
	private SocketAddress address = null;
	private SocketAddress beaconAddress = null;
	private DatagramChannel	channel = null;
	
	private ArrayList<LobbyListener> listeners = null;
	
	public Lobby()
	{
		System.setProperty("java.net.preferIPv4Stack", "true");
		
		listeners = new ArrayList<LobbyListener>();
		
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
	
	public void addListener(LobbyListener listener)
	{
		listeners.add(listener);
	}
	
	public void removeListener(LobbyListener listener)
	{
		listeners.remove(listener);
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
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			
			while (interfaces.hasMoreElements())
			{
				NetworkInterface networkInterface = interfaces.nextElement();
				for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses())
				{
					InetAddress broadcast = interfaceAddress.getBroadcast();
					
					channel.send(buffer, new InetSocketAddress(broadcast, BEACON_PORT));
				}
			}
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
				
				if (buffer.position() < 8) continue;
				if (buffer.get(0) != (byte)0xBE) continue;
				if (buffer.get(1) != (byte)0xEF) continue;
				if (buffer.get(2) != (byte)0xCA) continue;
				if (buffer.get(3) != (byte)0xFE) continue;
				
				System.out.println("Discovery: Got reply from " + replyAddress);
				
				for (LobbyListener listener : listeners)
					listener.foundClient(replyAddress);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
