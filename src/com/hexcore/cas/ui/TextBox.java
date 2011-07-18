package com.hexcore.cas.ui;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;

public class TextBox extends Widget
{
	private String	text;
	
	public TextBox(Vector2i size)
	{
		super(size);
		this.text = "";
	}

	public TextBox(Vector2i position, Vector2i size)
	{
		super(position, size);
		this.text = "";
	}

	public boolean	canGetFocus() {return true;}
	
	public String	getText() {return text;}
	public void		setText(String text) {this.text = text;}

	@Override
	public void render(GL gl, Vector2i position)
	{
		Vector2i pos = this.position.add(position);
		
		window.getTheme().renderTextBox(gl, pos, size, text, focused);
	}

	@Override
	public boolean handleEvent(Event event, Vector2i position)
	{
		boolean handled = super.handleEvent(event, position);
		
		if ((event.type == Event.Type.MOUSE_CLICK) && event.pressed)
		{
			window.requestFocus(this);
		}
		else if (event.type == Event.Type.KEY_TYPED)
		{
			if (event.button == '\b')
			{
				if (text.length() > 0)
					text = text.substring(0, text.length() - 1);
			}
			else if (event.button >= ' ')
			{
				text += (char)event.button;
			}
			else if (event.button == '\n')
			{
				window.giveUpFocus(this);
			}
			
			Event changeEvent = new Event(Event.Type.CHANGE);
			changeEvent.target = this;
			window.sendWindowEvent(changeEvent);
		}
		
		return handled;
	}
}
