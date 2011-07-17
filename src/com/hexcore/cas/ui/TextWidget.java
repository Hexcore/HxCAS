package com.hexcore.cas.ui;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;


public class TextWidget extends Widget
{
	private String		caption;
	private Text.Size	textSize;
	private Colour		colour;
	
	public TextWidget(String caption)
	{		
		this(caption, Text.Size.SMALL);
	}
	
	public TextWidget(String caption, Text.Size textSize)
	{		
		this(caption, textSize, Colour.BLACK);
	}		
	
	public TextWidget(String caption, Text.Size textSize, Colour colour)
	{		
		super(new Vector2i());
		this.caption = caption;
		this.textSize = textSize;
		this.colour = colour;
	}	
		
	public TextWidget(Vector2i position, String caption)
	{
		super(position, new Vector2i());
		this.caption = caption;
		this.textSize = Text.Size.SMALL;
	}
	
	@Override
	public void render(GL gl, Vector2i position)
	{
		if (!visible) return;
		
		Vector2i pos = this.position.add(position);
		window.getTheme().renderText(gl, caption, pos, colour, textSize);
		
		window.renderBorder(gl, pos, size, Colour.WHITE);
	}
	
	public String 	getCaption() {return caption;}
	public void		setCaption(String caption) {this.caption = caption;}
	
	@Override
	public void relayout()
	{
		if (window != null) setSize(window.getTheme().calculateTextSize(caption, textSize));
	}
}
