package com.hexcore.cas.math;

public class Rectf
{
	public Vector2f position = new Vector2f(0, 0);
	public Vector2f size = new Vector2f(0, 0);
	
	public Rectf()
	{
	}	
	
	public Rectf(Vector2f size)
	{
		this.size.set(size);
	}
	
	public Rectf(Vector2f position, Vector2f size)
	{
		this.position.set(position);
		this.size.set(size);
	}
	
	public Vector2f	getPosition() {return position;}
	public Vector2f	getSize() {return size;}
	public float	getX() {return position.x;}
	public float	getY() {return position.y;}
	public float	getWidth() {return size.x;}
	public float	getHeight() {return size.y;}
	
	public void		setPosition(Vector2f position) {this.position.set(position);}
	public void		setSize(Vector2f size) {this.size.set(size);}
	
	public static Rectf getBoundingBox(Vector2f[] points)
	{
		if (points.length <= 0) return null;
		
		Vector2f lowest = new Vector2f(points[0]);
		Vector2f highest = new Vector2f(points[0]);
		
		for (int i = 1; i < points.length; i++)
		{
			Vector2f p = points[i];
			
			if (p.x < lowest.x) lowest.x = p.x;
			if (p.y < lowest.y) lowest.y = p.y;
			if (p.x > highest.x) highest.x = p.x;
			if (p.y > highest.y) highest.y = p.y;
		}
		
		return new Rectf(lowest, highest.subtract(lowest));
	}
}
