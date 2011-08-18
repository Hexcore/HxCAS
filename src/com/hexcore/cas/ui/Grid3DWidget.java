package com.hexcore.cas.ui;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;

import com.hexcore.cas.math.Vector2f;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.math.Vector3f;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.Grid;

public class Grid3DWidget<T extends Grid> extends GridWidget<T>
{
	public class Slice
	{
		public int		colourProperty;
		public int		heightProperty;
		public float	scale;
		
		public Slice(int heightProperty, float scale) 
		{
			this.colourProperty = -1;
			this.heightProperty = heightProperty;
			this.scale = scale;
		}		
		
		public Slice(int colourProperty, int heightProperty, float scale) 
		{
			this.colourProperty = colourProperty;
			this.heightProperty = heightProperty;
			this.scale = scale;
		}
	}
	
	protected ArrayList<Slice>	slices;
	
	protected boolean	drawGrid = true;
	
	protected float		yaw = 0.0f, pitch = -30.0f;
	protected Vector3f	cameraPosition;
	
	protected boolean	cameraMoving = false;
	protected Vector2i	cameraMoveStart = new Vector2i();
	
	public Grid3DWidget(Vector2i size, T grid, int tileSize)
	{
		this(new Vector2i(), size, grid, tileSize);
	}

	public Grid3DWidget(Vector2i position, Vector2i size, T grid, int tileSize)
	{
		super(position, size, grid, tileSize);
		cameraPosition = new Vector3f(grid.getWidth() * tileSize / 2.0f, grid.getHeight() * tileSize / 2.0f, 200);
		slices = new ArrayList<Slice>();
	}
	
	@Override
	public boolean canGetFocus() {return true;}

	public void setDrawGrid(boolean state)
	{
		drawGrid = state;
	}
	
	public void addSlice(int heightProperty, float scale)
	{
		slices.add(new Slice(heightProperty, scale));
	}	
	
	public void addSlice(int colourProperty, int heightProperty, float scale)
	{
		slices.add(new Slice(colourProperty, heightProperty, scale));
	}
	
	public void clearSlices()
	{
		slices.clear();
	}
	
	@Override
	public void update(Vector2i position, float delta)
	{
		if (window.getKeyState(KeyEvent.VK_UP))
		{
			cameraPosition.x += Math.sin(yaw * Math.PI / 180.0f) * 50.0f * delta;
			cameraPosition.y += Math.cos(yaw * Math.PI / 180.0f) * 50.0f * delta;
		}
		if (window.getKeyState(KeyEvent.VK_DOWN))
		{
			cameraPosition.x -= Math.sin(yaw * Math.PI / 180.0f) * 50.0f * delta;
			cameraPosition.y -= Math.cos(yaw * Math.PI / 180.0f) * 50.0f * delta;
		}
		if (window.getKeyState(KeyEvent.VK_LEFT))
		{
			cameraPosition.x -= Math.cos(yaw * Math.PI / 180.0f) * 50.0f * delta;
			cameraPosition.y += Math.sin(yaw * Math.PI / 180.0f) * 50.0f * delta;
		}
		if (window.getKeyState(KeyEvent.VK_RIGHT))
		{
			cameraPosition.x += Math.cos(yaw * Math.PI / 180.0f) * 50.0f * delta;
			cameraPosition.y -= Math.sin(yaw * Math.PI / 180.0f) * 50.0f * delta;
		}
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
        glu.gluPerspective(45.0f, (float)size.x / size.y, 2.0f, 4096.0f);
        gl2.glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        gl2.glRotatef(yaw, 0.0f, 0.0f, 1.0f);
        gl2.glTranslatef(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);
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
			window.requestFocus(this);
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
			cameraPosition.z += event.amount;
		}
		
		return false;
	}
	
	protected void renderColumn(GL gl, Vector2f position, Cell cell, Vector2f[] shape)
	{
		float		startHeight = 0.0f;
		boolean		bottom = true;
		
		for (Slice slice : slices)
		{
			Colour		colour = Colour.DARK_GREY;
			int			colProperty = (slice.colourProperty < 0) ? colourProperty : slice.colourProperty;
			float		height = (float)cell.getValue(slice.heightProperty) * slice.scale;
			
			if ((height <= 0.0f) && !bottom) continue;
			bottom = false;
			
			if (colourRules != null)
				colour = colourRules.getColour(cell, colProperty);
			else if (cell.getValue(colourProperty) > 0) 
				colour = Colour.LIGHT_GREY;
											
			render3DPolygon(gl, position, shape, startHeight, height, colour);
			startHeight += height;
		}
	}
	
	protected void render3DPolygon(GL gl, Vector2f pos, Vector2f[] polygon, float startHeight, float height, Colour colour)
	{
		GL2 gl2 = gl.getGL2();
		height += startHeight;
		
		window.applyColour(gl2, colour);
				
		if (drawGrid)
		{
			gl2.glEnable(GL2.GL_POLYGON_OFFSET_FILL);
			gl2.glPolygonOffset(0.5f, 0.5f);
		}
		
		gl2.glBegin(GL.GL_TRIANGLE_FAN);
			gl2.glNormal3f(0.0f, 0.0f, 1.0f);
			for (Vector2f v : polygon) gl2.glVertex3f(pos.x+v.x, pos.y+v.y, height);
		gl2.glEnd();			
		
		for (int i = 0; i < polygon.length; i++)
		{
			Vector2f f = polygon[i];
			Vector2f s = polygon[(i == polygon.length - 1) ? 0 : i + 1];
			Vector2f normal = (f.subtract(s)).getPerpendicular().getNormalised();
			gl2.glBegin(GL.GL_TRIANGLE_STRIP);
				gl2.glNormal3f(normal.x, normal.y, 0.0f);
				gl2.glVertex3f(pos.x+f.x, pos.y+f.y, startHeight);
				gl2.glVertex3f(pos.x+f.x, pos.y+f.y, height);
				gl2.glVertex3f(pos.x+s.x, pos.y+s.y, startHeight);
				gl2.glVertex3f(pos.x+s.x, pos.y+s.y, height);
			gl2.glEnd();
		}
		
		if (drawGrid) 
		{
			gl2.glDisable(GL2.GL_POLYGON_OFFSET_LINE);

			window.applyColour(gl2, Colour.BLACK);
						
			gl2.glBegin(GL.GL_LINE_LOOP);
			for (Vector2f v : polygon) gl2.glVertex3f(pos.x+v.x, pos.y+v.y, height);
			gl2.glEnd();	
			
			if (height > startHeight)
			{
				gl2.glBegin(GL.GL_LINES);
				for (Vector2f v : polygon)
				{
					gl2.glVertex3f(pos.x+v.x, pos.y+v.y, startHeight);
					gl2.glVertex3f(pos.x+v.x, pos.y+v.y, height);	
				}
				gl2.glEnd();
			}
		}
	}
}
