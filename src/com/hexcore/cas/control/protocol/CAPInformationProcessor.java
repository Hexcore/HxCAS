package com.hexcore.cas.control.protocol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CAPInformationProcessor
{
	private CAPInterface inter = null;
	private ArrayList<Byte> currInBytes = null;
	private HashMap<String, String> header = null;
	
	public CAPInformationProcessor()
		throws IOException
	{
		inter = new CAPInterface(this);
		header = new HashMap<String, String>();
	}
	
	private void process()
	{
		for(int i = 0; ; i++)
		{
			byte b = currInBytes.get(i).byteValue();
			int ib = -1;
			String sb = null;
			String key = "";
			try
			{
				ib = b;
			}
			catch(NumberFormatException ex)
			{
				sb = Byte.toString(b);
			}
			if(ib != -1 && sb == null)
			{
				i++; //Skips : character
				for(; i < (i + ib); i++)
					key += Byte.toString(currInBytes.get(i));
			}
			else if(ib == -1 && sb != null)
			{
				if(key.compareTo("VERSION") == 0)
				{
					//int ver = Integer.parseInt(arg0)
				}
				else if(key.compareTo("TYPE") == 0)
				{
					
				}
				else
					System.out.println("-- currently unknown header field --");
			}
		}
	}
	
	public void setCurrentInformation(ArrayList<Byte> in)
	{
		if(currInBytes.size() == 0)
		{
			currInBytes = in;
			process();
			currInBytes.clear();
		}
		else
		{
			inter.sendState(1);
		}
	}
	
	public void start()
		throws IOException
	{
		inter.start();
	}
}
