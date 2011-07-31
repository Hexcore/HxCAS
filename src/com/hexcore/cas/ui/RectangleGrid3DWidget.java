package com.hexcore.cas.ui;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

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
		GL2 		gl2 = gl.getGL2();
		Vector2i	s = new Vector2i(tileSize, tileSize);
		
		for (int y = 0; y < grid.getHeight(); y++)
			for (int x = 0; x < grid.getWidth(); x++)
			{
				Cell 		cell = grid.getCell(x, y);
				Colour		colour = Colour.DARK_GREY;
				float		height = cell.getValue(heightProperty) * heightScale;
				
				if (colourRule != null)
					colour = colourRule.getColour(cell.getValue(colourProperty));
				else if (cell.getValue(colourProperty) > 0) 
					colour = Colour.LIGHT_GREY;
					
				Vector2i	p = new Vector2i(x * tileSize, y * tileSize);
				
				if (drawGrid)
				{
					gl2.glEnable(GL2.GL_POLYGON_OFFSET_FILL);
					gl2.glPolygonOffset(0.5f, 0.5f);
				}
				
				window.applyColour(gl2, colour);
				gl2.glBegin(GL.GL_TRIANGLE_STRIP);
					gl2.glNormal3f(0.0f, 0.0f, 1.0f);
					gl2.glVertex3f(p.x, p.y, height);
					gl2.glVertex3f(p.x+s.x, p.y, height);
					gl2.glVertex3f(p.x, p.y+s.y, height);
					gl2.glVertex3f(p.x+s.x, p.y+s.y, height);
				gl2.glEnd();			
				
				gl2.glBegin(GL.GL_TRIANGLE_STRIP);
					gl2.glNormal3f(0.0f,-1.0f, 0.0f);
					gl2.glVertex3f(p.x, p.y, 0.0f);
					gl2.glVertex3f(p.x, p.y, height);
					gl2.glVertex3f(p.x+s.x, p.y, 0.0f);
					gl2.glVertex3f(p.x+s.x, p.y, height);
				gl2.glEnd();
				
				gl2.glBegin(GL.GL_TRIANGLE_STRIP);
					gl2.glNormal3f(1.0f, 0.0f, 0.0f);
					gl2.glVertex3f(p.x+s.x, p.y, 0.0f);
					gl2.glVertex3f(p.x+s.x, p.y, height);
					gl2.glVertex3f(p.x+s.x, p.y+s.y, 0.0f);
					gl2.glVertex3f(p.x+s.x, p.y+s.y, height);
				gl2.glEnd();
					
				gl2.glBegin(GL.GL_TRIANGLE_STRIP);
					gl2.glNormal3f(0.0f, 1.0f, 0.0f);
					gl2.glVertex3f(p.x+s.x, p.y+s.y, 0.0f);
					gl2.glVertex3f(p.x+s.x, p.y+s.y, height);
					gl2.glVertex3f(p.x, p.y+s.y, 0.0f);
					gl2.glVertex3f(p.x, p.y+s.y, height);
				gl2.glEnd();
					
				gl2.glBegin(GL.GL_TRIANGLE_STRIP);
					gl2.glNormal3f(-1.0f, 0.0f, 0.0f);
					gl2.glVertex3f(p.x, p.y+s.y, 0.0f);
					gl2.glVertex3f(p.x, p.y+s.y, height);
					gl2.glVertex3f(p.x, p.y, 0.0f);
					gl2.glVertex3f(p.x, p.y, height);
				gl2.glEnd();
				
				if (drawGrid) 
				{
					gl2.glDisable(GL2.GL_POLYGON_OFFSET_LINE);

					window.applyColour(gl2, Colour.BLACK);
					
					gl2.glDepthFunc(GL.GL_LEQUAL);
					
					gl2.glBegin(GL.GL_LINE_LOOP);
						gl2.glVertex3f(p.x, p.y, height);
						gl2.glVertex3f(p.x+s.x, p.y, height);
						gl2.glVertex3f(p.x+s.x, p.y+s.y, height);
						gl2.glVertex3f(p.x, p.y+s.y, height);
					gl2.glEnd();	
					
					gl2.glBegin(GL.GL_LINES);
						gl2.glVertex3f(p.x, p.y, 0.0f);
						gl2.glVertex3f(p.x, p.y, height);
						gl2.glVertex3f(p.x+s.x, p.y, 0.0f);
						gl2.glVertex3f(p.x+s.x, p.y, height);
						gl2.glVertex3f(p.x+s.x, p.y+s.y, 0.0f);
						gl2.glVertex3f(p.x+s.x, p.y+s.y, height);
						gl2.glVertex3f(p.x, p.y+s.y, 0.0f);
						gl2.glVertex3f(p.x, p.y+s.y, height);
					gl2.glEnd();	
					gl2.glDepthFunc(GL.GL_LESS);
				}
			}
	}
}
