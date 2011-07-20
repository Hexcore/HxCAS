package com.hexcore.cas.model.test;

import java.io.IOException;

import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.WorldReader;
import junit.framework.TestCase;

public class TestWorldReader extends TestCase
{
	private WorldReader reader = new WorldReader("Test Data/wold.cawzip");
	
	public void test1GetName()
	{
		assertEquals("wold.cawzip", reader.getWorldName());
	}
	
	public void test2SetName()
	{
		reader.setWorldName("Test Data/world.cawzip");
		assertEquals("world.cawzip", reader.getWorldName());
	}
	
	public void test3ReadWorld()
		throws IOException
	{
		reader.setWorldName("Test Data/world.cawzip");
		Grid[] world = reader.readWorld();

		//Generation 1
		assertEquals('T', world[0].getType());
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
		assertEquals('T', world[1].getType());
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
		assertEquals('T', world[2].getType());
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
