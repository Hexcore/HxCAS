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
	public void loadGeometry(GL gl)
	{        
		float 	r = tileSize / 2.0f;
		float 	h = tileSize * (float)Math.sqrt(2) * 0.5f;
		
		Vector2f[]	downTriangle = new Vector2f[3];
		downTriangle[0] = new Vector2f(0.0f,	0.0f);
		downTriangle[1] = new Vector2f(r+r, 	0.0f);
		downTriangle[2] = new Vector2f(r,		h);
		
		Vector2f[]	upTriangle = new Vector2f[3];
		upTriangle[0] = new Vector2f(r, 	0.0f);
		upTriangle[1] = new Vector2f(r+r,	h);
		upTriangle[2] = new Vector2f(0.0f,	h);
		
		resetVertexBuffer(gl, 3);
				
		for (int y = 0; y < grid.getHeight(); y++)
			for (int x = 0; x < grid.getWidth(); x++)
			{
				Cell 		cell = grid.getCell(x, y);				
				Vector2f 	p = new Vector2f(x * r, y * h);
								
				if ((x & 1) == (y & 1)) // Checker-board pattern
					addColumn(p, cell, downTriangle);
				else
					addColumn(p, cell, upTriangle);
			}
		
		loadVertexBuffer(gl);
	}
}
