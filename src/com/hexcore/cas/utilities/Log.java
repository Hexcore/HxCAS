package com.hexcore.cas.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Log
{
	private static File logFile = new File("log.txt");
	
	public static void debug(String tag, String msg)
	{
		try
		{
			OutputStream out = new FileOutputStream(logFile, true);
			out.write(("[DDD] " + tag + ": " + msg + "\n").getBytes());
			out.close();
		}
		catch(FileNotFoundException ex)
		{
			ex.printStackTrace();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		//System.out.println("[DDD] " + tag + ": " + msg);
	}
	
	public static void information(String tag, String msg)
	{
		try
		{
			OutputStream out = new FileOutputStream(logFile, true);
			out.write(("[DDD] " + tag + ": " + msg + "\n").getBytes());
			out.close();
		}
		catch(FileNotFoundException ex)
		{
			ex.printStackTrace();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		//System.out.println("[III] " + tag + ": " + msg);
	}
	
	public static void warning(String tag, String msg)
	{
		try
		{
			OutputStream out = new FileOutputStream(logFile, true);
			out.write(("[DDD] " + tag + ": " + msg + "\n").getBytes());
			out.close();
		}
		catch(FileNotFoundException ex)
		{
			ex.printStackTrace();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		//System.out.println("[WWW] " + tag + ": " + msg);
	}
	
	public static void error(String tag, String msg)
	{
		try
		{
			OutputStream out = new FileOutputStream(logFile, true);
			out.write(("[DDD] " + tag + ": " + msg + "\n").getBytes());
			out.close();
		}
		catch(FileNotFoundException ex)
		{
			ex.printStackTrace();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		//System.err.println("[EEE] " + tag + ": " + msg);
	}
}
