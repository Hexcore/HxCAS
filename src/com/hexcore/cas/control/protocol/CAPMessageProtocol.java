package com.hexcore.cas.control.protocol;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.hexcore.cas.utilities.Log;

public class CAPMessageProtocol extends Thread
{
	private static final String TAG = "CAPMessageProtocol";
	
	private static final int NO_BYTE = -2;
	
	private volatile boolean running = false;
	private LinkedBlockingQueue<Message> messageQueue = null;
	private Socket socket = null;

	private BufferedInputStream inputStream;
	private int currentByte = NO_BYTE;
	
	public CAPMessageProtocol(Socket socket)
	{
		this.socket = socket;
		this.messageQueue = new LinkedBlockingQueue<Message>();
		
		try
		{
			inputStream = new BufferedInputStream(socket.getInputStream());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean isRunning()
	{
		return running;
	}
	
	private Node readNode() throws ProtocolErrorException
	{
		Node node = null;
		byte b = peakByte();
		switch(b)
		{
			case 'l':
				node = list();
				break;
			case 'i':
				node = intValue();
				break;
			case 'd':
				node = dictionary();
				break;
			case 'f':
				node = doubleValue();
				break;
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
				node = byteString();
				break;
			default:
				Log.error(TAG, "Expected start of a node, got '" + (char)b + "'");
		}
		
		return node;
	}
		
	private ByteNode byteString() throws ProtocolErrorException
	{		
		int num = 0;
		while(peakByte() != ':') num = num * 10 + (nextByte() - '0');
		
		expect(':');
		
		ArrayList<Byte> list = new ArrayList<Byte>();
		int cnt = 0;
		while(cnt++ < num)
		{
			list.add(new Byte(peakByte()));
			nextByte();
		}
		int s = list.size();
		byte[] b = new byte[s];
		for(int i = 0; i < s; i++)
			b[i] = list.get(i).byteValue();
		return new ByteNode(b);
	}
	
	private DictNode dictionary() throws ProtocolErrorException
	{		
		DictNode d = new DictNode();
		
		expect('d');
		
		while(peakByte() != 'e')
		{			
			String key;
			Node value;
						
			//Keys
			byte b = peakByte();
			switch(b)
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
					key = byteString().toString();
					break;
				default:
					Log.error(TAG, "Expected start of byte string, got '" + (char)b + "'");
					Log.error(TAG, "Key for dictionary not a byte string.");
					return null;
			}
			
			//Values
			value = readNode();
			if (value == null)
			{
				Log.error(TAG, "Value for dictionary not a valid choice.");
				break;
			}
			
			d.addToDict(key, value);
		}
		
		expect('e');
		
		return d;
	}
	
	public void disconnect()
	{
		running = false;
	}
	
	private DoubleNode doubleValue() throws ProtocolErrorException
	{
		ByteBuffer buff = ByteBuffer.allocate(8);
		
		expect('f');
		
		for (int i = 0; i < 8; i++) buff.put(nextByte());
		
		expect('e');
		
		return new DoubleNode(buff.getDouble());
	}
	
	public Socket getSocket()
	{
		return socket;
	}
	
	private IntNode intValue() throws ProtocolErrorException
	{
		int value = 0;
		expect('i');
		while(peakByte() != 'e') value = value * 10 + (nextByte() - '0');
		expect('e');
		return new IntNode(value);
	}
	
	private ListNode list() throws ProtocolErrorException
	{
		expect('l');
		
		ListNode list = new ListNode();
		
		while(peakByte() != 'e')
		{
			Node node = readNode();
			if (node == null) break;
			list.addToList(node);
		}
		
		expect('e');
		
		return list;
	}
	
	private void receiveMessage() throws ProtocolErrorException
	{
		Node body = null;
				
		DictNode header = dictionary();
				
		expect(';');
		
		// Read in body
		if(peakByte() != ';') body = dictionary();
				
		// Read in last separator
		expect(';');
		
		// Add message
		try
		{
			messageQueue.put(new Message(header, body));
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public void sendMessage(Message message)
	{
		System.out.println(message.toString());
		
		try
		{
			OutputStream out = socket.getOutputStream();	
			message.write(out);
			out.flush();
		}
		catch(IOException e)
		{
			Log.error(TAG, "Error: Could not send message, OutputStream raised a IOException");
		}
	}
	
	public void sendState(int is, String mess)
	{
		DictNode header = new DictNode();
		header.addToDict("TYPE", new ByteNode("STATUS"));
		header.addToDict("VERSION", new IntNode(1));
		
		DictNode body = new DictNode();
		body.addToDict("STATE", new IntNode(is));
		body.addToDict("MSG", new ByteNode(mess));
		Message msg = new Message(header, body);
		sendMessage(msg);
	}
	
	public Message waitForMessage()
	{
		try
		{
			return messageQueue.poll(1000, TimeUnit.MILLISECONDS);
		}
		catch(InterruptedException e)
		{
			return null;
		}
	}
	
	private void expect(char c) throws ProtocolErrorException
	{
		expect((byte)c);
	}
	
	private void expect(byte b) throws ProtocolErrorException
	{
		byte g = nextByte(); 
		if (g != b) Log.error(TAG, "Protocol Stream Error: Expected '" + (char)b + "', got '" + (char)g + "'");
	}
	
	private byte nextByte() throws ProtocolErrorException
	{
		byte b = 0;
		
		if (currentByte == NO_BYTE)
		{
			try
			{
				int i = inputStream.read();
				
				if (i == -1)
				{
					Log.error(TAG, "Unexpected end of stream");
					throw new ProtocolErrorException("Unexpected end of stream");
				}
								
				b = (byte)i;
			}
			catch (IOException e1)
			{
				throw new ProtocolErrorException("Unexpected end of stream - " + e1.getMessage());
			}
		}
		else
		{
			b = (byte)currentByte;
			currentByte = NO_BYTE;
		}
		
		return b;
	}	
	
	private byte peakByte() throws ProtocolErrorException
	{
		if (currentByte == NO_BYTE)
		{
			try
			{
				currentByte = inputStream.read();
				
				if (currentByte == -1)
				{
					Log.error(TAG, "Unexpected end of stream");
					throw new ProtocolErrorException("Unexpected end of stream");
				}
			}
			catch (IOException e1)
			{
				throw new ProtocolErrorException("Unexpected end of stream - " + e1.getMessage());
			}
		}
		
		return (byte)currentByte;
	}
	
	public void run()
	{
		running = true;

		while (running) 
		{
			try
			{
				receiveMessage();
			}
			catch(ProtocolErrorException e)
			{
				Log.error(TAG, e.getMessage());
				disconnect();
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
}
