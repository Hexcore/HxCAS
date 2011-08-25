package com.hexcore.cas.control.protocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import com.hexcore.cas.control.client.Overseer;

public abstract class CAPInformationProcessor
{
	protected byte[] currInBytes = null;
	protected DictNode header = null;
	protected DictNode body = null;
	
	protected int index = 0;
	
	public CAPInformationProcessor()
		throws IOException
	{
		header = new DictNode();
		body = new DictNode();
	}
	
	protected ByteNode byteString()
	{
		int num = 0;
		while(currInBytes[index] != ':')
		{
			num = num * 10 + currInBytes[index];
			index++;
		}
		index++;
		ArrayList<Byte> list = new ArrayList<Byte>();
		int cnt = 0;
		while(cnt++ < num)
		{
			list.add(new Byte(currInBytes[index]));
			index++;
		}
		int s = list.size();
		byte[] b = new byte[s];
		for(int i = 0; i < s; i++)
			b[i] = list.get(i).byteValue();
		return new ByteNode(b);
	}
	
	protected DictNode dictionary()
	{
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<Node> values = new ArrayList<Node>();
		while(currInBytes[index] != 'e')
		{
			//Keys
			switch(currInBytes[index++])
			{
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
					keys.add(byteString().toString());
					break;
				default:
					System.out.println("Key for dictionary not a valid choice.");
					return null;
			}
			//Values
			switch(currInBytes[index++])
			{
				case 'l':
					values.add(list());
					break;
				case 'i':
					values.add(intValue());
					break;
				case 'd':
					values.add(dictionary());
					break;
				case 'f':
					values.add(doubleValue());
					break;
				default:
					System.out.println("Value for dictionary not a valid choice.");
					return null;
			}
		}
		DictNode d = new DictNode();
		for(int i = 0; i < keys.size(); i++)
			d.addToDict(keys.get(i).toUpperCase(), values.get(i));
		index++;
		return d;
	}
	
	protected DoubleNode doubleValue()
	{
		ByteBuffer buff = ByteBuffer.allocate(8);
		buff.put(currInBytes, index, 8);
		index += 9;
		return new DoubleNode(buff.getDouble());
	}
	
	protected IntNode intValue()
	{
		int value = 0;
		while(currInBytes[index] != 'e')
		{
			value = value * 10 + currInBytes[index];
			index++;
		}
		index++;
		return new IntNode(value);
	}
	
	protected ListNode list()
	{
		ListNode list = new ListNode();
		while(currInBytes[index] != 'e')
		{
			switch(currInBytes[index++])
			{
				case 'l':
					list.addToList(list());
					break;
				case 'i':
					list.addToList(intValue());
					break;
				case 'd':
					list.addToList(dictionary());
					break;
				case 'f':
					list.addToList(doubleValue());
					break;
				default:
					System.out.println("Value for list not a valid choice.");
					return null;
			}
		}
		index++;
		return list;
	}
	
	protected int processInput()
	{
		header = dictionary();
		index++;
		if(currInBytes[index] == ';') // No body;
			return 0;
		else
		{
			body = dictionary();
			index++;
		}
		if(index >= currInBytes.length)
			return 0; // Every byte is accounted for.
		else
		{
			System.out.println("There was a problem with interpreting the message.");
			return 2;
		}
	}
	
	protected abstract void interpretInput();
	
	public abstract void setCurrentInformation(byte[] in);
	
	public abstract void start()
		throws IOException;
}
