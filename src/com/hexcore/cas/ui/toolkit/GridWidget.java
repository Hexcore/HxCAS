package com.hexcore.cas.ui.toolkit;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.ColourRuleSet;
import com.hexcore.cas.model.Grid;

public abstract class GridWidget extends Widget
{
	protected Grid	grid;
	protected int	cellSize;
	
	protected Colour		backgroundColour = Colour.BLACK;
	protected ColourRuleSet	colourRules;
	
	public GridWidget(Vector2i size, Grid grid, int cellSize)
	{
		super(size);
		this.grid = grid;
		this.cellSize = cellSize;
	}

	public GridWidget(Vector2i position, Vector2i size, Grid grid, int cellSize)
	{
		super(position, size);
		this.grid = grid;
		this.cellSize = cellSize;
	}

	public void setBackgroundColour(Colour colour)
	{
		backgroundColour = colour;
	}
	
	public void setGrid(Grid grid)
	{
		this.grid = grid;
	}

	public void setColourRuleSet(ColourRuleSet ruleSet)
	{
		colourRules = ruleSet;
	}
}
