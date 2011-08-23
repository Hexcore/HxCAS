package com.hexcore.cas.ui;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.hexcore.cas.ui.Theme.Property;
import com.hexcore.cas.utilities.ConfigScanner;
import com.hexcore.cas.utilities.ConfigScanner.Symbol;


public class ThemeParser
{
	private ConfigScanner				scanner;
	private HashMap<String, Theme.Type>	types;
	
	private int	errors = 0;
	
	private Set<String>	validStates;
	private Set<String>	validTypes;
	private Set<String>	validProperties;
	
	ThemeParser(String filename)
	{
		validStates = new HashSet<String>();
		validStates.add("normal");
		validStates.add("hover");
		validStates.add("active");
		validStates.add("focus");
		validStates.add("checked");
		validStates.add("vertical");
		validStates.add("horizontal");
		validStates.add("selected");
		
		validTypes = new HashSet<String>();
		validTypes.add("Button");
		validTypes.add("Panel");
		validTypes.add("Scrollbar");
		validTypes.add("ScrollbarHandle");
		validTypes.add("ScrollbarFill");
		validTypes.add("TextBox");
		validTypes.add("CheckBox");
		validTypes.add("CheckBoxCaption");
		validTypes.add("DropDownBox");
		validTypes.add("DropDownBoxArrow");
		validTypes.add("DropDownBoxList");
		validTypes.add("DropDownBoxItem");
		validTypes.add("Tab");
		validTypes.add("TabInside");
		validTypes.add("Window");
		
		validProperties = new HashSet<String>();
		validProperties.add("background");
		validProperties.add("border");
		validProperties.add("border-radius");
		validProperties.add("text-colour");
		validProperties.add("text-offset");
		validProperties.add("text-shadow-colour");
		validProperties.add("text-shadow-offset");
		validProperties.add("padding");
		
		types = new HashMap<String, Theme.Type>();
		scanner = new ConfigScanner(filename);

		while (scanner.isValid())
		{
			ConfigScanner.Symbol symbol = scanner.getSymbol();
			if (symbol == null) break;
			
			String typeName = symbol.text;
			
			//System.out.println("Type: " + typeName);
						
			if (validTypes.contains(typeName) || (typeName.charAt(0) == '.'))
				readObject(typeName);
			else
			{
				error("Unknown type '" + typeName + "'");
				fastForward("}");
			}
		}
		
		if (errors == 0)
			System.out.println("Loaded theme: " + filename);
		else if (errors == 1)
			System.out.println("Found a error in theme file: " + filename);
		else
			System.out.println("Found " + errors + " in the theme file: " + filename);
	}
	
	public HashMap<String, Theme.Type> getTypes()
	{
		return types;
	}
	
	private void readObject(String typeName)
	{		
		Theme.Type type = new Theme.Type(typeName);
		type.state = "normal";
		
		Symbol symbol = scanner.peakSymbol();
		
		if (symbol.text.equals(":"))
		{
			String state = "";
			
			while (symbol.text.equals(":"))
			{
				scanner.getSymbol();
				
				String newState = scanner.getSymbol().text;
				
				if (!validStates.contains(newState))
				{
					error("Unknown state '" + newState + "'");
					return;
				}
				
				state += (state.length() == 0 ? "" : ":") + newState;
				
				symbol = scanner.peakSymbol();
			}		
		
			type.state = state;
		}
		
		if (!expect("{")) 
		{
			fastForward("{", "}");
			return;
		}
		
		while (scanner.isValid())
		{
			String propertyName = scanner.getSymbol().text;
			
			if (propertyName == null) 
			{
				error("Unexpected error, possibly end of file");
				break;
			}
			if (propertyName.equals("}")) break;
			
			if (!expect(":")) 
			{
				fastForward(";", "}");
				continue;
			}
			
			if (validProperties.contains(propertyName))
				readProperty(type, propertyName);
			else
			{
				error("Unknown property '" + propertyName + "'");
				fastForward(";", "}");
			}
		}
		
		types.put(type.name + ":" + type.state, type);
	}
	
	private void readProperty(Theme.Type type, String name)
	{
		String value = scanner.peakSymbol().text;
		if (value == null) return;
		
		Theme.Property	property = new Theme.Property(name);
				
		if (value.equals("none") || value.equals("center") || value.equals("horizontal") || value.equals("vertical") || value.equals("image") || value.equals("rgb") || value.equals("rgba"))
		{			
			Fill fill = readFill();

			if (fill == null) 
			{
				fastForward(";", "}");
				//System.out.println(type.name + "(" + type.state + ") : " + name + " = invalid property");
				return;
			}

			property.type = Property.Type.FILL;
			property.fill = fill;
			
			//System.out.println(type.name + "(" + type.state + ") : " + name + " = " + property.fill);
		}
		else
		{
			Symbol symbol = scanner.getSymbol();
			
			if (symbol.type == ConfigScanner.Symbol.Type.INTEGER)
			{
				property.x = symbol.integer;
				property.type = Theme.Property.Type.INTEGER;
				
				symbol = scanner.peakSymbol();
				
				if (symbol.type == ConfigScanner.Symbol.Type.INTEGER)
				{
					property.y = symbol.integer;
					property.type = Theme.Property.Type.POINT;
					
					scanner.getSymbol();
					
					//System.out.println(type.name + "(" + type.state + ") : " + name + " = <" + property.x + ", " + property.y + ">");
				}
				//else
				//	System.out.println(type.name + "(" + type.state + ") : " + name + " = " + property.x);
			}
			else
			{
				property.value = symbol.text;
				property.type = Theme.Property.Type.STRING;
				
				//System.out.println(type.name + "(" + type.state + ") : " + name + " = '" + symbol.text + "'");
			}
		}
		
		if (!expect(";")) return;
		
		type.add(property);
	}
	
	public Fill readFill()
	{
		String 	value = scanner.peakSymbol().text;
		Fill	fill = null;
		
		if (value.equals("none"))
		{
			scanner.getSymbol();
			
			fill = new Fill();
		}
		else if (value.equals("horizontal") || value.equals("vertical"))
		{
			Symbol 		symbol = scanner.getSymbol();
			Fill.Type	fillType = symbol.text.equals("vertical") ? Fill.Type.VERTICAL_GRADIENT : Fill.Type.HORIZONTAL_GRADIENT;
			
			Colour	colourA = readColour();
			if (colourA == null) return null;
			Colour	colourB = readColour();
			if (colourB == null) return null;
			
			fill = new Fill(fillType, colourA, colourB);
		}
		else if (value.equals("center") || value.equals("image"))
		{
			boolean	center = false;	
			
			if (value.equals("center")) 
			{
				center = true;
				scanner.nextSymbol();
			}
						
			Image	image = readImage();
			fill = new Fill(image);
			if (center) fill.setFlag(Fill.CENTER);
		}
		else if (value.equals("rgb") || value.equals("rgba"))
		{			
			Colour	colour = readColour();
			if (colour == null) return null;
			Symbol symbol = scanner.peakSymbol();
			
			if (symbol.text.equals("rgb") || symbol.text.equals("rgba"))
			{
				Colour colour2 = readColour();
				if (colour2 == null) return null;
				fill = new Fill(colour, colour2);
			}		
			else if (symbol.text.equals("center") || symbol.text.equals("image"))
			{
				boolean	center = false;	
				
				if (symbol.text.equals("center")) 
				{
					center = true;
					scanner.nextSymbol();
				}
				
				Image	image = readImage();
				fill = new Fill(image, colour);
				if (center) fill.setFlag(Fill.CENTER);
			}
			else
			{
				fill = new Fill(colour);
			}
		}
		
		return fill;
	}
	
	public Image readImage()
	{		
		if (!expect("image")) return null;
		if (!expect("(")) return null;
		
		String	filename = scanner.getSymbol().text;
		Image	image = new Image(filename);
		if (!image.isValid()) return null;
		
		if (!expect(")")) return null;
		return image;
	}
	
	public Colour readColour()
	{
		ConfigScanner.Symbol symbol = scanner.getSymbol();
		
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
		
	private boolean expect(String expected)
	{
		ConfigScanner.Symbol symbol = scanner.getSymbol();
		if (!expected.equals(symbol.text))
		{
			error("Expected '" + expected + "' - Got '" + symbol.text + "'");
			return false;
		}
		return true;
	}
	
	private boolean expect(String expected, String msg)
	{		
		ConfigScanner.Symbol symbol = scanner.getSymbol();
		if (!expected.equals(symbol.text))
		{
			error("Expected '" + expected + "' - Got '" + symbol.text + "' - " + msg);
			return false;
		}
		return true;
	}

	private float expectDecimal()
	{
		ConfigScanner.Symbol symbol = scanner.getSymbol();
		if (symbol.type != ConfigScanner.Symbol.Type.DECIMAL)
		{
			error("Expected a decimal value");
			return 0.0f;
		}	
		return symbol.decimal;
	}
	
	private void fastForward(String until)
	{
		String str = scanner.getSymbol().text;
		while ((str != null) && !str.equals(until)) str = scanner.getSymbol().text;
	}
	
	private void fastForward(String until1, String until2)
	{
		String str = scanner.getSymbol().text;
		while ((str != null) && !str.equals(until1) && !str.equals(until2)) str = scanner.getSymbol().text;
	}	
	
	private void error(String msg)
	{
		System.out.println("Error (Line " + scanner.getLineNumber() + "): " + msg);
		errors++;
	}
}
