package com.hexcore.cas.control.protocol.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.hexcore.cas.control.protocol.CAPMessageProtocol;
import com.hexcore.cas.control.protocol.DictNode;
import com.hexcore.cas.control.protocol.Message;

public class GridThroughput
{
	public static final int messages = 100000;
	
	static public void main(String[] args)
	{
		new GridThroughput();
	}
	
	public GridThroughput()
	{
		try
		{
			long start = System.nanoTime();
			
			ServerSocket serverSocket = new ServerSocket(3329);
			Server server = new Server(serverSocket);
			server.start();

			Socket clientSocket = new Socket("localhost", 3329);
			Client client = new Client(clientSocket);
			client.start();
			
			server.join();
			client.join();
			
			long end = System.nanoTime();
			
			System.out.println("Time: " + (float)(end - start) / 1000000000 + "s");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	class Client extends Thread
	{
		Socket socket;
		CAPMessageProtocol protocol;
		
		public Client(Socket socket)
		{
			this.socket = socket;
		}
		
		@Override
		public void run()
		{
			System.out.println("Starting Client");
			protocol = new CAPMessageProtocol(socket);
			protocol.start();
			
			Message message;
			for (int i = 0; i < messages; i++)
			{
				message = protocol.waitForMessage();
				
				System.out.print("]");
				
				message = new Message(new DictNode());
				protocol.sendMessage(message);				
			}
			
			protocol.disconnect();
		}
	}
	
	class Server extends Thread
	{
		ServerSocket listenSocket;
		Socket socket;
		CAPMessageProtocol protocol;
		
		public Server(ServerSocket serverSocket)
		{
			this.listenSocket = serverSocket;
		}
		
		@Override
		public void run()
		{
			System.out.println("Starting Server");
			try
			{
				socket = listenSocket.accept();
				
				System.out.println("Connected");
				
				protocol = new CAPMessageProtocol(socket);
				protocol.start();
				
				Message message;
				for (int i = 0; i < messages; i++)
				{
					System.out.print("[");
					
					message = new Message(new DictNode());
					protocol.sendMessage(message);
					
					message = protocol.waitForMessage();
				}
				
				System.out.println("");
				
				protocol.disconnect();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
