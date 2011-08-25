package com.hexcore.cas.control.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class CAPInterface extends Thread
{
	private Socket sock = null;
	private CAPInformationProcessor parent = null;
	private boolean mustRun = true;
	
	public CAPInterface(CAPInformationProcessor p, Socket s)
		throws IOException
	{
		sock = s;
		parent = p;
	}
	
	public void sendState(int is)
	{
		try
		{
			OutputStream out = sock.getOutputStream();
			String s = "";
			String stateStr = "STATUS";
			s += stateStr.getBytes().length + ":" + stateStr + "i" + is + "e";
			out.write(s.getBytes());
		}
		catch(IOException e)
		{
			System.out.println("-- ERROR IN SENDSTATE(int) OF CAPINTERFACE -- could not get or write to outputstream");
		}
	}
	
	public void sendState(int is, String mess)
	{
		try
		{
			OutputStream out = sock.getOutputStream();
			String s = "";
			String stateStr = "STATUS";
			s += stateStr.getBytes().length + ":" + stateStr + "i" + is + "e" + mess.getBytes().length + ":" + mess;
			out.write(s.getBytes());
		}
		catch(IOException e)
		{
			System.out.println("-- ERROR IN SENDSTATE(int string) OF CAPINTERFACE -- could not get or write to outputstream");
		}
	}
	
	public void sendMessage(String mess)
	{
		try
		{
			OutputStream out = sock.getOutputStream();
			String s = mess.getBytes().length + ":" + mess;
			out.write(s.getBytes());
		}
		catch(IOException e)
		{
			System.out.println("-- ERROR IN SENDSTATE(string) OF CAPINTERFACE -- could not get or write to outputstream");
		}
	}
	
	public void sendGrid(byte[] gridBytes)
	{
		try
		{
			OutputStream out = sock.getOutputStream();
			String s = "RESULT".getBytes().length + ":" + "RESULT";
			out.write(s.getBytes());
			out.write(gridBytes);
		}
		catch(IOException e)
		{
			System.out.println("-- ERROR IN SENDSTATE OF CAPINTERFACE -- could not get or write to outputstream");
		}
	}
	
	public void disconnect()
	{
		mustRun = false;
	}
	
	public void run()
	{
		byte[] buff = new byte[1];
		while(mustRun)
		{
			try
			{
				ArrayList<Byte> inBy = new ArrayList<Byte>();
				InputStream in = sock.getInputStream();
				int tmp = in.read(buff);
				while(tmp != -1)
				{
					inBy.add(buff[0]);
					tmp = in.read();
				}
				byte[] sendThrough = new byte[inBy.size()];
				for(int i = 0; i < inBy.size(); i++)
					sendThrough[i] = inBy.get(i).byteValue();
				parent.setCurrentInformation(sendThrough);
			}
			catch(IOException e)
			{
				System.out.println("-- ERROR IN RUN OF CAPINTERFACE -- could not receive bytes correctly");
			}
		}
		try
		{
			sock.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}