package com.hexcore.cas.ui;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;

public class SliderWidget extends Widget
{
	private float minimum;
	private float maximum;
	private float value;
	
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
	}
	
	public void setMinimum(float minimum) {this.minimum = minimum;}
	public void setMaximum(float maximum) {this.maximum = maximum;}
	public void setValue(float value) {this.value = value;}
	
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
		
		window.getTheme().renderScrollbar(gl, pos, size, mouseover);
		window.getTheme().renderScrollbarHandle(gl, pos, size, percent, mouseover);
	}
}
