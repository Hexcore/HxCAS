package com.hexcore.cas.ui;

import java.util.ArrayList;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;


/*
 *  Renders components relative to the top right of the layout.
 *  Performs no modifications to the positions of the contained components.
 *  
 *  Can be derived to implement positioning of contained components. e.g. LinearLayout
 */
public class Layout extends Widget
{
	private Widget				parent;
	protected Fill 				background = null;
	protected ArrayList<Widget>	components;
	
	public Layout(Vector2i size)
	{
		super(size);
		this.parent = null;
		components = new ArrayList<Widget>();
	}
	
	public Layout(Widget parent)
	{
		super(parent.getInnerSize());
		this.parent = parent;
		components = new ArrayList<Widget>();
		
		setSize(parent.getInnerSize().subtract(getMargin()).subtract(getMargin()));
	}
	
	public void relayout()
	{
		if (parent != null)	setSize(parent.getInnerSize().subtract(getMargin()).subtract(getMargin()));
	}
	
	@Override
	public void render(GL gl, Vector2i position)
	{
		if (!visible) return;
		
		Vector2i pos = this.position.add(position);
		
		if (background != null) window.renderRectangle(gl, pos, size, background);
		
		for (Widget component : components)
			component.render(gl, pos);
		
		/*if (mouseover)
		{
			window.renderRectangle(gl, pos.subtract(margin), size.add(margin).add(margin), new Colour(0.0f, 0.5f, 1.0f, 0.2f));
			window.renderBorder(gl, pos.subtract(margin), size.add(margin).add(margin), new Colour(0.0f, 0.5f, 1.0f));		
	
			window.renderRectangle(gl, pos, size, new Colour(1.0f, 0.5f, 0.0f, 0.2f));
			window.renderBorder(gl, pos, size, new Colour(1.0f, 0.5f, 0.0f));
		}*/
	}
	
	@Override
	public boolean handleEvent(Event event, Vector2i position)
	{	
		if (event.type == Event.Type.RESIZE) relayout();
		
		for (Widget component : components)
			component.receiveEvent(event, position);
		
		return false;
	}
	
	public void setBackground(Fill fill)
	{
		background = fill;
	}
	
	public void add(Widget component) 
	{
		components.add(component); 
		component.setWindow(window);
		relayout();
	}
}
