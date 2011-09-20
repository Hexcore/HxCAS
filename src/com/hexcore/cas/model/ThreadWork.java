package com.hexcore.cas.model;

import com.hexcore.cas.math.Recti;

public class ThreadWork
{
	private Grid grid = null;
	private int currGeneration = 0;
	private int ID = -1;
	private Recti workable = null;
	
	public ThreadWork(Grid g, Recti w, int lastID, int cG)
	{
		ID = lastID;
		currGeneration = cG;
		grid = g.clone();
		workable = new Recti(w.getPosition(), w.getSize());
	}
	
	public ThreadWork clone()
	{
		return new ThreadWork(this.getGrid(), this.getWorkableArea(), this.getID(), this.getCurrentGeneration());
	}
	
	public boolean equals(ThreadWork tw)
	{
		if(this.getID() == tw.getID())
			return true;
		return false;
	}
	
	public int getCurrentGeneration()
	{
		return currGeneration;
	}
	
	public Grid getGrid()
	{
		return grid;
	}
	
	public int getID()
	{
		return ID;
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
