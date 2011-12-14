package com.hexcore.cas.ui.toolkit;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;

public class CheckBox extends ClickableWidget implements CaptionWidget
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
	
	public CheckBox(int width, String caption)
	{
		super(new Vector2i(width, 20));
		this.caption = caption;
	}	
	
	public CheckBox(String caption)
	{
		super(new Vector2i(200, 20));
		this.caption = caption;
	}	

	public boolean	isChecked() {return checked;}
	public void		setChecked(boolean checked) {this.checked = checked;}
	
	@Override public String getCaption() {return caption;}
	@Override public void setCaption(String caption) {this.caption = caption;}
	
	public void handleClick()
	{
		checked = !checked;
		
		Event changeEvent = new Event(Event.Type.CHANGE);
		changeEvent.target = this;
		window.sendWindowEvent(changeEvent);
	}
	
	@Override
	public void render(GL gl, Vector2i position)
	{
		Vector2i pos = this.position.add(position);
		
		window.getTheme().renderCheckBox(gl, pos, size, caption, focused, checked);
	}
}
