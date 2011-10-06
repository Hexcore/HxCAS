package com.hexcore.cas.ui.toolkit;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.RectangleGrid;

public class RectangleGridWidget extends Grid2DWidget<RectangleGrid>
{
	public RectangleGridWidget(RectangleGrid grid, int cellSize)
	{
		this(new Vector2i(0, 0), grid, cellSize);
	}

	public RectangleGridWidget(Vector2i position, RectangleGrid grid, int cellSize)
	{
		super(new Vector2i(grid.getWidth() * cellSize, grid.getHeight() * cellSize), grid, cellSize);
	}

	@Override
	public void render(GL gl, Vector2i position)
	{
		Vector2i pos = this.position.add(position);
		Graphics.renderRectangle(gl, pos, size, backgroundColour);
		
		int s = (int)(cellSize * zoom);
		
		for (int y = 0; y < grid.getHeight(); y++)
			for (int x = 0; x < grid.getWidth(); x++)
			{
				Cell 		cell = grid.getCell(x, y);
				Colour		colour = Colour.DARK_GREY;
				
				if (colourRules != null)
					colour = colourRules.getColour(cell, colourProperty);
				else if (cell.getValue(colourProperty) > 0) 
					colour = Colour.LIGHT_GREY;
					
				Graphics.renderRectangle(gl, pos.add(x * s, y * s), new Vector2i(s, s), colour);
				Graphics.renderBorder(gl, pos.add(x * s, y * s), new Vector2i(s, s), Colour.WHITE);
			}
	}	
}
