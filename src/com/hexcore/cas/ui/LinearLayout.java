package com.hexcore.cas.ui;

import com.hexcore.cas.math.Vector2i;

public class LinearLayout extends Layout
{
	public enum Direction {HORIZONTAL, VERTICAL};
	
	public Direction	direction;
	
	public LinearLayout(Vector2i size, Direction direction)
	{
		super(size);
		this.direction = direction;
	}
	
	public LinearLayout(Direction direction)
	{
		this.direction = direction;
	}
	
	@Override
	public void relayout()
	{
		int	dirIndex = (direction == Direction.VERTICAL) ? 1 : 0;
		
		int	posDir = 0;
		int posPerp = 0;
		int	lastMargin = 0;
		
		int	fillComponents = 0;
		
		if (isSet(Widget.WRAP))
		{
			int	maxWidth = -1, maxHeight = -1;
			for (Widget component : components)
			{
				if (!component.isVisible()) continue;
				
				if (component.isSet(Widget.WRAP)) component.relayout();
				
				if (!component.isSet(Widget.FILL))
				{
					if (component.getWidth() > maxWidth) maxWidth = component.getWidth();
					if (component.getHeight() > maxHeight) maxHeight = component.getHeight();
				}
			}
			
			if (direction == Direction.VERTICAL)
			{
				if ((maxWidth > 0) && isSet(Widget.WRAP_HORIZONTAL)) setWidth(maxWidth + margin.x * 2);
			}
			else if (direction == Direction.HORIZONTAL)
			{
				if ((maxHeight > 0) && isSet(Widget.WRAP_VERTICAL)) setHeight(maxHeight + margin.y * 2);
			}
		}
						
		for (Widget component : components)
		{
			if (!component.isVisible()) continue;
			
			// Shift up component by margin
			int marginDir = component.getMargin().get(dirIndex);
			posDir += Math.max(lastMargin, marginDir);
			posPerp = component.getMargin().get(1 - dirIndex);
						
			// Change component size if requested
			if (direction == Direction.VERTICAL)
			{
				if (component.isSet(Widget.FILL_HORIZONTAL))
					component.setWidth(getWidth() - component.getMargin().x * 2);
				else if (component.isSet(Widget.CENTER_HORIZONTAL))
					posPerp = (getWidth() - component.getWidth()) / 2;
			}
			else if (direction == Direction.HORIZONTAL)
			{
				if (component.isSet(Widget.FILL_VERTICAL))
					component.setHeight(getHeight() - component.getMargin().y * 2);
				else if (component.isSet(Widget.CENTER_VERTICAL))
					posPerp = (getHeight() - component.getHeight()) / 2;
			}
			
			// Set position
			Vector2i	pos = (direction == Direction.VERTICAL) ? new Vector2i(posPerp, posDir) : new Vector2i(posDir, posPerp);
			component.setPosition(pos);
			
			// Record if fill component
			if (component.isSet((direction == Direction.VERTICAL) ? Widget.FILL_VERTICAL : Widget.FILL_HORIZONTAL)) fillComponents++;
			
			// Get ready for next component
			lastMargin = marginDir;
			posDir += component.getSize().get(dirIndex);
		}
		
		int totalSize = posDir + lastMargin;
		
		// Expand components that are marked to fill
		if (fillComponents > 0)
		{
			int addedSize = (size.get(dirIndex) - totalSize) / fillComponents;
			
			int	posAdd = 0;
			for (Widget component : components)
			{
				if (posAdd > 0)
				{
					if (dirIndex == 0)
						component.setX(component.getX() + posAdd);
					else
						component.setY(component.getY() + posAdd);
				}
				
				if (component.isSet((dirIndex == 1) ? Widget.FILL_VERTICAL : Widget.FILL_HORIZONTAL)) 
				{
					if (dirIndex == 0)
						component.setWidth(component.getWidth() + addedSize);
					else
						component.setHeight(component.getHeight() + addedSize);
					
					posAdd += addedSize;
				}
			}
		}
		
		for (Widget component : components) component.relayout();
	}
}
