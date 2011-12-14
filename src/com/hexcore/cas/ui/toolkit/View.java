package com.hexcore.cas.ui.toolkit;

import java.util.ArrayList;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;

public class View extends Widget
{
	protected int				currentWidget = 0;
	protected ArrayList<Widget>	widgets;
	
	protected Fill 	background = null;
	
	public View()
	{
		this(new Vector2i(0, 0));
	}	
	
	public View(Vector2i size)
	{
		super(size);
		widgets = new ArrayList<Widget>();
	}
	
	public View(Vector2i position, Vector2i size)
	{
		super(position, size);
		widgets = new ArrayList<Widget>();
	}
	
	@Override
	public Widget findByName(String name)
	{
		if (this.name.equals(name)) return this;
		
		for (Widget w : widgets)
		{
			Widget widget = w.findByName(name);
			if (widget != null) return widget;
		}
		
		return null;
	}
	
	@Override
	public void relayout()
	{
		Widget contents = getWidget();
		if (contents == null) return;

		Vector2i cPos = contents.getPosition(), cSize = contents.getSize();
		Vector2i innerSize = getInnerSize();
		
		if (contents.isSet(FILL_HORIZONTAL))
		{
			cPos.x = contents.getMargin().x; 
			cSize.x = innerSize.x - contents.getMargin().x * 2; 
		}
		else if (contents.isSet(CENTER_HORIZONTAL))
			cPos.x = (innerSize.x - cSize.x) / 2;
		
		if (contents.isSet(FILL_VERTICAL))
		{
			cPos.y = contents.getMargin().y; 
			cSize.y = innerSize.y - contents.getMargin().y * 2; 
		}
		else if (contents.isSet(CENTER_VERTICAL))
			cPos.y = (innerSize.y - cSize.y) / 2;	
		
		contents.relayout();
	}
	
	@Override
	public void update(Vector2i position, float delta)
	{
		if (!visible) return;
		
		super.update(position, delta);
		
		Vector2i pos = this.position.add(position);
		Widget contents = getWidget();
		if (contents != null) contents.update(pos, delta);
	}
	
	@Override
	public void render(GL gl, Vector2i position)
	{
		if (!visible) return;
		
		Vector2i pos = this.position.add(position);
		
		window.addClipRectangle(gl, pos, size);
		if (background != null) Graphics.renderRectangle(gl, pos, size, 0, background);
		Widget contents = getWidget();
		if (contents != null) contents.render(gl, pos);
		window.removeClipRectangle(gl);
		
		//window.renderBorder(gl, pos, size, new Colour(0.0f, 0.5f, 1.0f));
	}
	
	@Override
	public boolean handleEvent(Event event, Vector2i position)
	{
		Widget contents = getWidget();
		if (contents == null) return false;
		
		boolean handled = false;
				
		if (contents.receiveEvent(event, position)) handled = true;
		
		return handled;
	}
	
	public void setBackground(Fill fill)
	{
		background = fill;
	}
	
	public Widget getWidget()
	{
		if ((currentWidget < 0) || (currentWidget >= widgets.size())) return null;
		return widgets.get(currentWidget);
	}
	
	public int getIndex()
	{
		return currentWidget;
	}	
	
	public void setIndex(int index)
	{
		currentWidget = index;
		relayout();
	}
	
	public void add(Widget widget) 
	{
		widgets.add(widget);
		widget.setParent(this);
	}
	
	public void remove(Widget widget)
	{
		widgets.remove(widget);
	}
}
