package com.hexcore.cas.control.discovery.test;

import com.hexcore.cas.control.discovery.Lobby;

public class ServerLobbyTest
{
	public ServerLobbyTest()
	{
		Lobby lobby = new Lobby();
		
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
}
