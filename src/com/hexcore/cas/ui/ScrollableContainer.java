package com.hexcore.cas.ui;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;

public class ScrollableContainer extends Container
{	
	enum DragState {NONE, VERTICAL, HORIZONTAL};
	
	private	Vector2i 	maxSize;
	private Vector2i	viewSize;
	private Vector2i	scrollPos;
	private boolean		verticalScrollbar;
	private boolean		horizontalScrollbar;
	private DragState	dragState = DragState.NONE;
	private Vector2i	dragPrev;
	
	public ScrollableContainer(Vector2i size)
	{
		super(size);
		scrollPos = new Vector2i();
		dragPrev = new Vector2i();
		viewSize = new Vector2i(size);
		maxSize = new Vector2i(size);
	}
	
	public ScrollableContainer(Vector2i position, Vector2i size)
	{
		super(position, size);
		scrollPos = new Vector2i();
		dragPrev = new Vector2i();
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
			viewSize.y = size.y - window.getTheme().getScrollbarSize();
		}
		
		if (maxSize.y > viewSize.y) 
		{
			verticalScrollbar = true;
			viewSize.x = size.x - window.getTheme().getScrollbarSize();
			
			if (maxSize.x > viewSize.x) 
			{
				horizontalScrollbar = true;
				viewSize.y = size.y - window.getTheme().getScrollbarSize();
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
			if (background != null) window.renderRectangle(gl, pos, size, 0, background);
			contents.render(gl, pos.subtract(scrollPos));
			window.resetView(gl);
			
			if (verticalScrollbar)
				window.getTheme().renderVerticalScrollbar(gl, pos, size, scrollPos.y, maxSize.y, viewSize.y);

			if (horizontalScrollbar) 
				window.getTheme().renderHorizontalScrollbar(gl, pos, size, scrollPos.x, maxSize.x, viewSize.x);
			
			if (verticalScrollbar && horizontalScrollbar)
				window.getTheme().renderScrollbarFill(gl, pos, size);
		}
	}
	
	@Override
	public boolean handleEvent(Event event, Vector2i position)
	{
		boolean handled = false;
		
		if (event.type == Event.Type.MOUSE_MOTION)
		{
			if (dragState == DragState.VERTICAL)
			{
				scroll(0, event.position.y - dragPrev.y);
				dragPrev.set(event.position);
			}
			else if (dragState == DragState.HORIZONTAL)
			{
				scroll(event.position.x - dragPrev.x, 0);
				dragPrev.set(event.position);				
			}
		}
		else if (event.type == Event.Type.MOUSE_SCROLL)
		{
			if (contents.receiveEvent(event, position)) return true;
			scroll(0, event.amount);
			return true;
		}
		else if (event.type == Event.Type.MOUSE_CLICK)
		{
			if (event.pressed)
			{
				int scrollbarSize = window.getTheme().getScrollbarSize();
				
				if (event.position.x > position.x + size.x - scrollbarSize)
				{
					dragState = DragState.VERTICAL;
					dragPrev.set(event.position);
				}
				else if (event.position.y > position.y + size.y - scrollbarSize)
				{
					dragState = DragState.HORIZONTAL;
					dragPrev.set(event.position);
				}				
			}
			else
				dragState = DragState.NONE;
		}
		
		if (!handled)
		{
			handled = contents.receiveEvent(event, position);
		}
		
		return handled;
	}
	
	private void scroll(int x, int y)
	{
		scrollPos.inc(x, y);
		if (scrollPos.x < 0) scrollPos.x = 0;
		if (scrollPos.x >= maxSize.x - viewSize.x) scrollPos.x = maxSize.x - viewSize.x;
		
		if (scrollPos.y < 0) scrollPos.y = 0;
		if (scrollPos.y >= maxSize.y - viewSize.y) scrollPos.y = maxSize.y - viewSize.y;
	}
}
