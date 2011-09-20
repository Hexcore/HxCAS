package com.hexcore.cas.ui.toolkit;


import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;

public class Panel extends Container
{	
	public Panel(Vector2i size)
	{
		super(size);
	}	
	
	public Panel(Vector2i position, Vector2i size)
	{
		super(position, size);
	}
	
	@Override
	public void render(GL gl, Vector2i position)
	{
		if (!visible) return;
		
		Vector2i pos = this.position.add(position);	
		window.getTheme().renderPanel(gl, pos, size);

		if (contents != null) contents.render(gl, pos);
	}
}
