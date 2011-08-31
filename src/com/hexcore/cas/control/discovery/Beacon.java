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
		byte[]	requestBuffer = new byte[8];
		byte[]	responseBuffer = new byte[8];
		
		System.out.println("Discovery: Listening on " + address);
		
		try
		{
			DatagramSocket socket = new DatagramSocket(address);
			DatagramPacket request = new DatagramPacket(requestBuffer, 8);
			
			while (running)
			{
				// Wait for request
				socket.receive(request);
				System.out.println("Discovery: Got request from " + request.getSocketAddress());
	
				// Construct response
				responseBuffer[0] = (byte)0xBE;
				responseBuffer[1] = (byte)0xEF;
				responseBuffer[2] = (byte)0xCA;
				responseBuffer[3] = (byte)0xFE;
				
				DatagramPacket response = new DatagramPacket(responseBuffer, 8, request.getSocketAddress());
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
