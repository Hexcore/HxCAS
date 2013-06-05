package com.hexcore.cas.model;

import com.hexcore.cas.math.Vector2i;

/**
 * Class Grid
 * 	This is the grid representing a world.
 * 	It stores details of the wrappability, number of properties and size.
 * 
 * @author Divan Burger; Megan Duncan; Apurva Kumar
 */

public abstract class Grid
{
	protected boolean		wrap = true;
	protected Cell[][] 		cells = null;
	protected int			numProperties = 0;
	protected Vector2i 		size = null;

	/**
	 * Grid copy constructor.
	 * 
	 * @param grid - the grid to be cloned
	 */
	public Grid(Grid grid)
	{
		this.numProperties = grid.numProperties;
		this.wrap = grid.wrap;
		this.size = new Vector2i(grid.size);
		this.cells = new Cell[grid.size.y][grid.size.x];
		for(int y = 0; y < size.y; y++)
			for(int x = 0; x < size.x; x++)
				this.cells[y][x] = new Cell(grid.getCell(x, y));
	}
	
	/**
	 * Grid custom constructor.
	 * 
	 * @param size - width and height of the desired grid size
	 * @param example - an example cell that will be used to populate the grid
	 */
	public Grid(Vector2i size, Cell example)
	{
		this.numProperties = example.getValueCount();
		this.size = new Vector2i(size);
		this.cells = new Cell[size.y][size.x];
		for(int y = 0; y < size.y; y++)
			for(int x = 0; x < size.x; x++)
			{
				this.cells[y][x] = new Cell(example.getValueCount());
				for(int i = 0; i < example.getValueCount(); i++)
					this.cells[y][x].setValue(i, example.getValue(i));
			}
	}
	
	/**
	 * Grid custom constructor.
	 * 
	 * @param size - width and height of the desired grid size
	 * @param numProperties - the number of properties that each cell of the grid must have
	 */
	public Grid(Vector2i size, int numProperties)
	{
		this.numProperties = numProperties;
		this.size = new Vector2i(size);
		this.cells = new Cell[size.y][size.x];
		for(int y = 0; y < size.y; y++)
			for(int x = 0; x < size.x; x++)
				this.cells[y][x] = new Cell(numProperties);
	}

	/**
	 * Abstract clone function.
	 * Use clone functions of the individual grid types.
	 */
	public abstract Grid clone();
	
	/**
	 * Returns the cell at the given coordinates. 
	 * 
	 * @param x - column in which the cell can be found in the grid
	 * @param y - row in which the cell can be found in the grid
	 * 
	 * @return - the cell at column x and row y
	 */
	public Cell getCell(int x, int y)
	{
		return cells[y][x];
	}
	
	/**
	 * Returns the cell at the given coordinates.
	 * 
	 * @param pos - the Vector2i of the column and row in which the cell can be found in the grid
	 * 
	 * @return - the cell at column pos.x and row pos.y
	 */
	public Cell getCell(Vector2i pos)
	{
		return cells[pos.y][pos.x];
	}
	
	/**
	 * Returns the height of the grid.
	 * 
	 * @return - the height of the grid
	 */
	public int getHeight()
	{
		return size.y;
	}
	
	/**
	 * Abstract getNeighbourhoodRange function.
	 * Use getNeighbourhoodRange functions of the individual grid types.
	 */
	public abstract Vector2i getNeighbourhoodRange();
	
	/**
	 * Abstract getNeighbours function.
	 * Use getNeighbours functions of the individual grid types.
	 */
	public abstract Cell[] getNeighbours(Vector2i pos);
	
	/**
	 * Returns the number of properties that each cell in the grid has.
	 * 
	 * @return - number of properties per cell
	 */
	public int getNumProperties()
	{
		return numProperties;
	}
	
	/**
	 * Abstract getType function.
	 * Use getType functions of the individual grid types.
	 */
	public abstract GridType getType();
	
	/**
	 * Returns the symbol for the enumerated types of the different grids.
	 * 
	 * @return - character symbol for the enumerated type
	 */
	public char getTypeSymbol()
	{
		return getType().symbol;
	}
	
	/**
	 * Returns the width and height of the grid as a Vector2i object.
	 * 
	 * @return - size of the grid
	 */
	public Vector2i getSize()
	{
		return size;
	}
	
	/**
	 * Returns the width of the grid.
	 * 
	 * @return - the width of the grid
	 */
	public int getWidth()
	{
		return size.x;
	}
	
	/**
	 * Returns true if the grid is wrappable, otherwise returns false.
	 * 
	 * @return - boolean true or false
	 */
	public boolean isWrappable()
	{
		return wrap;
	}
	
	/**
	 * Sets the cell at the given coordinates with a copy of the given cell.
	 * 
	 * @param x - column in which the cell can be found in the grid
	 * @param y - row in which the cell can be found in the grid
	 * @param cell - given cell that must be copied
	 */
	public void setCell(int x, int y, Cell cell)
	{
		cells[y][x] = new Cell(cell);
	}
	
	/**
	 * Sets the cell at the given coordinates with a cell that has all the given values.
	 * 
	 * @param x - column in which the cell can be found in the grid
	 * @param y - row in which the cell can be found in the grid
	 * @param vals - array in which the cell must be given as property values
	 */
	public void setCell(int x, int y, double[] vals)
	{
		cells[y][x] = new Cell(vals);
	}
	
	/**
	 * Sets the cell at the given coordinates with a copy of the given cell.
	 * 
	 * @param pos - the Vector2i of the column and row in which the cell can be found in the grid
	 * @param cell - given cell that must be copied
	 */
	public void setCell(Vector2i pos, Cell cell)
	{
		cells[pos.y][pos.x] = new Cell(cell);
	}
	
	/**
	 * Sets the cell at the given coordinates with a cell that has all the given values.
	 * 
	 * @param pos - the Vector2i of the column and row in which the cell can be found in the grid
	 * @param vals - array in which the cell must be given as property values
	 */
	public void setCell(Vector2i pos, double[] vals)
	{
		cells[pos.y][pos.x] = new Cell(vals);
	}
	
	/**
	 * Sets the grid to either a wrappable grid or a non-wrappable grid.
	 * 
	 * @param wrappable - boolean value that is to be set
	 */
	public void setWrappable(boolean wrappable)
	{
		wrap = wrappable;
	}
}
