package com.hexcore.cas.math;

/**
 * Class Vector2i:
 * 	A class that contains two integers, 
 * 	useful for storing sizes or positions.
 * 	Member variables are public so 
 * 	"Vector2i point; point.x = 5;" is allowed.
 * 
 * @author Divan
 */
public class Vector2i
{
	public int x;
	public int y;
	
	public Vector2i()
	{
		this.x = 0;
		this.y = 0;
	}
	
	public Vector2i(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public Vector2i(Vector2f v)
	{
		this.x = (int)v.x;
		this.y = (int)v.y;
	}
	
	public Vector2i(Vector2i v)
	{
		this.x = v.x;
		this.y = v.y;
	}
	
	public Vector2i add(int x, int y)
	{
		return new Vector2i(this.x + x, this.y + y);
	}
	
	public Vector2i add(Vector2i p)
	{
		return new Vector2i(x + p.x, y + p.y);
	}
	
	public void dec(int x, int y)
	{
		this.x -= x;
		this.y -= y;
	}
	
	public void dec(Vector2i p)
	{
		x -= p.x;
		y -= p.y;
	}
	
	public boolean equals(Vector2i p)
	{
		return (x == p.x) && (y == p.y);
	}
	
	public int get(int index)
	{
		return (index == 0) ? x : y;
	}
	
	public void inc(int x, int y)
	{
		this.x += x;
		this.y += y;
	}
	
	public void inc(Vector2i p)
	{
		x += p.x;
		y += p.y;
	}
	
	public float length()
	{
		return (float)Math.sqrt(x*x + y*y);
	}
	
	public static Vector2i max(Vector2i a, Vector2i b)
	{
		return new Vector2i(Math.max(a.x, b.x), Math.max(a.y, b.y));
	}
	
	public void set(int x, int y) 
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2i subtract(int x, int y)
	{
		return new Vector2i(this.x - x, this.y - y);
	}
	
	public Vector2i subtract(Vector2i p)
	{
		return new Vector2i(x - p.x, y - p.y);
	}
	
	public void set(Vector2i v) 
	{
		this.x = v.x;
		this.y = v.y;
	}
	
	@Override
	public String toString()
	{
		return "Vector2i<" + x + ", " + y + ">";
	}
}
