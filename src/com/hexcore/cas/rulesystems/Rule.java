package com.hexcore.cas.rulesystems;

import java.util.ArrayList;

import com.hexcore.cas.model.Cell;

public interface Rule
{
	public void run(Cell cell, Cell[] neighbours);
	public int getNumProperties();
	public ArrayList<String> getPropertyList();
}
