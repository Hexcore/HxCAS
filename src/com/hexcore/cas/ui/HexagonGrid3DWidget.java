package com.hexcore.cas.ui;

import javax.media.opengl.GL;
import com.hexcore.cas.math.Vector2f;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.HexagonGrid;

public class HexagonGrid3DWidget extends Grid3DWidget<HexagonGrid>
{
	public HexagonGrid3DWidget(Vector2i size, HexagonGrid grid, int tileSize)
	{
		super(size, grid, tileSize);
	}

	@Override
	public void render3D(GL gl)
	{        
		float	s = tileSize;
		float	h = tileSize / 2;
		float	r = tileSize * (float)Math.cos(30.0 * Math.PI / 180.0);
		
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
					
				Vector2f	p = new Vector2f(x*r*2, y*(s+h));
				if ((y & 1) == 1) p.inc(r, 0);
				
				Vector2f[]	hexagon = new Vector2f[6];
				hexagon[0] = new Vector2f(p.x+r, 	p.y);
				hexagon[1] = new Vector2f(p.x+r+r, 	p.y+h);
				hexagon[2] = new Vector2f(p.x+r+r,	p.y+h+s);
				hexagon[3] = new Vector2f(p.x+r,	p.y+h+s+h);
				hexagon[4] = new Vector2f(p.x, 		p.y+h+s);
				hexagon[5] = new Vector2f(p.x, 		p.y+h);
				
				render3DPolygon(gl, colour, hexagon, height);
			}
	}
}
