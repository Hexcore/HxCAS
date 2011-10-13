package com.hexcore.cas.control.server;

import com.hexcore.cas.math.Recti;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Grid;

public class ThreadWork
{
	private Grid		grid = null;
	private int			generation = 0;
	private int			id = -1;
	private Vector2i	position;
	private Recti		workable = null;
	private long		startTime = -1;
	
	public ThreadWork(int generation, int id, Grid grid, Vector2i position, Recti workable)
	{
		this.generation = generation;
		this.id = id;
		this.grid = grid.clone();
		this.position = new Vector2i(position);
		this.workable = new Recti(workable);
		this.startTime = -1;
	}
	
	public ThreadWork clone()
	{
		return new ThreadWork(this.getCurrentGeneration(), this.getID(), this.getGrid(), this.getPosition(), this.getWorkableArea());
	}
	
	public boolean equals(ThreadWork tw)
	{
		return (this.generation == tw.generation && this.id == tw.id);
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
	
	public Vector2i getPosition()
	{
		return position;
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
