package com.hexcore.cas.ui;

import java.util.List;

import com.hexcore.cas.math.Vector2i;

public class FlowedText
{
	public List<String>	lines;
	public Vector2i		size;
	public int			lineHeight;
	public Text.Size	textSize;
	
	public int getPreviousLineCursorPosition(int cursor)
	{
		Vector2i cursorLocation = getCursorLocation(cursor);
		int	newCursor = 0, line;
		
		for (line = 0; line < cursorLocation.y - 1; line++) 
			newCursor += lines.get(line).length();
		
		newCursor += Math.min(cursorLocation.x, lines.get(line).length() - 1);
		
		return newCursor;
	}
	
	public int getNextLineCursorPosition(int cursor)
	{
		Vector2i cursorLocation = getCursorLocation(cursor);
		int	newCursor = 0, line;
		
		if (cursorLocation.y == lines.size() - 1) return cursor;
		
		for (line = 0; line < cursorLocation.y + 1; line++) 
			newCursor += lines.get(line).length();
		
		newCursor += Math.min(cursorLocation.x, lines.get(line).length() - 1);
		
		return newCursor;
	}
	
	public Vector2i getCursorLocation(int cursor)
	{
		int	height = 0;
		for (String line : lines)
		{
			if (cursor < line.length()) return new Vector2i(cursor, height);
			cursor -= line.length();
			height++;
		}
		
		return new Vector2i(0, 0);
	}	
	
	public Vector2i getCursorPosition(Theme theme, int cursor)
	{
		int	height = 0;
		for (String line : lines)
		{
			if (cursor < line.length())
			{
				int x = theme.calculateTextSize(line.substring(0, cursor), textSize).x;
				return new Vector2i(x, height);
			}
			
			cursor -= line.length();
			height += lineHeight;
		}
		
		return new Vector2i(0, 0);
	}
}
