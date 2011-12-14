package com.hexcore.cas.ui.toolkit;

import java.util.ArrayList;
import java.util.List;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.utilities.ConfigScanner.Symbol;
import com.hexcore.cas.utilities.Log;

public class LayoutParser extends GUIToolkitParser 
{		
	private final static String TAG = "LayoutParser";
	
	private Theme theme = null;
	
	public LayoutParser() 
	{
	}

	public Widget parse(String layoutName, Widget parent)
	{
		if (parent == null) 
		{
			Log.error(TAG, "Parent can not be null");
			return null;
		}
		else if (parent.getWindow() == null)
		{
			Log.error(TAG, "Parent does not have a parent window set, try attaching it to a widget whose ancestor or itself is attached to a window");
			return null;		
		}
		
		theme = parent.getWindow().getTheme();
				
		reset();
		scanner.addSymbols(new char[] {':', '{', '}', ',', '(', ')', ';'});
		scanner.readFile("data/layouts/" + layoutName + ".layout");
		
		if (scanner.isValid()) return readWidget(parent);		
		return null;
	}
	
	private Widget readWidget(Widget parent)
	{
		expect("widget");
		
		Widget widget = null;
		Symbol symbol = scanner.getSymbol();
		
		// Create widget
		if (symbol.text.equals("Container"))
		{
			widget = new Container();
		}
		else if (symbol.text.equals("ScrollableContainer"))
		{
			widget = new ScrollableContainer();
		}
		else if (symbol.text.equals("View"))
		{
			widget = new View();
		}
		else if (symbol.text.equals("TabbedView"))
		{
			widget = new TabbedView();
		}
		else if (symbol.text.equals("Layout"))
		{
			widget = new Layout();
		}	
		else if (symbol.text.equals("LinearLayout"))
		{
			LinearLayout.Direction direction = LinearLayout.Direction.VERTICAL;
			
			symbol = scanner.peakSymbol();
			if (symbol.text.equals(":"))
			{
				expect(":");
				symbol = scanner.getSymbol();
				if (symbol.text.equals("horizontal"))
					direction = LinearLayout.Direction.HORIZONTAL;
				else if (symbol.text.equals("vertical"))
					direction = LinearLayout.Direction.VERTICAL;
				else
					error("Invalid direction: " + symbol.text);
			}
			
			widget = new LinearLayout(direction);
		}
		else if (symbol.text.equals("Button"))
		{
			widget = new Button();
		}
		else if (symbol.text.equals("Text"))
		{
			widget = new TextWidget("");
		}
		else if (symbol.text.equals("TextArea"))
		{
			widget = new TextArea(100, 10);
		}		
		else if (symbol.text.equals("Image"))
		{
			widget = new ImageWidget();
		}
		else if (symbol.text.equals("TextBox"))
		{
			widget = new TextBox();
		}		
		else if (symbol.text.equals("NumberBox"))
		{
			widget = new NumberBox(0);
		}		
		else if (symbol.text.equals("CheckBox"))
		{
			widget = new CheckBox("");
		}
		else if (symbol.text.equals("DropDownBox"))
		{
			widget = new DropDownBox();
		}
		else if (symbol.text.equals("Slider"))
		{
			widget = new SliderWidget();
		}		
		else if (symbol.text.equals("List"))
		{
			widget = new ListWidget();
		}				
		else
		{
			error("Invalid widget type: " + symbol.text);
			fastForward("}");
			return null;
		}
		
		// Add to parent
		if (widget != null)
		{
			if (parent instanceof Layout)
				((Layout)parent).add(widget);
			else if (parent instanceof Container)
				((Container)parent).setContents(widget);
			else if (parent instanceof View)
				((View)parent).add(widget);			
			else
				error("Parent of widget is not a container, view or layout");
		}

		// Read inner widgets and properties
		if (!expect("{"))
		{
			fastForward("{", "}");
			return null;
		}
		
		if (widget != null)
			while (scanner.isValid())
			{
				symbol = scanner.peakSymbol();
				
				if (symbol.text.equals("widget"))
					readWidget(widget);
				else if (symbol.text.equals("}"))
					break;
				else
					readProperty(widget);
			}
		
		expect("}");
					
		return widget;
	}
	
	private void readProperty(Widget widget)
	{
		Symbol symbol = scanner.getSymbol();
		String name = symbol.text;
		
		if (!expect(":"))
		{
			fastForward(";", "}");
			return;
		}
		
		if (name.equals("name"))
		{
			symbol = scanner.getSymbol();
			widget.setName(symbol.text);
		}
		else if (name.equals("width"))
		{
			int size = expectInteger();
			widget.setWidth(size);
		}
		else if (name.equals("height"))
		{
			int size = expectInteger();
			widget.setHeight(size);		
		}
		else if (name.equals("margin"))
		{
			int height = expectInteger();
			int width = height;
			
			symbol = scanner.peakSymbol();
			if (symbol.type == Symbol.Type.INTEGER) width = expectInteger();
			
			widget.setMargin(new Vector2i(width, height));
		}
		else if (name.equals("padding"))
		{
			int height = expectInteger();
			int width = height;
			
			symbol = scanner.peakSymbol();
			if (symbol.type == Symbol.Type.INTEGER) width = expectInteger();
			
			widget.setPadding(new Vector2i(width, height));
		}		
		else if (name.equals("alignment"))
		{			
			int flags = readAlignment();
			widget.setFlag(flags);
		}
		else if (name.equals("shrink-wrap"))
		{
			int flags = 0;
			
			symbol = scanner.getSymbol();
			if (symbol.text.equals("vertical"))
				flags |= Widget.WRAP_VERTICAL;
			else if (symbol.text.equals("horizontal"))
				flags |= Widget.WRAP_HORIZONTAL;
			else if (symbol.text.equals("both"))
				flags |= Widget.WRAP;
			else
			{
				error("Invalid shrink-wrap type: " + symbol.text);
				fastForward(";");
				return;
			}
		
			widget.setFlag(flags);
		}
		else if (name.equals("image"))
		{
			if (widget instanceof ImageWidget)
			{
				ImageWidget imageWidget = (ImageWidget)widget;
				Image image = readImage(theme.getName());
				if (image != null) imageWidget.setImage(image);
			}
			else
			{
				error("This widget is not a Image Widget");
				fastForward(";");
				return;
			}
		}
		else if (name.equals("text-size"))
		{
			if (widget instanceof TextWidget)
			{
				TextWidget textWidget = (TextWidget)widget;
				textWidget.setTextSize(readTextSize());
			}
			else
			{
				error("This widget is not a Text Widget");
				fastForward(";");
				return;
			}
		}
		else if (name.equals("text-colour"))
		{
			if (widget instanceof TextWidget)
			{
				TextWidget textWidget = (TextWidget)widget;
				textWidget.setTextColour(readColour());
			}
			else
			{
				error("This widget is not a Text Widget");
				fastForward(";");
				return;
			}
		}
		else if (name.equals("text-wrap"))
		{
			if (widget instanceof TextWidget)
			{
				String wrap = scanner.getSymbol().text;
				
				TextWidget textWidget = (TextWidget)widget;
				
				if (wrap.equals("wrap"))
					textWidget.setFlowed(true);
				else if (wrap.equals("none"))
					textWidget.setFlowed(false);
				else
				{
					error("Invalid option for text-wrap: " + wrap);
					fastForward(";");
					return;
				}
			}
			else
			{
				error("This widget is not a Text Widget");
				fastForward(";");
				return;
			}
		}
		else if (name.equals("line-numbers"))
		{
			if (widget instanceof TextArea)
			{
				String state = scanner.getSymbol().text;
				TextArea textWidget = (TextArea)widget;
				
				if (state.equals("true"))
					textWidget.setLineNumbers(true);
				else if (state.equals("false"))
					textWidget.setLineNumbers(false);
				else
					error("Invalid option for line-numbers");
			}
			else
			{
				error("This widget is not a text box");
				fastForward(";");
				return;
			}
		}
		else if (name.equals("text"))
		{
			if (widget instanceof TextBox)
			{
				symbol = scanner.getSymbol();
				TextBox textWidget = (TextBox)widget;
				textWidget.setText(symbol.text);
			}
			else
			{
				error("This widget is not a text box");
				fastForward(";");
				return;
			}
		}				
		else if (name.equals("value"))
		{
			if (widget instanceof NumberBox)
			{
				int number = expectInteger();
				NumberBox valueWidget = (NumberBox)widget;
				valueWidget.setValue(number);
			}
			else
			{
				error("This widget is not a number box");
				fastForward(";");
				return;
			}
		}
		else if (name.equals("caption"))
		{
			if (widget instanceof CaptionWidget)
			{
				symbol = scanner.getSymbol();
				CaptionWidget captionWidget = (CaptionWidget)widget;
				captionWidget.setCaption(symbol.text);
			}
			else
			{
				error("This widget does not have a caption");
				fastForward(";");
				return;
			}
		}
		else if (name.equals("icon"))
		{
			if (widget instanceof Button)
			{
				Button button = (Button)widget;
				Image icon = readImage(theme.getName());
				if (icon != null) button.setIcon(icon);
			}
			else
			{
				error("This widget does not have a caption");
				fastForward(";");
				return;
			}
		}	
		else if (name.equals("selected"))
		{
			if (widget instanceof DropDownBox)
			{
				DropDownBox dropdownbox = (DropDownBox)widget;
				int index = expectInteger();
				dropdownbox.setSelected(index);
			}
			else
			{
				error("This widget does not support selecting of an item");
				return;
			}
		}		
		else if (name.equals("items"))
		{
			List<String> items = readStringList();
			
			if (widget instanceof DropDownBox)
			{
				DropDownBox dropdownbox = (DropDownBox)widget;
				for (String item : items) dropdownbox.addItem(item);
			}
			else if (widget instanceof ListWidget)
			{
				ListWidget listWidget = (ListWidget)widget;
				for (String item : items) listWidget.addItem(item);
			}	
			else
			{
				error("This widget does not have a list of items");
				return;
			}
		}
		else
		{
			error("Unknown property: " + name);
			fastForward(";", "}");
			return;
		}
		
		if (!expect(";"))
		{
			fastForward(";", "}");
			return;
		}
	}
	
	private List<String> readStringList()
	{
		List<String> list = new ArrayList<String>();
		
		while (true)
		{
			String item = scanner.getSymbol().text;
			list.add(item);
			
			Symbol symbol = scanner.peakSymbol();
			if (!symbol.text.equals(",")) break;
			scanner.getSymbol();
		}
		
		return list;
	}
	
	private int readAlignment()
	{
		int flags = 0;
		
		Symbol symbol = scanner.getSymbol();
		if (symbol.text.equals("top"))
			flags |= 0;
		else if (symbol.text.equals("center"))
			flags |= Widget.CENTER_VERTICAL;
		else if (symbol.text.equals("fill"))
			flags |= Widget.FILL_VERTICAL;
		else
		{
			error("Unknown vertical alignment: " + symbol.text);	
			fastForward(";");
			return flags;
		}
		
		symbol = scanner.peakSymbol();
		if (!symbol.text.equals(";"))
		{
			symbol = scanner.getSymbol();
			if (symbol.text.equals("left"))
				flags |= 0;
			else if (symbol.text.equals("center"))
				flags |= Widget.CENTER_HORIZONTAL;
			else if (symbol.text.equals("fill"))
				flags |= Widget.FILL_HORIZONTAL;
			else
			{
				error("Unknown horizontal alignment: " + symbol.text);
				fastForward(";");
				return flags;
			}
		}
		else
		{
			if ((flags & Widget.CENTER_VERTICAL) > 0) flags |=  Widget.CENTER_HORIZONTAL;
			if ((flags & Widget.FILL_VERTICAL) > 0) flags |=  Widget.FILL_HORIZONTAL;
		}	
		
		return flags;
	}
}
