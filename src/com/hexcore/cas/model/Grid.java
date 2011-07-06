package com.hexcore.cas.model;

import com.hexcore.cas.math.Vector2i;

public abstract class Grid
{
	public abstract Cell[] getNeighbours(Vector2i pos);
}
