package com.hexcore.cas.control.server;

import com.hexcore.cas.math.Recti;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Grid;

/**
 * Class ThreadWork
 * 	The class that is used by the server and client to designate and
 * 	complete partitions of work and computation.
 * 
 * @authors Divan Burger; Megan Duncan; Apurva Kumar
 */

public class ThreadWork
{
	private Grid		grid = null;
	private int			generation = 0;
	private int			id = -1;
	private long		startTime = -1;
	private Recti		workable = null;
	private Vector2i	position;
	
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
	
	public Vector2i getPosition()
	{
		return position;
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
