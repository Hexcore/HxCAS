package com.hexcore.cas.ui;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.TriangleGrid;

public class TriangleGridWidget extends GridWidget<TriangleGrid>
{
	public TriangleGridWidget(TriangleGrid grid, int tileSize)
	{
		this(new Vector2i(0, 0), grid, tileSize);
	}

	public TriangleGridWidget(Vector2i position, TriangleGrid grid, int tileSize)
	{
		super(position, new Vector2i((grid.getWidth() * tileSize) / 2 + tileSize / 2, 
								      grid.getHeight() * (int)(tileSize * Math.sqrt(2) * 0.5)),
								      grid, tileSize);
	}
	
	@Override
	public void render(GL gl, Vector2i position)
	{
		int r = tileSize / 2;
		int h = (int)(tileSize * Math.sqrt(2) * 0.5);
		
		Vector2i 	pos = this.position.add(position);
		Vector2i[]	downTriangle = new Vector2i[3];
		downTriangle[0] = new Vector2i(0, 	0);
		downTriangle[1] = new Vector2i(r+r, 0);
		downTriangle[2] = new Vector2i(r,	h);
		
		Vector2i[]	upTriangle = new Vector2i[3];
		upTriangle[0] = new Vector2i(r, 	0);
		upTriangle[1] = new Vector2i(r+r, h);
		upTriangle[2] = new Vector2i(0,	h);
		
		for (int y = 0; y < grid.getHeight(); y++)
			for (int x = 0; x < grid.getWidth(); x++)
			{
				Cell		cell = grid.getCell(x, y);
				Colour		colour = Colour.DARK_GREY;
				Vector2i 	p = new Vector2i(x * r, y * h);
				
				if (colourRule != null)
					colour = colourRule.getColour(cell.getValue(0));
				else if (cell.getValue(0) > 0) 
					colour = Colour.LIGHT_GREY;
				
				if ((x & 1) == (y & 1)) // Checker-board pattern
				{
					window.renderPolygon(gl, pos.add(p), downTriangle, false, colour);
					window.renderPolygon(gl, pos.add(p), downTriangle, true, Colour.WHITE);
				}
				else
				{
					window.renderPolygon(gl, pos.add(p), upTriangle, false, colour);
					window.renderPolygon(gl, pos.add(p), upTriangle, true, Colour.WHITE);
				}
		
			}
	}
}
