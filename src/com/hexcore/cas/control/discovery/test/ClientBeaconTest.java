package com.hexcore.cas.control.discovery.test;

import com.hexcore.cas.control.discovery.Beacon;

public class ClientBeaconTest
{
	public ClientBeaconTest()
	{
		Beacon beacon = new Beacon(3118);
		beacon.start();
	}
	
	public static void main(String[] args)
	{
		new ClientBeaconTest();
	}
}
