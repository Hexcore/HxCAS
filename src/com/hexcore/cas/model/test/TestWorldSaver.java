package com.hexcore.cas.model.test;

import java.io.IOException;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.HexagonGrid;
import com.hexcore.cas.model.TriangleGrid;
import com.hexcore.cas.model.WorldReader;
import com.hexcore.cas.model.WorldSaver;

import junit.framework.TestCase;

public class TestWorldSaver extends TestCase
{
	private final WorldReader reader = new WorldReader("Test Data/savedWorld.cawzip");
	private final WorldSaver saver = new WorldSaver("Test Data/savedWorld.cawzip");
	
	public void test1GetName()
	{
		assertEquals("savedWorld.cawzip", saver.getWorldName());
	}
	
	public void test2SetName()
	{
		saver.setWorldName("Test Data/bleg.cawzip");
		assertEquals("bleg.cawzip", saver.getWorldName());
	}
	
	public void test3AddGeneration()
	{
		WorldSaver s = new WorldSaver("Test Data/addGen.cawzip");
		TriangleGrid w = new TriangleGrid(new Vector2i(1, 1));
		s.addGeneration(w);
		assertEquals(1, s.getListWorldSize());
	}
	
	public void test4SaveWorld()
		throws IOException
	{
		HexagonGrid w1 = new HexagonGrid(new Vector2i(2, 3));
		int[] val = new int[2];
		val[0] = 0;
		val[1] = 1;
		w1.setCells(new Vector2i(0, 0), val);
		val[0] = 2;
		val[1] = 3;
		w1.setCells(new Vector2i(1, 0), val);
		val[0] = 4;
		val[1] = 5;
		w1.setCells(new Vector2i(0, 1), val);
		val[0] = 6;
		val[1] = 7;
		w1.setCells(new Vector2i(1, 1), val);
		val[0] = 8;
		val[1] = 9;
		w1.setCells(new Vector2i(0, 2), val);
		val[0] = 10;
		val[1] = 11;
		w1.setCells(new Vector2i(1, 2), val);
		saver.addGeneration(w1);
		
		HexagonGrid w2 = new HexagonGrid(new Vector2i(2, 3));
		val[0] = 1;
		val[1] = 2;
		w2.setCells(new Vector2i(0, 0), val);
		val[0] = 3;
		val[1] = 4;
		w2.setCells(new Vector2i(1, 0), val);
		val[0] = 5;
		val[1] = 6;
		w2.setCells(new Vector2i(0, 1), val);
		val[0] = 7;
		val[1] = 8;
		w2.setCells(new Vector2i(1, 1), val);
		val[0] = 9;
		val[1] = 10;
		w2.setCells(new Vector2i(0, 2), val);
		val[0] = 11;
		val[1] = 12;
		w2.setCells(new Vector2i(1, 2), val);
		saver.addGeneration(w2);
		
		HexagonGrid w3 = new HexagonGrid(new Vector2i(2, 3));
		val[0] = 2;
		val[1] = 3;
		w3.setCells(new Vector2i(0, 0), val);
		val[0] = 4;
		val[1] = 5;
		w3.setCells(new Vector2i(1, 0), val);
		val[0] = 6;
		val[1] = 7;
		w3.setCells(new Vector2i(0, 1), val);
		val[0] = 8;
		val[1] = 9;
		w3.setCells(new Vector2i(1, 1), val);
		val[0] = 10;
		val[1] = 11;
		w3.setCells(new Vector2i(0, 2), val);
		val[0] = 12;
		val[1] = 13;
		w3.setCells(new Vector2i(1, 2), val);
		saver.addGeneration(w3);
		
		saver.saveWorld();
		Grid[] world = reader.readWorld();
		
		//Generation 1
		assertEquals('H', world[0].getType());
		assertEquals(2, world[0].getWidth());
		assertEquals(3, world[0].getHeight());
		assertEquals(0, world[0].getCell(0, 0).getValue(0));
		assertEquals(1, world[0].getCell(0, 0).getValue(1));
		assertEquals(2, world[0].getCell(1, 0).getValue(0));
		assertEquals(3, world[0].getCell(1, 0).getValue(1));
		assertEquals(4, world[0].getCell(0, 1).getValue(0));
		assertEquals(5, world[0].getCell(0, 1).getValue(1));
		assertEquals(6, world[0].getCell(1, 1).getValue(0));
		assertEquals(7, world[0].getCell(1, 1).getValue(1));
		assertEquals(8, world[0].getCell(0, 2).getValue(0));
		assertEquals(9, world[0].getCell(0, 2).getValue(1));
		assertEquals(10, world[0].getCell(1, 2).getValue(0));
		assertEquals(11, world[0].getCell(1, 2).getValue(1));
		
		//Generation 2
		assertEquals('H', world[1].getType());
		assertEquals(2, world[1].getWidth());
		assertEquals(3, world[1].getHeight());
		assertEquals(1, world[1].getCell(0, 0).getValue(0));
		assertEquals(2, world[1].getCell(0, 0).getValue(1));
		assertEquals(3, world[1].getCell(1, 0).getValue(0));
		assertEquals(4, world[1].getCell(1, 0).getValue(1));
		assertEquals(5, world[1].getCell(0, 1).getValue(0));
		assertEquals(6, world[1].getCell(0, 1).getValue(1));
		assertEquals(7, world[1].getCell(1, 1).getValue(0));
		assertEquals(8, world[1].getCell(1, 1).getValue(1));
		assertEquals(9, world[1].getCell(0, 2).getValue(0));
		assertEquals(10, world[1].getCell(0, 2).getValue(1));
		assertEquals(11, world[1].getCell(1, 2).getValue(0));
		assertEquals(12, world[1].getCell(1, 2).getValue(1));
		
		//Generation 3
		assertEquals('H', world[2].getType());
		assertEquals(2, world[2].getWidth());
		assertEquals(3, world[2].getHeight());
		assertEquals(2, world[2].getCell(0, 0).getValue(0));
		assertEquals(3, world[2].getCell(0, 0).getValue(1));
		assertEquals(4, world[2].getCell(1, 0).getValue(0));
		assertEquals(5, world[2].getCell(1, 0).getValue(1));
		assertEquals(6, world[2].getCell(0, 1).getValue(0));
		assertEquals(7, world[2].getCell(0, 1).getValue(1));
		assertEquals(8, world[2].getCell(1, 1).getValue(0));
		assertEquals(9, world[2].getCell(1, 1).getValue(1));
		assertEquals(10, world[2].getCell(0, 2).getValue(0));
		assertEquals(11, world[2].getCell(0, 2).getValue(1));
		assertEquals(12, world[2].getCell(1, 2).getValue(0));
		assertEquals(13, world[2].getCell(1, 2).getValue(1));
	}
}
