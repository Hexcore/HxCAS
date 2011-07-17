package com.hexcore.cas.math;

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
	
	public Vector2i	getPosition() {return position;}
	public Vector2i	getSize() {return size;}
	public int		getX() {return position.x;}
	public int		getY() {return position.y;}
	public int		getWidth() {return size.x;}
	public int		getHeight() {return size.y;}
	
	public void		setPosition(Vector2i position) {this.position.set(position);}
	public void		setSize(Vector2i size) {this.size.set(size);}
}
