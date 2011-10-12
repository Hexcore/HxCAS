package com.hexcore.cas.utilities;

public abstract class ConfigParser
{
	private static final String TAG = "ConfigScanner";
	
	protected ConfigScanner scanner;
	protected int errors = 0;
	
	public ConfigParser()
	{
		scanner = new ConfigScanner();
	}
	
	public int getNumErrors()
	{
		return errors;
	}
	
	protected boolean expect(String expected)
	{
		ConfigScanner.Symbol symbol = scanner.getSymbol();
		if (!expected.equals(symbol.text))
		{
			error("Expected '" + expected + "' - Got '" + symbol.text + "'");
			return false;
		}
		return true;
	}
	
	protected boolean expect(String expected, String msg)
	{		
		ConfigScanner.Symbol symbol = scanner.getSymbol();
		if (!expected.equals(symbol.text))
		{
			error("Expected '" + expected + "' - Got '" + symbol.text + "' - " + msg);
			return false;
		}
		return true;
	}

	protected float expectDecimal()
	{
		ConfigScanner.Symbol symbol = scanner.getSymbol();
		if (symbol.type != ConfigScanner.Symbol.Type.DECIMAL)
		{
			error("Expected a decimal value");
			return 0.0f;
		}	
		return symbol.decimal;
	}
	
	protected void fastForward(String until)
	{
		String str = scanner.getSymbol().text;
		while ((str != null) && !str.equals(until)) str = scanner.getSymbol().text;
	}
	
	protected void fastForward(String until1, String until2)
	{
		String str = scanner.getSymbol().text;
		while ((str != null) && !str.equals(until1) && !str.equals(until2)) str = scanner.getSymbol().text;
	}
	
	protected void error(String msg)
	{
		Log.error(TAG, "Error (Line " + scanner.getLineNumber() + "): " + msg);
		errors++;
	}
}
