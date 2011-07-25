package com.hexcore.cas.ui;

import java.util.TreeMap;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;

public class TabbedView extends View
{
	protected TreeMap<Integer, String>	captions;
	
	public TabbedView(Vector2i size)
	{
		super(size);
		captions = new TreeMap<Integer, String>();
	}

	public TabbedView(Vector2i position, Vector2i size)
	{
		super(position, size);
		captions = new TreeMap<Integer, String>();
	}
	
	@Override
	public Vector2i getInnerSize()
	{
		return super.getInnerSize().subtract(0, window.getTheme().getTabHeight());
	}
	
	@Override
	public void add(Widget widget)
	{
		captions.put(widgets.size(), "Tab");
		super.add(widget);
	}	
	
	public void add(Widget widget, String name)
	{
		captions.put(widgets.size(), name);
		super.add(widget);
	}
	
	@Override
	public void remove(Widget widget)
	{
		int index = widgets.indexOf(widget);
		captions.remove(index);
		widgets.remove(index);
	}
	
	@Override
	public void render(GL gl, Vector2i position)
	{
		if (!visible) return;
		
		Vector2i pos = this.position.add(position);

		//window.renderBorder(gl, pos, size, new Colour(1.0f, 0.5f, 0.0f));
		
		int	x = 0;
		for (int i = 0; i < widgets.size(); i++)
		{
			String caption = captions.get(i);
			Vector2i tabSize = window.getTheme().getTabSize(caption);
			window.getTheme().renderTab(gl, pos.add(x, 0), caption, i == getIndex());
			x += tabSize.x;
		}
		
		Vector2i innerSize = getInnerSize();
		pos.inc(0, window.getTheme().getTabHeight());
		
		window.setClipping(gl, pos, innerSize);
		
		if (background == null) 
			window.getTheme().renderTabInside(gl, pos, innerSize);
		else
			window.renderRectangle(gl, pos, innerSize, 0, background);
		
		
		Widget contents = getWidget();
		if (contents != null) contents.render(gl, pos);
		window.resetClipping(gl);
		
		//window.renderBorder(gl, pos, innerSize, new Colour(0.0f, 0.5f, 1.0f));
	}

	@Override
	public boolean handleEvent(Event event, Vector2i position)
	{
		boolean handled = false;
		
		if ((event.type == Event.Type.MOUSE_CLICK) && !event.pressed)
		{
			if ((event.position.x >= position.x) && (event.position.y >= position.y) && (event.position.y <= position.y + window.getTheme().getTabHeight()))
			{
				int	x = position.x;
				for (int i = 0; i < widgets.size(); i++)
				{
					Vector2i tabSize = window.getTheme().getTabSize(captions.get(i));
					x += tabSize.x;
					if (event.position.x <= x)
					{
						setIndex(i);
						break;
					}
				}
				handled = true;
			}
		}
		
		if (!handled && super.handleEvent(event, position)) 
			handled = true;
		
		return handled;
	}
}
