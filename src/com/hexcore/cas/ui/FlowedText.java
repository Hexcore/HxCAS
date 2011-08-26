package com.hexcore.cas.ui;

import java.util.List;

import com.hexcore.cas.math.Vector2i;

public class FlowedText
{
	public List<String>	lines;
	public Vector2i		size;
	public int			lineHeight;
	public Text.Size	textSize;
	
	public int getNumLines()
	{
		return lines.size();
	}
	
	public int getLineBeginningCursorPositionFromLine(int line)
	{
		int	cursor = 0;
		for (int i = 0; i < line; i++) cursor += lines.get(i).length();
		return cursor;
	}
	
	public int getLineBeginningCursorPosition(int cursor)
	{
		int	newCursor = 0;
		
		for (String line : lines)
		{
			if (line.length() + newCursor > cursor) break;
			newCursor += line.length();
		}

		return newCursor;
	}
	
	public int getLineEndCursorPositionFromLine(int line)
	{
		int	cursor = 0;
		for (int i = 0; i <= line; i++) cursor += lines.get(i).length();
		return cursor - 1;
	}
	
	public int getLineEndCursorPosition(int cursor)
	{
		int	newCursor = 0;
		
		for (String line : lines)
		{
			if (newCursor > cursor) break;
			newCursor += line.length();
		}

		return newCursor - 1;
	}
	
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
	
	public int getCursorIndex(Theme theme, Vector2i position)
	{
		int	lineNum = position.y / lineHeight;
		if (lineNum < 0 || lineNum >= lines.size()) return -1;
		
		int offsetIndex = 0;
		for (int i = 0; i < lineNum; i++) offsetIndex += lines.get(i).length();
		
		String line = lines.get(lineNum);
		int	lastLength = 0;
		for (int i = 0; i < line.length() - 1; i++)
		{
			int length = theme.calculateTextSize(line.substring(0, i+1), textSize).x;
			if (position.x <= (length + lastLength) / 2 + 1) return offsetIndex + i;
			lastLength = length;
		}
		
		return offsetIndex + line.length() - 1;
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

		if (lines.isEmpty() || lines.get(lines.size() - 1).isEmpty())
			return new Vector2i(0, height);
		else
			return new Vector2i(lines.get(lines.size() - 1).length(), height);
	}	
	
	public Vector2i getCursorPosition(Theme theme, Vector2i cursorLocation)
	{
		if (lines.isEmpty()) return new Vector2i(0, 0);
		
		int	ly = Math.max(Math.min(cursorLocation.y, lines.size() - 1), 0);
		
		String	line = lines.get(ly);
		
		int lx = Math.max(Math.min(cursorLocation.x, line.length() - 1), 0);
		
		int		x = theme.calculateTextSize(line.substring(0, lx), textSize).x;
		return new Vector2i(x, lineHeight * ly);
	}	
	
	public Vector2i getCursorPosition(Theme theme, int cursor)
	{
		return getCursorPosition(theme, getCursorLocation(cursor));
	}
}
