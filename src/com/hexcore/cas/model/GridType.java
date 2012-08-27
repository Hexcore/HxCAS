package com.hexcore.cas.model;

import com.hexcore.cas.math.Vector2i;

/**
 * Class GridType
 * 	Manages 4 different possible grid shapes:
 * 	Rectangle, hexagon, triangle and von Nuemann
 * 
 * @author Divan Burger
 */

public enum GridType
{
	RECTANGLE('R', RectangleGrid.class),
	HEXAGON('H', HexagonGrid.class),
	TRIANGLE('T', TriangleGrid.class),
	VONNEUMANN('V', VonNeumannGrid.class);
	
	char symbol;
	Class<? extends Grid> clazz;
	
	public Grid create(Vector2i size, Cell example)
	{
		try
		{
			return (Grid)clazz.getDeclaredConstructor(Vector2i.class, Cell.class).newInstance(size, example);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;	
	}
	
	public Grid create(Vector2i size, int numProperties)
	{
		try
		{
			return (Grid)clazz.getDeclaredConstructor(Vector2i.class, int.class).newInstance(size, numProperties);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/////////////////////////////////////////////
	/// Private functions
	GridType(char symbol, Class<? extends Grid> clazz)
	{
		this.symbol = symbol;
		this.clazz = clazz;
	}
}
