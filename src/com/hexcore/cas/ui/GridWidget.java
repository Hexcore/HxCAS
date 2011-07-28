package com.hexcore.cas.ui;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.ColourRule;
import com.hexcore.cas.model.Grid;

public abstract class GridWidget<T extends Grid> extends Widget
{
	protected T		grid;
	protected int	tileSize;
	protected int	colourProperty; //< The property that is used to determine the colour to draw
	
	protected ColourRule	colourRule = null; // This is temporary
	
	public GridWidget(Vector2i size, T grid, int tileSize)
	{
		super(size);
		this.grid = grid;
		this.tileSize = tileSize;
		this.colourProperty = 0;
	}

	public GridWidget(Vector2i position, Vector2i size, T grid, int tileSize)
	{
		super(position, size);
		this.grid = grid;
		this.tileSize = tileSize;
		this.colourProperty = 0;
	}

	public void setColourProperty(int propertyIndex)
	{
		colourProperty = propertyIndex;
	}

	public void setGrid(T grid)
	{
		this.grid = grid;
	}
	
	/*
	 * This is temporary
	 */
	public void setColourRule(ColourRule rule)
	{
		colourRule = rule;
	}
}
