package com.hexcore.cas.ui;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.ColourRuleSet;
import com.hexcore.cas.model.Grid;

public abstract class GridWidget<T extends Grid> extends Widget
{
	protected T		grid;
	protected int	tileSize;
	protected int	colourProperty = 0; //< The property that is used to determine the colour to use
	
	protected Colour	backgroundColour = Colour.BLACK;
	
	protected ColourRuleSet	colourRules;
	
	public GridWidget(Vector2i size, T grid, int tileSize)
	{
		super(size);
		this.grid = grid;
		this.tileSize = tileSize;
	}

	public GridWidget(Vector2i position, Vector2i size, T grid, int tileSize)
	{
		super(position, size);
		this.grid = grid;
		this.tileSize = tileSize;
	}

	public void setBackgroundColour(Colour colour)
	{
		backgroundColour = colour;
	}
	
	public void setColourProperty(int propertyIndex)
	{
		colourProperty = propertyIndex;
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
