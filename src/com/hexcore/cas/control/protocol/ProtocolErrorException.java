package com.hexcore.cas.control.protocol;

/**
 * Class ProtocolErrorException
 * 
 * @authors Divan Burger
 */

public class ProtocolErrorException extends Exception
{
	private static final long serialVersionUID	= 597444503488694238L;
	
	public ProtocolErrorException(String msg)
	{
		super(msg);
	}
}
