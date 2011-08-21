package com.hexcore.cas.ui;

import java.util.List;

import com.hexcore.cas.math.Vector2i;

public class FlowedText
{
	public List<String>	lines;
	public Vector2i		size;
	public int			lineHeight;
	public Text.Size	textSize;
	
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
