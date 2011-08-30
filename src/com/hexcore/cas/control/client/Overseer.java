package com.hexcore.cas.control.client;

import com.hexcore.cas.control.protocol.CAPInformationProcessor;
import com.hexcore.cas.math.Recti;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.HexagonGrid;
import com.hexcore.cas.model.RectangleGrid;
import com.hexcore.cas.model.TriangleGrid;
import com.hexcore.cas.rulesystems.HexcoreVM;

public abstract class Overseer
{
	protected CAPInformationProcessor capIP = null;
	protected HexcoreVM vm = null;
	protected Grid grid = null;
	
	public Overseer(Grid g)
	{
		grid = g;
		vm = new HexcoreVM();
	}
	
	public int checkState()
	{
		return -1;
	}
	
	public void disconnect()
	{
		capIP.disconnect();
	}
	
	public Grid getGrid()
	{
		return grid;
	}
	
	public void setGrid(Grid g)
	{
		grid = g.clone();
	}
	
	public abstract void start();
}
