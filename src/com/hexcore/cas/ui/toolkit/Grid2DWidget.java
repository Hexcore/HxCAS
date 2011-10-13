package com.hexcore.cas.ui.toolkit;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Grid;

public class Grid2DWidget extends GridWidget
{
	protected int		colourProperty = 0; //< The property that is used to determine the colour to use
	protected float		zoom = 1.0f;
	protected Vector2i 	scroll = new Vector2i(0, 0);
	
	protected Colour	cellBorderColour = Colour.WHITE;
	protected Colour	cellSelectedBorderColour = Colour.RED;
	
	protected boolean	drawSelected = true;
	protected Vector2i	selectedCell = new Vector2i(0, 0);
	
	public Grid2DWidget(Vector2i size, Grid grid, int cellSize)
	{
		super(size, grid, cellSize);
	}

	public Grid2DWidget(Vector2i position, Vector2i size, Grid grid, int cellSize)
	{
		super(position, size, grid, cellSize);
	}	
	
	public float getZoom() { return zoom; }
	public void setZoom(float zoom) { this.zoom = zoom; }
	public void changeZoom(float amount) { this.zoom += zoom; }
	
	public void setDrawSelected(boolean state) { state = drawSelected; }
	public boolean isSelectedDrawn() {return drawSelected;}	
	public Vector2i getSelectedCell() { return selectedCell; }
	
	public void setColourProperty(int propertyIndex) { colourProperty = propertyIndex; }
	public void setCellBorderColour(Colour colour) { cellBorderColour = colour; }
	public void setSelectedCellBorderColour(Colour colour) { cellSelectedBorderColour = colour; }
	
	@Override
	public boolean handleEvent(Event event, Vector2i position)
	{
		if (event.type == Event.Type.MOUSE_SCROLL)
		{
			zoom *= (1.0f - event.amount * 0.002f);
			if (zoom < 0.01f) zoom = 0.01f;
			return true;
		}
		
		return false;
	}
}
