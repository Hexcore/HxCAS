package com.hexcore.cas.ui.toolkit;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.ColourRuleSet;
import com.hexcore.cas.model.Grid;

public abstract class GridWidget<T extends Grid> extends Widget
{
	protected T		grid;
	protected int	cellSize;
	
	protected Colour		backgroundColour = Colour.BLACK;
	protected ColourRuleSet	colourRules;
	
	public GridWidget(Vector2i size, T grid, int cellSize)
	{
		super(size);
		this.grid = grid;
		this.cellSize = cellSize;
	}

	public GridWidget(Vector2i position, Vector2i size, T grid, int cellSize)
	{
		super(position, size);
		this.grid = grid;
		this.cellSize = cellSize;
	}

	public void setBackgroundColour(Colour colour)
	{
		backgroundColour = colour;
	}
	
	public void setGrid(T grid)
	{
		this.grid = grid;
	}

	public void setColourRuleSet(ColourRuleSet ruleSet)
	{
		colourRules = ruleSet;
	}
}
