package com.hexcore.cas.math;

/*
 * A class that contains three floating point number, useful for storing sizes or positions.
 */
public class Vector3f
{
	public float x;
	public float y;
	public float z;
	
	public Vector3f()
	{
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}	
	
	public Vector3f(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f(Vector2f v, float z)
	{
		this.x = v.x;
		this.y = v.y;
		this.z = z;
	}	
	
	public Vector3f(Vector3f v)
	{
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}

	public void set(float x, float y, float z) 
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void set(Vector2f v, float z) 
	{
		this.x = v.x;
		this.y = v.y;
		this.z = z;
	}
	
	public void set(Vector3f v) 
	{
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}
	
	public float	get(int index) {return (index != 2) ? ((index == 0) ? x : y) : z;}
	
	public boolean equals(Vector3f p)
	{
		return (x == p.x) && (y == p.y) && (z == p.z);
	}
	
	@Override
	public String toString()
	{
		return "Vector3f<" + x + ", " + y + ", " + z + ">";
	}
	
	public void inc(Vector3f p)
	{
		x += p.x;
		y += p.y;
		z += p.z;
	}
	
	public void inc(float x, float y, float z)
	{
		this.x += x;
		this.y += y;
		this.z += z;
	}
	
	public Vector3f add(Vector3f p)
	{
		return new Vector3f(x + p.x, y + p.y, z + p.z);
	}
	
	public Vector3f add(float x, float y, float z)
	{
		return new Vector3f(this.x + x, this.y + y, this.z + z);
	}
	
	public void dec(Vector3f p)
	{
		x -= p.x;
		y -= p.y;
		z -= p.z;
	}
	
	public void dec(float x, float y, float z)
	{
		this.x -= x;
		this.y -= y;
		this.z -= z;
	}
	
	public Vector3f subtract(Vector3f p)
	{
		return new Vector3f(x - p.x, y - p.y, z - p.z);
	}
	
	public Vector3f subtract(float x, float y, float z)
	{
		return new Vector3f(this.x - x, this.y - y, this.z - z);
	}
}