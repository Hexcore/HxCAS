package com.hexcore.cas.ui;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.ColourRule;

public abstract class GridWidget extends Widget
{
	protected ColourRule	colourRule = null; // This is temporary
	
	public GridWidget(Vector2i size)
	{
		super(size);
	}

	public GridWidget(Vector2i position, Vector2i size)
	{
		super(position, size);
	}
	
	/*
	 * This is temporary
	 */
	public void setColourRule(ColourRule rule)
	{
		colourRule = rule;
	}
}
