package com.hexcore.cas.ui.toolkit;

import com.hexcore.cas.math.Vector2i;

public class NumberBox extends TextBox
{
	public NumberBox(int width)
	{
		super(width);
	}

	public NumberBox(Vector2i position, Vector2i size)
	{
		super(position, size);
	}

	public NumberBox(Vector2i position, int width)
	{
		super(position, width);
	}

	public void setValue(int value)
	{
		setText(Integer.toString(value));
	}
	
	public int getValue(int fallback)
	{
		try
		{
			return Integer.parseInt(getText());
		}
		catch (NumberFormatException nfe)
		{
			return fallback;
		}
	}	
	
	@Override
	public boolean handleEvent(Event event, Vector2i position)
	{
		boolean handled = false;
		
		if (focused)
		{
			if (event.type == Event.Type.KEY_TYPED && !event.hasModifier(Event.CTRL))
				if ((event.button =='\b') || (event.button == 127))
					handled = false;
				else if ((event.button < '0') || (event.button > '9'))
					handled = true;
		}

		if (!handled) handled = super.handleEvent(event, position);	
		return handled;
	}
}
