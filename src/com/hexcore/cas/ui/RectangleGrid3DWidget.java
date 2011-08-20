package com.hexcore.cas.ui;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2f;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.RectangleGrid;

public class RectangleGrid3DWidget extends Grid3DWidget<RectangleGrid>
{
	private Grid prevGrid = null;
	
	public RectangleGrid3DWidget(Vector2i size, RectangleGrid grid, int tileSize)
	{
		super(size, grid, tileSize);
	}
	
	public void loadGeometry(GL gl)
	{
		float	s = tileSize;
		
		Vector2f[]	rect = new Vector2f[4];
		rect[0] = new Vector2f(0.0f, 0.0f);
		rect[1] = new Vector2f(s, 0.0f);
		rect[2] = new Vector2f(s, s);
		rect[3] = new Vector2f(0.0f, s);
		
		resetVertexBuffer(gl, 4);

		for (int y = 0; y < grid.getHeight(); y++)
			for (int x = 0; x < grid.getWidth(); x++)
			{
				Cell 		cell = grid.getCell(x, grid.getHeight() - y - 1);
				Vector2f	p = new Vector2f(x * tileSize, y * tileSize);
				
				addColumn(p, cell, rect);
			}
		
		loadVertexBuffer(gl);	
	}

	@Override
	public void render3D(GL gl)
	{
		float	s = tileSize;
		
		Vector2f[]	rect = new Vector2f[4];
		rect[0] = new Vector2f(0.0f, 0.0f);
		rect[1] = new Vector2f(s, 0.0f);
		rect[2] = new Vector2f(s, s);
		rect[3] = new Vector2f(0.0f, s);
		
		if (window.isDebugLayout())
		{
			for (int y = 0; y < grid.getHeight(); y++)
				for (int x = 0; x < grid.getWidth(); x++)
				{
					Cell 		cell = grid.getCell(x, grid.getHeight() - y - 1);
					Vector2f	p = new Vector2f(x * tileSize, y * tileSize);
					
					renderColumn(gl, p, cell, rect);
				}	
		}
		else
		{
			if (prevGrid != grid)
			{
				loadGeometry(gl);
				prevGrid = grid;
			}
			
			renderVertexBuffer(gl);
		}
	}
}
