package com.hexcore.cas.model;

import com.hexcore.cas.math.Vector2i;

public abstract class Grid
{
	private Cell[][] cells = null;
	private Vector2i size = null;
	
	public Grid(Vector2i size)
	{
		this.size = size;
		this.cells = new Cell[size.y][size.x];
		for(int y = 0; y < size.y; y++)
			for(int x = 0; x < size.x; x++)
				this.cells[y][x] = new Cell(1);
	}
	
	public Grid(Vector2i size, Cell example)
	{
		this.size = size;
		this.cells = new Cell[size.y][size.x];
		for(int y = 0; y < size.y; y++)
			for(int x = 0; x < size.x; x++)
			{
				this.cells[y][x] = new Cell(example.getValueCount());
				for(int i = 0; i < example.getValueCount(); i++)
					this.cells[y][x].setValue(i, example.getValue(i));
			}
	}
	
	public abstract Cell[] getNeighbours(Vector2i pos);
	
	public Cell getCell(int x, int y)
	{
		return cells[y][x];
	}
	
	public Cell getCell(Vector2i pos)
	{
		return cells[pos.y][pos.x];
	}
	
	public int getWidth()
	{
		return size.x;
	}
	
	public int getHeight()
	{
		return size.y;
	}
	
	public Vector2i getSize()
	{
		return size;
	}
}
