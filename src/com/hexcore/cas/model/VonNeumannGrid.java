package com.hexcore.cas.model;

import com.hexcore.cas.math.Vector2i;

/**
 * Class VonNuemannGrid
 * 	Stores details specific to a grid made of square cells with a Von Neumann neighbourhood.
 * 
 * @author Megan Duncan
 */

public class VonNeumannGrid extends Grid
{
	/**
	 * VonNeumannGrid copy constructor.
	 * 
	 * @param grid - the grid to be cloned
	 */
	public VonNeumannGrid(Grid grid)
	{
		super(grid);
	}
	
	/**
	 * VonNeumannGrid custom constructor.
	 * 
	 * @param size - width and height of the desired grid size
	 * @param example - an example cell that will be used to populate the grid
	 */
	public VonNeumannGrid(Vector2i size, Cell example)
	{
		super(size, example);
	}
	
	/**
	 * VonNeumannGrid custom constructor.
	 * 
	 * @param size - width and height of the desired grid size
	 * @param numProperties - the number of properties that each cell of the grid must have
	 */
	public VonNeumannGrid(Vector2i size, int numProperties)
	{
		super(size, numProperties);
	}
	
	/**
	 * Returns a cloned Von Neumann grid of the current Von Neumann grid.
	 * 
	 * @return - a new cloned VonNeumanGrid
	 */
	public Grid clone()
	{
		return new VonNeumannGrid(this);
	}
	
	/**
	 * Returns the range of the Von Neumann grid type, being the four touching cells directly
	 * above, below, left and right.
	 * 
	 * @return - a Vector2i of the range of the Von Neumann grid type, being 1 all round 
	 */
	public Vector2i getNeighbourhoodRange()
	{
		return new Vector2i(1, 1);
	}
	
	@Override
	/**
	 * Returns the possible neighbours of the a cell on a Von Neumann grid.
	 * Returns null if the neighbour is the target cell itself.
	 * 
	 * @param pos - location of cell whose neighbours are requested
	 * 
	 * @return - cell array of 4 neighbours
	 */
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
	
	/**
	 * Returns an enumeration for the Von Neumann grid type.
	 * 
	 * @return - the enumerated value of the Von Neumann grid
	 */
	public GridType getType()
	{
		return GridType.VONNEUMANN;
	}
	
	/////////////////////////////////////////////
	/// Private functions
	/**
	 * Chooses whether the neighbour is valid or should be set to null.
	 * 
	 * @param neighbours - array to be altered
	 * @param x - x position of target cell
	 * @param y - y position of target cell
	 * @param xdim - x dimension of the grid
	 * @param ydim - y dimension of the grid
	 * @param i - number of neighbour
	 * 
	 * @return - the updated neighbours array
	 */
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
