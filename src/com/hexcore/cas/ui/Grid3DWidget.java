package com.hexcore.cas.ui;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Grid;

public class Grid3DWidget<T extends Grid> extends GridWidget<T>
{
	protected int	heightProperty = 0; //< The property that is used to determine the height
	protected float	heightScale = 1.0f;
	
	public Grid3DWidget(Vector2i size, T grid, int tileSize)
	{
		super(size, grid, tileSize);
	}

	public Grid3DWidget(Vector2i position, Vector2i size, T grid, int tileSize)
	{
		super(position, size, grid, tileSize);
	}
	
	public void setHeightProperty(int propertyIndex)
	{
		heightProperty = propertyIndex;
	}
	
	public void setHeightScale(float scale)
	{
		heightScale = scale;
	}	
	
	@Override
	public void render(GL gl, Vector2i position)
	{
		Vector2i pos = this.position.add(position);
		
		window.renderRectangle(gl, pos, size, Colour.BLACK);
		window.setViewport(gl, pos, size);
		
		GL2 	gl2 = gl.getGL2();
		GLU		glu = new GLU();
		
        gl2.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl2.glLoadIdentity();
        glu.gluPerspective(60.0f, (float)size.x / size.y, 0.1f, 1000.0f);
        gl2.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl2.glLoadIdentity();
        gl2.glTranslatef(0.0f, 120.0f, 0.0f);
        gl2.glRotatef(-30.0f, 1.0f, 0.0f, 0.0f);
        gl2.glTranslatef(-grid.getWidth() * tileSize / 2.0f, -grid.getHeight() * tileSize / 2.0f, -200.0f);
        
        gl2.glEnable(GL2.GL_LIGHTING);
        gl2.glEnable(GL2.GL_LIGHT0);
        gl2.glEnable(GL2.GL_COLOR_MATERIAL);
        gl2.glEnable(GL.GL_DEPTH_TEST);
        
        gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, Colour.WHITE.toFloatBuffer());

        render3D(gl);
		
		window.resetView(gl2);
		gl2.glDisable(GL2.GL_LIGHTING);
		gl2.glDisable(GL.GL_DEPTH_TEST);
	}
	
	protected void render3D(GL gl)
	{
		
	}
}
