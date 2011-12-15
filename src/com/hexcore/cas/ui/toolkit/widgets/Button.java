package com.hexcore.cas.ui.toolkit.widgets;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.ui.toolkit.Colour;
import com.hexcore.cas.ui.toolkit.Fill;
import com.hexcore.cas.ui.toolkit.Graphics;
import com.hexcore.cas.ui.toolkit.Image;
import com.hexcore.cas.ui.toolkit.Text;
import com.hexcore.cas.ui.toolkit.Theme;
import com.hexcore.cas.ui.toolkit.Text.Size;
import com.hexcore.cas.ui.toolkit.Theme.BorderShape;
import com.hexcore.cas.ui.toolkit.Theme.ButtonState;

public class Button extends ClickableWidget implements CaptionWidget
{
	private String	caption;
	private String	description;
	
	private int id;
	
	private Image	icon;
	private Image	hoverIcon;
	
	private boolean toggles = false;
	private boolean toggleState = false;
	
	public Button(Image icon)
	{
		this(new Vector2i(0, 0), icon.getSize().add(16, 16), "", "");
		setIcon(icon);
	}
	
	public Button(Image icon, Image hoverIcon)
	{
		this(new Vector2i(0, 0), Vector2i.max(icon.getSize(), hoverIcon.getSize()).add(16, 16), "", "");
		setIcon(icon, hoverIcon);
	}
	
	public Button()
	{
		this(new Vector2i(0, 0), new Vector2i(165, 35), "", "");
	}
	
	public Button(String caption)
	{
		this(new Vector2i(0, 0), new Vector2i(165, 35), caption, "");
	}
	
	public Button(Vector2i size, String caption)
	{
		this(new Vector2i(0, 0), size, caption, "");
	}
	
	public Button(Vector2i size, String caption, int id)
	{
		this(new Vector2i(0, 0), size, caption, "");
		this.id = id;
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
	public void relayout()
	{
		if ((caption.length() == 0) && (icon != null)) 
			setSize(icon.getSize().add(16, 16));
	}
	
	public void setToggles(boolean state) {toggles = state;}
	
	public void setToggleState(boolean state) {toggleState = state;}
	public boolean getToggleState() {return toggleState;}

	@Override
	public void render(GL gl, Vector2i position)
	{
		if (!visible) return;
		
		Vector2i pos = this.position.add(position);
		
		Theme theme = window.getTheme();
		
		Theme.ButtonState state = Theme.ButtonState.NORMAL;
		
		if (active && mouseover)
			state = Theme.ButtonState.ACTIVE;
		else if (toggles || toggleState)
			state = Theme.ButtonState.TOGGLED;
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

		Vector2i	padding = theme.getVector2i("Button", state.name, "padding");
		
		pos = pos.add(textOffset);
		
		Vector2i	textSize = theme.calculateTextSize(caption, Text.Size.MEDIUM);
		
		if (description.isEmpty())
		{
			int	iconWidth = 0;
			Image currentIcon = icon;
			if (mouseover && hoverIcon != null) currentIcon = hoverIcon;

			if (currentIcon != null)
			{
				iconWidth = theme.getInteger("Button", state.name, "icon-space-width", 56);
				
				int xpos = caption.isEmpty() ? 8 : (iconWidth - currentIcon.getWidth()) / 2;
				Vector2i iconPos = pos.add(xpos, (size.y - currentIcon.getHeight()) / 2);
				Graphics.renderRectangle(gl, iconPos, currentIcon);
				
				if (!caption.isEmpty())
				{
					Fill leftFill = theme.getFill("Button", state.name, "divider-left-colour", Fill.NONE);
					Fill rightFill = theme.getFill("Button", state.name, "divider-right-colour", Fill.NONE);
	
					Graphics.renderRectangle(gl, pos.add(iconWidth, padding.y), new Vector2i(1, size.y - padding.y * 2), 0, leftFill);
					Graphics.renderRectangle(gl, pos.add(iconWidth+1, padding.y), new Vector2i(1, size.y - padding.y * 2), 0, rightFill);
				}
			}
			
			Vector2i	textPos = pos.add(iconWidth + (size.x - iconWidth - textSize.x) / 2, (size.y - textSize.y) / 2);
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
	public void		setIcon(Image icon) {this.icon = icon; this.hoverIcon = null;}
	public void		setIcon(Image icon, Image hoverIcon) {this.icon = icon; this.hoverIcon = hoverIcon;}
}
