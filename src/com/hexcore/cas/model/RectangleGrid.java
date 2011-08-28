package com.hexcore.cas.model;

import com.hexcore.cas.math.Vector2i;

public class RectangleGrid extends Grid
{

	public RectangleGrid(Vector2i size) 
	{
		super(size);
		gridType = 'R';
	}//end constructor
	
	public RectangleGrid(Vector2i size, Cell example)
	{
		super(size, example);
		gridType = 'R';
	}
	
	public RectangleGrid(Grid g)
	{
		super(g);
		gridType = 'R';
	}
	
	public Grid clone()
	{
		return new RectangleGrid(this);
	}
	
	@Override
	/**
	 * Returns the possible neighbours of the a cell on a rectangular grid. Null if the neighbour is the target
	 * cell itself.
	 * 
	 * @param pos - location of cell whose neighbours are requested.
	 * @return - Cell array of 8 neighbours.
	 * @author Apurva Kumar
	 */
	public Cell[] getNeighbours(Vector2i pos)
	{
		//System.out.println("Before : " + getCell(pos).getValue(0));
		//Initialisations
		Cell [] neighbours = new Cell[8];
		int i = 0;//counter, goes till 8.
		int xdim = getWidth();//get dimensions stored
		int ydim = getHeight();
		int x = pos.x;//get target cell's location stored.
		int y = pos.y;
		
		if(wrap)
		{
			//TOP LEFT: y-1, x-1
			neighbours[0] = this.getCell(new Vector2i((x+xdim-1)%xdim, (y+ydim-1)%ydim));
			//TOP CENTRE: y-1, x
			neighbours[1] = this.getCell(new Vector2i(x, (y+ydim-1)%ydim));
			//TOP RIGHT: y-1, x+1
			neighbours[2] = this.getCell(new Vector2i((x+1)%xdim, (y+ydim-1)%ydim));
			//LEFT: y, x-1
			neighbours[3] = this.getCell(new Vector2i((x+xdim-1)%xdim, y));
			//RIGHT: y, x+1
			neighbours[4] = this.getCell(new Vector2i((x+1)%xdim, y));
			//BOTTOM LEFT: y+1, x-1
			neighbours[5] = this.getCell(new Vector2i((x+xdim-1)%xdim, (y+1)%ydim));
			//BOTTOM CENTRE: y+1, x
			neighbours[6] = this.getCell(new Vector2i(x, (y+1)%ydim));
			//BOTTOM RIGHT: y+1, x+1
			neighbours[7] = this.getCell(new Vector2i((x+1)%xdim, (y+1)%ydim));
		}
		else
		{
			//TOP LEFT: y-1, x-1
			neighbours = setNeighbours(neighbours, x-1, y-1, 	xdim, ydim, i++);
			//TOP CENTRE: y-1, x
			neighbours = setNeighbours(neighbours, x, y-1, 		xdim, ydim, i++);
			//TOP RIGHT: y-1, x+1
			neighbours = setNeighbours(neighbours, x+1, y-1, 	xdim, ydim, i++);
			//LEFT: y, x-1
			neighbours = setNeighbours(neighbours, x-1, y, 		xdim, ydim, i++);
			//RIGHT: y, x+1
			neighbours = setNeighbours(neighbours, x+1, y, 		xdim, ydim, i++);
			//BOTTOM LEFT: y+1, x-1
			neighbours = setNeighbours(neighbours, x-1, y+1, 	xdim, ydim, i++);
			//BOTTOM CENTRE: y+1, x
			neighbours = setNeighbours(neighbours, x, y+1, 		xdim, ydim, i++);
			//BOTTOM RIGHT: y+1, x+1
			neighbours = setNeighbours(neighbours, x+1, y+1, 	xdim, ydim, i++);
		}
		return neighbours;
	}//end method getNeighbours
	
	/**
	 * Private internal function. Chooses whether the neighbour is valid or should be set to null.
	 * @param x - x position of target cell.
	 * @param y - y position of target cell.
	 * @param neighbours - array to be altered.
	 * @param xdim - x dimension of the grid
	 * @param ydim - y dimension of the grid.
	 * @param i - number of neighbour.
	 * @return - the updated neighbours array.
	 * @author Apurva Kumar
	 */
	private Cell[] setNeighbours(Cell [] neighbours, int x, int y, int xdim, int ydim, int i)
	{
		if((x < 0) || (x > xdim))
		{
			neighbours[i] = null;
		}
		else if((y < 0) || (y > ydim))
		{
			neighbours[i] = null;
		}
		else
			neighbours[i] = getCell( x, y);
		
		return neighbours;
	}//end method setNeighbours

}
