package com.hexcore.cas.model.test;

import java.io.IOException;

import junit.framework.TestCase;

import com.hexcore.cas.model.World;
import com.hexcore.cas.model.WorldReader;

public class TestWorldReader extends TestCase
{
	private WorldReader reader = new WorldReader("Test Data/wold.caw");
	
	public void test1GetName()
	{
		assertEquals("wold.caw", reader.getWorldName());
	}
	
	public void test2SetName()
	{
		reader.setWorldName("Test Data/world.caw");
		assertEquals("world.caw", reader.getWorldName());
	}
	
	public void test3ReadWorld()
		throws IOException
	{
		reader.setWorldName("Test Data/world.caw");
		World world = reader.readWorld();

		assertEquals("I AM RULES!", world.getRulesAndColours());
		
		//Generation 1
		assertEquals('T', world.getWorld()[0].getType());
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
		assertEquals('T', world.getWorld()[1].getType());
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
		assertEquals('T', world.getWorld()[2].getType());
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
