package com.hexcore.cas.control.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class CAPInterface extends Thread
{
	private ServerSocket sock = null;
	private CAPInformationProcessor parent = null;
	private Socket cSock = null;
	
	public CAPInterface(CAPInformationProcessor p)
		throws IOException
	{
		sock = new ServerSocket(3119);
		parent = p;
	}
	
	public void sendState(int is)
	{
		try
		{
			OutputStream out = cSock.getOutputStream();
			String s = "";
			String stateStr = "STATE";
			s += stateStr.getBytes().length + ":" + stateStr + "i" + is + "e"; //<len>:<contents>i<is int>)e
			out.write(s.getBytes());
		}
		catch(IOException e)
		{
			System.out.println("-- ERROR IN SENDSTATE OF CAPINTERFACE -- could not get or write to outputstream");
		}
		
	}
	
	public void run()
	{
		byte[] buff = new byte[1];
		try
		{
			cSock = sock.accept();
		}
		catch(IOException ex)
		{
			System.out.println("-- ERROR IN RUN OF CAPINTERFACE -- could not accept connection");
		}
		while(true)
		{
			try
			{
				ArrayList<Byte> inBy = new ArrayList<Byte>();
				InputStream in = cSock.getInputStream();
				int tmp = in.read(buff);
				while(tmp != -1)
				{
					inBy.add(buff[0]);
					tmp = in.read();
				}
				parent.setCurrentInformation(inBy);
			}
			catch(IOException e)
			{
				System.out.println("-- ERROR IN RUN OF CAPINTERFACE -- could not receive bytes correctly");
				//e.printStackTrace();
			}
		}
	}
}