package com.hexcore.cas.ui;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;

public class ShapeWidget extends Widget
{
	public Fill fill;
	
	public ShapeWidget(Vector2i size, Fill fill)
	{
		super(new Vector2i(0, 0), size);
		this.fill = fill;
	}
	
	public ShapeWidget(Vector2i position, Vector2i size, Fill fill)
	{
		super(position, size);
		this.fill = fill;
	}	
	
	@Override
	public void render(GL gl, Vector2i position)
	{
		if (!visible) return;
		
		Vector2i pos = this.position.add(position);
		
		window.renderRectangle(gl, pos, size, 0, fill);
	}
}
