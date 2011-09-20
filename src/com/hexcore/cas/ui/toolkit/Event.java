package com.hexcore.cas.ui.toolkit;

import java.awt.event.InputEvent;

import com.hexcore.cas.math.Vector2i;

public class Event
{
	public enum Type {MOUSE_MOTION, MOUSE_CLICK, MOUSE_SCROLL, KEY_PRESS, KEY_TYPED, GAINED_FOCUS, LOST_FOCUS, ACTION, CLICK, CHANGE, MOUSE_OUT};

	public static final int	CTRL = 1;
	public static final int	ALT = 2;
	public static final int	SHIFT = 4;
	
	public Event(Type type)
	{
		this.type = type;
	}
	
	public Type			type;
	public Vector2i		position;
	public int			amount;
	public int			button;
	public char			character;
	public boolean		pressed;
	public Widget		target = null;
	public int			modifiers = 0;
	
	public void setModifiers(InputEvent e)
	{
		if (e.isAltDown()) modifiers |= ALT;
		if (e.isControlDown()) modifiers |= CTRL;
		if (e.isShiftDown()) modifiers |= SHIFT;
	}
	
	public boolean hasModifier(int modifier)
	{
		return (modifiers & modifier) > 0;
	}
	
	public boolean isMousePress() {return (type == Type.MOUSE_CLICK) && pressed;}
	public boolean isMouseRelease() {return (type == Type.MOUSE_CLICK) && !pressed;}
}
