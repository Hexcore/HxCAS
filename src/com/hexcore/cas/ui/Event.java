package com.hexcore.cas.ui;

import com.hexcore.cas.math.Vector2i;

public class Event
{
	public enum Type {MOUSE_MOTION, MOUSE_CLICK, MOUSE_SCROLL, KEY_PRESS, KEY_TYPED, GAINED_FOCUS, LOST_FOCUS, ACTION, CHANGE, MOUSE_OUT};

	public Event(Type type)
	{
		this.type = type;
		this.target = null;
	}
	
	public Type			type;
	public Vector2i		position;
	public int			amount;
	public int			button;
	public char			character;
	public boolean		pressed;
	public Widget		target;
	
	public boolean isMousePress() {return (type == Type.MOUSE_CLICK) && pressed;}
	public boolean isMouseRelease() {return (type == Type.MOUSE_CLICK) && !pressed;}
}
