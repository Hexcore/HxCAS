package com.hexcore.cas.utilities;

import java.util.HashMap;

import com.hexcore.cas.utilities.ConfigScanner.Symbol;

public class Configuration extends ConfigParser
{	
	public class ConfigCategory
	{
		String					name;
		HashMap<String, String>	properties = new HashMap<String, String>();
		
		public ConfigCategory(String name) {this.name = name;}
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
				System.out.println("A category's name must be a string");
				break;
			}
			
			if (symbol.text.contains("."))
			{
				System.out.println("A category's name may not include a '.'");
				break;
			}
						
			//System.out.println("Block: " + symbol.text);
			ConfigCategory category = new ConfigCategory(symbol.text);
			
			readBlock(category);
			
			addCategory(category);
		}
	}
	
	private void readBlock(ConfigCategory category)
	{
		if (!expect("{"))
		{
			fastForward("}");
			return;
		}
		
		Symbol symbol = scanner.peakSymbol();
		while (!symbol.text.equals("}"))
		{
			scanner.nextSymbol();
			
			//System.out.println("Key: " + symbol.text);		
			String key = symbol.text;
			
			symbol = scanner.peakSymbol();
			
			if (symbol.text.equals("{")) 
			{				
				String blockName = category.name + "." + key;
				
				//System.out.println("Block: " + blockName);
				ConfigCategory subCategory = new ConfigCategory(blockName);
				
				readBlock(subCategory);
				
				addCategory(subCategory);
			}
			else
			{
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
			}
			
			symbol = scanner.peakSymbol();
		}
		
		expect("}");
	}
	
	private void addCategory(ConfigCategory category)
	{
		categories.put(category.name, category);
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
