package com.hexcore.cas.ui.toolkit.widgets;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.ui.toolkit.Colour;
import com.hexcore.cas.ui.toolkit.FlowedText;
import com.hexcore.cas.ui.toolkit.Graphics;
import com.hexcore.cas.ui.toolkit.Text;

public class TextWidget extends Widget implements CaptionWidget
{
	private String		caption;
	private Text.Size	textSize;
	private Colour		colour;
	
	private FlowedText	flowedText;
	private boolean		flowed = false;
	
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
		this.flowedText = null;
	}	
		
	public TextWidget(Vector2i position, String caption)
	{
		super(position, new Vector2i());
		this.caption = caption;
		this.textSize = Text.Size.SMALL;
		this.flowedText = null;
	}
	
	public void setTextSize(Text.Size size) {this.textSize = size; relayout();}
	public void setTextColour(Colour colour) {this.colour = colour; relayout();}
	 
	public void 	setFlowed(boolean state) {flowed = state;}
	public boolean	isFlowed() {return flowed;}
	
	@Override
	public void render(GL gl, Vector2i position)
	{
		if (!visible) return;
		
		Vector2i pos = this.position.add(position);
		
		if ((size.x > 0) && (flowedText != null))
			window.getTheme().renderFlowedText(gl, pos, flowedText, colour);
		else
			window.getTheme().renderText(gl, caption, pos, colour, textSize);
		
		if (window.isDebugLayout()) 
			Graphics.renderBorder(gl, pos, size, new Colour(0.0f, 0.5f, 1.0f));
	}
	
	public String 	getCaption() {return caption;}
	public void		setCaption(String caption) {this.caption = caption; reflowText();}
	
	@Override
	public void setSize(Vector2i v)
	{
		super.setSize(v);
		reflowText();
	}
	
	@Override
	public void relayout()
	{
		if (window != null) 
		{
			reflowText();
			
			if (flowedText != null)
				super.setSize(flowedText.size);
			else
				super.setSize(new Vector2i(window.getTheme().calculateTextSize(caption, textSize)));
		}
	}
	
	public void reflowText()
	{
		if (window != null)
		{
			if (flowed)
				flowedText = window.getTheme().flowText(caption, size.x, textSize);
			else
				flowedText = null;
		}
	}
}
