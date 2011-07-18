package com.hexcore.cas.test;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.HexagonGrid;
import com.hexcore.cas.model.RectangleGrid;
import com.hexcore.cas.model.TriangleGrid;

public class GameOfLife 
{	
	Grid	grid;
	
	public static void main(String [] args)
	{
		GameOfLife 	game = new GameOfLife();
		int 		iter = 10;
		
		//run the game of life :P
		game.run(iter);
	}
	
	public GameOfLife()
	{
		//start with 5x5 rectangle grid because I know how this is expected to perform.
		grid = new RectangleGrid(new Vector2i(5, 5));
		
		//set specific pattern here - can be changed to an input file later.
		//Going to start with a simple blinker at the moment
		grid.getCell(new Vector2i(2,1)).setValue(0, 1);
		grid.getCell(new Vector2i(2,2)).setValue(0, 1);
		grid.getCell(new Vector2i(2,3)).setValue(0, 1);
	}
	
	public GameOfLife(Grid grid)
	{
		this.grid = grid;
	}
	
	public void run(int iter)
	{
		displayGrid();
		while (iter > 0)
		{
			generateNextGeneration();
			displayGrid();
			iter--;
		}//while
	}//end method run
	
	public void displayGrid()
	{
		for(int i = 0; i < grid.getHeight(); i++)
		{
			for(int j = 0; j < grid.getWidth(); j++)
			{
				System.out.print(grid.getCell(j, i).getValue(0) + " ");
			}//for
			System.out.println();
		}//for
		System.out.println();
	}//end method displayGrid
	
	public Grid getGrid()
	{
		return grid;
	}
	
	public void generateNextGeneration()
	{
		//make a new copy of the grid.
		char c = grid.getType();
		Grid temp = null;
		switch(c)
		{
		case 'R':
			temp = new RectangleGrid(grid);
			break;
		case 'T':
			temp = new TriangleGrid(grid);
			break;
		case 'H':
			temp = new HexagonGrid(grid);
			break;
		}
		
		for(int y = 0; y < grid.getHeight(); y++)
		{
			for(int x = 0; x < grid.getWidth(); x++)
			{
				//for each cell:
				Cell [] neighbours = grid.getNeighbours(new Vector2i(x,y));//getNeighbours
				//now apply rules, and set the appropriate value to the temporary grid.
				temp.getCell(x, y).setValue(0, applyRules(neighbours,grid.getCell(x, y).getValue(0)));
			}
		}
		
		grid = temp;//return the updated grid.
	}
	
	public int applyRules(Cell[] neighbours, int value)
	{
		int count = 0;
		for(int i = 0; i < neighbours.length; i++)
		{
			if(neighbours[i].getValue(0) == 1)
				count++;
		}
		//apply rules
		if(count < 2)
			return 0;
		else if (count > 3)
			return 0;
		else if((value == 0) && (count == 3))
			return 1;
		else 
			return value;
	}
}
