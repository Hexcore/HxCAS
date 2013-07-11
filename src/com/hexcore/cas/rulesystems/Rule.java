package com.hexcore.cas.rulesystems;

import java.util.ArrayList;

import com.hexcore.cas.model.Cell;

/**
 * Class Rule

 * @authors Karl Zoller
 */

public interface Rule
{
	public void run(Cell cell, Cell[] neighbours);
	public void step();
	public void resetStep();
	public int getNumProperties();
	public ArrayList<String> getPropertyList();
}
