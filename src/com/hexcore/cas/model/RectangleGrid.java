package com.hexcore.cas.model;

import com.hexcore.cas.math.Vector2i;

public class RectangleGrid extends Grid
{

	public RectangleGrid(Vector2i size) 
	{
		super(size);
	}
	
	@Override
	public Cell[] getNeighbours(Vector2i pos)
	{
		//initialise return Value
		Cell[] neighbours = new Cell[8];
		Vector2i temp;
		int i = 0;
		for(int j = 0; j < 3; j++)
		{
			temp = new Vector2i(pos.x-1, pos.y+j-1);
			neighbours[i++] = getCell(temp);
		}
		
		for(int j = 0; j < 3; j++)
		{
			temp = new Vector2i(pos.x+1, pos.y+j-1);
			neighbours[i++] = getCell(temp);
		}
		temp = new Vector2i(pos.x, pos.y-1);
		neighbours[i++] = getCell(temp);
		
		temp = new Vector2i(pos.x, pos.y+1);
		neighbours[i++] = getCell(temp);
		
		return neighbours;
	}

}
