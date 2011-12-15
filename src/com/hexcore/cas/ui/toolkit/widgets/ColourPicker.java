package com.hexcore.cas.ui.toolkit.widgets;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import com.hexcore.cas.math.Vector2f;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.ui.toolkit.Colour;
import com.hexcore.cas.ui.toolkit.Event;
import com.hexcore.cas.ui.toolkit.Fill;
import com.hexcore.cas.ui.toolkit.Graphics;
import com.hexcore.cas.ui.toolkit.Theme;
import com.hexcore.cas.ui.toolkit.Window;
import com.hexcore.cas.ui.toolkit.Event.Type;

public class ColourPicker extends Widget
{
	private float	hueAngle = 0.0f;
	private float	ba = 1.0f, bb = 0.0f, bc = 0.0f;
	
	private boolean wheelActive = false;
	private boolean triangleActive = false;
		
	public ColourPicker(Window window)
	{
		super(new Vector2i(240, 285));
	}
		
	public Colour getColour() 
	{
		Colour col = hueAngleToColour(hueAngle);
		return new Colour(bb + ba * col.r, bb + ba * col.g, bb + ba * col.b);
	}
	
	@Override
	public void render(GL gl, Vector2i position)
	{
		Vector2i pos = this.position.add(position);
		
		GL2 gl2 = gl.getGL2();
		Theme theme = window.getTheme();
		int borderRadius = theme.getInteger("ColourPicker", "border-radius", 0);
		Fill backgroundFill = theme.getFill("ColourPicker", "background", new Fill(new Colour(0.75f, 0.75f, 0.75f)));
		
		// Draw background
		Graphics.renderRectangle(gl2, pos, new Vector2i(220, 220), borderRadius, backgroundFill);
		
		// Draw hue circle
		gl2.glBegin(GL.GL_TRIANGLE_STRIP);
		for (float a = 0.0f; a <= Math.PI * 2.0f + 0.1f; a += 0.1f)
		{
			Colour col = hueAngleToColour(a);
			
			float cosa = (float)Math.cos(a);
			float sina = (float)Math.sin(a);
			
			gl2.glColor3f(col.r, col.g, col.b);
			gl2.glVertex2f(pos.x + cosa * 80.0f + 110.0f, pos.y + sina * 80.0f + 110.0f);
			gl2.glVertex2f(pos.x + cosa * 100.0f + 110.0f, pos.y + sina * 100.0f + 110.0f);
		}
		gl2.glEnd();
		
		// Draw hue indicator
		gl2.glBegin(GL.GL_TRIANGLE_STRIP);
		{
			gl2.glColor3f(1.0f, 1.0f, 1.0f);
			
			float cosa = (float)Math.cos(hueAngle - 0.01f);
			float sina = (float)Math.sin(hueAngle - 0.01f);
			gl2.glVertex2f(pos.x + cosa * 80.0f + 110.0f, pos.y + sina * 80.0f + 110.0f);
			gl2.glVertex2f(pos.x + cosa * 100.0f + 110.0f, pos.y + sina * 100.0f + 110.0f);
			
			cosa = (float)Math.cos(hueAngle + 0.01f);
			sina = (float)Math.sin(hueAngle + 0.01f);
			gl2.glVertex2f(pos.x + cosa * 80.0f + 110.0f, pos.y + sina * 80.0f + 110.0f);
			gl2.glVertex2f(pos.x + cosa * 100.0f + 110.0f, pos.y + sina * 100.0f + 110.0f);
		}
		gl2.glEnd();		
		
		// Draw saturation/lightness triangle
		gl2.glBegin(GL.GL_TRIANGLES);
		{
			float third = (float)(Math.PI * 2.0 / 3.0);
			Colour col = hueAngleToColour(hueAngle);
			
			float cosa = (float)Math.cos(hueAngle);
			float sina = (float)Math.sin(hueAngle);	
			
			gl2.glColor3f(col.r, col.g, col.b);
			gl2.glVertex2f(pos.x + cosa * 80.0f + 110.0f, pos.y + sina * 80.0f + 110.0f);
			
			cosa = (float)Math.cos(hueAngle + third);
			sina = (float)Math.sin(hueAngle + third);	
			
			gl2.glColor3f(1.0f, 1.0f, 1.0f);
			gl2.glVertex2f(pos.x + cosa * 80.0f + 110.0f, pos.y + sina * 80.0f + 110.0f);
			
			cosa = (float)Math.cos(hueAngle + third + third);
			sina = (float)Math.sin(hueAngle + third + third);	
			
			gl2.glColor3f(0.0f, 0.0f, 0.0f);			
			gl2.glVertex2f(pos.x + cosa * 80.0f + 110.0f, pos.y + sina * 80.0f + 110.0f);
		}
		gl2.glEnd();
		
		// Draw saturation/lightness indicator
		gl2.glBegin(GL.GL_TRIANGLE_STRIP);
		{
			Vector2f p = new Vector2f(110.0f, 110.0f);
			
			float third = (float)(Math.PI * 2.0 / 3.0);
			
			float x1 = (float)Math.cos(hueAngle);
			float y1 = (float)Math.sin(hueAngle);	
			float x2 = (float)Math.cos(hueAngle + third);
			float y2 = (float)Math.sin(hueAngle + third);	
			float x3 = (float)Math.cos(hueAngle + third + third);
			float y3 = (float)Math.sin(hueAngle + third + third);	
			
			p.x += (ba * x1 + bb * x2 + bc * x3) * 80.0f;
			p.y += (ba * y1 + bb * y2 + bc * y3) * 80.0f;
			
			if (bb <= 0.5f) 
				gl2.glColor3f(1.0f, 1.0f, 1.0f);
			else
				gl2.glColor3f(0.0f, 0.0f, 0.0f);
			
			for (float a = 0.0f; a <= Math.PI * 2.0f + 0.25f; a += 0.25f)
			{
				float cosa = (float)Math.cos(a);
				float sina = (float)Math.sin(a);
				
				gl2.glVertex2f(pos.x + cosa * 3.0f + p.x, pos.y + sina * 3.0f + p.y);
				gl2.glVertex2f(pos.x + cosa * 5.0f + p.x, pos.y + sina * 5.0f + p.y);
			}
		}
		gl2.glEnd();
		
		// Draw colour square
		Graphics.renderRectangle(gl2, pos.add(0, 230), new Vector2i(220, 50), borderRadius, new Fill(getColour()));
	}
	
	@Override	
	public boolean handleEvent(Event event, Vector2i position)
	{
		boolean handled = super.handleEvent(event, position);
		
		if (event.type == Event.Type.MOUSE_CLICK || event.type == Event.Type.MOUSE_MOTION)
		{
			Vector2i posFromCenter = event.position.subtract(position).subtract(110, 110);
		
			if (event.type == Event.Type.MOUSE_CLICK && event.pressed)
			{
				float distance = posFromCenter.length();
				
				if (distance <= 80.0f)
					triangleActive = true;
				else if (distance <= 100.0f) 
					wheelActive = true;
			}
			else if (event.type == Event.Type.MOUSE_CLICK && !event.pressed)
			{
				wheelActive = false;
				triangleActive = false;
			}

			if (wheelActive)
			{
				float oldHueAngle = hueAngle;
				hueAngle = (float)Math.atan2(posFromCenter.y, posFromCenter.x);
				
				if (hueAngle != oldHueAngle)
				{
					Event newEvent = new Event(Event.Type.CHANGE);
					newEvent.target = this;
					window.sendWindowEvent(newEvent);
				}
			}
			else if (triangleActive)
			{
				float third = (float)(Math.PI * 2.0 / 3.0);
				
				float x1 = (float)Math.cos(hueAngle) * 80.0f;
				float y1 = (float)Math.sin(hueAngle) * 80.0f;	
				float x2 = (float)Math.cos(hueAngle + third) * 80.0f;
				float y2 = (float)Math.sin(hueAngle + third) * 80.0f;	
				float x3 = (float)Math.cos(hueAngle + third + third) * 80.0f;
				float y3 = (float)Math.sin(hueAngle + third + third) * 80.0f;		
				float xx3 = posFromCenter.x - x3;
				float yy3 = posFromCenter.y - y3;
				
				float y2y3 = y2 - y3;
				float x3x2 = x3 - x2;
				float y3y1 = y3 - y1;
				float x1x3 = x1 - x3;
				float y1y3 = y1 - y3;
				
				float below = 1.0f / (y2y3 * x1x3 + x3x2 * y1y3);
				float a = (y2y3 * xx3 + x3x2 * yy3) * below;
				float b = (y3y1 * xx3 + x1x3 * yy3) * below;
				float c = 1.0f - a - b;
				
				if (a >= 0.0f && b >= 0.0f && c >= 0.0f)
				{
					ba = a;
					bb = b;
					bc = c;
					
					Event newEvent = new Event(Event.Type.CHANGE);
					newEvent.target = this;
					window.sendWindowEvent(newEvent);
				}
			}
		}
		
		return handled;
	}
	
	private Colour hueAngleToColour(float angle)
	{
		float h = (float)(-angle / (Math.PI * 2.0) * 6.0);
		Colour col = Colour.BLACK;
		
		if (h < 0.0f) 
			h += 6.0f;
		else if (h >= 6.0f)
			h -= 6.0f;
		
		if (h < 1.0f)
			col = new Colour(1.0f, h, 0.0f);
		else if (h < 2.0f)
			col = new Colour(1.0f - (h - 1.0f), 1.0f, 0.0f);
		else if (h < 3.0f)
			col = new Colour(0.0f, 1.0f, h - 2.0f);	
		else if (h < 4.0f)
			col = new Colour(0.0f, 1.0f - (h - 3.0f), 1.0f);
		else if (h < 5.0f)
			col = new Colour(h - 4.0f, 0.0f, 1.0f);	
		else if (h < 6.0f)
			col = new Colour(1.0f, 0.0f, 1.0f - (h - 5.0f));
		
		return col;
	}
}
