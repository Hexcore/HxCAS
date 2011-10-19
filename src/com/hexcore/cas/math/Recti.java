package com.hexcore.cas.math;

/**
 * Class Recti:
 *  Stores a set of cells as two points in integer:
 * 	position - a vector2f object which represents 
 * 	the left-top corner of a rectangle of cells.
 * 	size - a vector2f object which represents 
 * 	the size of the rectangle described.
 * 
 * @author Divan
 *
 */
public class Recti
{
	public Vector2i position = new Vector2i(0, 0);
	public Vector2i size = new Vector2i(0, 0);
	
	public Recti()
	{
	}	
	
	public Recti(Vector2i size)
	{
		this.size.set(size);
	}
	
	public Recti(Vector2i position, Vector2i size)
	{
		this.position.set(position);
		this.size.set(size);
	}

	public Recti(int x, int y, int w, int h)
	{
		this.position.set(x, y);
		this.size.set(w, h);
	}
	
	public Recti(Recti rect)
	{
		this.position.set(rect.position);
		this.size.set(rect.size);		
	}

	public Vector2i	getPosition() {return position;}
	public Vector2i	getSize() {return size;}
	public int		getX() {return position.x;}
	public int		getY() {return position.y;}
	public int		getWidth() {return size.x;}
	public int		getHeight() {return size.y;}
	
	public void		setPosition(Vector2i position) {this.position.set(position);}
	public void		setSize(Vector2i size) {this.size.set(size);}
	
	public Recti	intersect(Recti other)
	{
		Vector2i s = new Vector2i(position);
		Vector2i e = position.add(size);
		if (s.x < other.position.x) s.x = other.position.x;
		if (s.y < other.position.y) s.y = other.position.y;
		if (e.x > other.position.x + other.size.x) e.x = other.position.x + other.size.x;
		if (e.y > other.position.y + other.size.y) e.y = other.position.y + other.size.y;
		return new Recti(s, e.subtract(s));
	}
	
	public static Recti getBoundingBox(Vector2i[] points)
	{
		if (points.length <= 0) return null;
		
		Vector2i lowest = new Vector2i(points[0]);
		Vector2i highest = new Vector2i(points[0]);
		
		for (int i = 1; i < points.length; i++)
		{
			Vector2i p = points[i];
			
			if (p.x < lowest.x) lowest.x = p.x;
			if (p.y < lowest.y) lowest.y = p.y;
			if (p.x > highest.x) highest.x = p.x;
			if (p.y > highest.y) highest.y = p.y;
		}
		
		return new Recti(lowest, highest.subtract(lowest));
	}
	
	@Override
	public String toString()
	{
		return "Recti<" + position.x + ", " + position.y + "; " + size.x + ", " + size.y + ">";
	}
}
