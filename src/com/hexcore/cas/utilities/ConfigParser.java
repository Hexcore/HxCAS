package com.hexcore.cas.utilities;

/**
 * Class ConfigParser
 * 
 * @author Divan Burger; Karl Zoller
 */

public abstract class ConfigParser
{
	/////////////////////////////////////////////
	/// Private Variables
	private static final String TAG = "ConfigScanner";
	
	/////////////////////////////////////////////
	/// Protected Variables
	protected ConfigScanner scanner;
	protected int errors = 0;
	
	public ConfigParser()
	{
		reset();
	}
	
	public int getNumErrors()
	{
		return errors;
	}
	
	public void reset()
	{
		scanner = new ConfigScanner();
		errors = 0;
	}
	
	/////////////////////////////////////////////
	/// Protected functions
	protected void error(String msg)
	{
		Log.error(TAG, "Error (Line " + scanner.getLineNumber() + "): " + msg);
		errors++;
	}
	
	protected boolean expect(String expected)
	{
		ConfigScanner.Symbol symbol = scanner.getSymbol();
		if(symbol == null)
		{
			error("Expected '" + expected + "' - Reached end of file");
			return false;		
		}
		else if(!expected.equals(symbol.text))
		{
			error("Expected '" + expected + "' - Got '" + symbol.text + "'");
			return false;
		}
		return true;
	}
	
	protected boolean expect(String expected, String msg)
	{		
		ConfigScanner.Symbol symbol = scanner.getSymbol();
		if(symbol == null)
		{
			error("Expected '" + expected + "' - Reached end of file");
			return false;		
		}
		else if(!expected.equals(symbol.text))
		{
			error("Expected '" + expected + "' - Got '" + symbol.text + "' - " + msg);
			return false;
		}
		return true;
	}
	
	protected float expectDecimal()
	{
		ConfigScanner.Symbol symbol = scanner.getSymbol();
		if(symbol.type != ConfigScanner.Symbol.Type.DECIMAL)
		{
			error("Expected a decimal value");
			return 0.0f;
		}
		return symbol.decimal;
	}
	
	protected int expectInteger()
	{
		ConfigScanner.Symbol symbol = scanner.getSymbol();
		if(symbol.type != ConfigScanner.Symbol.Type.INTEGER)
		{
			error("Expected a integer value");
			return 0;
		}
		return symbol.integer;
	}
	
	protected void fastForward(String until)
	{
		String str = scanner.getSymbol().text;
		while((str != null) && !str.equals(until))
			str = scanner.getSymbol().text;
	}
	
	protected void fastForward(String until1, String until2)
	{
		String str = scanner.getSymbol().text;
		while((str != null) && !str.equals(until1) && !str.equals(until2))
			str = scanner.getSymbol().text;
	}
}
