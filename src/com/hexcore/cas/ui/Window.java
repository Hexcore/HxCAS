package com.hexcore.cas.ui;

import java.awt.Dimension;
import java.awt.FileDialog;

import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.GLMatrixFunc;

import com.hexcore.cas.math.Rectf;
import com.hexcore.cas.math.Vector2f;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.ui.Theme.BorderShape;
import com.jogamp.opengl.util.FPSAnimator;

public class Window extends Layout implements GLEventListener, MouseMotionListener, MouseListener, MouseWheelListener, KeyListener
{	
	private Frame 		frame;
	private GLCanvas	canvas;
	private FPSAnimator	animator;
	private	Theme		theme;
			
	private Vector2i	defaultMargin;
	private Widget		focusedWidget = null;
	
	private ArrayList<WindowEventListener>	eventListeners;
	
	private boolean[]		keyState;
	private Timer			keyRepeatTimer = new Timer("KeyRepeatFilter");
	private KeyRepeatFilter	keyRepeatFilter = null;

	private	boolean	updateComponents = true;	
	private boolean	initDone = false;
	private boolean	debugLayout = false;
	
	public Window(String title)
	{
		this(title, 800, 600);
	}
	
	public Window(String title, int width, int height)
	{
		super(new Vector2i(width, height));
				
		keyState = new boolean[1024];
		for (int i = 0; i < 1024; i++) keyState[i] = false;
		
		defaultMargin = new Vector2i(8, 8);
		components = new ArrayList<Widget>();
		eventListeners = new ArrayList<WindowEventListener>();
		setWindow(this);
		
		
		theme = new Theme(this);
		
		//////////////////
		
		GLCapabilities capabilites = new GLCapabilities(GLProfile.get(GLProfile.GL2));
		capabilites.setDoubleBuffered(true);
		capabilites.setSampleBuffers(true);
		capabilites.setNumSamples(4);
		
		canvas = new GLCanvas(capabilites);
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setAutoSwapBufferMode(false);

		frame = new Frame(title);
			
		frame.setSize(width, height);
		frame.setResizable(true);
		frame.addWindowListener(new WindowListener());
		frame.add(canvas);
		frame.pack();
	}
	
	public boolean isDebugLayout()
	{
		return debugLayout;
	}
	
	public void setDebugLayout(boolean state)
	{
		debugLayout = state;
	}
	
	public void show()
	{
		frame.setVisible(true);
		frame.addMouseListener(this);
		frame.addMouseMotionListener(this);
		frame.addMouseWheelListener(this);
		frame.addKeyListener(this);
		canvas.addKeyListener(this);
				
		/////////////////////
		
		canvas.requestFocus();
		canvas.addGLEventListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
						
		animator = new FPSAnimator(canvas, 60);
		animator.start();
	}
	
	public void update(float delta)
	{
		update(new Vector2i(), delta);
		for (Widget component : components) component.update(new Vector2i(), delta);
	}
	
	public void exit() 
	{
		frame.dispose();
		System.exit(0);
	}
	
	public Vector2i getDefaultMargin()
	{
		return defaultMargin;
	}
	
	public void loadTheme(String filename)
	{
		theme.loadFromFile(filename);
	}
	
	public Theme getTheme()
	{
		return theme;
	}
	
	public long getTime()
	{
		return animator.getCurrentTime();
	}
		
	public String askUserForFile(String title)
	{
		FileDialog dialog = new FileDialog(frame, title);
		dialog.setVisible(true);
		return dialog.getFile();
	}
	
	public float getAspectRatio()
	{
		return (float)size.x / size.y;
	}
	
	public void setViewport(GL gl, Vector2i position, Vector2i size)
	{
		GL2 gl2 = gl.getGL2();
		
		int width = size.x > 0 ? size.x : 1;
		int height = size.y > 0 ? size.y : 1;
		gl2.glViewport(position.x, this.size.y - (height + position.y), width, height);
	}
	
	public void setClipping(GL gl, Vector2i position, Vector2i size)
	{
		GL2 gl2 = gl.getGL2();
		
		gl2.glViewport(position.x, this.size.y - (size.y + position.y), size.x, size.y);
		
        gl2.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl2.glLoadIdentity();
        gl2.glOrtho(position.x, position.x + size.x, position.y + size.y, position.y, -1000.0, 1000.0);
        gl2.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl2.glLoadIdentity();
	}
	
	public void resetView(GL gl)
	{
		GL2 gl2 = gl.getGL2();
		
		gl2.glViewport(0, 0, size.x, size.y);
		
        gl2.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl2.glLoadIdentity();
        gl2.glOrtho(0.0, size.x, size.y, 0.0, -1000.0, 1000.0);
        gl2.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl2.glLoadIdentity();
	}
	
	public void giveUpFocus(Widget widget)
	{
		if (focusedWidget != null)
		{
			Event event = new Event(Event.Type.LOST_FOCUS);
			focusedWidget.receiveEvent(event);
			focusedWidget = null;
		}
	}
	
	public void requestFocus(Widget widget)
	{
		if ((widget == null) || !widget.canGetFocus()) return;
		
		giveUpFocus(focusedWidget);
		focusedWidget = widget;
		
		Event event = new Event(Event.Type.GAINED_FOCUS);
		focusedWidget.receiveEvent(event);
	}
	
	public void toggleFocus(Widget widget)
	{
		if (focusedWidget == widget)
			giveUpFocus(widget);
		else
			requestFocus(widget);
	}
				
	public boolean getKeyState(int keyCode)
	{
		if (keyCode > 1024) return false;
		return keyState[keyCode];
	}
	
	@Override
	public void display(GLAutoDrawable drawable)
	{
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0.0, size.x, size.y, 0.0, -1000.0, 1000.0);
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();
                
        update(1.0f / 60.0f);
        render(drawable);
        drawable.swapBuffers();
	}
	
	@Override
	public void dispose(GLAutoDrawable drawable)
	{

	}
	
	@Override
	public void init(GLAutoDrawable drawable)
	{
		GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.93f, 0.93f, 0.93f, 1.0f);
        gl.glClearDepth(1.0f);   
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
       
        if (!initDone)
        {
        	for (WindowEventListener listener : eventListeners)
        		listener.initialise();
        	
        	initDone = true;
        }
	}
	
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
	{
		if (width < 800) width = 800;
		if (height < 600) height = 600;
		
		size = new Vector2i(width, height);
        relayout();
	}
	
	private int addCornerToArray(int index, Vector2f[] array, Vector2f start, int radius, int quarter, boolean isBorder)
	{
		for (int i = 0; i <= radius; i++)
		{
			double angle = ((double)i / radius) * Math.PI / 2.0 + (Math.PI / 2) * (quarter - 2);
			Vector2f p = new Vector2f(start.x + (float)(Math.sin(angle) * radius), start.y + (float)(Math.cos(angle) * radius));
			
			if (isBorder)
				switch (quarter)
				{
					case 0: p.inc( 0.5f,  0.5f); break;
					case 1: p.inc( 0.5f, -0.5f); break;
					case 2: p.inc(-0.5f, -0.5f); break;
					case 3: p.inc(-0.5f,  0.5f); break;
				}
			
			array[index++] = p;
		}
		return index;
	}
		
	private Vector2f[] createRoundedRectangle(Vector2i pos, Vector2i size, int radius, BorderShape borderShape, boolean isBorder)
	{
		int	corners = borderShape.getNumCorners();
		
		Vector2f[]	points = new Vector2f[(radius + 1) * corners + (4 - corners)];
		int	index = 0;
		
		if (borderShape.has(BorderShape.TOP_LEFT) && (radius > 0))
			index = addCornerToArray(index, points, new Vector2f(radius, radius), radius, 0, isBorder);
		else
			points[index++] = isBorder ? new Vector2f(0.5f, 0.5f) : new Vector2f(0, 0);
		
		if (borderShape.has(BorderShape.BOTTOM_LEFT) && (radius > 0))
			index = addCornerToArray(index, points, new Vector2f(radius, size.y - radius), radius, 1, isBorder);
		else
			points[index++] = isBorder ? new Vector2f(0.5f, size.y-0.5f) : new Vector2f(0, size.y);
		
		if (borderShape.has(BorderShape.BOTTOM_RIGHT) && (radius > 0))
			index = addCornerToArray(index, points, new Vector2f(size.x - radius, size.y -radius), radius, 2, isBorder);
		else
			points[index++] = isBorder ? new Vector2f(size.x-0.5f, size.y-0.5f) : new Vector2f(size.x, size.y);
		
		if (borderShape.has(BorderShape.TOP_RIGHT) && (radius > 0))
			index = addCornerToArray(index, points, new Vector2f(size.x - radius, radius), radius, 3, isBorder);
		else
			points[index++] = isBorder ? new Vector2f(size.x-0.5f, 0.5f) : new Vector2f(size.x, 0);
		
		return points;
	}
	
	public void renderRoundedBorderedRectangle(GL gl, Vector2i pos, Vector2i size, int radius, Fill fill, Fill border)
	{		
		Vector2f[]	points = createRoundedRectangle(pos, size, radius, new BorderShape(BorderShape.ALL_CORNERS), true);
		renderPolygon(gl, pos, points, false, fill);
		renderPolygon(gl, pos, points, true, border);
	}	
	
	public void renderRoundedBorderedRectangle(GL gl, Vector2i pos, Vector2i size, int radius, BorderShape borderShape, Fill fill, Fill border)
	{		
		Vector2f[]	points = createRoundedRectangle(pos, size, radius, borderShape, true);
		renderPolygon(gl, pos, points, false, fill);
		renderPolygon(gl, pos, points, true, border);
	}	
	
	public void renderRoundedRectangle(GL gl, Vector2i pos, Vector2i size, int radius, Colour colour)
	{		
		Vector2f[]	points = createRoundedRectangle(pos, size, radius, new BorderShape(BorderShape.ALL_CORNERS), false);
		renderPolygon(gl, pos, points, false, colour);
	}
	
	public void renderRoundedRectangle(GL gl, Vector2i pos, Vector2i size, int radius, BorderShape borderShape, Fill fill)
	{
		Vector2f[]	points = createRoundedRectangle(pos, size, radius, borderShape, false);
		renderPolygon(gl, pos, points, false, fill);
	}
	
	public void renderRoundedRectangle(GL gl, Vector2i pos, Vector2i size, int radius, Fill fill)
	{
		Vector2f[]	points = createRoundedRectangle(pos, size, radius, new BorderShape(BorderShape.ALL_CORNERS), false);
		renderPolygon(gl, pos, points, false, fill);
	}	
	
	public void renderRoundedBorder(GL gl, Vector2i pos, Vector2i size, int radius, Colour colour)
	{
		Vector2f[]	points = createRoundedRectangle(pos, size, radius, new BorderShape(BorderShape.ALL_CORNERS), true);
		renderPolygon(gl, pos, points, true, colour);
	}	
	
	public void renderRoundedBorder(GL gl, Vector2i pos, Vector2i size, int radius, BorderShape borderShape, Fill fill)
	{
		Vector2f[]	points = createRoundedRectangle(pos, size, radius, borderShape, true);
		renderPolygon(gl, pos, points, true, fill);
	}
	
	public void renderPolygon(GL gl, Vector2i pos, Vector2f[] vertices, boolean outline, Colour colour)
	{
		GL2 gl2 = gl.getGL2();
		
		applyColour(gl2, colour);
        gl2.glBegin(outline ? GL.GL_LINE_LOOP : GL.GL_TRIANGLE_FAN);
        for (Vector2f vertex : vertices) gl2.glVertex2f(pos.x + vertex.x, pos.y + vertex.y);
		gl2.glEnd();
	}
	
	public void renderPolygon(GL gl, Vector2i pos, Vector2f[] vertices, boolean outline, Fill fill)
	{
		if (fill.getType() == Fill.Type.SOLID)
		{
			renderPolygon(gl, pos, vertices, outline, fill.getColour(0));
			return;
		}
		
		if ((fill.getType() != Fill.Type.HORIZONTAL_GRADIENT) && (fill.getType() != Fill.Type.VERTICAL_GRADIENT))
			return;
		
		GL2 gl2 = gl.getGL2();
		
		Colour	colour = fill.getColour(0);
		Rectf	rect = Rectf.getBoundingBox(vertices);
		
        gl2.glBegin(outline ? GL.GL_LINE_LOOP : GL.GL_TRIANGLE_FAN);
        	for (Vector2f vertex : vertices)
        	{        		
        		if (fill.getType() == Fill.Type.VERTICAL_GRADIENT)
        			colour = fill.getColour(0).mix(fill.getColour(1), (float)(vertex.y - rect.position.y) / rect.size.y);
        		else
        			colour = fill.getColour(0).mix(fill.getColour(1), (float)(vertex.x - rect.position.x) / rect.size.x);
        		
        		applyColour(gl2, colour);
        		gl2.glVertex2f(pos.x + vertex.x, pos.y + vertex.y);
        	}
		gl2.glEnd();
	}
		
	public void renderRectangle(GL gl, Vector2i pos, Image image)
	{
		renderRectangle(gl, pos, image.getSize(), image);
	}
	
	public void renderRectangle(GL gl, Vector2i pos, Vector2i size, Image image)
	{
		GL2 gl2 = gl.getGL2();
		
		image.bind();
		
		applyColour(gl2, Colour.WHITE);
        gl2.glBegin(GL.GL_TRIANGLE_STRIP);
	    	gl2.glTexCoord2f(0.0f, 0.0f); gl2.glVertex2f(pos.x,pos.y);
	    	gl2.glTexCoord2f(0.0f, 1.0f); gl2.glVertex2f(pos.x,pos.y+size.y);
	    	gl2.glTexCoord2f(1.0f, 0.0f); gl2.glVertex2f(pos.x+size.x,pos.y);
	    	gl2.glTexCoord2f(1.0f, 1.0f); gl2.glVertex2f(pos.x+size.x,pos.y+size.y);
		gl2.glEnd();
		
		image.unbind();
	}
	
	public void renderRectangle(GL gl, Vector2i pos, Vector2i size, int radius, Fill fill)
	{
		if (fill == null) return;
		
		if (radius <= 0)
		{
			if (fill.getType() == Fill.Type.SOLID)
				renderRectangle(gl, pos, size, fill.getColour(0));
			else if (fill.getType() == Fill.Type.VERTICAL_GRADIENT)
				renderRectangleTB(gl, pos, size, fill.getColour(0), fill.getColour(1));
			else if (fill.getType() == Fill.Type.HORIZONTAL_GRADIENT)
				renderRectangleLR(gl, pos, size, fill.getColour(0), fill.getColour(1));
			else if ((fill.getType() == Fill.Type.IMAGE) || (fill.getType() == Fill.Type.IMAGE_COLOUR))
			{
				Vector2i imagePos = new Vector2i(pos);
				Vector2i imageSize = new Vector2i(size);
				
				if ((fill.getFlags() & Fill.CENTER) != 0)
				{
					imageSize = fill.getImage().getSize();
					imagePos = pos.add(new Vector2i((size.x - imageSize.x) / 2, (size.y - imageSize.y) / 2));
				}
				
				if (fill.getType() == Fill.Type.IMAGE)
					renderRectangle(gl, imagePos, imageSize, fill.getImage());
				else if (fill.getType() == Fill.Type.IMAGE_COLOUR)
				{
					renderRectangle(gl, pos, size, fill.getColour(0));
					renderRectangle(gl, imagePos, imageSize, fill.getImage());	
				}
			}
		}
		else
		{
			if ((fill.getType() == Fill.Type.IMAGE) || (fill.getType() == Fill.Type.IMAGE_COLOUR))
			{
				Vector2i imagePos = new Vector2i(pos);
				Vector2i imageSize = new Vector2i(size);
				
				if ((fill.getFlags() & Fill.CENTER) != 0)
				{
					imageSize = fill.getImage().getSize();
					imagePos = pos.add(new Vector2i((size.x - imageSize.x) / 2, (size.y - imageSize.y) / 2));
				}
				
				if (fill.getType() == Fill.Type.IMAGE)
					renderRectangle(gl, imagePos, imageSize, fill.getImage());
				else if (fill.getType() == Fill.Type.IMAGE_COLOUR)
				{
					renderRoundedRectangle(gl, pos, size, radius, fill.getColour(0));
					renderRectangle(gl, imagePos, imageSize, fill.getImage());	
				}
			}
			else
				renderRoundedRectangle(gl, pos, size, radius, fill);
		}
	}
	
	public void renderRectangle(GL gl, Vector2i pos, Vector2i size, Colour colour)
	{
		GL2 gl2 = gl.getGL2();
		
		applyColour(gl2, colour);
        gl2.glBegin(GL.GL_TRIANGLE_STRIP);
	    	gl2.glVertex2f(pos.x,pos.y);
	    	gl2.glVertex2f(pos.x,pos.y+size.y);
	    	gl2.glVertex2f(pos.x+size.x,pos.y);
	    	gl2.glVertex2f(pos.x+size.x,pos.y+size.y);
		gl2.glEnd();
	}
	
	public void renderRectangleTB(GL gl, Vector2i pos, Vector2i size, Colour top, Colour bottom)
	{
		GL2 gl2 = gl.getGL2();
		
        gl2.glBegin(GL.GL_TRIANGLE_STRIP);
	    	applyColour(gl2, top); 
	    	gl2.glVertex2f(pos.x,pos.y);
	    	gl2.glVertex2f(pos.x+size.x,pos.y);
	    	applyColour(gl2, bottom); 
	    	gl2.glVertex2f(pos.x,pos.y+size.y);
	    	gl2.glVertex2f(pos.x+size.x,pos.y+size.y);
		gl2.glEnd();
	}
	
	public void renderRectangleLR(GL gl, Vector2i pos, Vector2i size, Colour left, Colour right)
	{
		GL2 gl2 = gl.getGL2();
		
        gl2.glBegin(GL.GL_TRIANGLE_STRIP);
	    	applyColour(gl2, left); 
	    	gl2.glVertex2f(pos.x,pos.y);
	    	gl2.glVertex2f(pos.x,pos.y+size.y);
	    	applyColour(gl2, right); 
	    	gl2.glVertex2f(pos.x+size.x,pos.y);
	    	gl2.glVertex2f(pos.x+size.x,pos.y+size.y);
		gl2.glEnd();
	}
	
	public void renderBorder(GL gl, Vector2i pos, Vector2i size, int radius, Fill fill)
	{
		if (fill == null) return;
		
		if (radius <= 0)
		{
			if (fill.getType() == Fill.Type.SOLID)
				renderBorder(gl, pos, size, fill.getColour(0));
			else if (fill.getType() == Fill.Type.VERTICAL_GRADIENT)
				renderBorderTB(gl, pos, size, fill.getColour(0), fill.getColour(1));
			else if (fill.getType() == Fill.Type.HORIZONTAL_GRADIENT)
				renderBorderLR(gl, pos, size, fill.getColour(0), fill.getColour(1));	
		}
		else
			renderRoundedBorder(gl, pos, size, radius, new BorderShape(BorderShape.ALL_CORNERS), fill);
	}
	
	public void renderBorder(GL gl, Vector2i pos, Vector2i size, Colour colour)
	{
		if (colour.a <= 0.0f) return;

		GL2 gl2 = gl.getGL2();
		
		applyColour(gl2, colour); 
	    gl2.glBegin(GL.GL_LINE_LOOP);
	    	gl2.glVertex2f(pos.x+size.x-0.5f,pos.y+0.5f);
	    	gl2.glVertex2f(pos.x+size.x-0.5f,pos.y+size.y-0.5f);
			gl2.glVertex2f(pos.x+0.5f,pos.y+size.y-0.5f);
			gl2.glVertex2f(pos.x+0.5f,pos.y+0.5f);
		gl2.glEnd();			
	}
	
	public void renderBorderTB(GL gl, Vector2i pos, Vector2i size, Colour top, Colour bottom)
	{
		if ((top.a <= 0.0f) && (bottom.a <= 0.0f)) return;
		
		GL2 gl2 = gl.getGL2();

	    gl2.glBegin(GL.GL_LINE_LOOP);
	    	applyColour(gl2, top); 
	    	gl2.glVertex2f(pos.x+0.5f,pos.y+0.5f);
	    	gl2.glVertex2f(pos.x+size.x-0.5f,pos.y+0.5f);
	    	applyColour(gl2, bottom); 
	    	gl2.glVertex2f(pos.x+size.x-0.5f,pos.y+size.y-0.5f);
			gl2.glVertex2f(pos.x+0.5f,pos.y+size.y-0.5f);
		gl2.glEnd();
	}
	
	public void renderBorderLR(GL gl, Vector2i pos, Vector2i size, Colour left, Colour right)
	{
		if ((left.a <= 0.0f) && (right.a <= 0.0f)) return;
		
		GL2 gl2 = gl.getGL2();

	    gl2.glBegin(GL.GL_LINE_LOOP);
	    	applyColour(gl2, left); 
			gl2.glVertex2f(pos.x+0.5f,pos.y+size.y-0.5f);
	    	gl2.glVertex2f(pos.x+0.5f,pos.y+0.5f);
	    	applyColour(gl2, right); 
	    	gl2.glVertex2f(pos.x+size.x-0.5f,pos.y+0.5f);
	    	gl2.glVertex2f(pos.x+size.x-0.5f,pos.y+size.y-0.5f);
		gl2.glEnd();
	}
		
	///////////
	
	protected void applyColour(GL2 gl, Colour colour)
	{
		if (colour == null)
			gl.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
		else
			gl.glColor4f(colour.r, colour.g, colour.b, colour.a);			
	}
	
	///////////
	
	public void addListener(WindowEventListener wel)
	{
		eventListeners.add(wel);
	}
	
	public void sendWindowEvent(Event event)
	{
		for (WindowEventListener listener : eventListeners)
			listener.handleWindowEvent(event);
		
		canvas.display();
	}
		
	private void render(GLAutoDrawable drawable)
	{
		if (updateComponents)
		{
	        for (Widget component : components) component.relayout();
	        updateComponents = false;
		}
		
		GL gl = drawable.getGL();
		render(gl, new Vector2i(0, 0));
		if (focusedWidget != null) focusedWidget.renderExtras(gl, focusedWidget.getRealPosition());
	}
			
	private void sendEvent(Event event)
	{
		for (Widget component : components)
			component.receiveEvent(event, new Vector2i(0, 0));
		
		canvas.display();
	}
		
	////
		
	private class WindowListener extends WindowAdapter
	{
		public void windowClosing(WindowEvent we) {exit();}
	}
	
	////

	@Override
	public void mouseDragged(MouseEvent e)
	{
		Event event = new Event(Event.Type.MOUSE_MOTION);
		event.position = new Vector2i(e.getX(), e.getY());
		sendEvent(event);
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		Event event = new Event(Event.Type.MOUSE_MOTION);
		event.position = new Vector2i(e.getX(), e.getY());
		sendEvent(event);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{

	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		Event event = new Event(Event.Type.MOUSE_MOTION);
		event.position = new Vector2i(e.getX(), e.getY());
		sendEvent(event);
	}

	@Override
	public void mouseExited(MouseEvent e)
	{

	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		Event event = new Event(Event.Type.MOUSE_CLICK);
		event.position = new Vector2i(e.getX(), e.getY());
		event.button = e.getButton();
		event.pressed = true;
		sendEvent(event);
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		Event event = new Event(Event.Type.MOUSE_CLICK);
		event.position = new Vector2i(e.getX(), e.getY());
		event.button = e.getButton();
		event.pressed = false;
		sendEvent(event);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		Event event = new Event(Event.Type.MOUSE_SCROLL);
		event.position = new Vector2i(e.getX(), e.getY());
		event.amount = e.getWheelRotation() * e.getScrollAmount() * 10;
		sendEvent(event);	
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		int	keyCode = e.getKeyCode();
		if (keyCode < 1024) keyState[keyCode] = true;
		
		Event event = new Event(Event.Type.KEY_PRESS);
		event.pressed = true;
		event.button = keyCode;
		
		if ((keyRepeatFilter != null) && (keyRepeatFilter.event != null))
		{
			Event event2 = keyRepeatFilter.event;
			
			if (event2.button == keyCode) 
				keyRepeatFilter.cancel();
			else
				keyReleased(event2);
			
			keyRepeatFilter = null;
		}
		
		sendEvent(event);
	}

	@Override
	public void keyReleased(KeyEvent e)
	{		
		int keyCode = e.getKeyCode();
		Event event = new Event(Event.Type.KEY_PRESS);
		event.pressed = false;
		event.button = keyCode;
		
		keyRepeatFilter = new KeyRepeatFilter(event);
		keyRepeatTimer.schedule(keyRepeatFilter, 10);
	}
	
	public void keyReleased(Event event)
	{
		if (event.button < 1024) keyState[event.button] = false;
		sendEvent(event);
	}
	
	@Override
	public void keyTyped(KeyEvent e)
	{
		if (e.getKeyChar() == '`') debugLayout = !debugLayout;
		
		Event event = new Event(Event.Type.KEY_TYPED);
		event.button = (int)e.getKeyChar();
		sendEvent(event);
	}
	
	class KeyRepeatFilter extends TimerTask
	{
		public Event 	event;
		
		KeyRepeatFilter(Event event)
		{
			this.event = event;
		}
		
		@Override
		public boolean cancel()
		{
			event = null;
			return super.cancel();
		}
		
		@Override
		public void run()
		{
			if (event != null)
			{
				Event temp = event;
				event = null;
				keyReleased(temp);
			}
		}
	}
}
