package com.hexcore.cas.ui.toolkit;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2f;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.HexagonGrid;

public class HexagonGridWidget extends Grid2DWidget<HexagonGrid>
{
	public HexagonGridWidget(HexagonGrid grid, int cellSize)
	{
		this(new Vector2i(0, 0), grid, cellSize);
	}

	public HexagonGridWidget(Vector2i position, HexagonGrid grid, int cellSize)
	{
		super(new Vector2i(grid.getWidth() * (cellSize + cellSize / 2) + cellSize / 2, 
						   grid.getHeight() * 2 * (int)(cellSize * 0.866f) + (int)(cellSize * 0.866f)),
						   grid, cellSize);
	}

	@Override
	public void render(GL gl, Vector2i position)
	{
		Vector2i pos = this.position.add(position);
		Graphics.renderRectangle(gl, pos, size, backgroundColour);
		
		float s = cellSize * zoom;
		float h = s / 2;
		float r = (float)(s * Math.cos(30.0 * Math.PI / 180.0));
		
		Vector2f[]	hexagon = new Vector2f[6];
		hexagon[0] = new Vector2f(0.0f,	r);
		hexagon[1] = new Vector2f(h,	r+r);
		hexagon[2] = new Vector2f(h+s,	r+r);
		hexagon[3] = new Vector2f(h+s+h,r);
		hexagon[4] = new Vector2f(h+s,	0.0f);
		hexagon[5] = new Vector2f(h,	0.0f);
		
		for (Vector2f v : hexagon) v.inc(0.5f, 0.5f);
		
		Vector2f cpos = new Vector2f(pos); 
		for (int y = 0; y < grid.getHeight(); y++)
			for (int x = 0; x < grid.getWidth(); x++)
			{
				Cell 		cell = grid.getCell(x, y);
				Colour		colour = Colour.DARK_GREY;
				Vector2f	p = cpos.add(x*(s+h), y*r*2);
				
				if ((x & 1) == 1) p.inc(0, r);
				
				if (colourRules != null)
					colour = colourRules.getColour(cell, colourProperty);
				else if (cell.getValue(colourProperty) > 0) 
					colour = Colour.LIGHT_GREY;
					
				Graphics.renderPolygon(gl, new Vector2i(p), hexagon, false, colour);
				Graphics.renderPolygon(gl, new Vector2i(p), hexagon, true, cellBorderColour);
			}
	}	
}