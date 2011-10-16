package com.hexcore.cas.math;

/**
 * Class Vector2f:
 * 	A class that contains two floating point number, 
 * 	useful for storing sizes or positions.
 * 
 * @author Divan
 *
 */
public class Vector2f
{
	public float x;
	public float y;
	
	public Vector2f()
	{
		this.x = 0;
		this.y = 0;
	}	
	
	public Vector2f(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2f(Vector2f v)
	{
		this.x = v.x;
		this.y = v.y;
	}

	public Vector2f(Vector2i v)
	{
		this.x = v.x;
		this.y = v.y;		
	}

	public void set(float x, float y) 
	{
		this.x = x;
		this.y = y;
	}
	
	public void set(Vector2f v) 
	{
		this.x = v.x;
		this.y = v.y;
	}
	
	public float	get(int index) {return (index == 0) ? x : y;}
	
	public boolean equals(Vector2f p)
	{
		return (x == p.x) && (y == p.y);
	}
	
	@Override
	public String toString()
	{
		return "Vector2f<" + x + ", " + y + ">";
	}
	
	public void normalise()
	{
		float l = 1.0f / (float)Math.sqrt(x*x + y*y);
		x *= l; y *= l;
	}
	
	public Vector2f getNormalised()
	{
		float l = 1.0f / (float)Math.sqrt(x*x + y*y);
		return new Vector2f(x * l, y * l);
	}
	
	public Vector2f getPerpendicular()
	{
		return new Vector2f(y, -x);
	}

	public void inc(Vector2f p)
	{
		x += p.x;
		y += p.y;
	}
	
	public void inc(float x, float y)
	{
		this.x += x;
		this.y += y;
	}
	
	public Vector2f add(Vector2f p)
	{
		return new Vector2f(x + p.x, y + p.y);
	}
	
	public Vector2f add(float x, float y)
	{
		return new Vector2f(this.x + x, this.y + y);
	}
	
	public void dec(Vector2f p)
	{
		x -= p.x;
		y -= p.y;
	}
	
	public void dec(float x, float y)
	{
		this.x -= x;
		this.y -= y;
	}
	
	public Vector2f subtract(Vector2f p)
	{
		return new Vector2f(x - p.x, y - p.y);
	}
	
	public Vector2f subtract(float x, float y)
	{
		return new Vector2f(this.x - x, this.y - y);
	}
}