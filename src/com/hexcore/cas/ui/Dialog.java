package com.hexcore.cas.ui;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;

public class Dialog extends Container
{
	public Dialog(Window window, Vector2i size)
	{
		super(new Vector2i((window.getWidth() - size.x) / 2, (window.getHeight() - size.y) / 2), size);
		setWindow(window);
	}
	
	@Override
	public void relayout()
	{
		super.relayout();
		setPosition(new Vector2i((window.getWidth() - size.x) / 2, (window.getHeight() - size.y) / 2));
	}
	
	@Override
	public void render(GL gl, Vector2i position)
	{
		if (!visible) return;
		
		Vector2i pos = this.position.add(position);	
		window.getTheme().renderDialog(gl, pos, size);

		if (contents != null) contents.render(gl, pos);
	}
}
