package com.hexcore.cas.control.discovery;

import java.net.InetSocketAddress;

public interface LobbyListener
{
	public void foundClient(InetSocketAddress address);
}
