package com.hexcore.cas.model;

import com.hexcore.cas.math.Vector2i;

public class ThreadState
{
	public Cell[] work = null;
	public Vector2i startingPosition = null;
	public int num = -1;
	
	public ThreadState(Cell[] w, Vector2i v, int n)
	{
		int s = w.length;
		work = new Cell[s];
		for(int i = 0; i < s; i++)
			work[i] = new Cell(w[i]);
		startingPosition = new Vector2i(v);
		num = n;
	}
}