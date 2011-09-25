package com.hexcore.cas.model;

import com.hexcore.cas.math.Recti;

public class ThreadWork
{
	private Grid grid = null;
	private int generation = 0;
	private int id = -1;
	private Recti workable = null;
	private long startTime = -1;
	
	public ThreadWork(int id, Grid grid, Recti workable, int generation)
	{
		this.id = id;
		this.generation = generation;
		this.grid = grid.clone();
		this.workable = new Recti(workable);
		this.startTime = -1;
	}
	
	public ThreadWork clone()
	{
		return new ThreadWork(this.getID(), this.getGrid(), this.getWorkableArea(), this.getCurrentGeneration());
	}
	
	public boolean equals(ThreadWork tw)
	{
		if(this.getID() == tw.getID())
			return true;
		return false;
	}
	
	public int getCurrentGeneration()
	{
		return generation;
	}
	
	public Grid getGrid()
	{
		return grid;
	}
	
	public int getID()
	{
		return id;
	}
	
	public Recti getWorkableArea()
	{
		return workable;
	}
	
	public long getStartTime()
	{
		return startTime;
	}	
	
	public void setStartTime(long time)
	{
		startTime = time;
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
