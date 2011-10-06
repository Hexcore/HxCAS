package com.hexcore.cas.ui.toolkit;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2f;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.TriangleGrid;

public class TriangleGridWidget extends Grid2DWidget<TriangleGrid>
{
	public TriangleGridWidget(TriangleGrid grid, int cellSize)
	{
		this(new Vector2i(0, 0), grid, cellSize);
	}

	public TriangleGridWidget(Vector2i position, TriangleGrid grid, int cellSize)
	{
		super(position, new Vector2i((grid.getWidth() * cellSize) / 2 + cellSize / 2, 
								      grid.getHeight() * (int)(cellSize * Math.sqrt(2) * 0.5)),
								      grid, cellSize);
	}
	
	@Override
	public void render(GL gl, Vector2i position)
	{
		Vector2i pos = this.position.add(position);
		Graphics.renderRectangle(gl, pos, size, backgroundColour);
		
		float s = cellSize * zoom;
		float r = s / 2;
		float h = (float)(s * Math.sqrt(2) * 0.5);
		
		Vector2f[]	downTriangle = new Vector2f[3];
		downTriangle[0] = new Vector2f(0.0f, 	0.0f);
		downTriangle[1] = new Vector2f(r+r, 	0.0f);
		downTriangle[2] = new Vector2f(r,		h);
		
		Vector2f[]	downTriangleBorder = new Vector2f[3];
		downTriangleBorder[0] = new Vector2f(0.5f, 		0.5f);
		downTriangleBorder[1] = new Vector2f(r+r+0.5f, 	0.5f);
		downTriangleBorder[2] = new Vector2f(r+0.5f,	h+0.5f);		
		
		Vector2f[]	upTriangle = new Vector2f[3];
		upTriangle[0] = new Vector2f(r, 	0.0f);
		upTriangle[1] = new Vector2f(r+r, 	h);
		upTriangle[2] = new Vector2f(0.0f,	h);
		
		Vector2f[]	upTriangleBorder = new Vector2f[3];
		upTriangleBorder[0] = new Vector2f(r+0.5f, 		0.5f);
		upTriangleBorder[1] = new Vector2f(r+r+0.5f, 	h+0.5f);
		upTriangleBorder[2] = new Vector2f(0.5f,		h+0.5f);		
		
		for (Vector2f v : downTriangle) v.inc(0.5f, 0.5f);
		for (Vector2f v : upTriangle) v.inc(0.5f, 0.5f);
		
		for (int y = 0; y < grid.getHeight(); y++)
			for (int x = 0; x < grid.getWidth(); x++)
			{
				Cell		cell = grid.getCell(x, y);
				Colour		colour = Colour.DARK_GREY;
				Vector2i 	p = pos.add((int)(x * r), (int)(y * h));
				
				if (colourRules != null)
					colour = colourRules.getColour(cell, colourProperty);
				else if (cell.getValue(colourProperty) > 0) 
					colour = Colour.LIGHT_GREY;
				
				if ((x & 1) == (y & 1)) // Checker-board pattern
				{
					Graphics.renderPolygon(gl, p, downTriangle, false, colour);
					Graphics.renderPolygon(gl, p, downTriangleBorder, true, cellBorderColour);
				}
				else
				{
					Graphics.renderPolygon(gl, p, upTriangle, false, colour);
					Graphics.renderPolygon(gl, p, upTriangleBorder, true, cellBorderColour);
				}
		
			}
	}
}
