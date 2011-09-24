package com.hexcore.cas.control.protocol;

import com.hexcore.cas.model.Grid;
import com.hexcore.cas.rulesystems.HexcoreVM;

public abstract class Overseer extends Thread
{
	protected Grid grid = null;
	
	public Overseer()
	{
	}
	
	public Overseer(Grid g)
	{
		grid = g;
	}
	
	public int checkState()
	{
		return -1;
	}
	
	public Grid getGrid()
	{
		return grid;
	}
	
	public void setGrid(Grid g)
	{
		grid = g.clone();
	}
}
