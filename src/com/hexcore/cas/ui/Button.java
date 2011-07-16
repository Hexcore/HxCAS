package com.hexcore.cas.ui;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;

public class Button extends ClickableWidget
{
	private String	caption;
	private String	description;
	
	public Button(Vector2i size, String caption)
	{
		this(new Vector2i(0, 0), size, caption, "");
	}
	
	public Button(Vector2i size, String caption, String description)
	{
		this(new Vector2i(0, 0), size, caption, description);
	}
	
	public Button(Vector2i position, Vector2i size, String caption)
	{
		this(position, size, caption, "");
	}	
	
	public Button(Vector2i position, Vector2i size, String caption, String description)
	{
		super(position, size);
		this.caption = caption;
		this.description = description;
	}

	@Override
	public void render(GL gl, Vector2i position)
	{
		if (!visible) return;
		
		Vector2i pos = this.position.add(position);
		
		Theme.ButtonState state = Theme.ButtonState.NORMAL;
		
		if (active && mouseover)
			state = Theme.ButtonState.ACTIVE;
		else if (mouseover)
			state = Theme.ButtonState.HOVER;
		else if (focused)
			state = Theme.ButtonState.FOCUS;	
		
		window.getTheme().renderButton(gl, pos, size, state, caption, description);
	}
		
	public String 	getCaption() {return caption;}
	public String 	getDescription() {return description;}
	
	public void		setCaption(String caption) {this.caption = caption;}
	public void		setDescription(String description) {this.description = description;}
}
