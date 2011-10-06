package com.hexcore.cas.ui.toolkit;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Grid;

public class Grid2DWidget<T extends Grid> extends GridWidget<T>
{
	protected int	colourProperty = 0; //< The property that is used to determine the colour to use
	protected float	zoom = 1.0f;
	
	protected Colour cellBorderColour = Colour.WHITE;
	
	public Grid2DWidget(Vector2i size, T grid, int cellSize)
	{
		super(size, grid, cellSize);
	}

	public Grid2DWidget(Vector2i position, Vector2i size, T grid, int cellSize)
	{
		super(position, size, grid, cellSize);
	}	
	
	public float getZoom() { return zoom; }
	public void setZoom(float zoom) { this.zoom = zoom; }
	public void changeZoom(float amount) { this.zoom += zoom; }
	
	public void setColourProperty(int propertyIndex) { colourProperty = propertyIndex; }
	public void setCellBorderColour(Colour colour) { cellBorderColour = colour; }
	
	@Override
	public boolean handleEvent(Event event, Vector2i position)
	{
		if (event.type == Event.Type.MOUSE_SCROLL)
		{
			zoom -= event.amount * 0.001f;
			if (zoom < 0.001f) zoom = 0.001f;
			return true;
		}
		
		return false;
	}
}
