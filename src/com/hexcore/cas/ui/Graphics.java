package com.hexcore.cas.ui;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import com.hexcore.cas.math.Rectf;
import com.hexcore.cas.math.Vector2f;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.ui.Theme.BorderShape;

public class Graphics
{
	private static int addCornerToArray(int index, Vector2f[] array, Vector2f start, int radius, int quarter, boolean isBorder)
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
		
	private static Vector2f[] createRoundedRectangle(Vector2i pos, Vector2i size, int radius, BorderShape borderShape, boolean isBorder)
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
	
	public static void renderRoundedBorderedRectangle(GL gl, Vector2i pos, Vector2i size, int radius, Fill fill, Fill border)
	{		
		Vector2f[]	points = createRoundedRectangle(pos, size, radius, new BorderShape(BorderShape.ALL_CORNERS), true);
		renderPolygon(gl, pos, points, false, fill);
		renderPolygon(gl, pos, points, true, border);
	}	
	
	public static void renderRoundedBorderedRectangle(GL gl, Vector2i pos, Vector2i size, int radius, BorderShape borderShape, Fill fill, Fill border)
	{		
		Vector2f[]	points = createRoundedRectangle(pos, size, radius, borderShape, true);
		renderPolygon(gl, pos, points, false, fill);
		renderPolygon(gl, pos, points, true, border);
	}	
	
	public static void renderRoundedRectangle(GL gl, Vector2i pos, Vector2i size, int radius, Colour colour)
	{		
		Vector2f[]	points = createRoundedRectangle(pos, size, radius, new BorderShape(BorderShape.ALL_CORNERS), false);
		renderPolygon(gl, pos, points, false, colour);
	}
	
	public static void renderRoundedRectangle(GL gl, Vector2i pos, Vector2i size, int radius, BorderShape borderShape, Fill fill)
	{
		Vector2f[]	points = createRoundedRectangle(pos, size, radius, borderShape, false);
		renderPolygon(gl, pos, points, false, fill);
	}
	
	public static void renderRoundedRectangle(GL gl, Vector2i pos, Vector2i size, int radius, Fill fill)
	{
		Vector2f[]	points = createRoundedRectangle(pos, size, radius, new BorderShape(BorderShape.ALL_CORNERS), false);
		renderPolygon(gl, pos, points, false, fill);
	}	
	
	public static void renderRoundedBorder(GL gl, Vector2i pos, Vector2i size, int radius, Colour colour)
	{
		Vector2f[]	points = createRoundedRectangle(pos, size, radius, new BorderShape(BorderShape.ALL_CORNERS), true);
		renderPolygon(gl, pos, points, true, colour);
	}	
	
	public static void renderRoundedBorder(GL gl, Vector2i pos, Vector2i size, int radius, BorderShape borderShape, Fill fill)
	{
		Vector2f[]	points = createRoundedRectangle(pos, size, radius, borderShape, true);
		renderPolygon(gl, pos, points, true, fill);
	}
	
	public static void renderPolygon(GL gl, Vector2i pos, Vector2f[] vertices, boolean outline, Colour colour)
	{
		GL2 gl2 = gl.getGL2();
		
		applyColour(gl2, colour);
        gl2.glBegin(outline ? GL.GL_LINE_LOOP : GL.GL_TRIANGLE_FAN);
        for (Vector2f vertex : vertices) gl2.glVertex2f(pos.x + vertex.x, pos.y + vertex.y);
		gl2.glEnd();
	}
	
	public static void renderPolygon(GL gl, Vector2i pos, Vector2f[] vertices, boolean outline, Fill fill)
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
		
	public static void renderRectangle(GL gl, Vector2i pos, Image image)
	{
		renderRectangle(gl, pos, image.getSize(), image);
	}
	
	public static void renderRectangle(GL gl, Vector2i pos, Vector2i size, Image image)
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
	
	public static void renderRectangle(GL gl, Vector2i pos, Vector2i size, int radius, Fill fill)
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
	
	public static void renderRectangle(GL gl, Vector2i pos, Vector2i size, Colour colour)
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
	
	public static void renderRectangleTB(GL gl, Vector2i pos, Vector2i size, Colour top, Colour bottom)
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
	
	public static void renderRectangleLR(GL gl, Vector2i pos, Vector2i size, Colour left, Colour right)
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
	
	public static void renderBorder(GL gl, Vector2i pos, Vector2i size, int radius, Fill fill)
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
	
	public static void renderBorder(GL gl, Vector2i pos, Vector2i size, Colour colour)
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
	
	public static void renderBorderTB(GL gl, Vector2i pos, Vector2i size, Colour top, Colour bottom)
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
	
	public static void renderBorderLR(GL gl, Vector2i pos, Vector2i size, Colour left, Colour right)
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
	
	protected static void applyColour(GL2 gl, Colour colour)
	{
		if (colour == null)
			gl.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
		else
			gl.glColor4f(colour.r, colour.g, colour.b, colour.a);			
	}
}
