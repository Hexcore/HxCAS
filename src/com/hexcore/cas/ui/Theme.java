package com.hexcore.cas.ui;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import com.hexcore.cas.math.Recti;
import com.hexcore.cas.math.Vector2i;
import com.jogamp.opengl.util.awt.TextRenderer;

public class Theme
{
	public enum ButtonState {NORMAL, FOCUS, HOVER, ACTIVE};
	
	public static class Property
	{
		public enum Type {STRING, FILL, INTEGER, POINT};
		
		Property(String name)
		{
			this.name = name;
			this.type = Type.STRING;
		}

		int			getInteger() {return x;}
		Vector2i	getVector2i() {return new Vector2i(x, y);}
		
		String	name;
		Type	type;
		
		String	value;
		Fill	fill;
		int		x, y;
	}
	
	public static class Type
	{
		Type(String name)
		{
			this.name = name;
			this.properties = new HashMap<String, Property>();
		}
		
		void add(Property property)
		{
			properties.put(property.name, property);
		}
		
		String		state;
		String		name;
		
		HashMap<String, Property>	properties;
	}
	
	private HashMap<String, Type> 				typeProperties;
	private HashMap<Text.Size, TextRenderer>	textRenderers;
	private	Window								window;

	public Theme(Window window)
	{
		this.window = window;
		
		textRenderers = new HashMap<Text.Size, TextRenderer>();
		textRenderers.put(Text.Size.HUGE, new TextRenderer(new Font("Arial", Font.BOLD, 36), true, false));
		textRenderers.put(Text.Size.LARGE, new TextRenderer(new Font("Arial", Font.BOLD, 24), true, false));
		textRenderers.put(Text.Size.MEDIUM, new TextRenderer(new Font("Arial", Font.PLAIN, 18), true, false));
		textRenderers.put(Text.Size.SMALL, new TextRenderer(new Font("Serif", Font.PLAIN, 12), true, false));
		textRenderers.put(Text.Size.TINY, new TextRenderer(new Font("Serif", Font.PLAIN, 10), true, false));
		
		typeProperties = new HashMap<String, Type>();
	}
	
	public Property getProperty(String typeName, String propertyName)
	{
		return getProperty(typeName, "normal", propertyName);
	}
	
	public Property getProperty(String typeName, String state, String propertyName)
	{
		String typeState = typeName + ":" + state;
		
		Type type = typeProperties.get(typeState);
		if (type == null) return state.equals("normal") ? null : getProperty(typeName, "normal", propertyName);
		
		Property property = type.properties.get(propertyName);
		if (property == null) return state.equals("normal") ? null : getProperty(typeName, "normal", propertyName);
		
		return property;
	}	
	
	public Colour getColour(String typeName, String propertyName)
	{
		return getColour(typeName, "normal", propertyName);
	}
	
	public Colour getColour(String typeName, String state, String propertyName)
	{
		Property property = getProperty(typeName, state, propertyName);
		if (property == null) return Colour.BLACK;
		if (property.type != Property.Type.FILL) return Colour.BLACK;
		
		return property.fill.getColour(0);
	}
	
	public Colour getColour(String typeName, String state, String propertyName, Colour fallback)
	{
		Property property = getProperty(typeName, state, propertyName);
		if (property == null) return fallback;
		if (property.type != Property.Type.FILL) return fallback;
		if (property.fill.getType() == Fill.Type.NONE) return fallback;
		return property.fill.getColour(0);
	}
	
	public Fill getFill(String typeName, String propertyName)
	{
		return getFill(typeName, "normal", propertyName, Fill.NONE);
	}
	
	public Fill getFill(String typeName, String propertyName, Fill fallback)
	{
		return getFill(typeName, "normal", propertyName, fallback);
	}
	
	public Fill getFill(String typeName, String state, String propertyName)
	{
		return getFill(typeName, state, propertyName, Fill.NONE);
	}
	
	public Fill getFill(String typeName, String state, String propertyName, Fill fallback)
	{
		Property property = getProperty(typeName, state, propertyName);
		if (property == null) return fallback;
		if (property.type != Property.Type.FILL) return fallback;
		return property.fill;
	}	
	
	public Vector2i getVector2i(String typeName, String propertyName)
	{
		return getVector2i(typeName, "normal", propertyName, new Vector2i(0, 0));
	}	
	
	public Vector2i getVector2i(String typeName, String propertyName, Vector2i fallback)
	{
		return getVector2i(typeName, "normal", propertyName, fallback);
	}	
	
	public Vector2i getVector2i(String typeName, String state, String propertyName)
	{
		return getVector2i(typeName, state, propertyName, new Vector2i(0, 0));
	}		
	
	public Vector2i getVector2i(String typeName, String state, String propertyName, Vector2i fallback)
	{
		Property property = getProperty(typeName, state, propertyName);
		if (property == null) return fallback;
		if (property.type != Property.Type.POINT) return fallback;
		
		return property.getVector2i();
	}
	
	public void loadFromFile(String file)
	{
		ThemeParser themeParser = new ThemeParser(file);
		typeProperties = themeParser.getTypes();		
	}
	
	public void renderButton(GL gl, Vector2i pos, Vector2i size, ButtonState state, String caption, String description)
	{
		String stateName;
		
		switch (state)
		{
			default:
			case NORMAL:
				stateName = "normal";
				break;
			case FOCUS:
				stateName = "focus";
				break;				
			case HOVER:
				stateName = "hover";
				break;
			case ACTIVE:
				stateName = "active";
				break;
		}
		
		window.renderRectangle(gl, pos, size, getFill("Button", stateName, "background"));
		window.renderBorder(gl, pos, size, getFill("Button", stateName, "border"));	 
		
		pos = pos.add(getVector2i("Button", stateName, "text-offset"));
		
		Colour textColour = getColour("Button", stateName, "text-colour");
		
		if (description.isEmpty())
			window.getTheme().renderText(gl, caption, pos, size, textColour, Text.Size.MEDIUM);
		else
		{
			int			textGap = 4;
			Vector2i	bigTextSize = window.getTheme().calculateTextSize(caption, Text.Size.MEDIUM);
			Vector2i	smallTextSize = window.getTheme().calculateTextSize(description, Text.Size.SMALL);
					
			int		top = (size.y - bigTextSize.y - textGap - smallTextSize.y) / 2;
			int		bigLeft = (size.x - bigTextSize.x) / 2;
			int		smallLeft = (size.x - smallTextSize.x) / 2;
			
			window.getTheme().renderText(gl, caption, pos.add(bigLeft, top), textColour, Text.Size.MEDIUM);
			window.getTheme().renderText(gl, description, pos.add(smallLeft, top + bigTextSize.y + textGap), textColour, Text.Size.SMALL);
		}
	}
	
	public void renderPanel(GL gl, Vector2i pos, Vector2i size)
	{
		window.renderRectangle(gl, pos, size, getFill("Panel", "background"));
		window.renderBorder(gl, pos, size, getFill("Panel", "border"));
	}
	
	public void renderVerticalScrollbar(GL gl, Vector2i position, Vector2i size, int value, int max, int viewable)
	{
		Vector2i pos = position.add(size.x - 16, 0);
		
		int	scroll = value * (viewable - 16) / (max - viewable);
		
		window.renderRectangle(gl, pos, new Vector2i(16, viewable), getFill("Scrollbar", "vertical", "background"));
		window.renderRectangle(gl, pos.add(0, scroll), new Vector2i(16, 16), getFill("ScrollbarHandle", "vertical", "background"));
		window.renderBorder(gl, pos.add(0, scroll), new Vector2i(16, 16), getFill("ScrollbarHandle", "vertical", "border"));
	}
	
	public void renderHorizontalScrollbar(GL gl, Vector2i position, Vector2i size, int value, int max, int viewable)
	{
		Vector2i pos = position.add(0, size.y - 16);
		
		int	scroll = value * (viewable - 16) / (max - viewable);
		
		window.renderRectangle(gl, pos, new Vector2i(viewable, 16), getFill("Scrollbar", "horizontal", "background"));
		window.renderRectangle(gl, pos.add(scroll, 0), new Vector2i(16, 16), getFill("ScrollbarHandle", "horizontal", "background"));
		window.renderBorder(gl, pos.add(scroll, 0), new Vector2i(16, 16), getFill("ScrollbarHandle", "horizontal", "border"));
	}
	
	public void renderScrollbarFill(GL gl, Vector2i position, Vector2i size)
	{
		window.renderRectangle(gl, position.add(size).subtract(16, 16), new Vector2i(16, 16), getFill("ScrollbarFill", "background"));
		window.renderBorder(gl, position.add(size).subtract(16, 16), new Vector2i(16, 16), getFill("ScrollbarFill", "border"));
	}
	
	public void renderTextBox(GL gl, Vector2i position, Vector2i size, String text, boolean focus)
	{
		String stateName = "normal";
		if (focus) stateName = "focus";
		
		window.renderRectangle(gl, position, size, getFill("TextBox", stateName, "background"));
		window.renderBorder(gl, position, size, getFill("TextBox", stateName, "border"));
		
		Vector2i 	textSize = window.getTheme().calculateTextSize(text, Text.Size.SMALL);
		Vector2i 	padding = getVector2i("TextBox", stateName, "padding", new Vector2i(3, 3));
		Colour		textColour = getColour("TextBox", stateName, "text-colour", Colour.BLACK);
		
		renderText(gl, text, position.add(padding.x, (size.y - textSize.y) / 2), textColour, Text.Size.SMALL);
		
		if (focus && ((window.getTime() / 500) % 2 == 0))
		{
			window.renderRectangle(gl, position.add(textSize.x + padding.x, padding.y), new Vector2i(padding.x, size.y - padding.y * 2), textColour);
		}
	}
	
	public void renderCheckBox(GL gl, Vector2i position, Vector2i size, String text, boolean focus, boolean checked)
	{
		String stateName = "normal";
		if (checked && focus) 
			stateName = "checked:focus";	
		else if (checked)
			stateName = "checked";
		else if (focus) 
			stateName = "focus";
		
		// Render box
		Vector2i padding = getVector2i("CheckBox", stateName, "padding", new Vector2i(3, 3));				
		Vector2i boxSize = new Vector2i(16, 16);
		Vector2i boxPos = position.add(0, (size.y - 16) / 2);
		
		window.renderRectangle(gl, boxPos, boxSize, getFill("CheckBox", stateName, "background"));
		window.renderBorder(gl, boxPos, boxSize, getFill("CheckBox", stateName, "border"));
				
		// Render text
		Vector2i textPadding = getVector2i("CheckBoxCaption", stateName, "padding", new Vector2i(3, 3));		
		Vector2i textSize = window.getTheme().calculateTextSize(text, Text.Size.SMALL);
		Vector2i textPos = position.add(padding.x + textPadding.x + boxSize.x, (size.y - textSize.y) / 2);
		
		Vector2i textBoxSize = textSize.add(textPadding).add(textPadding);	
		Vector2i textBoxPos = textPos.subtract(textPadding);	
			
		window.renderRectangle(gl, textBoxPos, textBoxSize, getFill("CheckBoxCaption", stateName, "background"));
		window.renderBorder(gl, textBoxPos, textBoxSize, getFill("CheckBoxCaption", stateName, "border"));
		
		Colour	textColour = getColour("CheckBoxCaption", stateName, "text-colour");
		renderText(gl, text, textPos, textColour, Text.Size.SMALL);
	}
	
	public void renderDropDownBox(GL gl, Vector2i position, Vector2i size, String selectedItem, boolean focus)
	{
		String stateName = "normal";
		if (focus) stateName = "focus";
		
		// Render box
		Vector2i padding = getVector2i("DropDownBox", stateName, "padding", new Vector2i(3, 3));	
		Vector2i boxSize = size.subtract(16, 0);
		
		window.renderRectangle(gl, position, boxSize, getFill("DropDownBox", stateName, "background"));
		window.renderBorder(gl, position, boxSize, getFill("DropDownBox", stateName, "border"));
		
		Vector2i textSize = window.getTheme().calculateTextSize(selectedItem, Text.Size.SMALL);
		Vector2i textPos = position.add(padding.x, (size.y - textSize.y) / 2);
		
		Colour	textColour = getColour("DropDownBox", stateName, "text-colour");
		renderText(gl, selectedItem, textPos, textColour, Text.Size.SMALL);
		
		// Render arrow
		Vector2i arrowPos = position.add(size.x - 16, 0);
		Vector2i arrowSize = new Vector2i(16, size.y);
		
		window.renderRectangle(gl, arrowPos, arrowSize, getFill("DropDownBoxArrow", stateName, "background"));
		window.renderBorder(gl, arrowPos, arrowSize, getFill("DropDownBoxArrow", stateName, "border"));		
	}
	
	public Recti getDropDownBoxItemRect(Vector2i position, Vector2i size, int selected)
	{
		String stateName = "focus";
		
		Vector2i	itemPadding = getVector2i("DropDownBoxItem", stateName, "padding", new Vector2i(3, 3));
		int			textHeight = calculateTextHeight(Text.Size.SMALL);
		Vector2i	itemPos = position.add(0, size.y + (textHeight + itemPadding.y * 2) * selected);
		Vector2i	itemSize = new Vector2i(size.x, textHeight + itemPadding.y * 2);
		
		return new Recti(itemPos, itemSize);
	}
	
	public void renderDropDownBoxList(GL gl, Vector2i position, Vector2i size, List<String> items, int selected, int hovered)
	{
		String stateName = "focus";
		
		Vector2i	itemPadding = getVector2i("DropDownBoxItem", stateName, "padding", new Vector2i(3, 3));
		int			textHeight = calculateTextHeight(Text.Size.SMALL);
		Vector2i	boxPos = position.add(0, size.y);
		Vector2i	boxSize = new Vector2i(size.x, (textHeight + itemPadding.y * 2) * items.size());
		
		window.renderRectangle(gl, boxPos, boxSize, getFill("DropDownBoxList", stateName, "background"));
		window.renderBorder(gl, boxPos, boxSize, getFill("DropDownBoxList", stateName, "border"));
		
		Vector2i	itemPos = new Vector2i(boxPos);
		for (int i = 0; i < items.size(); i++)
		{
			if (hovered == i)
			{
				window.renderRectangle(gl, itemPos, new Vector2i(size.x, textHeight + itemPadding.y * 2), getFill("DropDownBoxItem", stateName, "background"));
				window.renderBorder(gl, boxPos, boxSize, getFill("DropDownBoxItem", stateName, "border"));
			}
			
			itemPos = itemPos.add(0, itemPadding.y);
			renderText(gl, items.get(i), itemPos.add(itemPadding.x, 0), getColour("DropDownBoxItem", stateName, "text-colour"), Text.Size.SMALL);
			itemPos = itemPos.add(0, textHeight + itemPadding.y);
		}
	}
	
	public int calculateTextHeight(Text.Size textSize)
	{
		TextRenderer textRenderer = textRenderers.get(textSize);
		FontRenderContext context = textRenderer.getFontRenderContext();
		Font font = textRenderer.getFont();
		return (int)Math.ceil(font.getMaxCharBounds(context).getHeight());
	}
	
	public Vector2i calculateTextSize(String text, Text.Size textSize)
	{
		TextRenderer textRenderer = textRenderers.get(textSize);
		Rectangle2D	bounds = textRenderer.getBounds(text);
		return new Vector2i((int)bounds.getMaxX(), -(int)bounds.getMinY());
	}
	
	public void renderText(GL gl, String text, Vector2i position, Colour colour, Text.Size textSize)
	{
		TextRenderer textRenderer = textRenderers.get(textSize);
		
		textRenderer.setColor(colour.r, colour.g, colour.b, colour.a);
		
		Rectangle2D	bounds = textRenderer.getBounds(text);
		
		GL2 gl2 = gl.getGL2();
		gl2.glPushMatrix();
		gl2.glTranslatef(position.x, position.y - (float)bounds.getMinY(), 0.0f);
		gl2.glScalef(1.0f, -1.0f, 1.0f);
		
		textRenderer.begin3DRendering();
		textRenderer.draw(text, 0, 0);
		textRenderer.end3DRendering();
		
		gl2.glPopMatrix();
	}
	
	public void renderText(GL gl, String text, Vector2i position, Vector2i size, Colour colour, Text.Size textSize)
	{
		TextRenderer textRenderer = textRenderers.get(textSize);
		
		textRenderer.setColor(colour.r, colour.g, colour.b, colour.a);
		
		Rectangle2D	bounds = textRenderer.getBounds(text);
		Vector2i textBounds = new Vector2i((int)bounds.getMaxX(), -(int)bounds.getMinY());
		Vector2i pos = position.add(0, -(int)bounds.getMinY());
		
		GL2 gl2 = gl.getGL2();
		gl2.glPushMatrix();
		gl2.glTranslatef(pos.x + (int)((size.x - textBounds.x) * 0.5f), pos.y + (int)((size.y - textBounds.y) * 0.5f), 0.0f);
		gl2.glScalef(1.0f, -1.0f, 1.0f);
		
		textRenderer.begin3DRendering();
		textRenderer.draw(text, 0, 0);
		textRenderer.end3DRendering();
		
		gl2.glPopMatrix();
	}
}
