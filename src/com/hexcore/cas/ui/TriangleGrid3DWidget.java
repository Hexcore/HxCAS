package com.hexcore.cas.ui;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2f;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.TriangleGrid;

public class TriangleGrid3DWidget extends Grid3DWidget<TriangleGrid>
{
	public TriangleGrid3DWidget(Vector2i size, TriangleGrid grid, int tileSize)
	{
		super(size, grid, tileSize);
	}

	@Override
	public void render3D(GL gl)
	{        
		float 	r = tileSize / 2.0f;
		float 	h = tileSize * (float)Math.sqrt(2) * 0.5f;
				
		for (int y = 0; y < grid.getHeight(); y++)
			for (int x = 0; x < grid.getWidth(); x++)
			{
				Cell 		cell = grid.getCell(x, grid.getHeight() - y - 1);
				Colour		colour = Colour.DARK_GREY;
				float		height = cell.getValue(heightProperty) * heightScale;
				
				if (colourRule != null)
					colour = colourRule.getColour(cell.getValue(colourProperty));
				else if (cell.getValue(colourProperty) > 0) 
					colour = Colour.LIGHT_GREY;
					
				Vector2f 	p = new Vector2f(x * r, y * h);
				
				Vector2f[]	downTriangle = new Vector2f[3];
				downTriangle[0] = new Vector2f(p.x, 	p.y);
				downTriangle[1] = new Vector2f(p.x+r+r, p.y);
				downTriangle[2] = new Vector2f(p.x+r,	p.y+h);
				
				Vector2f[]	upTriangle = new Vector2f[3];
				upTriangle[0] = new Vector2f(p.x+r, 	p.y);
				upTriangle[1] = new Vector2f(p.x+r+r,	p.y+h);
				upTriangle[2] = new Vector2f(p.x,		p.y+h);
				
				if ((x & 1) == (y & 1)) // Checker-board pattern
					render3DPolygon(gl, colour, downTriangle, height);
				else
					render3DPolygon(gl, colour, upTriangle, height);
			}
	}
}
