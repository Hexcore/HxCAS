package com.hexcore.cas.ui;

import java.awt.event.KeyEvent;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;

public class TextArea extends TextBox
{	
	private	int	rows;
	
	private FlowedText	flowedText;
	
	@Deprecated
	public TextArea(Vector2i size)
	{
		super(size);
	}
	
	public TextArea(int width, int rows)
	{
		super(width);
		this.rows = rows;
	}
	
	@Deprecated
	public TextArea(Vector2i position, Vector2i size)
	{
		super(position, size);
	}
	
	public TextArea(Vector2i position, int width, int rows)
	{
		super(position, width);
		this.rows = rows;
	}
	
	@Override
	public void	setText(String text) {this.text = text; relayout();}
	
	@Override
	public void setSize(Vector2i v)
	{
		super.setSize(v);
		relayout();
	}
	
	@Override
	public void relayout()
	{
		if (window != null) 
		{
			reflowText();
			
			int	boxHeight = padding.y * 2 + window.getTheme().calculateTextHeight(textSize) * rows;
			super.setHeight(boxHeight);
		}
	}
	
	@Override
	public void render(GL gl, Vector2i position)
	{
		Vector2i pos = this.position.add(position);
		
		window.setClipping(gl, pos, size);
		if (flowedText != null) 
			window.getTheme().renderTextArea(gl, pos, size, flowedText, cursorIndex, focused, window.getTime());
		window.resetView(gl);
	}
	
	@Override
	public boolean handleEvent(Event event, Vector2i position)
	{
		boolean handled = false;
		
		if (focused)
		{
			if ((event.type == Event.Type.KEY_PRESS) && !event.pressed)
			{
				if ((event.button == KeyEvent.VK_UP) && (cursorIndex > 0))
					cursorIndex = flowedText.getPreviousLineCursorPosition(cursorIndex);
				else if ((event.button == KeyEvent.VK_DOWN) && (cursorIndex < text.length()))
					cursorIndex = flowedText.getNextLineCursorPosition(cursorIndex);
				if ((event.button == KeyEvent.VK_HOME) && (cursorIndex > 0))
					cursorIndex = flowedText.getLineBeginningCursorPosition(cursorIndex);
				else if ((event.button == KeyEvent.VK_END) && (cursorIndex < text.length()))
					cursorIndex = flowedText.getLineEndCursorPosition(cursorIndex);	
				else
					handled = false;
			}
			else if ((event.type == Event.Type.KEY_TYPED) && (event.button == '\n'))
			{
				text = text.substring(0, cursorIndex) + '\n' + text.substring(cursorIndex);
				cursorIndex++;
				handled = true;
			}
		}
	
		if (!handled) handled = super.handleEvent(event, position);
		if (handled) relayout();
		return handled;
	}
	
	public void reflowText()
	{
		if (window != null) flowedText = window.getTheme().flowText(text, -1, textSize);
	}
}
