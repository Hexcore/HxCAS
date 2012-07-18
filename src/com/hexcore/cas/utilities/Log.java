package com.hexcore.cas.utilities;

/**
 * Class Log
 * 	Used to create a log of the running system.
 * 	Can be used in debugging, to display general information,
 * 	to indicate an error has occurred or for displaying warnings.
 * 
 * @author Divan Burger
 */

public class Log
{
	public static void debug(String tag, String msg)
	{
		System.out.println("[DDD] " + tag + ": " + msg);
	}
	
	public static void error(String tag, String msg)
	{
		System.err.println("[EEE] " + tag + ": " + msg);
	}
	
	public static void information(String tag, String msg)
	{
		System.out.println("[III] " + tag + ": " + msg);
	}
	
	public static void warning(String tag, String msg)
	{
		System.out.println("[WWW] " + tag + ": " + msg);
	}
}
