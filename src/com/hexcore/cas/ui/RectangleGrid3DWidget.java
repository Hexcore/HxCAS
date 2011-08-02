package com.hexcore.cas.ui;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2f;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.RectangleGrid;

public class RectangleGrid3DWidget extends Grid3DWidget<RectangleGrid>
{
	public RectangleGrid3DWidget(Vector2i size, RectangleGrid grid, int tileSize)
	{
		super(size, grid, tileSize);
	}

	@Override
	public void render3D(GL gl)
	{
		Vector2i	s = new Vector2i(tileSize, tileSize);
		
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
					
				Vector2i	p = new Vector2i(x * tileSize, y * tileSize);

				Vector2f[]	rect = new Vector2f[4];
				rect[0] = new Vector2f(p.x, p.y);
				rect[1] = new Vector2f(p.x+s.x, p.y);
				rect[2] = new Vector2f(p.x+s.x, p.y+s.y);
				rect[3] = new Vector2f(p.x, p.y+s.y);
				
				render3DPolygon(gl, colour, rect, height);
			}
	}
}
