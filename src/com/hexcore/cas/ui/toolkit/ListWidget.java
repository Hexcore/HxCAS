package com.hexcore.cas.ui.toolkit;

import java.util.ArrayList;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;

public class ListWidget extends Widget
{
	private int					selected = -1;
	private int					mouseoverItem = -1;
	private ArrayList<String>	items;
	
	public ListWidget(Vector2i size)
	{
		super(size);
		items = new ArrayList<String>();
	}

	public ListWidget(Vector2i position, Vector2i size)
	{
		super(position, size);
		items = new ArrayList<String>();
	}
		
	public void		addItem(String text) {items.add(text);}
	
	public void 	setSelected(int index) {selected = index;}
	public int 		getSelected() {return selected;}
	
	public String	getSelectedText() 
	{
		if ((selected < 0) || (selected >= items.size())) return "";
		return items.get(selected);
	}
	
	@Override
	public void render(GL gl, Vector2i position)
	{
		Vector2i 	pos = this.position.add(position);		
		Theme 		theme = window.getTheme();
		Text.Size	textSize = Text.Size.SMALL;
		int			textHeight = theme.calculateTextHeight(textSize);
		
		window.setClipping(gl, pos, size);
		
		int		borderRadius = theme.getInteger("List", "border-radius", 0);
		Fill	backgroundFill = theme.getFill("List", "background");
		Fill	borderFill = theme.getFill("List", "border");
		Colour	textColour = theme.getColour("List", "text-colour", Colour.BLACK);
		
		Fill	selectedFill = theme.getFill("List", "selected-background");
		Fill	hoverFill = theme.getFill("List", "hover-background");
		
		Vector2i padding = theme.getVector2i("List", "padding", new Vector2i(2, 2));
		Vector2i itemPadding = theme.getVector2i("List", "item-padding", new Vector2i(2, 2));
		
		Graphics.renderRectangle(gl, pos, size, borderRadius, backgroundFill);
		
		Vector2i itemPos = pos.add(padding);
		for (int i = 0; i < items.size(); i++)
		{
			String item = items.get(i);
			
			if (i == selected)
			{
				Vector2i itemSize = new Vector2i(size.x - padding.x * 2, textHeight + itemPadding.y * 2);
				Graphics.renderRectangle(gl, itemPos, itemSize, 0, selectedFill);				
			}
			else if (i == mouseoverItem)
			{
				Vector2i itemSize = new Vector2i(size.x - padding.x * 2, textHeight + itemPadding.y * 2);
				Graphics.renderRectangle(gl, itemPos, itemSize, 0, hoverFill);
			}
			
			itemPos.inc(0, itemPadding.y);
			theme.renderText(gl, item, itemPos.add(itemPadding.x, 0), textColour, textSize);
			itemPos.inc(0, textHeight + itemPadding.y);
		}
		
		Graphics.renderBorder(gl, pos, size, borderRadius, borderFill);
		
		window.resetView(gl);
	}
	
	@Override
	public boolean handleEvent(Event event, Vector2i position)
	{
		boolean handled = super.handleEvent(event, position);
		
		if ((event.type == Event.Type.MOUSE_MOTION) || ((event.type == Event.Type.MOUSE_CLICK) && event.pressed))
		{
			if (event.position.x >= position.x && event.position.x <= position.x + size.x)
			{	
				Theme 		theme = window.getTheme();
				Text.Size	textSize = Text.Size.SMALL;
				Vector2i 	padding = theme.getVector2i("List", "padding", new Vector2i(2, 2));
				Vector2i 	itemPadding = theme.getVector2i("List", "item-padding", new Vector2i(2, 2));
				int			textHeight = theme.calculateTextHeight(textSize);
				int			itemHeight = textHeight + itemPadding.y * 2;
				
				int index = (event.position.y - position.y - padding.y) / itemHeight;
				
				if (index >= 0 && index < items.size())
				{
					mouseoverItem = index;
					
					if ((event.type == Event.Type.MOUSE_CLICK) && event.pressed)
						selected = index;
				}
			}
		}
		
		return handled;
	}
}
