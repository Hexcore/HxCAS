package com.hexcore.cas.ui;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import com.hexcore.cas.math.Recti;
import com.hexcore.cas.math.Vector2i;
import com.jogamp.opengl.util.awt.TextRenderer;

public class Theme
{
	public static class BorderShape 
	{		
		public static final int NONE 	= 0;
		public static final int LEFT 	= 1;
		public static final int TOP 	= 2; 
		public static final int RIGHT 	= 4; 
		public static final int BOTTOM 	= 8; 
		public static final int MIDDLE 	= 16;
		
		public static final int TOP_LEFT 	= 32;
		public static final int TOP_RIGHT 	= 64; 
		public static final int BOTTOM_LEFT = 128; 
		public static final int BOTTOM_RIGHT= 256; 	
		
		public static final int ALL_SIDES	= LEFT | TOP | RIGHT | BOTTOM;
		public static final int ALL_CORNERS	= TOP_LEFT | TOP_RIGHT | BOTTOM_LEFT | BOTTOM_RIGHT;
		
		public int	shape;
		
		public BorderShape()
		{
			this.shape = NONE;
		}
		
		public BorderShape(int shape)
		{
			this.shape = shape;
		}

		public int getNumCorners()
		{
			int corners = 0;
			if ((shape & TOP_LEFT) > 0) corners++;
			if ((shape & TOP_RIGHT) > 0) corners++;
			if ((shape & BOTTOM_LEFT) > 0) corners++;
			if ((shape & BOTTOM_RIGHT) > 0) corners++;
			return corners;
		}
		
		public void add(int flag)
		{
			shape |= flag;
		}
		
		public boolean has(int flag)
		{
			return (shape & flag) > 0;
		}
	};
	
	public enum ButtonState {NORMAL, FOCUS, HOVER, ACTIVE, SELECTED};
	
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

	public Theme()
	{
		textRenderers = new HashMap<Text.Size, TextRenderer>();
		textRenderers.put(Text.Size.HUGE, new TextRenderer(new Font("Helvetica", Font.PLAIN, 36), true, false));
		textRenderers.put(Text.Size.LARGE, new TextRenderer(new Font("Helvetica", Font.PLAIN, 24), true, false));
		textRenderers.put(Text.Size.MEDIUM, new TextRenderer(new Font("Helvetica", Font.PLAIN, 18), true, false));
		textRenderers.put(Text.Size.SMALL, new TextRenderer(new Font("Helvetica", Font.PLAIN, 14), true, false));
		textRenderers.put(Text.Size.TINY, new TextRenderer(new Font("Helvetica", Font.PLAIN, 12), true, false));
		
		typeProperties = new HashMap<String, Type>();
	}
	
	public Theme(String filename)
	{
		this();
		loadFromFile(filename);
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
	
	public Colour getColour(String typeName, String propertyName, Colour fallback)
	{
		return getColour(typeName, "normal", propertyName, fallback);
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
	
	public int getInteger(String typeName, String propertyName)
	{
		return getInteger(typeName, "normal", propertyName, 0);
	}	
	
	public int getInteger(String typeName, String propertyName, int fallback)
	{
		return getInteger(typeName, "normal", propertyName, fallback);
	}	
	
	public int getInteger(String typeName, String state, String propertyName)
	{
		return getInteger(typeName, state, propertyName, 0);
	}		
	
	public int getInteger(String typeName, String state, String propertyName, int fallback)
	{
		Property property = getProperty(typeName, state, propertyName);
		if (property == null) return fallback;
		if (property.type != Property.Type.INTEGER) return fallback;
		
		return property.getInteger();
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
	
		int 		borderRadius = getInteger("Button", stateName, "border-radius", 0);
		BorderShape corners = new BorderShape(BorderShape.ALL_CORNERS);
		
		Graphics.renderRoundedBorderedRectangle(gl, pos, size, borderRadius, corners, 
				getFill("Button", stateName, "background"),
				getFill("Button", stateName, "border"));	 
		
		Colour 		textColour = getColour("Button", stateName, "text-colour");
		Colour 		shadowColour = getColour("Button", stateName, "text-shadow-colour", Colour.TRANSPARENT);
		
		Vector2i	textOffset = getVector2i("Button", stateName, "text-offset");
		Vector2i	shadowOffset = getVector2i("Button", stateName, "text-shadow-offset", new Vector2i(0, 1));

		pos = pos.add(textOffset);
		
		if (description.isEmpty())
			renderShadowedText(gl, caption, pos, size, textColour, shadowColour, shadowOffset, Text.Size.MEDIUM);
		else
		{
			int			textGap = 4;
			Vector2i	bigTextSize = calculateTextSize(caption, Text.Size.MEDIUM);
			Vector2i	smallTextSize = calculateTextSize(description, Text.Size.SMALL);
					
			int		top = (size.y - bigTextSize.y - textGap - smallTextSize.y) / 2;
			int		bigLeft = (size.x - bigTextSize.x) / 2;
			int		smallLeft = (size.x - smallTextSize.x) / 2;
			
			renderShadowedText(gl, caption, pos.add(bigLeft, top), textColour, shadowColour, shadowOffset, Text.Size.MEDIUM);
			renderShadowedText(gl, description, pos.add(smallLeft, top + bigTextSize.y + textGap), textColour, shadowColour, shadowOffset, Text.Size.SMALL);
		}
	}
	
	public void renderPanel(GL gl, Vector2i pos, Vector2i size)
	{
		int borderRadius = getInteger("Panel", "border-radius", 0);
		Graphics.renderRectangle(gl, pos, size, borderRadius, getFill("Panel", "background"));
		Graphics.renderBorder(gl, pos, size, borderRadius, getFill("Panel", "border"));
	}
	
	public int getScrollbarSize()
	{
		return 16;
	}
	
	public void renderVerticalScrollbar(GL gl, Vector2i position, Vector2i size, int value, int max, int viewable)
	{
		int wh = getScrollbarSize();
		Vector2i pos = position.add(size.x - wh, 0);
		
		int	scrollBlockSize = viewable * viewable / max;
		int	scroll = value * (viewable - scrollBlockSize) / (max - viewable);
		
		int borderRadius = getInteger("Scrollbar", "vertical", "border-radius", 0);
		Graphics.renderRectangle(gl, pos, new Vector2i(wh, viewable), borderRadius, getFill("Scrollbar", "vertical", "background"));
		
		int handleBorderRadius = getInteger("ScrollbarHandle", "vertical", "border-radius", 0);
		Graphics.renderRectangle(gl, pos.add(0, scroll), new Vector2i(wh, scrollBlockSize), handleBorderRadius, getFill("ScrollbarHandle", "vertical", "background"));
		Graphics.renderBorder(gl, pos.add(0, scroll), new Vector2i(wh, scrollBlockSize), handleBorderRadius, getFill("ScrollbarHandle", "vertical", "border"));
	}
	
	public void renderHorizontalScrollbar(GL gl, Vector2i position, Vector2i size, int value, int max, int viewable)
	{
		int wh = getScrollbarSize();
		Vector2i pos = position.add(0, size.y - wh);
		
		int	scrollBlockSize = viewable * viewable / max;
		int	scroll = value * (viewable - scrollBlockSize) / (max - viewable);
		
		int borderRadius = getInteger("Scrollbar", "horizontal", "border-radius", 0);
		Graphics.renderRectangle(gl, pos, new Vector2i(viewable, wh), borderRadius, getFill("Scrollbar", "horizontal", "background"));
		
		int handleBorderRadius = getInteger("ScrollbarHandle", "horizontal", "border-radius", 0);
		Graphics.renderRectangle(gl, pos.add(scroll, 0), new Vector2i(scrollBlockSize, wh), handleBorderRadius, getFill("ScrollbarHandle", "horizontal", "background"));
		Graphics.renderBorder(gl, pos.add(scroll, 0), new Vector2i(scrollBlockSize, wh), handleBorderRadius, getFill("ScrollbarHandle", "horizontal", "border"));
	}
	
	public void renderScrollbarFill(GL gl, Vector2i position, Vector2i size)
	{
		int wh = getScrollbarSize();
		Graphics.renderRectangle(gl, position.add(size).subtract(wh, wh), new Vector2i(wh, wh), 0, getFill("ScrollbarFill", "background"));
		Graphics.renderBorder(gl, position.add(size).subtract(wh, wh), new Vector2i(wh, wh), 0, getFill("ScrollbarFill", "border"));
	}
	
	public void renderTextBox(GL gl, Vector2i position, Vector2i size, String text, int cursorIndex, boolean focus, long time)
	{
		String stateName = "normal";
		if (focus) stateName = "focus";
		
		int borderRadius = getInteger("TextBox", stateName, "border-radius", 0);
		Graphics.renderRectangle(gl, position, size, borderRadius, getFill("TextBox", stateName, "background"));
		
		int			textHeight = calculateTextHeight(Text.Size.SMALL);
		Vector2i 	padding = getVector2i("TextBox", stateName, "padding", new Vector2i(3, 3));
		Colour		textColour = getColour("TextBox", stateName, "text-colour", Colour.BLACK);
		
		renderText(gl, text, position.add(padding), textColour, Text.Size.SMALL);
		Graphics.renderBorder(gl, position, size, borderRadius, getFill("TextBox", stateName, "border"));
		
		if (focus && ((time / 500) % 2 == 0))
		{
			int cursorPos = calculateTextSize(text.substring(0, cursorIndex), Text.Size.SMALL).x;
			Graphics.renderRectangle(gl, position.add(cursorPos + 2, padding.y), new Vector2i(1, textHeight), textColour);
		}
	}
	
	public void renderTextArea(GL gl, Vector2i position, Vector2i size, FlowedText text, int cursorIndex, boolean focus, long time)
	{
		String stateName = "normal";
		if (focus) stateName = "focus";
		
		int borderRadius = getInteger("TextBox", stateName, "border-radius", 0);
		Graphics.renderRectangle(gl, position, size, borderRadius, getFill("TextBox", stateName, "background"));
		
		int			textHeight = calculateTextHeight(Text.Size.SMALL);
		Vector2i 	padding = getVector2i("TextBox", stateName, "padding", new Vector2i(3, 3));
		Colour		textColour = getColour("TextBox", stateName, "text-colour", Colour.BLACK);
		
		renderFlowedText(gl, position.add(padding), text, textColour);
		Graphics.renderBorder(gl, position, size, borderRadius, getFill("TextBox", stateName, "border"));
		
		if (focus && ((time / 500) % 2 == 0))
		{
			Vector2i cursorPos = text.getCursorPosition(this, cursorIndex);
			Graphics.renderRectangle(gl, position.add(padding).add(cursorPos), new Vector2i(1, textHeight), textColour);
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
		
		int borderRadius = getInteger("CheckBox", stateName, "border-radius", 0);
		Graphics.renderRectangle(gl, boxPos, boxSize, borderRadius, getFill("CheckBox", stateName, "background"));
		Graphics.renderBorder(gl, boxPos, boxSize, borderRadius, getFill("CheckBox", stateName, "border"));
				
		// Render text
		Vector2i textPadding = getVector2i("CheckBoxCaption", stateName, "padding", new Vector2i(3, 3));		
		Vector2i textSize = calculateTextSize(text, Text.Size.SMALL);
		Vector2i textPos = position.add(padding.x + textPadding.x + boxSize.x, (size.y - textSize.y) / 2);
		
		Vector2i textBoxSize = textSize.add(textPadding).add(textPadding);	
		Vector2i textBoxPos = textPos.subtract(textPadding);	
			
		borderRadius = getInteger("CheckBoxCaption", stateName, "border-radius", 0);
		Graphics.renderRectangle(gl, textBoxPos, textBoxSize, borderRadius, getFill("CheckBoxCaption", stateName, "background"));
		Graphics.renderBorder(gl, textBoxPos, textBoxSize, borderRadius, getFill("CheckBoxCaption", stateName, "border"));
		
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
		
		int 		borderRadius = getInteger("DropDownBox", stateName, "border-radius", 0);
		BorderShape	borderShape = new BorderShape(BorderShape.TOP_LEFT | BorderShape.BOTTOM_LEFT);	
		Graphics.renderRoundedBorderedRectangle(gl, position, boxSize, borderRadius, borderShape,
				getFill("DropDownBox", stateName, "background"), getFill("DropDownBox", stateName, "border"));
		
		Vector2i textSize = calculateTextSize(selectedItem, Text.Size.SMALL);
		Vector2i textPos = position.add(padding.x, (size.y - textSize.y) / 2);
		
		Colour	textColour = getColour("DropDownBox", stateName, "text-colour");
		renderText(gl, selectedItem, textPos, textColour, Text.Size.SMALL);
		
		// Render arrow
		Vector2i arrowPos = position.add(size.x - 16, 0);
		Vector2i arrowSize = new Vector2i(16, size.y);
		
		borderRadius = getInteger("DropDownBoxArrow", stateName, "border-radius", 0);
		Graphics.renderRectangle(gl, arrowPos, arrowSize, borderRadius, getFill("DropDownBoxArrow", stateName, "background"));
		Graphics.renderBorder(gl, arrowPos, arrowSize, borderRadius, getFill("DropDownBoxArrow", stateName, "border"));		
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
		
		int borderRadius = getInteger("DropDownBoxList", stateName, "border-radius", 0);
		Graphics.renderRectangle(gl, boxPos, boxSize, borderRadius, getFill("DropDownBoxList", stateName, "background"));
		Graphics.renderBorder(gl, boxPos, boxSize, borderRadius, getFill("DropDownBoxList", stateName, "border"));
		
		Vector2i	itemPos = new Vector2i(boxPos);
		for (int i = 0; i < items.size(); i++)
		{
			if (hovered == i)
			{
				borderRadius = getInteger("DropDownBoxItem", stateName, "border-radius", 0);
				Graphics.renderRectangle(gl, itemPos, new Vector2i(size.x, textHeight + itemPadding.y * 2), borderRadius, getFill("DropDownBoxItem", stateName, "background"));
				Graphics.renderBorder(gl, boxPos, boxSize, borderRadius, getFill("DropDownBoxItem", stateName, "border"));
			}
			
			itemPos = itemPos.add(0, itemPadding.y);
			renderText(gl, items.get(i), itemPos.add(itemPadding.x, 0), getColour("DropDownBoxItem", stateName, "text-colour"), Text.Size.SMALL);
			itemPos = itemPos.add(0, textHeight + itemPadding.y);
		}
	}
	
	public int getTabHeight()
	{
		Vector2i padding = getVector2i("Tab", "padding", new Vector2i(18, 6));
		return calculateTextHeight(Text.Size.MEDIUM) + padding.y;
	}
	
	public Vector2i	getTabSize(String caption)
	{
		Vector2i padding = getVector2i("Tab", "padding", new Vector2i(18, 6));
		return calculateTextSize(caption, Text.Size.MEDIUM).add(padding);
	}
	
	public void renderTab(GL gl, Vector2i position, String caption, ButtonState state, BorderShape sides)
	{
		String stateName;
		
		switch (state)
		{
			default:
			case NORMAL:
				stateName = "normal";
				break;		
			case HOVER:
				stateName = "hover";
				break;
			case ACTIVE:
				stateName = "active";
				break;
			case SELECTED:
				stateName = "selected";
				break;				
		}
		
		Vector2i size = getTabSize(caption);
				
		int			borderRadius = getInteger("Tab", stateName, "border-radius", 8);
		BorderShape corners = new BorderShape();
		if (sides.has(BorderShape.LEFT)) corners.add(BorderShape.TOP_LEFT | BorderShape.BOTTOM_LEFT);
		if (sides.has(BorderShape.RIGHT)) corners.add(BorderShape.TOP_RIGHT | BorderShape.BOTTOM_RIGHT);
		Graphics.renderRoundedBorderedRectangle(gl, position, size.add(1, 0), borderRadius, corners, 
				getFill("Tab", stateName, "background"), getFill("Tab", stateName, "border"));
		
		Colour	textColour = getColour("Tab", stateName, "text-colour");
		Colour	textShadowColour = getColour("Tab", stateName, "text-shadow-colour", Colour.TRANSPARENT);
		
		Vector2i textOffset = getVector2i("Tab", stateName, "text-offset", new Vector2i(0, 0));
		Vector2i shadowOffset = getVector2i("Tab", stateName, "text-shadow-offset", new Vector2i(0, 1));
		
		renderShadowedText(gl, caption, position.add(textOffset), size, textColour, textShadowColour, shadowOffset, Text.Size.MEDIUM);
	}
	
	public void renderTabInside(GL gl, Vector2i position, Vector2i size)
	{
		int borderRadius = getInteger("TabInside", "border-radius", 0);
		Graphics.renderRectangle(gl, position, size, borderRadius, getFill("TabInside", "background"));
		Graphics.renderBorder(gl, position, size, borderRadius, getFill("TabInside", "border"));
	}
	
	public int calculateTextHeight(Text.Size textSize)
	{
		TextRenderer textRenderer = textRenderers.get(textSize);
		FontRenderContext context = textRenderer.getFontRenderContext();
		Font font = textRenderer.getFont();
		
		LineMetrics metrics = font.getLineMetrics("test", context);
		return (int)(metrics.getAscent() + metrics.getDescent());
	}
	
	public Vector2i calculateTextSize(String text, Text.Size textSize)
	{
		TextRenderer textRenderer = textRenderers.get(textSize);
		FontRenderContext context = textRenderer.getFontRenderContext();
		Font font = textRenderer.getFont();
		
		LineMetrics metrics = font.getLineMetrics(text, context);
		return new Vector2i((int)textRenderer.getBounds(text).getMaxX(), (int)(metrics.getAscent() + metrics.getDescent()));
	}

	public void renderShadowedText(GL gl, String text, Vector2i position, Colour colour, Colour shadowColour, Vector2i shadowOffset, Text.Size textSize)
	{
		if (shadowColour.a > 0.0f) renderText(gl, text, position.add(shadowOffset), shadowColour, textSize);
		renderText(gl, text, position, colour, textSize);
	}
	
	public void renderText(GL gl, String text, Vector2i position, Colour colour, Text.Size textSize)
	{
		TextRenderer textRenderer = textRenderers.get(textSize);
		FontRenderContext context = textRenderer.getFontRenderContext();
		Font font = textRenderer.getFont();
		
		LineMetrics metrics = font.getLineMetrics(text, context);
		
		textRenderer.setColor(colour.r, colour.g, colour.b, colour.a);
				
		GL2 gl2 = gl.getGL2();
		gl2.glPushMatrix();
		gl2.glTranslatef(position.x, position.y + (int)metrics.getAscent(), 0.0f);
		gl2.glScalef(1.0f, -1.0f, 1.0f);
		
		textRenderer.begin3DRendering();
		textRenderer.draw(text, 0, 0);
		textRenderer.end3DRendering();
		
		gl2.glPopMatrix();
	}
	
	public FlowedText flowText(String text, int maxWidth, Text.Size textSize)
	{
		TextRenderer 		textRenderer = textRenderers.get(textSize);
		FontRenderContext 	context = textRenderer.getFontRenderContext();
		Font 				font = textRenderer.getFont();
		
		LineMetrics 	metrics = font.getLineMetrics(text, context);	
		int 			lineHeight = (int)metrics.getHeight();
		List<String>	lines = new LinkedList<String>();
		String			line = "";
		String			word = "";
		
		for (int c = 0; c <= text.length(); c++)
		{
			char 	character = (c == text.length()) ? ' ' : text.charAt(c);
			
			if (Character.isWhitespace(character))
			{
				int 	lineWidth = (int)textRenderer.getBounds(line + word).getMaxX();
				word += character;
				
				if ((lineWidth > maxWidth) && (maxWidth >= 0))
				{
					lines.add(line);
					line = word;
				}
			 	else
					line += word;
				
				if ((character == '\n') && !line.isEmpty())
				{
					lines.add(line);
					line = "";
				}
				
				word = "";
			}
			else
				word += character;
		}
		
		if (line.length() > 0) lines.add(line);
		
		FlowedText flowedText = new FlowedText();
		flowedText.lines = lines;
		flowedText.size = new Vector2i(maxWidth, lines.size() * lineHeight);
		flowedText.lineHeight = lineHeight;
		flowedText.textSize = textSize;
		return flowedText;
		
	}
	
	public void renderFlowedShadowedText(GL gl, Vector2i position, FlowedText flowedText, Colour colour, Colour shadowColour, Vector2i shadowOffset)
	{
		Vector2i	pos = new Vector2i(position);
		for (String line : flowedText.lines)
		{
			renderShadowedText(gl, line, pos, colour, shadowColour, shadowOffset, flowedText.textSize);
			pos.inc(0, flowedText.lineHeight);
		}
	}	
	
	public void renderFlowedText(GL gl, Vector2i position, FlowedText flowedText, Colour colour)
	{
		Vector2i	pos = new Vector2i(position);
		for (String line : flowedText.lines)
		{
			renderText(gl, line, pos, colour, flowedText.textSize);
			pos.inc(0, flowedText.lineHeight);
		}
	}	
	
	public void renderFlowedText(GL gl, String text, Vector2i position, int maxWidth, Colour colour, Text.Size textSize)
	{
		renderFlowedText(gl, position, flowText(text, maxWidth, textSize), colour);
	}
	
	public void renderShadowedText(GL gl, String text, Vector2i position, Vector2i size, Colour colour, Colour shadowColour, Vector2i shadowOffset, Text.Size textSize)
	{
		if (shadowColour.a > 0.0f) renderText(gl, text, position.add(shadowOffset), size, shadowColour, textSize);
		renderText(gl, text, position, size, colour, textSize);
	}
	
	public void renderText(GL gl, String text, Vector2i position, Vector2i size, Colour colour, Text.Size textSize)
	{
		TextRenderer textRenderer = textRenderers.get(textSize);
		FontRenderContext context = textRenderer.getFontRenderContext();
		Font font = textRenderer.getFont();
		
		LineMetrics metrics = font.getLineMetrics(text, context);
		int			textHeight = (int)(metrics.getAscent() + metrics.getDescent());
	
		textRenderer.setColor(colour.r, colour.g, colour.b, colour.a);
				
		Rectangle2D	bounds = textRenderer.getBounds(text);
		Vector2i textBounds = new Vector2i((int)bounds.getMaxX(), -(int)bounds.getMinY());
		Vector2i pos = position.add(0, -(int)bounds.getMinY());
		
		GL2 gl2 = gl.getGL2();
		gl2.glPushMatrix();
		gl2.glTranslatef(pos.x + (int)((size.x - textBounds.x) * 0.5f), pos.y + (int)((size.y - textHeight) * 0.5f) + 1, 0.0f);
		gl2.glScalef(1.0f, -1.0f, 1.0f);
		
		textRenderer.begin3DRendering();
		textRenderer.draw(text, 0, 0);
		textRenderer.end3DRendering();
		
		gl2.glPopMatrix();
	}
}
