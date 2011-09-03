package com.hexcore.cas.control.protocol;

import com.hexcore.cas.model.Grid;
import com.hexcore.cas.rulesystems.HexcoreVM;

public abstract class Overseer extends Thread
{
	protected CAPInformationProcessor capIP = null;
	protected HexcoreVM vm = null;
	protected Grid grid = null;
	
	public Overseer()
	{
		vm = new HexcoreVM();
	}
	
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
