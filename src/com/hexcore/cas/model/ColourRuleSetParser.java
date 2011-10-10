package com.hexcore.cas.model;

import java.util.Map;

import com.hexcore.cas.ui.toolkit.Colour;
import com.hexcore.cas.utilities.ConfigParser;
import com.hexcore.cas.utilities.ConfigScanner.Symbol;

public class ColourRuleSetParser extends ConfigParser
{
	public ColourRuleSetParser()
	{
		scanner.addSymbols(new char[] {':', '{', '}', ',', '(', ')', ';'});
	}
	
	public ColourRuleSet parse(String filename, Map<String, Integer> properties)
	{	
		scanner.readFile(filename);
		
		String			ruleSetName;
		ColourRuleSet	ruleSet = new ColourRuleSet(properties.size());
		
		if (!expect("colourset")) return null;
		
		Symbol symbol = scanner.getSymbol();
		
		if (symbol.type == Symbol.Type.STRING)
			ruleSetName = symbol.text;
		else
		{
			error("Expected a name for the ruleset");
			return null;
		}
		
		if (!expect("{")) return null;
		
		while (scanner.isValid()) 
		{
			symbol = scanner.peakSymbol();
			if (!symbol.text.equals("property")) break;
			
			readColourRule(ruleSet, properties);
		}

		expect("}");
		
		return ruleSet;
	}
	
	private void readColourRule(ColourRuleSet ruleSet, Map<String, Integer> properties)
	{
		String 		ruleName;
		ColourRule	rule = new ColourRule();
		
		if (!expect("property"))
		{
			fastForward("}");
			return;
		}
		
		Symbol symbol = scanner.getSymbol();
		
		if (symbol.type == Symbol.Type.STRING)
			ruleName = symbol.text;
		else
		{
			error("Expected a name for the property rule");
			fastForward("}");
			return;
		}
		
		if (!expect("{")) 
		{
			fastForward("}");
			return;
		}
		
		while (scanner.isValid())
		{
			symbol = scanner.peakSymbol();
			if (symbol.type != Symbol.Type.INTEGER) break;
			
			ColourRule.Range range = readRange();
			if (range != null) rule.addRange(range);
		}
		
		expect("}");
				
		if (rule != null)
		{
			Integer index = properties.get(ruleName);
			
			if (index == null)
				error("Invalid property name '" + ruleName + "', property doesn't exist in rule");
			else
				ruleSet.setColourRule(index, rule);
		}
	}
	
	private ColourRule.Range readRange()
	{
		int first = 0, second = 0;
		
		Symbol symbol = scanner.getSymbol();
		if (symbol.type == Symbol.Type.INTEGER)
			first = symbol.integer;
		else
		{
			error("Expected an integer specifying the start of the range, got '" + symbol.text + "'");
			fastForward("}");
			return null;
		}	
		
		expect("..");
		
		symbol = scanner.getSymbol();
		if (symbol.type == Symbol.Type.INTEGER)
			second = symbol.integer;
		else
		{
			error("Expected an integer specifying the end of the range, got '" + symbol.text + "'");
			fastForward("}");
			return null;
		}	
		
		expect(":");
		
		Colour startColour = readColour();
		
		symbol = scanner.peakSymbol();
		if (symbol.text.equals("rgb") || symbol.text.equals("rgba"))
		{
			Colour endColour = readColour();
			expect(";");
			return new ColourRule.Range(first, second, startColour, endColour);
		}
		
		expect(";");
		return new ColourRule.Range(first, second, startColour);
	}
	
	public Colour readColour()
	{
		Symbol symbol = scanner.getSymbol();
		
		if (symbol.text.equals("rgb"))
		{
			if (!expect("(")) return null;
			float r = expectDecimal();
			if (!expect(",")) return null;
			float g = expectDecimal();
			if (!expect(",")) return null;
			float b = expectDecimal();
			if (!expect(")")) return null;
			
			return new Colour(r, g, b);
		}
		else if (symbol.text.equals("rgba"))
		{
			if (!expect("(")) return null;
			float r = expectDecimal();
			if (!expect(",")) return null;
			float g = expectDecimal();
			if (!expect(",")) return null;	
			float b = expectDecimal();
			if (!expect(",", "Missing alpha value")) return null;	
			float a = expectDecimal();			
			if (!expect(")")) return null;
			
			return new Colour(r, g, b, a);
		}
		
		return null;
	}
}
