package com.hexcore.cas.ui.toolkit.widgets;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.ui.toolkit.Colour;
import com.hexcore.cas.ui.toolkit.Graphics;
import com.hexcore.cas.ui.toolkit.Window;

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
		
		if (window.isDebugLayout())
			Graphics.renderBorder(gl, pos, size, new Colour(0.0f, 1.0f, 0.5f));
	}
}
