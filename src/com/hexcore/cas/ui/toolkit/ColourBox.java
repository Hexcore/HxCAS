package com.hexcore.cas.ui.toolkit;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;

public class ColourBox extends ClickableWidget
{
	private Colour colour = Colour.WHITE;
	
	public ColourBox(Vector2i size)
	{
		super(size);
	}

	public void setColour(Colour colour)
	{
		this.colour = colour;
	}
	
	public Colour getColour()
	{
		return colour;
	}
	
	@Override
	public void render(GL gl, Vector2i position)
	{
		Vector2i pos = this.position.add(position);
		
		Graphics.renderRectangle(gl, pos, size, colour);
	}
}
