package com.hexcore.cas.ui;

import java.util.ArrayList;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;

public class DropDownBox extends Widget
{
	private int					selected;
	private ArrayList<String>	items;
	
	public DropDownBox(Vector2i size)
	{
		super(size);
		items = new ArrayList<String>();
	}

	public DropDownBox(Vector2i position, Vector2i size)
	{
		super(position, size);
		items = new ArrayList<String>();
	}
	
	public boolean	canGetFocus() {return true;}
	
	public void		addItem(String text) {items.add(text);}
	
	public void 	setSelected(int index) {selected = index;}
	public int 		getSelected() {return selected;}
	
	public String	getSelectedText() 
	{
		if ((selected < 0) || (selected >= items.size())) return "";
		return items.get(selected);
	}

	@Override
	public void render(GL gl, Vector2i position)
	{
		Vector2i pos = this.position.add(position);		
		window.getTheme().renderDropDownBox(gl, pos, size, getSelectedText(), focused);
	}
	
	@Override
	public void renderExtras(GL gl, Vector2i position)
	{
		window.getTheme().renderDropDownBoxList(gl, position, size, items, selected);
	}

	@Override
	public boolean handleEvent(Event event, Vector2i position)
	{
		boolean handled = super.handleEvent(event, position);
		
		if ((event.type == Event.Type.MOUSE_CLICK) && event.pressed)
		{
			window.toggleFocus(this);
			handled = true;
		}
		
		return handled;
	}
}
