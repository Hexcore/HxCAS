package com.hexcore.cas.ui;

import java.awt.event.KeyEvent;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;

public class TextBox extends Widget
{
	private String	text = "";
	private int		cursorIndex = 0;
	
	public TextBox(Vector2i size)
	{
		super(size);
	}

	public TextBox(Vector2i position, Vector2i size)
	{
		super(position, size);
	}

	public boolean	canGetFocus() {return true;}
	
	public String	getText() {return text;}
	public void		setText(String text) {this.text = text;}

	@Override
	public void render(GL gl, Vector2i position)
	{
		Vector2i pos = this.position.add(position);
		
		window.getTheme().renderTextBox(gl, pos, size, text, cursorIndex, focused);
	}

	@Override
	public boolean handleEvent(Event event, Vector2i position)
	{
		boolean handled = super.handleEvent(event, position);
		
		if (event.type == Event.Type.GAINED_FOCUS)
		{
			cursorIndex = text.length();
		}
		else if ((event.type == Event.Type.MOUSE_CLICK) && event.pressed)
		{
			window.requestFocus(this);
		}
		else if ((event.type == Event.Type.KEY_PRESS) && !event.pressed)
		{
			if ((event.button == KeyEvent.VK_LEFT) && (cursorIndex > 0))
				cursorIndex--;
			else if ((event.button == KeyEvent.VK_RIGHT) && (cursorIndex < text.length()))
				cursorIndex++;
			else if (event.button == KeyEvent.VK_BACK_SPACE)
			{
				if ((text.length() > 0) && (cursorIndex > 0))
				{
					text = text.substring(0, cursorIndex - 1) + text.substring(cursorIndex);
					cursorIndex--;
				}
			}
			else if ((event.button == KeyEvent.VK_DELETE) && (cursorIndex < text.length()))
			{
				text = text.substring(0, cursorIndex) + text.substring(cursorIndex + 1);
			}
		}
		else if (event.type == Event.Type.KEY_TYPED)
		{
			if ((event.button >= ' ') && (event.button != 127))
			{
				text = text.substring(0, cursorIndex) + (char)event.button + text.substring(cursorIndex);
				cursorIndex++;
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
