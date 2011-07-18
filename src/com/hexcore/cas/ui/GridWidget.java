package com.hexcore.cas.ui;

import com.hexcore.cas.math.Vector2i;

public abstract class GridWidget extends Widget
{
	public GridWidget(Vector2i size)
	{
		super(size);
	}

	public GridWidget(Vector2i position, Vector2i size)
	{
		super(position, size);
	}
}
