package com.hexcore.cas.ui;

public class Colour
{
	public final static Colour	BLACK = new Colour(0.0f, 0.0f, 0.0f);
	public final static Colour	WHITE = new Colour(1.0f, 1.0f, 1.0f);
	public final static Colour	LIGHT_GREY = new Colour(0.85f, 0.85f, 0.85f);
	public final static Colour	GREY = new Colour(0.75f, 0.75f, 0.75f);
	public final static Colour	DARK_GREY = new Colour(0.5f, 0.5f, 0.5f);
	public final static Colour	RED = new Colour(1.0f, 0.0f, 0.0f);
	public final static Colour	GREEN = new Colour(0.0f, 1.0f, 0.0f);
	public final static Colour	BLUE = new Colour(0.0f, 0.0f, 1.0f);
	public final static Colour	YELLOW = new Colour(1.0f, 1.0f, 0.0f);
	
	public float r, g, b, a;
	
	public Colour(float r, float g, float b)
	{
		this.r = r;
		this.b = b;
		this.g = g;
		this.a = 1.0f;
	}
	
	public Colour(float r, float g, float b, float a)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public Colour mix(Colour other)
	{
		return new Colour(r * 0.5f + other.r * 0.5f, g * 0.5f + other.g * 0.5f, b * 0.5f + other.b * 0.5f);
	}
	
	public Colour mix(Colour other, float amount)
	{
		float inv = 1 - amount;
		return new Colour(r * inv + other.r * amount, g * inv + other.g * amount, b * inv + other.b * amount);
	}
	
	public String toString()
	{
		return "Colour<" + r + ", " + g + ", " + b + ", " + a + ">";
	}
}