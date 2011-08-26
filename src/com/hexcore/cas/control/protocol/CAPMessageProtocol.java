package com.hexcore.cas.control.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class CAPMessageProtocol extends Thread
{
	private byte[] currInBytes = null;	
	private int index = 0;
	private volatile boolean running = false;
	private Socket socket = null;
	private LinkedBlockingQueue<Message> messageQueue = null;
	
	CAPMessageProtocol(Socket socket)
	{
		this.socket = socket;
		this.messageQueue = new LinkedBlockingQueue<Message>();
	}
	
	public Message waitForMessage()
	{
		try
		{
			return messageQueue.poll(1000, TimeUnit.MILLISECONDS);
		}
		catch (InterruptedException e)
		{
			return null;
		}
	}
	
	public Socket getSocket()
	{
		return socket;
	}

	public void receiveData(byte[] in)
	{
		currInBytes = in.clone();
		index = 0;
		
		Message message = processInput();
		
		try
		{
			messageQueue.put(message);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	public void sendMessage(Message message)
	{
		try
		{
			OutputStream out = socket.getOutputStream();	
			message.write(out);
		}
		catch(IOException e)
		{
			System.out.println("Error: Could not send message, OutputStream raised a IOException");
		}
	}
	
	public void disconnect()
	{
		running = false;
	}
	
	@Override
	public void run()
	{
		running = true;
		
		byte[] buff = new byte[1];
		while (running)
		{
			try
			{
				ArrayList<Byte> inBy = new ArrayList<Byte>();
				InputStream in = socket.getInputStream();
				int tmp = in.read(buff);
				
				while(tmp != -1)
				{
					inBy.add(buff[0]);
					tmp = in.read();
				}
				
				byte[] sendThrough = new byte[inBy.size()];
				
				for(int i = 0; i < inBy.size(); i++)
					sendThrough[i] = inBy.get(i).byteValue();
				
				receiveData(sendThrough);
			}
			catch(IOException e)
			{
				System.out.println("-- ERROR IN RUN OF CAPINTERFACE -- could not receive bytes correctly");
			}
		}
		try
		{
			socket.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private Message processInput()
	{
		Node body = null;
		DictNode header = dictionary();
		index++;
		
		if(currInBytes[index] != ';') // No body;
		{
			body = dictionary();
			index++;
		}
		
		if(index < currInBytes.length) // Not all bytes accounted for
		{
			System.out.println("There was a problem with interpreting the message.");
			return null;
		}
			
		return new Message(header, body); // Every byte is accounted for.
	}
	
	private ByteNode byteString()
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
	
	private DictNode dictionary()
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
	
	private DoubleNode doubleValue()
	{
		ByteBuffer buff = ByteBuffer.allocate(8);
		buff.put(currInBytes, index, 8);
		index += 9;
		return new DoubleNode(buff.getDouble());
	}
	
	private IntNode intValue()
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
	
	private ListNode list()
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
}
