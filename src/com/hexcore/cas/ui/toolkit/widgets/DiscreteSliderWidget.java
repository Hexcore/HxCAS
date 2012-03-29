package com.hexcore.cas.ui.toolkit.widgets;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.ui.toolkit.Event;
import com.hexcore.cas.ui.toolkit.Text;
import com.hexcore.cas.ui.toolkit.Theme.ButtonState;

public class DiscreteSliderWidget extends ClickableWidget
{
	protected boolean allowActivation = true;
	protected boolean showValue = false;

	protected int minimum;
	protected int maximum;
	protected int value;
	
	public DiscreteSliderWidget(int width)
	{
		this(width, 0, 100, 0);
	}
	
	public DiscreteSliderWidget(int width, int minimum, int maximum)
	{
		this(width, minimum, maximum, minimum);
	}
	
	public DiscreteSliderWidget(int width, int minimum, int maximum, int value)
	{
		super(new Vector2i(width, 10));
		this.minimum = minimum;
		this.maximum = maximum;
		this.value = value;
	}
	
	public void setShowValue(boolean state) {showValue = state;}
	
	public void setMinimum(int minimum) 
	{
		this.minimum = minimum;
		if (value < minimum) value = minimum;
	}
	
	public void setMaximum(int maximum) 
	{
		this.maximum = maximum;
		if (value > maximum) value = maximum;
	}
	
	public void setValue(int value) {this.value = value;}
	
	public boolean getShowValue() {return showValue;}
	public int getMinimum() {return minimum;}
	public int getMaximum() {return maximum;}
	public int getValue() {return value;}
	
	@Override
	public void relayout()
	{		
		if (window != null) 
			super.setHeight(window.getTheme().calculateTextHeight(Text.Size.SMALL));
	}
	
	@Override
	public void render(GL gl, Vector2i position)
	{
		if(allowActivation)
		{
			Vector2i pos = this.position.add(position);
			
			float percent = 1.0f;
			if (maximum > minimum) percent = (float)(value - minimum) / (maximum - minimum);
			
			ButtonState state = ButtonState.NORMAL;
			if (mouseover) state = ButtonState.HOVER;
			if (active) state = ButtonState.ACTIVE;
					
			window.getTheme().renderSlider(gl, pos, size, state);
			window.getTheme().renderSliderHandle(gl, pos, size, percent, state);
			
			if (showValue && active) 
				window.getTheme().renderSliderValue(gl, pos, size, value, 0, percent);
		}
		else
		{
			Vector2i pos = this.position.add(position);
			
			float percent = 1.0f;
			
			ButtonState state = ButtonState.NORMAL;
					
			window.getTheme().renderSlider(gl, pos, size, state);
			window.getTheme().renderSliderHandle(gl, pos, size, percent, state);
		}
	}
	
	@Override
	public boolean handleEvent(Event event, Vector2i position)
	{
		if(allowActivation)
		{
			receiveEventExtras(event, position);
			return super.handleEvent(event, position);
		}
		else
			return false;
	}
	
	@Override
	public boolean receiveEventExtras(Event event, Vector2i position)
	{
		if ((event.type == Event.Type.MOUSE_MOTION || event.type == Event.Type.MOUSE_CLICK) && active)
		{			
			int width = window.getTheme().getSliderHandleSize().x;
			value = Math.round((float)(event.position.x - position.x - width / 2) * maximum / (size.x - width) + minimum);
			value = Math.max(Math.min(value, maximum), minimum);
			
			Event windowEvent = new Event(Event.Type.CHANGE);
			windowEvent.target = this;
			window.sendWindowEvent(windowEvent);
		}
		
		return super.receiveEventExtras(event, position);
	}
	
	public void toggleActivation(boolean val)
	{
		allowActivation = val;
	}
}
