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
		System.out.println("Starting test6SaveWorld...");
		HexagonGrid w1 = new HexagonGrid(new Vector2i(2, 3));
		double[] val = new double[2];
		val[0] = 0.0;
		val[1] = 1.0;
		w1.setCell(new Vector2i(0, 0), val);
		val[0] = 2.0;
		val[1] = 3.0;
		w1.setCell(new Vector2i(1, 0), val);
		val[0] = 4.0;
		val[1] = 5.0;
		w1.setCell(new Vector2i(0, 1), val);
		val[0] = 6.0;
		val[1] = 7.0;
		w1.setCell(new Vector2i(1, 1), val);
		val[0] = 8.0;
		val[1] = 9.0;
		w1.setCell(new Vector2i(0, 2), val);
		val[0] = 10.0;
		val[1] = 11.0;
		w1.setCell(new Vector2i(1, 2), val);
		saver.addGeneration(w1);
		System.out.println("Added generation 1...");
		HexagonGrid w2 = new HexagonGrid(new Vector2i(2, 3));
		val[0] = 1.0;
		val[1] = 2.0;
		w2.setCell(new Vector2i(0, 0), val);
		val[0] = 3.0;
		val[1] = 4.0;
		w2.setCell(new Vector2i(1, 0), val);
		val[0] = 5.0;
		val[1] = 6.0;
		w2.setCell(new Vector2i(0, 1), val);
		val[0] = 7.0;
		val[1] = 8.0;
		w2.setCell(new Vector2i(1, 1), val);
		val[0] = 9.0;
		val[1] = 10.0;
		w2.setCell(new Vector2i(0, 2), val);
		val[0] = 11.0;
		val[1] = 12.0;
		w2.setCell(new Vector2i(1, 2), val);
		saver.addGeneration(w2);
		System.out.println("Added generation 2...");
		HexagonGrid w3 = new HexagonGrid(new Vector2i(2, 3));
		val[0] = 2.0;
		val[1] = 3.0;
		w3.setCell(new Vector2i(0, 0), val);
		val[0] = 4.0;
		val[1] = 5.0;
		w3.setCell(new Vector2i(1, 0), val);
		val[0] = 6.0;
		val[1] = 7.0;
		w3.setCell(new Vector2i(0, 1), val);
		val[0] = 8.0;
		val[1] = 9.0;
		w3.setCell(new Vector2i(1, 1), val);
		val[0] = 10.0;
		val[1] = 11.0;
		w3.setCell(new Vector2i(0, 2), val);
		val[0] = 12.0;
		val[1] = 13.0;
		w3.setCell(new Vector2i(1, 2), val);
		saver.addGeneration(w3);
		System.out.println("Added generation 3...");
		saver.setRulesAndColours("No rules set.\r\nNo colours set.");
		System.out.println("This test works!");
		saver.saveWorld();
		System.out.println("This test works!");
		World world = reader.readWorld();
		System.out.println("This test works!");
		assertTrue(world.getRulesAndColours().equals("No rules set.\r\nNo colours set."));

		System.out.println("This test works!");
		//Generation 1
		assertEquals('H', world.getWorld()[0].getType());
		System.out.println("This test works!");
		assertEquals(2, world.getWorld()[0].getWidth());
		assertEquals(3, world.getWorld()[0].getHeight());
		assertEquals(0.0, world.getWorld()[0].getCell(0, 0).getValue(0));
		assertEquals(1.0, world.getWorld()[0].getCell(0, 0).getValue(1));
		assertEquals(2.0, world.getWorld()[0].getCell(1, 0).getValue(0));
		assertEquals(3.0, world.getWorld()[0].getCell(1, 0).getValue(1));
		assertEquals(4.0, world.getWorld()[0].getCell(0, 1).getValue(0));
		assertEquals(5.0, world.getWorld()[0].getCell(0, 1).getValue(1));
		assertEquals(6.0, world.getWorld()[0].getCell(1, 1).getValue(0));
		assertEquals(7.0, world.getWorld()[0].getCell(1, 1).getValue(1));
		assertEquals(8.0, world.getWorld()[0].getCell(0, 2).getValue(0));
		assertEquals(9.0, world.getWorld()[0].getCell(0, 2).getValue(1));
		assertEquals(10.0, world.getWorld()[0].getCell(1, 2).getValue(0));
		assertEquals(11.0, world.getWorld()[0].getCell(1, 2).getValue(1));
		System.out.println("Tested generation 1...");
		//Generation 2
		assertEquals('H', world.getWorld()[1].getType());
		assertEquals(2, world.getWorld()[1].getWidth());
		assertEquals(3, world.getWorld()[1].getHeight());
		assertEquals(1.0, world.getWorld()[1].getCell(0, 0).getValue(0));
		assertEquals(2.0, world.getWorld()[1].getCell(0, 0).getValue(1));
		assertEquals(3.0, world.getWorld()[1].getCell(1, 0).getValue(0));
		assertEquals(4.0, world.getWorld()[1].getCell(1, 0).getValue(1));
		assertEquals(5.0, world.getWorld()[1].getCell(0, 1).getValue(0));
		assertEquals(6.0, world.getWorld()[1].getCell(0, 1).getValue(1));
		assertEquals(7.0, world.getWorld()[1].getCell(1, 1).getValue(0));
		assertEquals(8.0, world.getWorld()[1].getCell(1, 1).getValue(1));
		assertEquals(9.0, world.getWorld()[1].getCell(0, 2).getValue(0));
		assertEquals(10.0, world.getWorld()[1].getCell(0, 2).getValue(1));
		assertEquals(11.0, world.getWorld()[1].getCell(1, 2).getValue(0));
		assertEquals(12.0, world.getWorld()[1].getCell(1, 2).getValue(1));
		System.out.println("Tested generation 2...");
		//Generation 3
		assertEquals('H', world.getWorld()[2].getType());
		assertEquals(2, world.getWorld()[2].getWidth());
		assertEquals(3, world.getWorld()[2].getHeight());
		assertEquals(2.0, world.getWorld()[2].getCell(0, 0).getValue(0));
		assertEquals(3.0, world.getWorld()[2].getCell(0, 0).getValue(1));
		assertEquals(4.0, world.getWorld()[2].getCell(1, 0).getValue(0));
		assertEquals(5.0, world.getWorld()[2].getCell(1, 0).getValue(1));
		assertEquals(6.0, world.getWorld()[2].getCell(0, 1).getValue(0));
		assertEquals(7.0, world.getWorld()[2].getCell(0, 1).getValue(1));
		assertEquals(8.0, world.getWorld()[2].getCell(1, 1).getValue(0));
		assertEquals(9.0, world.getWorld()[2].getCell(1, 1).getValue(1));
		assertEquals(10.0, world.getWorld()[2].getCell(0, 2).getValue(0));
		assertEquals(11.0, world.getWorld()[2].getCell(0, 2).getValue(1));
		assertEquals(12.0, world.getWorld()[2].getCell(1, 2).getValue(0));
		assertEquals(13.0, world.getWorld()[2].getCell(1, 2).getValue(1));
		System.out.println("Tested generation 3...");
	}
}
