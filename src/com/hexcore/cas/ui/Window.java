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

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.GLMatrixFunc;

import com.hexcore.cas.math.Recti;
import com.hexcore.cas.math.Vector2i;
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

	private	boolean	updateComponents = true;	
	private boolean	initDone = false;
	
	public Window(String title)
	{
		this(title, 800, 600);
	}
	
	public Window(String title, int width, int height)
	{
		super(new Vector2i(width, height));
				
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
	
	public void resetClipping(GL gl)
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
        gl.glClearColor(0.1f, 0.2f, 0.4f, 1.0f);
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
		size = new Vector2i(width, height);
        relayout();
	}
	
	private int addCornerToArray(int index, Vector2i[] array, Vector2i start, int radius, int quarter)
	{
		for (int i = 0; i <= radius; i++)
		{
			double angle = ((double)i / radius) * Math.PI / 2.0 + (Math.PI / 2) * (quarter - 2);
			Vector2i p = new Vector2i(start.x + (int)(Math.sin(angle) * radius), start.y + (int)(Math.cos(angle) * radius));
			array[index++] = p;
		}
		return index;
	}
		
	public void renderRoundedRectangle(GL gl, Vector2i pos, Vector2i size, int radius, Colour colour)
	{		
		Vector2i[]	points = new Vector2i[(radius + 1) * 4];
		int	index = 0;
		
		index = addCornerToArray(index, points, new Vector2i(radius, radius), radius, 0);
		index = addCornerToArray(index, points, new Vector2i(radius, size.y - radius), radius, 1);
		index = addCornerToArray(index, points, new Vector2i(size.x - radius, size.y -radius), radius, 2);
		index = addCornerToArray(index, points, new Vector2i(size.x - radius, radius), radius, 3);
		
		renderPolygon(gl, pos, points, false, colour);
	}
	
	public void renderRoundedRectangle(GL gl, Vector2i pos, Vector2i size, int radius, Fill fill)
	{
		Vector2i[]	points = new Vector2i[(radius + 1) * 4];
		int	index = 0;
		
		index = addCornerToArray(index, points, new Vector2i(radius, radius), radius, 0);
		index = addCornerToArray(index, points, new Vector2i(radius, size.y - radius), radius, 1);
		index = addCornerToArray(index, points, new Vector2i(size.x - radius, size.y -radius), radius, 2);
		index = addCornerToArray(index, points, new Vector2i(size.x - radius, radius), radius, 3);
		
		renderPolygon(gl, pos, points, false, fill);
	}	
	
	public void renderRoundedBorder(GL gl, Vector2i pos, Vector2i size, int radius, Colour colour)
	{
		Vector2i[]	points = new Vector2i[(radius + 1) * 4];
		int	index = 0;
		
		index = addCornerToArray(index, points, new Vector2i(radius, radius), radius, 0);
		index = addCornerToArray(index, points, new Vector2i(radius, size.y - radius), radius, 1);
		index = addCornerToArray(index, points, new Vector2i(size.x - radius, size.y -radius), radius, 2);
		index = addCornerToArray(index, points, new Vector2i(size.x - radius, radius), radius, 3);
		
		renderPolygon(gl, pos, points, true, colour);
	}	
	
	public void renderRoundedBorder(GL gl, Vector2i pos, Vector2i size, int radius, Fill fill)
	{
		Vector2i[]	points = new Vector2i[(radius + 1) * 4];
		int	index = 0;
		
		index = addCornerToArray(index, points, new Vector2i(radius, radius), radius, 0);
		index = addCornerToArray(index, points, new Vector2i(radius, size.y - radius), radius, 1);
		index = addCornerToArray(index, points, new Vector2i(size.x - radius, size.y -radius), radius, 2);
		index = addCornerToArray(index, points, new Vector2i(size.x - radius, radius), radius, 3);
		
		renderPolygon(gl, pos, points, true, fill);
	}
	
	public void renderPolygon(GL gl, Vector2i pos, Vector2i[] vertices, boolean outline, Colour colour)
	{
		GL2 gl2 = gl.getGL2();
		
		applyColour(gl2, colour);
        gl2.glBegin(outline ? GL.GL_LINE_LOOP : GL.GL_TRIANGLE_FAN);
        for (Vector2i vertex : vertices) gl2.glVertex2f(pos.x + vertex.x, pos.y + vertex.y);
		gl2.glEnd();
	}
	
	public void renderPolygon(GL gl, Vector2i pos, Vector2i[] vertices, boolean outline, Fill fill)
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
		Recti 	rect = Recti.getBoundingBox(vertices);
		
        gl2.glBegin(outline ? GL.GL_LINE_LOOP : GL.GL_TRIANGLE_FAN);
        	for (Vector2i vertex : vertices)
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
			renderRoundedBorder(gl, pos, size, radius, fill);
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
		Event event = new Event(Event.Type.KEY_PRESS);
		event.pressed = true;
		event.button = e.getKeyCode();
		sendEvent(event);
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		Event event = new Event(Event.Type.KEY_PRESS);
		event.pressed = false;
		event.button = e.getKeyCode();
		sendEvent(event);
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		Event event = new Event(Event.Type.KEY_TYPED);
		event.button = (int)e.getKeyChar();
		sendEvent(event);
	}
}
