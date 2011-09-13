package com.hexcore.cas.ui;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.ui.Theme.BorderShape;

public class Button extends ClickableWidget
{
	private String	caption;
	private String	description;
	
	private Image	icon;
	
	public Button(Image icon)
	{
		this(new Vector2i(0, 0), icon.getSize().add(16, 16), "", "");
		setIcon(icon);
	}	
	
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
		this.icon = null;
	}

	@Override
	public void render(GL gl, Vector2i position)
	{
		if (!visible) return;
		
		Vector2i pos = this.position.add(position);
		
		Theme theme = window.getTheme();
		
		Theme.ButtonState state = Theme.ButtonState.NORMAL;
		
		if (active && mouseover)
			state = Theme.ButtonState.ACTIVE;
		else if (mouseover)
			state = Theme.ButtonState.HOVER;
		else if (focused)
			state = Theme.ButtonState.FOCUS;	
			
		int 		borderRadius = theme.getInteger("Button", state.name, "border-radius", 0);
		BorderShape corners = new BorderShape(BorderShape.ALL_CORNERS);
		
		Graphics.renderRoundedBorderedRectangle(gl, pos, size, borderRadius, corners, 
				theme.getFill("Button", state.name, "background"),
				theme.getFill("Button", state.name, "border"));	 
		
		Colour 		textColour = theme.getColour("Button", state.name, "text-colour");
		Colour 		shadowColour = theme.getColour("Button", state.name, "text-shadow-colour", Colour.TRANSPARENT);
		
		Vector2i	textOffset = theme.getVector2i("Button", state.name, "text-offset");
		Vector2i	shadowOffset = theme.getVector2i("Button", state.name, "text-shadow-offset", new Vector2i(0, 1));

		pos = pos.add(textOffset);
		
		Vector2i	textSize = theme.calculateTextSize(caption, Text.Size.MEDIUM);
		
		if (description.isEmpty())
		{
			int			imagePad = caption.isEmpty() ? 0 : 6;
			int			imageSpace = (icon != null) ? (icon.getWidth() + imagePad) : 0;
			int			imageHeight = (icon != null) ? icon.getHeight() : 0;
			
			int			xPos = (size.x - textSize.x - imageSpace) / 2;
			Vector2i	iconPos = pos.add(xPos, (size.y - imageHeight) / 2);
			Vector2i	textPos = pos.add(xPos + imageSpace, (size.y - textSize.y) / 2);
			
			if (icon != null) Graphics.renderRectangle(gl, iconPos, icon);
			theme.renderShadowedText(gl, caption, textPos, textColour, shadowColour, shadowOffset, Text.Size.MEDIUM);
		}
		else
		{
			int			textGap = 4;
			Vector2i	smallTextSize = theme.calculateTextSize(description, Text.Size.SMALL);
					
			int		top = (size.y - textSize.y - textGap - smallTextSize.y) / 2;
			int		bigLeft = (size.x - textSize.x) / 2;
			int		smallLeft = (size.x - smallTextSize.x) / 2;
			
			theme.renderShadowedText(gl, caption, pos.add(bigLeft, top), textColour, shadowColour, shadowOffset, Text.Size.MEDIUM);
			theme.renderShadowedText(gl, description, pos.add(smallLeft, top + textSize.y + textGap), textColour, shadowColour, shadowOffset, Text.Size.SMALL);
		}
	}
		
	public String 	getCaption() {return caption;}
	public String 	getDescription() {return description;}
	
	public void		setCaption(String caption) {this.caption = caption;}
	public void		setDescription(String description) {this.description = description;}
	public void		setIcon(Image icon) {this.icon = icon;}
}
