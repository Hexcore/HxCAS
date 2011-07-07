package com.hexcore.cas.model;

import com.hexcore.cas.math.Vector2i;

public class TriangleGrid extends Grid
{
	public TriangleGrid(Vector2i size) 
	{
		super(size);
	}
	
	public TriangleGrid(Vector2i size, Cell example)
	{
		super(size, example);
	}

	private Cell[] getNeighbours(boolean up, Vector2i pos)
	{
		Cell[] n = new Cell[12];
		int xSize = this.getHeight();
		int ySize = this.getWidth();
		int x = pos.get(0);
		int y = pos.get(1);
		
		int xMIN1 = x - 1;
		int xPLUS1 = x + 1;
		if(xPLUS1 >= xSize)
			xPLUS1 -= xSize;
		if(xMIN1 < 0)
			xMIN1 += xSize;
		
		int yMIN2 = y - 2;
		int yMIN1 = y - 1;
		int yPLUS1 = y + 1;
		int yPLUS2 = y + 2;
		if(yMIN2 < 0)
			yMIN2 += ySize;
		if(yMIN1 < 0)
			yMIN1 += ySize;
		if(yPLUS1 >= ySize)
			yPLUS1 -= ySize;
		if(yPLUS2 >= ySize)
			yPLUS2 -= ySize;
		if(up)
		{
			n[0] = this.getCell(new Vector2i(xMIN1, yMIN1));
			n[1] = this.getCell(new Vector2i(xMIN1, y));
			n[2] = this.getCell(new Vector2i(xMIN1, yPLUS1));
			n[3] = this.getCell(new Vector2i(x, yMIN2));
			n[4] = this.getCell(new Vector2i(x, yMIN1));
			n[5] = this.getCell(new Vector2i(x, yPLUS1));
			n[6] = this.getCell(new Vector2i(x, yPLUS2));
			n[7] = this.getCell(new Vector2i(xPLUS1, yMIN2));
			n[8] = this.getCell(new Vector2i(xPLUS1, yMIN1));
			n[9] = this.getCell(new Vector2i(xPLUS1, y));
			n[10] = this.getCell(new Vector2i(xPLUS1, yPLUS1));
			n[11] = this.getCell(new Vector2i(xPLUS1, yPLUS2));
		}
		else
		{
			n[0] = this.getCell(new Vector2i(xMIN1, yMIN2));
			n[1] = this.getCell(new Vector2i(xMIN1, yMIN1));
			n[2] = this.getCell(new Vector2i(xMIN1, y));
			n[3] = this.getCell(new Vector2i(xMIN1, yPLUS1));
			n[4] = this.getCell(new Vector2i(xMIN1, yPLUS2));
			n[5] = this.getCell(new Vector2i(x, yMIN2));
			n[6] = this.getCell(new Vector2i(x, yMIN1));
			n[7] = this.getCell(new Vector2i(x, yPLUS1));
			n[8] = this.getCell(new Vector2i(x, yPLUS2));
			n[9] = this.getCell(new Vector2i(xPLUS1, yMIN1));
			n[10] = this.getCell(new Vector2i(xPLUS1, y));
			n[11] = this.getCell(new Vector2i(xPLUS1, yPLUS1));
		}
		return n;
	}
	
	@Override
	public Cell[] getNeighbours(Vector2i pos)
	{
		int x = pos.get(0);
		int y = pos.get(1);
		if((x % 2) != 0)
		{
			//Upside-down first triangle
			if((y % 2) == 0)
				return getNeighbours(true, pos);
			else
				return getNeighbours(false, pos);
		}
		else
		{
			//Right-side up first triangle
			if((y % 2) == 0)
				return getNeighbours(false, pos);
			else
				return getNeighbours(true, pos);
		}
	}

}
