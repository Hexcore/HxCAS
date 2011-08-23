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
		
		theme = new Theme();
		
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
	
	public void updateWidgets(float delta)
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
		theme = new Theme();
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
		Colour	backgroundColour = theme.getColour("Window", "background", new Colour(0.93f, 0.93f, 0.93f));
		
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(backgroundColour.r, backgroundColour.g, backgroundColour.b, 1.0f);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0.0, size.x, size.y, 0.0, -1000.0, 1000.0);
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();
                
        for (WindowEventListener listener : eventListeners)
			listener.update(1.0f / 60.0f);
        		
        updateWidgets(1.0f / 60.0f);
        
        renderWidgets(drawable);
        
        for (WindowEventListener listener : eventListeners)
			listener.render();
        		
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
		
	private void renderWidgets(GLAutoDrawable drawable)
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
		if (focusedWidget != null)
			focusedWidget.receiveEventExtras(event, focusedWidget.getRealPosition());
		
		for (Widget component : components)
			component.receiveEvent(event);
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
