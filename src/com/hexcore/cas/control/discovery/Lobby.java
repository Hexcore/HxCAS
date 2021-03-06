package com.hexcore.cas.control.discovery;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Enumeration;

import com.hexcore.cas.utilities.Log;

/**
 * Class Lobby
 * 
 * @authors Divan Burger
 */

public class Lobby extends Thread
{
	/////////////////////////////////////////////
	/// Public Variables
	public int						listenPort;
	public int						beaconPort;
	
	/////////////////////////////////////////////
	/// Private Variables
	private static final String		TAG = "Lobby";
	
	private ArrayList<LobbyListener> listeners = null;
	private boolean					running = false;
	private DatagramChannel			channel = null;
	
	private SocketAddress			address = null;
	
	public Lobby(int beaconPort, int listenPort)
	{
		System.setProperty("java.net.preferIPv4Stack", "true");
		
		this.beaconPort = beaconPort;
		this.listenPort = listenPort;
		
		listeners = new ArrayList<LobbyListener>();
		
		address = new InetSocketAddress(listenPort);
		
		try
		{
			channel = DatagramChannel.open();
			channel.socket().setBroadcast(true);
			channel.socket().setReuseAddress(true);
			channel.socket().bind(address);
			channel.socket().setSoTimeout(2000);
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
		Log.information(TAG, "Pinging for clients on port " + beaconPort);
		
		ByteBuffer buffer = ByteBuffer.allocate(8);
		
		try
		{
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			
			// Try to broadcast on each interface on it's network's broadcast address
			while(interfaces.hasMoreElements())
			{
				NetworkInterface networkInterface = interfaces.nextElement();
				for(InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses())
				{
					if (interfaceAddress == null) continue;
					InetAddress broadcast = interfaceAddress.getBroadcast();
					buffer.position(0);
					channel.send(buffer, new InetSocketAddress(broadcast, beaconPort));
				}
			}
			
			// Try an generic local network broadcast
			InetAddress broadcast = InetAddress.getByName("255.255.255.255");
			buffer.position(0);
			
			try
			{
				channel.send(buffer, new InetSocketAddress(broadcast, beaconPort));
			}
			catch (SocketException e)
			{
				Log.error(TAG, "Could not ping for clients - could not create an socket");
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
		
		Log.information(TAG, "Listening for replies on " + address);
		
		try
		{
			while(running)
			{
				SocketAddress replyAddress = channel.receive(buffer);
				
				if(buffer.position() < 8) continue;
				if(buffer.get(0) != (byte)0xBE) continue;
				if(buffer.get(1) != (byte)0xEF) continue;
				if(buffer.get(2) != (byte)0xCA) continue;
				if(buffer.get(3) != (byte)0xFE) continue;
				
				if(!(replyAddress instanceof InetSocketAddress))
					continue;
				
				InetSocketAddress replyInetAddress = (InetSocketAddress)replyAddress;
				
				Log.information(TAG, "Got reply from " + replyInetAddress.getAddress());
				
				for(LobbyListener listener : listeners)
					listener.foundClient(replyInetAddress);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
