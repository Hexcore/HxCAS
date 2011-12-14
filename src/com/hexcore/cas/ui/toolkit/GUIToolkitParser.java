package com.hexcore.cas.ui.toolkit;

import com.hexcore.cas.utilities.ConfigParser;
import com.hexcore.cas.utilities.ConfigScanner;
import com.hexcore.cas.utilities.ConfigScanner.Symbol;

public class GUIToolkitParser extends ConfigParser 
{
	public GUIToolkitParser() 
	{
	}
	
	public Fill readFill(String themeName)
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
						
			Image	image = readImage(themeName);
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
				
				Image	image = readImage(themeName);
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
	
	public Image readImage(String themeName)
	{		
		if (!expect("image")) return null;
		if (!expect("(")) return null;
		
		String	category = scanner.getSymbol().text;
		
		if (!expect(",")) return null;
		
		String	name = scanner.getSymbol().text;
		
		Image	image = new Image("data/themes/" + themeName + "/images/" + category + "/" + name);
		if (!image.isValid()) 
		{
			expect(")");
			return null;
		}
		
		if (!expect(")")) return null;
		return image;
	}
	
	public Text.Size readTextSize()
	{
		ConfigScanner.Symbol symbol = scanner.getSymbol();
		
		if (symbol.text.equals("tiny"))
			return Text.Size.TINY;
		else if (symbol.text.equals("small"))
			return Text.Size.SMALL;	
		else if (symbol.text.equals("medium"))
			return Text.Size.MEDIUM;	
		else if (symbol.text.equals("large"))
			return Text.Size.LARGE;	
		else if (symbol.text.equals("huge"))
			return Text.Size.HUGE;	
		else if (symbol.text.equals("code"))
			return Text.Size.CODE;	
		else
		{
			error("Invalid text size: " + symbol.text);
			return Text.Size.SMALL;
		}
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
}
