package com.hexcore.cas.control.discovery;

import java.net.InetSocketAddress;

/**
 * Class LobbyListener
 * 
 * @authors Divan Burger
 */

public interface LobbyListener
{
	public void foundClient(InetSocketAddress address);
}
