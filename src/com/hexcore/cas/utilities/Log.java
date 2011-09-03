package com.hexcore.cas.utilities;

public class Log
{
	public static void debug(String tag, String msg)
	{
		System.out.println("[DDD] " + tag + ": " + msg);
	}
	
	public static void information(String tag, String msg)
	{
		System.out.println("[III] " + tag + ": " + msg);
	}
	
	public static void warning(String tag, String msg)
	{
		System.out.println("[WWW] " + tag + ": " + msg);
	}
	
	public static void error(String tag, String msg)
	{
		System.err.println("[EEE] " + tag + ": " + msg);
	}
}
