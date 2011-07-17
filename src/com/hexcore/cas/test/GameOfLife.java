package com.hexcore.cas.test;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.HexagonGrid;
import com.hexcore.cas.model.RectangleGrid;
import com.hexcore.cas.model.TriangleGrid;

public class GameOfLife 
{
	public static void main(String [] args)
	{
		//start with 5x5 rectangle grid because I know how this is expected to perform.
		RectangleGrid grid = new RectangleGrid(new Vector2i(5, 5));
		
		//set specific pattern here - can be changed to an input file later.
		//Going to start with a simple blinker at the moment
		grid.getCell(new Vector2i(2,1)).setValue(0, 1);
		grid.getCell(new Vector2i(2,2)).setValue(0, 1);
		grid.getCell(new Vector2i(2,3)).setValue(0, 1);
		
		//specify how many generations you want
		int iter = 10;
		//run the game of life :P
		run(grid, iter);
	}//end method main
	
	public static void run(Grid grid, int iter)
	{
		displayGrid(grid);
		while (iter > 0)
		{
			grid = generateNextGeneration(grid);
			displayGrid(grid);
			iter--;
		}//while
	}//end method run
	
	public static void displayGrid(Grid grid)
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
	
	public static Grid generateNextGeneration(Grid grid)
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
		return temp;//return the updated grid.
	}
	
	public static int applyRules(Cell[] neighbours, int value)
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
