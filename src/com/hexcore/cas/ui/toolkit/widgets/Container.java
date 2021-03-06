package com.hexcore.cas.ui.toolkit.widgets;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.ui.toolkit.Colour;
import com.hexcore.cas.ui.toolkit.Event;
import com.hexcore.cas.ui.toolkit.Fill;
import com.hexcore.cas.ui.toolkit.Graphics;
import com.hexcore.cas.utilities.Log;

/*
 * Only contains one component, a decorator in design patterns.
 * 
 * Treats inner component as is. No positioning is done except
 * if the inner component has either a FILL or CENTER flag set.
 * It is recommended that a Layout is used to positions 
 * components and a Container is used to decorate a Layout.
 * 
 */
public class Container extends Widget implements StyledWidget
{
	protected String themeClass = "";
	protected Widget contents = null;
	protected Fill background = null;
	protected Fill border = null;
	
	public Container()
	{
		super(new Vector2i(0, 0));
	}	
	
	public Container(Vector2i size)
	{
		super(size);
	}
	
	public Container(Vector2i position, Vector2i size)
	{
		super(position, size);
	}
	
	@Override
	public Widget findByName(String name)
	{
		if (this.name.equals(name)) return this;
		if (contents != null) return contents.findByName(name);
		return null;
	}
	
	@Override
	public void relayout()
	{
		if (contents == null) return;
		
		if (isSet(Widget.WRAP))
		{
			if (contents.isSet(Widget.WRAP)) contents.relayout();
			if (isSet(Widget.WRAP_HORIZONTAL)) setWidth(contents.getWidth() + margin.x * 2);
			if (isSet(Widget.WRAP_VERTICAL)) setHeight(contents.getHeight() + margin.y * 2);
		}

		Vector2i cPos = contents.getPosition(), cSize = contents.getSize();
		
		if (contents.isSet(FILL_HORIZONTAL))
		{
			cPos.x = contents.getMargin().x; 
			cSize.x = size.x - contents.getMargin().x * 2; 
		}
		else if (contents.isSet(CENTER_HORIZONTAL))
			cPos.x = (size.x - cSize.x) / 2;
		
		if (contents.isSet(FILL_VERTICAL))
		{
			cPos.y = contents.getMargin().y; 
			cSize.y = size.y - contents.getMargin().y * 2; 
		}
		else if (contents.isSet(CENTER_VERTICAL))
			cPos.y = (size.y - cSize.y) / 2;	
		
		contents.relayout();
	}
	
	@Override
	public void update(Vector2i position, float delta)
	{
		if (!visible) return;
		
		super.update(position, delta);
		
		Vector2i pos = this.position.add(position);
		if (contents != null) contents.update(pos, delta);
	}
	
	@Override
	public void render(GL gl, Vector2i position)
	{
		if (!visible) return;
		
		Vector2i pos = this.position.add(position);
		window.addClipRectangle(gl, pos, size);
		
		if (background != null) 
			Graphics.renderRectangle(gl, pos, size, 0, background);
		else if ((window != null) && !themeClass.isEmpty())
			Graphics.renderRectangle(gl, pos, size, 0, window.getTheme().getFill(themeClass, "background", Fill.NONE));

		if (contents != null) contents.render(gl, pos);
		
		if (border != null)
			Graphics.renderBorder(gl, pos, size, 0, border);	
		else if ((window != null) && !themeClass.isEmpty())
			Graphics.renderBorder(gl, pos, size, 0, window.getTheme().getFill(themeClass, "border", Fill.NONE));
		
		window.removeClipRectangle(gl);
		
		if (window.isDebugLayout())
			Graphics.renderBorder(gl, pos, size, new Colour(0.0f, 1.0f, 0.5f));
	}
		
	@Override
	public boolean handleEvent(Event event, Vector2i position)
	{
		if (contents == null) return false;
		
		boolean handled = false;
				
		if (contents.receiveEvent(event, position)) handled = true;
		
		return handled;
	}
	
	public void setThemeClass(String className)
	{
		this.themeClass = "." + className;
	}
	
	public void clearThemeClass()
	{
		this.themeClass = "";
	}
	
	public void setBackground(Fill fill)
	{
		background = fill;
	}
	
	public void setBorder(Fill fill)
	{
		border = fill;
	}	
	
	public void setContents(Widget component) 
	{
		if (component == this)
		{
			Log.error("Container", "Adding container to itself");
			return;
		}
		
		this.contents = component;
		if (component != null) component.setParent(this);
		relayout();
	}
}
