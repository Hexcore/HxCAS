package com.hexcore.cas.ui;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Grid;

public class Grid3DWidget<T extends Grid> extends GridWidget<T>
{
	protected int		heightProperty = 0; //< The property that is used to determine the height
	protected float		heightScale = 1.0f;
	protected float		yaw = 0.0f, pitch = -30.0f, zoom = -200.0f;
	protected boolean	cameraMoving = false;
	protected Vector2i	cameraMoveStart = new Vector2i();
	
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
        glu.gluPerspective(45.0f, (float)size.x / size.y, 0.1f, 1000.0f);
        gl2.glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        gl2.glRotatef(yaw, 0.0f, 0.0f, 1.0f);
        gl2.glTranslatef(-grid.getWidth() * tileSize / 2.0f, -grid.getHeight() * tileSize / 2.0f, zoom);
        gl2.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl2.glLoadIdentity();
        
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
	
	@Override
	public boolean handleEvent(Event event, Vector2i position)
	{
		if ((event.type == Event.Type.MOUSE_CLICK) && event.pressed)
		{
			cameraMoveStart.set(event.position);
			cameraMoving = true;
			return true;
		}
		else if ((event.type == Event.Type.LOST_FOCUS) || ((event.type == Event.Type.MOUSE_CLICK) && !event.pressed))
		{
			if (cameraMoving)
			{
				cameraMoving = false;
				return true;
			}
		}
		else if (cameraMoving && (event.type == Event.Type.MOUSE_MOTION))
		{
			yaw += event.position.x - cameraMoveStart.x;
			
			pitch += event.position.y - cameraMoveStart.y;
			pitch = Math.max(Math.min(pitch, 0.0f), -180.0f);
			
			cameraMoveStart.set(event.position);
			return true;
		}
		else if (event.type == Event.Type.MOUSE_SCROLL)
		{
			zoom += event.amount;
		}
		
		return false;
	}
}
