package com.hexcore.cas.ui.toolkit;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.ui.toolkit.Theme.ButtonState;

public class SliderWidget extends ClickableWidget
{
	protected boolean showValue = false;
	protected int showValuePlaces = 0;
	
	protected float minimum;
	protected float maximum;
	protected float value;
	
	public SliderWidget(int width)
	{
		this(width, 0.0f, 100.0f, 0.0f);
	}
	
	public SliderWidget(int width, float minimum, float maximum)
	{
		this(width, minimum, maximum, minimum);
	}
	
	public SliderWidget(int width, float minimum, float maximum, float value)
	{
		super(new Vector2i(width, 10));
		this.minimum = minimum;
		this.maximum = maximum;
		this.value = value;
	}
	
	public void setShowValue(boolean state) {showValue = state;}
	public void setShowValuePlaces(int places) {showValuePlaces = places;}
	public void setMinimum(float minimum) {this.minimum = minimum;}
	public void setMaximum(float maximum) {this.maximum = maximum;}
	public void setValue(float value) {this.value = value;}
	
	public boolean getShowValue() {return showValue;}
	public int getShowValuePlaces() {return showValuePlaces;}
	public float getMinimum() {return minimum;}
	public float getMaximum() {return maximum;}
	public float getValue() {return value;}
	
	@Override
	public void relayout()
	{		
		if (window != null) 
			super.setHeight(window.getTheme().calculateTextHeight(Text.Size.SMALL));
	}
	
	@Override
	public void render(GL gl, Vector2i position)
	{
		Vector2i pos = this.position.add(position);
		
		float percent = (value - minimum) / (maximum - minimum);
		
		ButtonState state = ButtonState.NORMAL;
		if (mouseover) state = ButtonState.HOVER;
		if (active) state = ButtonState.ACTIVE;
				
		window.getTheme().renderSlider(gl, pos, size, state);
		window.getTheme().renderSliderHandle(gl, pos, size, percent, state);
		
		if (showValue && active) 
			window.getTheme().renderSliderValue(gl, pos, size, value, showValuePlaces, percent);
	}
	
	@Override
	public boolean handleEvent(Event event, Vector2i position)
	{
		receiveEventExtras(event, position);
		return super.handleEvent(event, position);
	}
	
	@Override
	public boolean receiveEventExtras(Event event, Vector2i position)
	{
		if ((event.type == Event.Type.MOUSE_MOTION || event.type == Event.Type.MOUSE_CLICK) && active)
		{			
			int width = window.getTheme().getSliderHandleSize().x;
			value = (float)(event.position.x - position.x - width / 2) * maximum / (size.x - width) + minimum;
			value = Math.max(Math.min(value, maximum), minimum);
			
			Event windowEvent = new Event(Event.Type.CHANGE);
			windowEvent.target = this;
			window.sendWindowEvent(windowEvent);
		}
		
		return super.receiveEventExtras(event, position);
	}
}
