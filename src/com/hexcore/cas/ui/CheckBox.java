package com.hexcore.cas.ui;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;

public class CheckBox extends ClickableWidget
{
	protected String	caption;
	protected boolean	checked;
	
	public CheckBox(Vector2i position, Vector2i size, String caption)
	{
		super(position, size);
		this.caption = caption;
	}

	public CheckBox(Vector2i size, String caption)
	{
		super(size);
		this.caption = caption;
	}

	public boolean	isChecked() {return checked;}
	public void		setChecked(boolean checked) {this.checked = checked;}
	
	public void handleClick()
	{
		checked = !checked;
	}
	
	@Override
	public void render(GL gl, Vector2i position)
	{
		Vector2i pos = this.position.add(position);
		
		window.getTheme().renderCheckBox(gl, pos, size, caption, focused, checked);
	}
}
