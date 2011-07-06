package com.hexcore.cas.utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ConfigScanner
{
	public static class Symbol
	{
		public enum Type {STRING, INTEGER, DECIMAL};
		
		public Type		type;
		public String	text;
		public int		integer;
		public float	decimal;
		public int		line;
	}
	
	boolean			valid = false;
	BufferedReader	reader;
	int				nextChar;
	Set<Character>	symbols;
	int				line;
	
	Symbol			nextSymbol;
	
	public ConfigScanner(String filename)
	{
		try
		{
			reader = new BufferedReader(new FileReader(filename));
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Error: Could not open file : " + filename);
			e.printStackTrace();
		}
		
		symbols = new HashSet<Character>();
		symbols.add(':');
		symbols.add('{');
		symbols.add('}');
		symbols.add(',');
		symbols.add('(');
		symbols.add(')');
		symbols.add(';');
		
		line = 1;
		valid = true;
		
		nextSymbol();
	}
	
	private void nextChar()
	{
		if (nextChar == -1) valid = false;
		if (nextChar == '\n') line++;
		
		try
		{
			nextChar = reader.read();
		}
		catch(IOException ioe)
		{
			nextChar = -1;
		}
	}
		
	public int getLine()
	{
		return line;
	}
	
	public Symbol peakSymbol()
	{
		return nextSymbol;
	}
	
	public Symbol getSymbol()
	{
		Symbol symbol = nextSymbol;
		nextSymbol();
		return symbol;
	}
	
	public void nextSymbol() 
	{
		String text = getToken();
		
		if (text == null)
		{
			nextSymbol = null;
			return;
		}
		
		nextSymbol = new Symbol();
		nextSymbol.text = text;
		nextSymbol.type = Symbol.Type.STRING;
		nextSymbol.line = line;

		try
		{			
			nextSymbol.integer = Integer.decode(text);
			nextSymbol.type = Symbol.Type.INTEGER;
		}
		catch (NumberFormatException nfe)
		{
			try
			{			
				nextSymbol.decimal = Float.parseFloat(text);
				nextSymbol.type = Symbol.Type.DECIMAL;
			}
			catch (NumberFormatException nfe2)
			{
			}
		}
	}

	public boolean isValid()
	{
		return valid;
	}
	
	private int getChar()
	{
		return nextChar;
	}
	
	private String getToken()
	{
		String buf = "";
		
		int	c = -1;
		while (true)
		{
			c = getChar();
			if (c < 0) return null;
			
			if (c <= 32)
			{
				nextChar();
				if (!buf.isEmpty()) break;
			}
			else if (symbols.contains((char)c))
			{
				if (buf.isEmpty()) 
				{
					nextChar();
					buf += (char)c;
					return buf;
				}
				else
					break;
			}
			else
			{
				nextChar();
				buf += (char)c;
			}
		}
		
		if (buf.isEmpty()) return null;
		return buf;
	}
}
