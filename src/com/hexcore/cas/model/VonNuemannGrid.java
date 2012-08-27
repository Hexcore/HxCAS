package com.hexcore.cas.model;

import com.hexcore.cas.math.Vector2i;
/**
 * Class VonNuemannGrid
 * 	Stores details specific to a grid made of square cells with a Von Neumann neighbourhood.
 * 
 * @author Megan Duncan
 */
public class VonNuemannGrid extends Grid
{
	public VonNuemannGrid(Vector2i size, Cell example)
	{
		super(size, example);
	}
	
	public VonNuemannGrid(Vector2i size, int numProperties)
	{
		super(size, numProperties);
	}
	
	public VonNuemannGrid(Grid g)
	{
		super(g);
	}
	
	public Grid clone()
	{
		return new VonNuemannGrid(this);
	}
	
	public Vector2i getNeighbourhoodRange()
	{
		return new Vector2i(1, 1);
	}
	
	@Override
	public Cell[] getNeighbours(Vector2i pos)
	{
		Cell[] neighbours = new Cell[4];
		int i = 0;
		int x = pos.x;
		int y = pos.y;
		int xdim = getWidth();
		int ydim = getHeight();
		
		if(wrap)
		{
			neighbours[0] = this.getCell(new Vector2i(x, (y + ydim - 1) % ydim));
			neighbours[1] = this.getCell(new Vector2i((x + xdim - 1) % xdim, y));
			neighbours[2] = this.getCell(new Vector2i((x + 1) % xdim, y));
			neighbours[3] = this.getCell(new Vector2i(x, (y + 1) % ydim));
		}
		else
		{
			neighbours = setNeighbours(neighbours, x, y - 1, xdim, ydim, i++);
			neighbours = setNeighbours(neighbours, x - 1, y, xdim, ydim, i++);
			neighbours = setNeighbours(neighbours, x + 1, y, xdim, ydim, i++);
			neighbours = setNeighbours(neighbours, x, y + 1, xdim, ydim, i++);
		}
		
		return neighbours;
	}
	
	public GridType getType()
	{
		return GridType.VONNUEMANN;
	}
	
	private Cell[] setNeighbours(Cell [] neighbours, int x, int y, int xdim, int ydim, int i)
	{
		if((x < 0) || (x >= xdim))
			neighbours[i] = null;
		else if((y < 0) || (y >= ydim))
			neighbours[i] = null;
		else
			neighbours[i] = getCell(x, y);
		
		return neighbours;
	}
}
