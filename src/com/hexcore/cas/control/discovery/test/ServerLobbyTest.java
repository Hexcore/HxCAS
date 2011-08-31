package com.hexcore.cas.control.discovery.test;

import java.net.SocketAddress;

import com.hexcore.cas.control.discovery.Lobby;
import com.hexcore.cas.control.discovery.LobbyListener;

public class ServerLobbyTest implements LobbyListener
{
	public ServerLobbyTest()
	{
		Lobby lobby = new Lobby();
		
		lobby.addListener(this);
		lobby.start();
		
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		lobby.ping();
	}
	
	public static void main(String[] args)
	{
		new ServerLobbyTest();
	}

	@Override
	public void foundClient(SocketAddress address)
	{
		System.out.println("A client has been discovered : " + address);
	}
}
