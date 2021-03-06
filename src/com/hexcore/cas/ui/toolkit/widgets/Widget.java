package com.hexcore.cas.ui.toolkit.widgets;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.ui.toolkit.Event;
import com.hexcore.cas.ui.toolkit.Window;

public abstract class Widget
{
	public final static int FILL_VERTICAL = 1;
	public final static int FILL_HORIZONTAL = 2;
	public final static int FILL = FILL_VERTICAL | FILL_HORIZONTAL;
	public final static int CENTER_VERTICAL = 4;
	public final static int CENTER_HORIZONTAL = 8;
	public final static int CENTER = CENTER_VERTICAL | CENTER_HORIZONTAL;
	public final static int WRAP_VERTICAL = 16;
	public final static int WRAP_HORIZONTAL = 32;
	public final static int WRAP = WRAP_VERTICAL | WRAP_HORIZONTAL;
	
	protected Vector2i	position;
	protected Vector2i	size;
	protected Vector2i	margin;
	protected Vector2i	padding;
	
	protected Widget	parent = null;
	
	protected boolean	active = false;
	protected boolean	visible = true;
	protected boolean	focused = false;
	protected boolean	mouseover = false;
	protected Window	window;
	protected int		flags;
	
	protected String	name = "";
	protected String	tooltip = "";
	
	Widget(Vector2i size)
	{
		this(new Vector2i(0, 0), size);
	}
	
	Widget(Vector2i position, Vector2i size)
	{
		this.position = position;
		this.size = size;
		this.margin = new Vector2i(-1, -1);
		this.padding = new Vector2i(0, 0);
	}
	
	Widget(String name, Vector2i size)
	{
		this(name, new Vector2i(0, 0), size);
	}
	
	Widget(String name, Vector2i position, Vector2i size)
	{
		this.name = name;
		this.position = position;
		this.size = size;
		this.margin = new Vector2i(-1, -1);
		this.padding = new Vector2i(0, 0);
	}	
	
	public void setTooltip(String tooltip)
	{
		this.tooltip = tooltip;
	}
	
	public String getTooltip()
	{
		return tooltip;
	}	
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Widget findByName(String name)
	{
		if (this.name.equals(name)) return this;
		return null;
	}
		
	public void setX(int position)
	{
		this.position.x = position;
	}
	
	public void setY(int position)
	{
		this.position.y = position;
	}
	
	public void setPosition(Vector2i position)
	{
		this.position = position;
	}
	
	public void setWidth(int size)
	{
		this.size.x = size;
	}
	
	public void setHeight(int size)
	{
		this.size.y = size;
	}
	
	public void setSize(Vector2i size)
	{
		this.size = size;
	}
	
	public void setMargin(Vector2i margin)
	{
		this.margin = margin;
		relayout();
	}
	
	public void setPadding(Vector2i padding)
	{
		this.padding = padding;
		relayout();
	}
	
	public void setParent(Widget parent)
	{
		this.parent = parent;
		setWindow(parent.getWindow());
		relayout();
	}
	
	public Vector2i	getRealPosition()
	{
		if (parent != null) return parent.getRealPosition().add(position).add(getInnerOffset());
		return position;
	}
	
	public void relayout()
	{
		
	}
	
	public void setVisible(boolean state) 
	{
		this.visible = state;
		if (focused) window.giveUpFocus(this);
	}
	
	public void toggleVisibility() 
	{
		visible = !visible;
		if (focused) window.giveUpFocus(this);
	}
	
	public boolean	canGetFocus() {return false;}
	
	public int		getWidth() {return size.x;}
	public int		getHeight() {return size.y;}
	public Vector2i	getSize() {return size;}
	public int		getInnerWidth() {return size.x;}
	public int		getInnerHeight() {return size.y;}
	public Vector2i	getInnerSize() {return size;}
	public int		getInnerX() {return 0;}
	public int		getInnerY() {return 0;}
	public Vector2i	getInnerOffset() {return new Vector2i(0, 0);}
	public int		getX() {return position.x;}
	public int		getY() {return position.y;}
	public Vector2i	getPosition() {return position;}
	public Window	getWindow() {return window;}
	public Vector2i	getMargin() {return margin;}	
	public Vector2i	getPadding() {return padding;}	
	public Widget	getParent() {return parent;}
	public boolean	isMouseOver() {return mouseover;}
	public boolean	hasFocus() {return focused;}
	public boolean	isVisible() {return visible;}
	
	public void		clearFlag(int flag) {flags &= ~flag;}
	public void		setFlag(int flag) {flags |= flag;}
	public void		setFlag(int flag, boolean state) {if (state) setFlag(flag); else clearFlag(flag);}
	public boolean	isSet(int flag) {return (flags & flag) == flag;}
	
	public void update(Vector2i position, float delta)
	{
		
	}

	public void render(GL gl, Vector2i position)
	{
		
	}
	
	public void renderExtras(GL gl, Vector2i position)
	{
		
	}
				
	final public boolean receiveEvent(Event event)
	{
		return receiveEvent(event, new Vector2i());
	}
	
	final public boolean receiveEvent(Event event, Vector2i position)
	{	
		if (!visible) return false;
		
		Vector2i pos = this.position.add(position);
		
		if (event.type == Event.Type.GAINED_FOCUS)
		{
			focused = true;
			
			if (window != null)
			{
				Event newEvent = new Event(Event.Type.GAINED_FOCUS);
				window.sendWindowEvent(newEvent);
			}
		}
		else if (event.type == Event.Type.LOST_FOCUS)
		{
			focused = false;
		}
		else if (event.type == Event.Type.MOUSE_OUT)
		{
			mouseover = false;
		}
		else if ((event.type == Event.Type.MOUSE_MOTION) || (event.type == Event.Type.MOUSE_CLICK) || (event.type == Event.Type.MOUSE_SCROLL))
		{
			Vector2i start = pos;
			Vector2i end = pos.add(size);

			if ((event.position.x <= start.x) || (event.position.y <= start.y)
					|| (event.position.x >= end.x) || (event.position.y >= end.y))
			{
				mouseover = false;
				
				if (!active && (event.type != Event.Type.MOUSE_MOTION))
				{
					if (mouseover && (event.type == Event.Type.MOUSE_MOTION))
					{
						handleEvent(new Event(Event.Type.MOUSE_OUT), pos);
						return false;
					}
					else if (!event.isMouseRelease()) 
						return false;
				}
			}
			else
				mouseover = true;
		}
		
		return handleEvent(event, pos);
	}
	
	public boolean handleEvent(Event event, Vector2i position)
	{
		return false;
	}
	
	public boolean receiveEventExtras(Event event, Vector2i position)
	{
		return handleEventExtras(event, position);
	}	
	
	public boolean handleEventExtras(Event event, Vector2i position)
	{
		return false;
	}	
	
	public void setWindow(Window window) 
	{
		this.window = window;
		if (margin.x < 0) margin.x = window.getDefaultMargin().x;
		if (margin.y < 0) margin.y = window.getDefaultMargin().y;
	}
}
