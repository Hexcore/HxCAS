package com.hexcore.cas.ui.toolkit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.hexcore.cas.ui.toolkit.Theme.Property;
import com.hexcore.cas.utilities.ConfigScanner;
import com.hexcore.cas.utilities.ConfigScanner.Symbol;

public class ThemeParser extends GUIToolkitParser
{
	private String	themeName;
	
	private HashMap<String, Theme.Type>	types;
	
	private Set<String>	validStates;
	private Map<String, Set<String>> validTypeProperties;
	
	ThemeParser(String themeName)
	{
		this.themeName = themeName;
		
		scanner.addSymbols(new char[] {':', '{', '}', ',', '(', ')', ';'});
		scanner.readFile("data/themes/" + themeName + "/theme.thm");
		
		validStates = new HashSet<String>();
		validStates.add("normal");
		validStates.add("hover");
		validStates.add("active");
		validStates.add("focus");
		validStates.add("checked");
		validStates.add("vertical");
		validStates.add("horizontal");
		validStates.add("selected");
		validStates.add("toggled");
		
		validTypeProperties = new HashMap<String, Set<String>>();
		addTypeProperties("Button", 
				"background", "border", "border-radius", "text-colour", "text-offset", 
				"text-shadow-colour","text-shadow-offset", "padding",
				"divider-left-colour", "divider-right-colour", "icon-space-width");
		addTypeProperties("Panel", 
				"background", "border", "border-radius");
		addTypeProperties("Scrollbar", 
				"background", "border", "size");
		addTypeProperties("ScrollbarHandle", 
				"background", "border", "border-radius", "size");
		addTypeProperties("ScrollbarFill", 
				"background", "border");
		addTypeProperties("TextBox", 
				"background", "border", "border-radius", "text-colour", "text-offset", "text-shadow-colour","text-shadow-offset", "selected-colour");
		addTypeProperties("TextBoxLineNumbers", 
				"background", "border");
		addTypeProperties("CheckBox", 
				"background", "border", "border-radius");
		addTypeProperties("CheckBoxCaption", 
				"background", "border", "border-radius", "text-colour", "text-offset", "text-shadow-colour","text-shadow-offset", "padding");
		addTypeProperties("DropDownBox", 
				"background", "border", "text-colour", "text-offset", "border-radius", "padding");
		addTypeProperties("DropDownBoxArrow", 
				"background", "border");
		addTypeProperties("DropDownBoxList", 
				"background", "border", "text-colour", "text-offset", "padding");
		addTypeProperties("DropDownBoxItem", 
				"background", "border", "text-colour", "text-offset", "text-shadow-colour","text-shadow-offset", "padding");
		addTypeProperties("Tab", 
				"background", "border", "border-radius", "text-colour", "text-offset", "text-shadow-colour","text-shadow-offset", "padding");
		addTypeProperties("TabInside", 
				"background", "border", "border-radius");
		addTypeProperties("Slider", 
				"background", "border", "border-radius", "height");
		addTypeProperties("SliderHandle", 
				"background", "border", "border-radius", "height", "width");	
		addTypeProperties("List", 
				"background", "border", "border-radius", "text-colour", "hover-background", "selected-background", "padding", "item-padding");		
		addTypeProperties("Dialog", 
				"background", "border", "border-radius");
		addTypeProperties("DialogFade", 
				"background", "border");
		addTypeProperties("ColourPicker", 
				"background", "border-radius");		
		addTypeProperties("Window", 
				"background", "border");
		
		types = new HashMap<String, Theme.Type>();
		
		Theme.Type themeType = new Theme.Type("#:normal");
		types.put("#:normal", themeType);
		
		while (scanner.isValid())
		{
			ConfigScanner.Symbol symbol = scanner.getSymbol();
			if (symbol == null) break;
			
			String typeName = symbol.text;
			
			//System.out.println("Type: " + typeName);
			
			if (typeName.equals("name") || typeName.equals("author") || typeName.equals("iconSet"))
			{
				if (!expect(":")) 
				{
					fastForward(";");
					return;
				}
				
				String value = scanner.getSymbol().text;
				
				if (typeName.equals("name"))
					themeType.add(new Property("name", value));
				else if (typeName.equals("author"))
					themeType.add(new Property("author", value));
				
				if (!expect(";")) return;	
			}
			else if (validTypeProperties.containsKey(typeName) || (typeName.charAt(0) == '.'))
				readObject(typeName, (typeName.charAt(0) == '.'));
			else
			{
				error("Unknown type '" + typeName + "'");
				fastForward("}");
			}
		}
		
		if (errors == 0)
			System.out.println("Loaded theme: " + themeName);
		else if (errors == 1)
			System.out.println("Found a error in theme file: " + themeName);
		else
			System.out.println("Found " + errors + " errors in the theme file: " + themeName);
	}
	
	private void addTypeProperties(String type, String... properties)
	{
		Set<String> propertySet = new HashSet<String>();
		
		for (String property : properties) propertySet.add(property);
		
		validTypeProperties.put(type, propertySet);
	}
	
	public HashMap<String, Theme.Type> getTypes()
	{
		return types;
	}
	
	private void readObject(String typeName, boolean isClass)
	{		
		Set<String> typesValidProperties = validTypeProperties.get(typeName);

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
				
			if (isClass || typesValidProperties.contains(propertyName))
				readProperty(type, propertyName);
			else
			{
				error("Invalid property '" + propertyName + "' for '" + typeName + "'");
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
			Fill fill = readFill(themeName);

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
}
