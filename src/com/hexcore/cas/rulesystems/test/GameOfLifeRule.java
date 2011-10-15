package com.hexcore.cas.rulesystems.test;

import com.hexcore.cas.model.Cell;
import com.hexcore.cas.rulesystems.Rule;

public class GameOfLifeRule implements Rule
{
	// Game of Life implementation for testing
	
	@Override
	public void run(Cell cell, Cell[] neighbours)
	{
		int sum = 0;
		
		for (Cell neighbour : neighbours)
			if (neighbour != null)
				sum += neighbour.getValue(0);
		
		if(sum < 2 || sum > 3)
			cell.setValue(0, 0);
		else if(sum == 3)
			cell.setValue(0, 1);
	}

	@Override
	public int getNumProperties()
	{
		return 2;
	}
	
	
}
