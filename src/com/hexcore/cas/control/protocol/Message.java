package com.hexcore.cas.control.protocol;

import java.io.IOException;
import java.io.OutputStream;

public class Message
{
	private DictNode header;
	private Node body;
	
	public Message(DictNode header)
	{
		this.header = header;
		this.body = null;
	}	
	
	public Message(DictNode header, Node body)
	{
		this.header = header;
		this.body = body;
	}
	
	public DictNode getHeader()
	{
		return header;
	}
	
	public Node getBody()
	{
		return body;
	}
	
	public void write(OutputStream out) throws IOException
	{
		header.write(out);
		out.write(';');
		if (body != null) body.write(out);
		out.write(';');
	}
}
