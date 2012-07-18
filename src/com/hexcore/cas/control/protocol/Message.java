package com.hexcore.cas.control.protocol;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Class Message
 * 
 * @authors Divan Burger
 */

public class Message
{
	private DictNode	header;
	private Node		body;
	
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
	
	public String toString()
	{
		String str = "Message(";
		str += header.toString();
		if(body != null)
			str += ", " + body.toString(); 
		return str + ")";
	}
	
	public void write(OutputStream out)
		throws IOException
	{
		out.write('#');
		header.write(out);
		out.write(';');
		if(body != null)
			body.write(out);
		out.write('.');
	}
}
