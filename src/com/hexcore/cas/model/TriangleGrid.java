package com.hexcore.cas.model;

import com.hexcore.cas.math.Vector2i;

/**
 * Class TriangleGrid
 * Stores details specific to a grid made of triangle cells.
 * For example, the different number of neighbours that a triangle grid has.
 * 
 * @author Megan Duncan
 */

public class TriangleGrid extends Grid
{
	/**
	 * TriangleGrid copy constructor.
	 * 
	 * @param grid - the grid to be cloned
	 */
	public TriangleGrid(Grid grid)
	{
		super(grid);
	}
	
	/**
	 * TriangleGrid custom constructor.
	 * 
	 * @param size - width and height of the desired grid size
	 * @param example - an example cell that will be used to populate the grid
	 */
	public TriangleGrid(Vector2i size, Cell example)
	{
		super(size, example);
	}
	
	/**
	 * TriangleGrid custom constructor.
	 * 
	 * @param size - width and height of the desired grid size
	 * @param numProperties - the number of properties that each cell of the grid must have
	 */
	public TriangleGrid(Vector2i size, int numProperties)
	{
		super(size, numProperties);
	}
	
	/**
	 * Returns a cloned triangle grid of the current triangle grid.
	 * 
	 * @return - a new cloned TriangleGrid
	 */
	public Grid clone()
	{
		return new TriangleGrid(this);
	}
	
	/**
	 * Returns the range of the triangle grid type, being all the touching cells around it.
	 * 
	 * @return - a Vector2i of the range of the triangle grid type, being 1 up and down and 2 left and right 
	 */
	public Vector2i getNeighbourhoodRange()
	{
		return new Vector2i(2, 1);
	}
	
	@Override
	/**
	 * Returns the possible neighbours of the a cell on a triangle grid.
	 * Returns null if the neighbour is the target cell itself.
	 * 
	 * @param pos - location of cell whose neighbours are requested
	 * 
	 * @return - cell array of 12 neighbours
	 */
	public Cell[] getNeighbours(Vector2i pos)
	{
		int x = pos.x;
		int y = pos.y;
		if((y % 2) == 0)
		{
			//First triangle is upside down.
			if((x % 2) == 0)
				return getNeighbours(false, pos);
			else
				return getNeighbours(true, pos);
		}
		else
		{
			//First triangle is the right way up.
			if((x % 2) == 0)
				return getNeighbours(true, pos);
			else
				return getNeighbours(false, pos);
		}
	}
	
	/**
	 * Returns an enumeration for the triangle grid type.
	 * 
	 * @return - the enumerated value of the triangle grid
	 */
	public GridType getType()
	{
		return GridType.TRIANGLE;
	}
	
	/////////////////////////////////////////////
	/// Private functions
	/**
	 * Helper function to the public getNeighbours function.
	 * 
	 * @param up - whether the current triangle is facing upwards or downwards
	 * @param pos - location of cell whose neighbours are requested
	 * 
	 * @return - cell array of 12 neighbours
	 */
	private Cell[] getNeighbours(boolean up, Vector2i pos)
	{
		Cell[] n = new Cell[12];
		int xSize = this.getWidth();
		int ySize = this.getHeight();
		int x = pos.x;
		int y = pos.y;
		
		int yMIN1 = y - 1;
		int yPLUS1 = y + 1;
		int xMIN2 = x - 2;
		int xMIN1 = x - 1;
		int xPLUS1 = x + 1;
		int xPLUS2 = x + 2;
		
		if(wrap)
		{
			if(yMIN1 < 0)
				yMIN1 += ySize;
			if(yPLUS1 >= ySize)
				yPLUS1 -= ySize;
			if(xMIN2 < 0)
				xMIN2 += xSize;
			if(xMIN1 < 0)
				xMIN1 += xSize;
			if(xPLUS1 >= xSize)
				xPLUS1 -= xSize;
			if(xPLUS2 >= xSize)
				xPLUS2 -= xSize;
			
			if(up)
			{
				n[0] = this.getCell(new Vector2i(xMIN1, yMIN1));
				n[1] = this.getCell(new Vector2i(x, yMIN1));
				n[2] = this.getCell(new Vector2i(xPLUS1, yMIN1));
				n[3] = this.getCell(new Vector2i(xMIN2, y));
				n[4] = this.getCell(new Vector2i(xMIN1, y));
				n[5] = this.getCell(new Vector2i(xPLUS1, y));
				n[6] = this.getCell(new Vector2i(xPLUS2, y));
				n[7] = this.getCell(new Vector2i(xMIN2, yPLUS1));
				n[8] = this.getCell(new Vector2i(xMIN1, yPLUS1));
				n[9] = this.getCell(new Vector2i(x, yPLUS1));
				n[10] = this.getCell(new Vector2i(xPLUS1, yPLUS1));
				n[11] = this.getCell(new Vector2i(xPLUS2, yPLUS1));
			}
			else
			{
				n[0] = this.getCell(new Vector2i(xMIN2, yMIN1));
				n[1] = this.getCell(new Vector2i(xMIN1, yMIN1));
				n[2] = this.getCell(new Vector2i(x, yMIN1));
				n[3] = this.getCell(new Vector2i(xPLUS1, yMIN1));
				n[4] = this.getCell(new Vector2i(xPLUS2, yMIN1));
				n[5] = this.getCell(new Vector2i(xMIN2, y));
				n[6] = this.getCell(new Vector2i(xMIN1, y));
				n[7] = this.getCell(new Vector2i(xPLUS1, y));
				n[8] = this.getCell(new Vector2i(xPLUS2, y));
				n[9] = this.getCell(new Vector2i(xMIN1, yPLUS1));
				n[10] = this.getCell(new Vector2i(x, yPLUS1));
				n[11] = this.getCell(new Vector2i(xPLUS1, yPLUS1));
			}
		}
		else
		{
			if(up)
			{
				n[0] = setNeighbours(xMIN1, yMIN1, xSize, ySize);
				n[1] = setNeighbours(x, yMIN1, xSize, ySize);
				n[2] = setNeighbours(xPLUS1, yMIN1, xSize, ySize);
				n[3] = setNeighbours(xMIN2, y, xSize, ySize);
				n[4] = setNeighbours(xMIN1, y, xSize, ySize);
				n[5] = setNeighbours(xPLUS1, y, xSize, ySize);
				n[6] = setNeighbours(xPLUS2, y, xSize, ySize);
				n[7] = setNeighbours(xMIN2, yPLUS1, xSize, ySize);
				n[8] = setNeighbours(xMIN1, yPLUS1, xSize, ySize);
				n[9] = setNeighbours(x, yPLUS1, xSize, ySize);
				n[10] = setNeighbours(xPLUS1, yPLUS1, xSize, ySize);
				n[11] = setNeighbours(xPLUS2, yPLUS1, xSize, ySize);
			}
			else
			{
				n[0] = setNeighbours(xMIN2, yMIN1, xSize, ySize);
				n[1] = setNeighbours(xMIN1, yMIN1, xSize, ySize);
				n[2] = setNeighbours(x, yMIN1, xSize, ySize);
				n[3] = setNeighbours(xPLUS1, yMIN1, xSize, ySize);
				n[4] = setNeighbours(xPLUS2, yMIN1, xSize, ySize);
				n[5] = setNeighbours(xMIN2, y, xSize, ySize);
				n[6] = setNeighbours(xMIN1, y, xSize, ySize);
				n[7] = setNeighbours(xPLUS1, y, xSize, ySize);
				n[8] = setNeighbours(xPLUS2, y, xSize, ySize);
				n[9] = setNeighbours(xMIN1, yPLUS1, xSize, ySize);
				n[10] = setNeighbours(x, yPLUS1, xSize, ySize);
				n[11] = setNeighbours(xPLUS1, yPLUS1, xSize, ySize);
			}
		}
		return n;
	}
	
	/**
	 * Chooses whether the neighbour is valid or should be set to null.
	 * 
	 * @param x - x position of target cell
	 * @param y - y position of target cell
	 * @param xdim - x dimension of the grid
	 * @param ydim - y dimension of the grid
	 * 
	 * @return - the updated neighbours array
	 */
	private Cell setNeighbours(int x, int y, int xdim, int ydim)
	{
		if((x < 0) || (x >= xdim))
			return null;
		else if((y < 0) || (y >= ydim))
			return null;
		else
			return getCell(x, y);
	}
}
