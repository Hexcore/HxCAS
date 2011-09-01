package com.hexcore.cas.utilities;

import java.util.HashMap;

import com.hexcore.cas.utilities.ConfigScanner.Symbol;

public class Configuration extends ConfigParser
{	
	public class ConfigCategory
	{
		HashMap<String, String>	properties = new HashMap<String, String>();
	}
	
	HashMap<String, ConfigCategory>	categories;
	
	public Configuration(String filename)
	{
		scanner = new ConfigScanner();
		scanner.addSymbols(new char[] {'{', '}', '=', ';'});
		scanner.readFile(filename);
		
		categories = new HashMap<String, ConfigCategory>();
		
		while (scanner.isValid())
		{
			ConfigScanner.Symbol symbol = scanner.getSymbol();
			if (symbol == null) break;
			
			if (symbol.type != Symbol.Type.STRING)
			{
				System.out.println("A block's name must be a string");
				break;
			}
						
			//System.out.println("Block: " + symbol.text);
			ConfigCategory category = new ConfigCategory();
			categories.put(symbol.text, category);
		
			if (!expect("{"))
			{
				fastForward("}");
				continue;
			}
			
			symbol = scanner.peakSymbol();
			while (!symbol.text.equals("}"))
			{
				scanner.nextSymbol();
				
				//System.out.println("Key: " + symbol.text);		
				String key = symbol.text;
				
				if (!expect("="))
				{
					fastForward(";", "}");
					continue;
				}
				
				symbol = scanner.getSymbol();
				//System.out.println("Value: " + symbol.text);
				String value = symbol.text;
				
				expect(";");
				
				category.properties.put(key, value);
				
				symbol = scanner.peakSymbol();
			}
			
			expect("}");
		}
	}
	
	public String getString(String categoryName, String propertyName, String fallback)
	{
		ConfigCategory category = categories.get(categoryName);
		
		if (category != null)
		{
			String value = category.properties.get(propertyName);
			if (value != null) return value;
		}
		
		return fallback;
	}
	
	public int getInteger(String categoryName, String propertyName, int fallback)
	{
		ConfigCategory category = categories.get(categoryName);
		
		if (category != null)
		{
			String value = category.properties.get(propertyName);
			if (value == null) return fallback;
			
			try
			{
				return Integer.parseInt(value);
			}
			catch (NumberFormatException e)
			{
			}
		}
		
		return fallback;
	}
}
