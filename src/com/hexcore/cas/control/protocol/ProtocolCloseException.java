package com.hexcore.cas.control.protocol;

/**
 * Class ProtocolCloseException
 * 
 * @authors Divan Burger
 */

public class ProtocolCloseException extends Exception
{
	private static final long serialVersionUID	= -2072031012259226192L;
	
	public ProtocolCloseException(String msg)
	{
		super(msg);
	}
}
