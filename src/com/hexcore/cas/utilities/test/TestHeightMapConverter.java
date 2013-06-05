package com.hexcore.cas.utilities.test;

import org.junit.Test;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.RectangleGrid;
import com.hexcore.cas.utilities.HeightMapConverter;

public class TestHeightMapConverter
{
	@Test
	public void testSimpleJPG()
	{
		Cell c = new Cell(new double[]{0,0});
		Grid g = new RectangleGrid(new Vector2i(100, 100), c);
		
		
		HeightMapConverter hc = new HeightMapConverter();
		hc.loadHeightMap("Test Data/heightmap.jpg", g, 1);
	}
}
