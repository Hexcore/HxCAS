package com.hexcore.test;

import java.io.FileNotFoundException;

import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.WorldReader;
import junit.framework.TestCase;

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
		throws FileNotFoundException
	{
		reader.setWorldName("Test Data/world.caw");
		Grid world = reader.readWorld();
		assertEquals('T', world.getType());
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
