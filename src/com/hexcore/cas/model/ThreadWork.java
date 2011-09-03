package com.hexcore.cas.model;

import com.hexcore.cas.math.Recti;

public class ThreadWork
{
	private Grid grid = null;
	private Recti workable = null;
	
	public ThreadWork(Grid g, Recti w)
	{
		grid = g.clone();
		workable = new Recti(w.getPosition(), w.getSize());
	}
	
	public ThreadWork clone()
	{
		return new ThreadWork(this.getGrid(), this.getWorkableArea());
	}
	
	public Grid getGrid()
	{
		return grid;
	}
	
	public Recti getWorkableArea()
	{
		return workable;
	}
	
	public void setGrid(Grid g)
	{
		grid = g.clone();
	}
	
	public void setWorkableArea(Recti w)
	{
		workable = new Recti(w.getPosition(), w.getSize());
	}
}
