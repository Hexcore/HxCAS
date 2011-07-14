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
		public enum Type {UNKNOWN, STRING, INTEGER, DECIMAL};
		
		public Symbol(String text)
		{
			type = Type.UNKNOWN;
			this.text = text;
		}
		
		public Type		type;
		public String	text;
		public int		integer;
		public float	decimal;
		public int		line;
	}
	
	private boolean			valid = false;
	private BufferedReader	reader;
	private int				nextChar;
	private Set<Character>	symbols;
	private int				line;
	
	private Symbol			nextSymbol;
	
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
			 
	public int getLineNumber()
	{
		return line;
	}
	
	public boolean isValid()
	{
		return valid;
	}
	
	// Returns the next symbol without stepping to the next symbol
	public Symbol peakSymbol()
	{
		return nextSymbol;
	}
	
	// Returns a symbol and then steps to the next symbol
	public Symbol getSymbol()
	{
		Symbol symbol = nextSymbol;
		nextSymbol();
		return symbol;
	}
	
	// Steps to the next symbol
	public void nextSymbol() 
	{
		nextSymbol = getToken();
		if (nextSymbol == null) return;
		
		nextSymbol.line = line;
		if (nextSymbol.type == Symbol.Type.STRING) return;

		nextSymbol.type = Symbol.Type.STRING;

		try
		{			
			nextSymbol.integer = Integer.decode(nextSymbol.text);
			nextSymbol.type = Symbol.Type.INTEGER;
		}
		catch (NumberFormatException nfe)
		{
			try
			{			
				nextSymbol.decimal = Float.parseFloat(nextSymbol.text);
				nextSymbol.type = Symbol.Type.DECIMAL;
			}
			catch (NumberFormatException nfe2)
			{
			}
		}
	}
	
	
	/// Private Methods
	
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
	
	private int getChar()
	{
		return nextChar;
	}
	
	private Symbol getToken()
	{
		String 	buf = "";
		boolean	quoted = false;
		
		int	c = -1;
		while (true)
		{
			c = getChar();
			if (c < 0) return null;
			
			if (c == '"')
			{	
				nextChar();
				if (!quoted && !buf.isEmpty()) break;
				if (quoted) return new Symbol(buf);
				
				quoted = !quoted;
			}
			else if (quoted)
			{
				nextChar();
				buf += (char)c;
			}
			else if (c <= 32)
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
					return new Symbol(buf);
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
		return new Symbol(buf);
	}
}