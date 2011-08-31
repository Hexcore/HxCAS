package com.hexcore.cas.control.discovery;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class Beacon extends Thread
{
	public final static int BEACON_PORT = 3118;
	
	private boolean running = false;
	private SocketAddress address = null;
	
	public Beacon()
	{
		System.setProperty("java.net.preferIPv4Stack", "true");
		
		address = new InetSocketAddress(BEACON_PORT);
	}
	
	public void disconnect()
	{
		running = false;
	}
	
	@Override
	public void run()
	{
		running = true;
		byte[]	buffer = new byte[8];
		
		System.out.println("Discovery: Listening on " + address);
		
		try
		{
			DatagramSocket socket = new DatagramSocket(address);
			DatagramPacket request = new DatagramPacket(buffer, 8);
			
			while (running)
			{
				socket.receive(request);
				System.out.println("Discovery: Got request from " + request.getSocketAddress());
				DatagramPacket response = new DatagramPacket(buffer, 8, request.getSocketAddress());
				socket.send(response);
				System.out.println("Discovery: Reply sent");
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
