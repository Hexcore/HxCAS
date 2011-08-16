package com.hexcore.cas.model.test;

import java.io.IOException;

import junit.framework.TestCase;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.HexagonGrid;
import com.hexcore.cas.model.TriangleGrid;
import com.hexcore.cas.model.World;
import com.hexcore.cas.model.WorldReader;
import com.hexcore.cas.model.WorldSaver;

public class TestWorldSaver extends TestCase
{
	private final WorldReader reader = new WorldReader("Test Data/savedWorld.caw");
	private final WorldSaver saver = new WorldSaver("Test Data/savedWorld.caw");
	
	public void test1GetName()
	{
		assertEquals("savedWorld.caw", saver.getWorldName());
	}
	
	public void test2GetRulesAndColours()
	{
		assertEquals(null, saver.getRulesAndColours());
	}
	
	public void test3SetName()
	{
		saver.setWorldName("Test Data/bleg.caw");
		assertEquals("bleg.caw", saver.getWorldName());
	}
	
	public void test4AddGeneration()
	{
		WorldSaver s = new WorldSaver("Test Data/addGen.caw");
		TriangleGrid w = new TriangleGrid(new Vector2i(1, 1));
		s.addGeneration(w);
		assertEquals(1, s.getListWorldSize());
	}
	
	public void test5SetRulesAndColours()
	{
		saver.setRulesAndColours("I am RULES!\r\nI R COLOURS!");
		assertEquals("I am RULES!\r\nI R COLOURS!", saver.getRulesAndColours());
	}
	
	public void test6SaveWorld()
		throws IOException
	{
		HexagonGrid w1 = new HexagonGrid(new Vector2i(2, 3));
		int[] val = new int[2];
		val[0] = 0;
		val[1] = 1;
		w1.setCell(new Vector2i(0, 0), val);
		val[0] = 2;
		val[1] = 3;
		w1.setCell(new Vector2i(1, 0), val);
		val[0] = 4;
		val[1] = 5;
		w1.setCell(new Vector2i(0, 1), val);
		val[0] = 6;
		val[1] = 7;
		w1.setCell(new Vector2i(1, 1), val);
		val[0] = 8;
		val[1] = 9;
		w1.setCell(new Vector2i(0, 2), val);
		val[0] = 10;
		val[1] = 11;
		w1.setCell(new Vector2i(1, 2), val);
		saver.addGeneration(w1);
		
		HexagonGrid w2 = new HexagonGrid(new Vector2i(2, 3));
		val[0] = 1;
		val[1] = 2;
		w2.setCell(new Vector2i(0, 0), val);
		val[0] = 3;
		val[1] = 4;
		w2.setCell(new Vector2i(1, 0), val);
		val[0] = 5;
		val[1] = 6;
		w2.setCell(new Vector2i(0, 1), val);
		val[0] = 7;
		val[1] = 8;
		w2.setCell(new Vector2i(1, 1), val);
		val[0] = 9;
		val[1] = 10;
		w2.setCell(new Vector2i(0, 2), val);
		val[0] = 11;
		val[1] = 12;
		w2.setCell(new Vector2i(1, 2), val);
		saver.addGeneration(w2);
		
		HexagonGrid w3 = new HexagonGrid(new Vector2i(2, 3));
		val[0] = 2;
		val[1] = 3;
		w3.setCell(new Vector2i(0, 0), val);
		val[0] = 4;
		val[1] = 5;
		w3.setCell(new Vector2i(1, 0), val);
		val[0] = 6;
		val[1] = 7;
		w3.setCell(new Vector2i(0, 1), val);
		val[0] = 8;
		val[1] = 9;
		w3.setCell(new Vector2i(1, 1), val);
		val[0] = 10;
		val[1] = 11;
		w3.setCell(new Vector2i(0, 2), val);
		val[0] = 12;
		val[1] = 13;
		w3.setCell(new Vector2i(1, 2), val);
		saver.addGeneration(w3);
		
		saver.setRulesAndColours("No rules set.\r\nNo colours set.");
		
		saver.saveWorld();
		World world = reader.readWorld();
		
		assertEquals("No rules set.\r\nNo colours set.", world.getRulesAndColours());

		//Generation 1
		assertEquals('H', world.getWorld()[0].getType());
		assertEquals(2, world.getWorld()[0].getWidth());
		assertEquals(3, world.getWorld()[0].getHeight());
		assertEquals(0, world.getWorld()[0].getCell(0, 0).getValue(0));
		assertEquals(1, world.getWorld()[0].getCell(0, 0).getValue(1));
		assertEquals(2, world.getWorld()[0].getCell(1, 0).getValue(0));
		assertEquals(3, world.getWorld()[0].getCell(1, 0).getValue(1));
		assertEquals(4, world.getWorld()[0].getCell(0, 1).getValue(0));
		assertEquals(5, world.getWorld()[0].getCell(0, 1).getValue(1));
		assertEquals(6, world.getWorld()[0].getCell(1, 1).getValue(0));
		assertEquals(7, world.getWorld()[0].getCell(1, 1).getValue(1));
		assertEquals(8, world.getWorld()[0].getCell(0, 2).getValue(0));
		assertEquals(9, world.getWorld()[0].getCell(0, 2).getValue(1));
		assertEquals(10, world.getWorld()[0].getCell(1, 2).getValue(0));
		assertEquals(11, world.getWorld()[0].getCell(1, 2).getValue(1));
		
		//Generation 2
		assertEquals('H', world.getWorld()[1].getType());
		assertEquals(2, world.getWorld()[1].getWidth());
		assertEquals(3, world.getWorld()[1].getHeight());
		assertEquals(1, world.getWorld()[1].getCell(0, 0).getValue(0));
		assertEquals(2, world.getWorld()[1].getCell(0, 0).getValue(1));
		assertEquals(3, world.getWorld()[1].getCell(1, 0).getValue(0));
		assertEquals(4, world.getWorld()[1].getCell(1, 0).getValue(1));
		assertEquals(5, world.getWorld()[1].getCell(0, 1).getValue(0));
		assertEquals(6, world.getWorld()[1].getCell(0, 1).getValue(1));
		assertEquals(7, world.getWorld()[1].getCell(1, 1).getValue(0));
		assertEquals(8, world.getWorld()[1].getCell(1, 1).getValue(1));
		assertEquals(9, world.getWorld()[1].getCell(0, 2).getValue(0));
		assertEquals(10, world.getWorld()[1].getCell(0, 2).getValue(1));
		assertEquals(11, world.getWorld()[1].getCell(1, 2).getValue(0));
		assertEquals(12, world.getWorld()[1].getCell(1, 2).getValue(1));
		
		//Generation 3
		assertEquals('H', world.getWorld()[2].getType());
		assertEquals(2, world.getWorld()[2].getWidth());
		assertEquals(3, world.getWorld()[2].getHeight());
		assertEquals(2, world.getWorld()[2].getCell(0, 0).getValue(0));
		assertEquals(3, world.getWorld()[2].getCell(0, 0).getValue(1));
		assertEquals(4, world.getWorld()[2].getCell(1, 0).getValue(0));
		assertEquals(5, world.getWorld()[2].getCell(1, 0).getValue(1));
		assertEquals(6, world.getWorld()[2].getCell(0, 1).getValue(0));
		assertEquals(7, world.getWorld()[2].getCell(0, 1).getValue(1));
		assertEquals(8, world.getWorld()[2].getCell(1, 1).getValue(0));
		assertEquals(9, world.getWorld()[2].getCell(1, 1).getValue(1));
		assertEquals(10, world.getWorld()[2].getCell(0, 2).getValue(0));
		assertEquals(11, world.getWorld()[2].getCell(0, 2).getValue(1));
		assertEquals(12, world.getWorld()[2].getCell(1, 2).getValue(0));
		assertEquals(13, world.getWorld()[2].getCell(1, 2).getValue(1));
	}
}
