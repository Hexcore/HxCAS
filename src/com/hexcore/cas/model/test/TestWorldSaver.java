package com.hexcore.cas.model.test;

import java.io.IOException;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.HexagonGrid;
import com.hexcore.cas.model.WorldReader;
import com.hexcore.cas.model.WorldSaver;

import junit.framework.TestCase;

public class TestWorldSaver extends TestCase
{
	private final WorldReader reader = new WorldReader("Test Data/savedWorld.caw");
	private final WorldSaver saver = new WorldSaver("Test Data/savedWorld.caw", null);
	
	public void test1GetName()
	{
		assertEquals("savedWorld.caw", saver.getWorldName());
	}
	
	public void test2SetName()
	{
		saver.setWorldName("Test Data/bleg.caw");
		assertEquals("bleg.caw", saver.getWorldName());
	}
	
	public void test3SaveWorld()
		throws IOException
	{
		HexagonGrid w = new HexagonGrid(new Vector2i(2, 3));
		int[] val1 = new int[1];
		int[] val2 = new int[2];
		val1[0] = 0;
		w.setCells(new Vector2i(0, 0), val1);
		val1[0] = 1;
		w.setCells(new Vector2i(1, 0), val1);
		val1[0] = 2;
		w.setCells(new Vector2i(0, 1), val1);
		val2[0] = 3;
		val2[1] = 4;
		w.setCells(new Vector2i(1, 1), val2);
		val1[0] = 5;
		w.setCells(new Vector2i(0, 2), val1);
		val1[0] = 6;
		w.setCells(new Vector2i(1, 2), val1);
		saver.setWorld(w);
		saver.saveWorld();
		Grid world = reader.readWorld();
		assertEquals('H', world.getType());
		assertEquals(2, world.getWidth());
		assertEquals(3, world.getHeight());
		assertEquals(0, world.getCell(0, 0).getValue(0));
		assertEquals(1, world.getCell(1, 0).getValue(0));
		assertEquals(2, world.getCell(0, 1).getValue(0));
		assertEquals(3, world.getCell(1, 1).getValue(0));
		assertEquals(4, world.getCell(1, 1).getValue(1));
		assertEquals(5, world.getCell(0, 2).getValue(0));
		assertEquals(6, world.getCell(1, 2).getValue(0));
	}
}
