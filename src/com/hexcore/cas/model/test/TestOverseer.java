package com.hexcore.cas.model.test;

import junit.framework.TestCase;

import com.hexcore.cas.control.client.Overseer;
import com.hexcore.cas.math.Recti;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.HexagonGrid;
import com.hexcore.cas.model.RectangleGrid;

public class TestOverseer extends TestCase
{
	public void test1Constructor()
	{
		RectangleGrid g = new RectangleGrid(new Vector2i(4, 4), new Cell(1));
		for(int y = 0; y < 4; y++)
			for(int x = 0; x < 4; x++)
				g.getCell(x, y).setValue(0, 1);
		g.getCell(1, 1).setValue(0, 0);
		g.getCell(2, 2).setValue(0, 0);
		Overseer o = new Overseer(g, new Recti(new Vector2i(0, 0), new Vector2i(4, 4)));
		
		assertEquals('R', g.getType());
		assertEquals(4, g.getWidth());
		assertEquals(4, g.getHeight());
		assertEquals(1, g.getCell(0, 0).getValue(0));
		assertEquals(1, g.getCell(0, 1).getValue(0));
		assertEquals(1, g.getCell(0, 2).getValue(0));
		assertEquals(1, g.getCell(0, 3).getValue(0));
		assertEquals(1, g.getCell(1, 0).getValue(0));
		assertEquals(0, g.getCell(1, 1).getValue(0));
		assertEquals(1, g.getCell(1, 2).getValue(0));
		assertEquals(1, g.getCell(1, 3).getValue(0));
		assertEquals(1, g.getCell(2, 0).getValue(0));
		assertEquals(1, g.getCell(2, 1).getValue(0));
		assertEquals(0, g.getCell(2, 2).getValue(0));
		assertEquals(1, g.getCell(2, 3).getValue(0));
		assertEquals(1, g.getCell(3, 0).getValue(0));
		assertEquals(1, g.getCell(3, 1).getValue(0));
		assertEquals(1, g.getCell(3, 2).getValue(0));
		assertEquals(1, g.getCell(3, 3).getValue(0));
		
		assertEquals(0, o.getWorkable().getPosition().x);
		assertEquals(0, o.getWorkable().getPosition().y);
		assertEquals(4, o.getWorkable().getSize().x);
		assertEquals(4, o.getWorkable().getSize().y);
	}

	public void test2SetGrid()
	{
		RectangleGrid g = new RectangleGrid(new Vector2i(4, 4), new Cell(1));
		for(int y = 0; y < 4; y++)
			for(int x = 0; x < 4; x++)
				g.getCell(x, y).setValue(0, 1);
		Overseer o = new Overseer(g, new Recti(new Vector2i(0, 0), new Vector2i(4, 4)));
		HexagonGrid h = new HexagonGrid(new Vector2i(4, 4), new Cell(1));
		for(int y = 0; y < 4; y++)
			for(int x = 0; x < 4; x++)
				h.getCell(x, y).setValue(0, 0);
		o.setGrid(h);

		assertEquals('H', h.getType());
		assertEquals(4, h.getWidth());
		assertEquals(4, h.getHeight());
		assertEquals(0, h.getCell(0, 0).getValue(0));
		assertEquals(0, h.getCell(0, 1).getValue(0));
		assertEquals(0, h.getCell(0, 2).getValue(0));
		assertEquals(0, h.getCell(0, 3).getValue(0));
		assertEquals(0, h.getCell(1, 0).getValue(0));
		assertEquals(0, h.getCell(1, 1).getValue(0));
		assertEquals(0, h.getCell(1, 2).getValue(0));
		assertEquals(0, h.getCell(1, 3).getValue(0));
		assertEquals(0, h.getCell(2, 0).getValue(0));
		assertEquals(0, h.getCell(2, 1).getValue(0));
		assertEquals(0, h.getCell(2, 2).getValue(0));
		assertEquals(0, h.getCell(2, 3).getValue(0));
		assertEquals(0, h.getCell(3, 0).getValue(0));
		assertEquals(0, h.getCell(3, 1).getValue(0));
		assertEquals(0, h.getCell(3, 2).getValue(0));
		assertEquals(0, h.getCell(3, 3).getValue(0));
	}
	
	public void test3SetWorkable()
	{
		RectangleGrid g = new RectangleGrid(new Vector2i(4, 4), new Cell(1));
		for(int y = 0; y < 4; y++)
			for(int x = 0; x < 4; x++)
				g.getCell(x, y).setValue(0, 1);
		Overseer o = new Overseer(g, new Recti(new Vector2i(0, 0), new Vector2i(4, 4)));
		Recti r = new Recti(new Vector2i(1, 1), new Vector2i(1, 1));
		o.setWorkable(r);
		assertEquals(1, o.getWorkable().getPosition().x);
		assertEquals(1, o.getWorkable().getPosition().y);
		assertEquals(1, o.getWorkable().getSize().x);
		assertEquals(1, o.getWorkable().getSize().y);
	}
	
	public void test4Start()
	{
		int size = 250;
		RectangleGrid g = new RectangleGrid(new Vector2i(size, size), new Cell(1));
		for(int y = 0; y < size; y++)
			for(int x = 0; x < size; x++)
				g.getCell(x, y).setValue(0, 0);
		Overseer o = new Overseer(g, new Recti(new Vector2i(0, 0), new Vector2i(size, size)));
		o.start();

		assertEquals('R', g.getType());
		assertEquals(size, g.getWidth());
		assertEquals(size, g.getHeight());
		for(int x = 0; x < size; x++)
			for(int y = 0; y < size; y++)
			{
				String str = "y : " + y + "; x : " + x;
				assertEquals(str, 1, g.getCell(x, y).getValue(0));
			}
	}
}
