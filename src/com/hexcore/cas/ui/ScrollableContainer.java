package com.hexcore.cas.ui;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;

public class ScrollableContainer extends Container
{	
	private	Vector2i 	maxSize;
	private Vector2i	viewSize;
	private Vector2i	scroll;
	private boolean		verticalScrollbar;
	private boolean		horizontalScrollbar;
	
	public ScrollableContainer(Vector2i size)
	{
		super(size);
		scroll = new Vector2i();
		viewSize = new Vector2i(size);
		maxSize = new Vector2i(size);
	}
	
	public ScrollableContainer(Vector2i position, Vector2i size)
	{
		super(position, size);
		scroll = new Vector2i();
		viewSize = new Vector2i(size);
		maxSize = new Vector2i(size);
	}
	
	public int		getInnerWidth() {return viewSize.x;}
	public int		getInnerHeight() {return viewSize.y;}
	public Vector2i	getInnerSize() {return viewSize;}

	@Override
	public void relayout()
	{
		if (contents == null) return;
		
		Vector2i cPos = contents.getPosition(), cSize = contents.getSize();
		Vector2i cmSize = cSize.add(contents.getMargin()).add(contents.getMargin());
		
		verticalScrollbar = horizontalScrollbar = false;

		maxSize.x = size.x;
		maxSize.y = size.y;
		viewSize.x = size.x;
		viewSize.y = size.y;
		
		if ((cmSize.x > size.x) && !contents.isSet(FILL_HORIZONTAL))
			maxSize.x = cmSize.x;
		
		if ((cmSize.y > size.y) && !contents.isSet(FILL_VERTICAL))
			maxSize.y = cmSize.y;
		
		if (maxSize.x > viewSize.x) 
		{
			horizontalScrollbar = true;
			viewSize.y = size.y - 16;
		}
		
		if (maxSize.y > viewSize.y) 
		{
			verticalScrollbar = true;
			viewSize.x = size.x - 16;
			
			if (maxSize.x > viewSize.x) 
			{
				horizontalScrollbar = true;
				viewSize.y = size.y - 16;
			}
		}
		
		if (contents.isSet(FILL_HORIZONTAL))
		{
			cPos.x = contents.getMargin().x; 
			cSize.x = maxSize.x - contents.getMargin().x * 2; 
		}
		else if (contents.isSet(CENTER_HORIZONTAL))
			cPos.x = (maxSize.x - cSize.x) / 2;
		else if (cPos.x < contents.getMargin().x)
			cPos.x = contents.getMargin().x;
		
		if (contents.isSet(FILL_VERTICAL))
		{
			cPos.y = contents.getMargin().y; 
			cSize.y = maxSize.y - contents.getMargin().y * 2; 
		}
		else if (contents.isSet(CENTER_VERTICAL))
			cPos.y = (maxSize.y - cSize.y) / 2;
		else if (cPos.y < contents.getMargin().y)
			cPos.y = contents.getMargin().y;
		
		contents.relayout();
	}
	
	@Override
	public void render(GL gl, Vector2i position)
	{
		if (!visible) return;
		
		Vector2i pos = this.position.add(position);	
		
		if (contents != null)
		{
			window.setClipping(gl, pos, size);
			contents.render(gl, pos.subtract(scroll));
			window.resetClipping(gl);
			
			if (verticalScrollbar)
				window.getTheme().renderVerticalScrollbar(gl, pos, size, scroll.y, maxSize.y, viewSize.y);

			if (horizontalScrollbar) 
				window.getTheme().renderHorizontalScrollbar(gl, pos, size, scroll.x, maxSize.x, viewSize.x);
			
			if (verticalScrollbar && horizontalScrollbar)
				window.getTheme().renderScrollbarFill(gl, pos, size);
		}
	}
	
	@Override
	public boolean handleEvent(Event event, Vector2i position)
	{
		if (super.handleEvent(event, position)) return true;

		if (event.type == Event.Type.MOUSE_SCROLL)
		{
			scroll.y += event.amount;
			if (scroll.y < 0) scroll.y = 0;
			if (scroll.y >= maxSize.y - viewSize.y) scroll.y = maxSize.y - viewSize.y;
			return true;
		}
		else if (event.type == Event.Type.MOUSE_CLICK)
		{
			
		}
		
		return false;
	}
}
