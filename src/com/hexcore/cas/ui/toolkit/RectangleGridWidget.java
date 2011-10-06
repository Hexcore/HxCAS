package com.hexcore.cas.ui.toolkit;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2f;
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
		
		float s = cellSize * zoom;
		
		Vector2f[]	rectangle = new Vector2f[4];
		rectangle[0] = new Vector2f(0.0f, 0.0f);
		rectangle[1] = new Vector2f(0.0f, s);
		rectangle[2] = new Vector2f(s,	  s);
		rectangle[3] = new Vector2f(s,	  0.0f);
		
		Vector2f[]	rectangleBorder = new Vector2f[4];
		rectangleBorder[0] = new Vector2f(0.5f,	0.5f);
		rectangleBorder[1] = new Vector2f(0.5f,	s+0.5f);
		rectangleBorder[2] = new Vector2f(s+0.5f,	s+0.5f);
		rectangleBorder[3] = new Vector2f(s+0.5f,	0.5f);		
		
		for (int y = 0; y < grid.getHeight(); y++)
			for (int x = 0; x < grid.getWidth(); x++)
			{
				Cell 		cell = grid.getCell(x, y);
				Colour		colour = Colour.DARK_GREY;
				
				if (colourRules != null)
					colour = colourRules.getColour(cell, colourProperty);
				else if (cell.getValue(colourProperty) > 0) 
					colour = Colour.LIGHT_GREY;
					
				Vector2i p = pos.add((int)(x * s), (int)(y * s));
				Graphics.renderPolygon(gl, p, rectangle, false, colour);
				Graphics.renderPolygon(gl, p, rectangleBorder, true, cellBorderColour);
			}
	}	
}
