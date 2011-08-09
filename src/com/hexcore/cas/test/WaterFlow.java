package com.hexcore.cas.test;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.RectangleGrid;

public class WaterFlow 
{	
	RectangleGrid	grid;
		
	public WaterFlow()
	{
		Cell cell = new Cell(3);
		cell.setValue(0, 0); // Type
		cell.setValue(1, 0); // Water
		cell.setValue(2, 0); // Land
		
		grid = new RectangleGrid(new Vector2i(32, 32), cell);
		grid.setCell(4, 4, new int[] {1, 15});
		
		for (int y = 0; y < grid.getHeight(); y++)
			for (int x = 0; x < grid.getWidth(); x++)
				grid.getCell(x, y).setValue(2, (int)(Math.sin(x) * 5) + 5);
	}

	public RectangleGrid getGrid()
	{
		return grid;
	}
	
	public void generateNextGeneration()
	{
		RectangleGrid temp = new RectangleGrid(grid);
		
		for (int y = 0; y < grid.getHeight(); y++)
			for (int x = 0; x < grid.getWidth(); x++)
			{
				Cell	self = temp.getCell(x, y);
				Cell [] neighbours = grid.getNeighbours(new Vector2i(x,y));
				
				if (self.getValue(0) == 0)
				{
					int maxWater = 0, minWater = 100;
					
					for (Cell n : neighbours)
					{
						if ((n.getValue(0) == 0) && (n.getValue(1) > 0))
						{
							int water = n.getValue(2) + n.getValue(1) - self.getValue(2);
							if (water > maxWater) maxWater = water;
							if (water < minWater) minWater = water;
						}
						else if (n.getValue(0) == 1)
							maxWater = n.getValue(2) + 15 - self.getValue(2);
					}
	
					int ownHeight = self.getValue(1);
					if (maxWater > ownHeight) 
						self.setValue(1, self.getValue(1) + 1);
					else if (maxWater < ownHeight)
						self.setValue(1, self.getValue(1) - 1);
				}
				else
					self.setValue(1, 15);
			}
		
		grid = temp;
	}
}
