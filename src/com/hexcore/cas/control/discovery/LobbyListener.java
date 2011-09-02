package com.hexcore.cas.control.discovery;

import java.net.SocketAddress;

public interface LobbyListener
{
	public void foundClient(SocketAddress address);
}
