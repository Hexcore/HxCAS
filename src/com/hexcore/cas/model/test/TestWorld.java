package com.hexcore.cas.model.test;

import java.io.IOException;

import junit.framework.TestCase;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.HexagonGrid;
import com.hexcore.cas.model.World;


public class TestWorld extends TestCase
{
	private World world = new World();
	
	public void testSetAndGetFileName()
	{
		world.setFileName("Test Data/wold.caw");
		assertTrue(world.getWorldName().equals("wold.caw"));
	}
	
	public void testReadWorld()
		throws IOException
	{
		world.setFileName("Test Data/world.caw");
		world.load();

		String RAC = world.getRulesAndColours();
		Grid[] grids = world.getWorld();
		
		assertTrue(RAC.equals("I AM RULES!"));
		
		//Generation 1
		assertEquals('T', grids[0].getType());
		assertEquals(2, grids[0].getWidth());
		assertEquals(3, grids[0].getHeight());
		assertEquals(0.0, grids[0].getCell(0, 0).getValue(0));
		assertEquals(1.0, grids[0].getCell(0, 0).getValue(1));
		assertEquals(2.0, grids[0].getCell(1, 0).getValue(0));
		assertEquals(3.0, grids[0].getCell(1, 0).getValue(1));
		assertEquals(4.0, grids[0].getCell(0, 1).getValue(0));
		assertEquals(5.0, grids[0].getCell(0, 1).getValue(1));
		assertEquals(6.0, grids[0].getCell(1, 1).getValue(0));
		assertEquals(7.0, grids[0].getCell(1, 1).getValue(1));
		assertEquals(8.0, grids[0].getCell(0, 2).getValue(0));
		assertEquals(9.0, grids[0].getCell(0, 2).getValue(1));
		assertEquals(10.0, grids[0].getCell(1, 2).getValue(0));
		assertEquals(11.0, grids[0].getCell(1, 2).getValue(1));
		
		//Generation 2
		assertEquals('T', grids[1].getType());
		assertEquals(2, grids[1].getWidth());
		assertEquals(3, grids[1].getHeight());
		assertEquals(1.0, grids[1].getCell(0, 0).getValue(0));
		assertEquals(2.0, grids[1].getCell(0, 0).getValue(1));
		assertEquals(3.0, grids[1].getCell(1, 0).getValue(0));
		assertEquals(4.0, grids[1].getCell(1, 0).getValue(1));
		assertEquals(5.0, grids[1].getCell(0, 1).getValue(0));
		assertEquals(6.0, grids[1].getCell(0, 1).getValue(1));
		assertEquals(7.0, grids[1].getCell(1, 1).getValue(0));
		assertEquals(8.0, grids[1].getCell(1, 1).getValue(1));
		assertEquals(9.0, grids[1].getCell(0, 2).getValue(0));
		assertEquals(10.0, grids[1].getCell(0, 2).getValue(1));
		assertEquals(11.0, grids[1].getCell(1, 2).getValue(0));
		assertEquals(12.0, grids[1].getCell(1, 2).getValue(1));
		
		//Generation 3
		assertEquals('T', grids[2].getType());
		assertEquals(2, grids[2].getWidth());
		assertEquals(3, grids[2].getHeight());
		assertEquals(2.0, grids[2].getCell(0, 0).getValue(0));
		assertEquals(3.0, grids[2].getCell(0, 0).getValue(1));
		assertEquals(4.0, grids[2].getCell(1, 0).getValue(0));
		assertEquals(5.0, grids[2].getCell(1, 0).getValue(1));
		assertEquals(6.0, grids[2].getCell(0, 1).getValue(0));
		assertEquals(7.0, grids[2].getCell(0, 1).getValue(1));
		assertEquals(8.0, grids[2].getCell(1, 1).getValue(0));
		assertEquals(9.0, grids[2].getCell(1, 1).getValue(1));
		assertEquals(10.0, grids[2].getCell(0, 2).getValue(0));
		assertEquals(11.0, grids[2].getCell(0, 2).getValue(1));
		assertEquals(12.0, grids[2].getCell(1, 2).getValue(0));
		assertEquals(13.0, grids[2].getCell(1, 2).getValue(1));
	}
	
	public void testSaveWorld()
		throws IOException
	{
		world.setFileName("Test Data/savedWorld.caw");
		HexagonGrid[] worlds = new HexagonGrid[3];
		
		worlds[0] = new HexagonGrid(new Vector2i(2, 3));
		
		double[] val = new double[2];
		val[0] = 0.0;
		val[1] = 1.0;
		worlds[0].setCell(new Vector2i(0, 0), val);
		val[0] = 2.0;
		val[1] = 3.0;
		worlds[0].setCell(new Vector2i(1, 0), val);
		val[0] = 4.0;
		val[1] = 5.0;
		worlds[0].setCell(new Vector2i(0, 1), val);
		val[0] = 6.0;
		val[1] = 7.0;
		worlds[0].setCell(new Vector2i(1, 1), val);
		val[0] = 8.0;
		val[1] = 9.0;
		worlds[0].setCell(new Vector2i(0, 2), val);
		val[0] = 10.0;
		val[1] = 11.0;
		worlds[0].setCell(new Vector2i(1, 2), val);
		
		worlds[1] = new HexagonGrid(new Vector2i(2, 3));
		val[0] = 1.0;
		val[1] = 2.0;
		worlds[1].setCell(new Vector2i(0, 0), val);
		val[0] = 3.0;
		val[1] = 4.0;
		worlds[1].setCell(new Vector2i(1, 0), val);
		val[0] = 5.0;
		val[1] = 6.0;
		worlds[1].setCell(new Vector2i(0, 1), val);
		val[0] = 7.0;
		val[1] = 8.0;
		worlds[1].setCell(new Vector2i(1, 1), val);
		val[0] = 9.0;
		val[1] = 10.0;
		worlds[1].setCell(new Vector2i(0, 2), val);
		val[0] = 11.0;
		val[1] = 12.0;
		worlds[1].setCell(new Vector2i(1, 2), val);

		worlds[2] = new HexagonGrid(new Vector2i(2, 3));
		val[0] = 2.0;
		val[1] = 3.0;
		worlds[2].setCell(new Vector2i(0, 0), val);
		val[0] = 4.0;
		val[1] = 5.0;
		worlds[2].setCell(new Vector2i(1, 0), val);
		val[0] = 6.0;
		val[1] = 7.0;
		worlds[2].setCell(new Vector2i(0, 1), val);
		val[0] = 8.0;
		val[1] = 9.0;
		worlds[2].setCell(new Vector2i(1, 1), val);
		val[0] = 10.0;
		val[1] = 11.0;
		worlds[2].setCell(new Vector2i(0, 2), val);
		val[0] = 12.0;
		val[1] = 13.0;
		worlds[2].setCell(new Vector2i(1, 2), val);

		for(int i = 0; i < 3; i++)
			world.addGeneration(worlds[i]);
		
		world.setRulesAndColours("No rules set.\r\nNo colours set.");

		world.save();

		world.load();

		String RAC = world.getRulesAndColours();
		Grid[] grids = world.getWorld();

		assertTrue(RAC.equals("No rules set.\r\nNo colours set."));

		//Generation 1
		assertEquals('H', grids[0].getType());
		assertEquals(2, grids[0].getWidth());
		assertEquals(3, grids[0].getHeight());
		assertEquals(0.0, grids[0].getCell(0, 0).getValue(0));
		assertEquals(1.0, grids[0].getCell(0, 0).getValue(1));
		assertEquals(2.0, grids[0].getCell(1, 0).getValue(0));
		assertEquals(3.0, grids[0].getCell(1, 0).getValue(1));
		assertEquals(4.0, grids[0].getCell(0, 1).getValue(0));
		assertEquals(5.0, grids[0].getCell(0, 1).getValue(1));
		assertEquals(6.0, grids[0].getCell(1, 1).getValue(0));
		assertEquals(7.0, grids[0].getCell(1, 1).getValue(1));
		assertEquals(8.0, grids[0].getCell(0, 2).getValue(0));
		assertEquals(9.0, grids[0].getCell(0, 2).getValue(1));
		assertEquals(10.0, grids[0].getCell(1, 2).getValue(0));
		assertEquals(11.0, grids[0].getCell(1, 2).getValue(1));

		//Generation 2
		assertEquals('H', grids[1].getType());
		assertEquals(2, grids[1].getWidth());
		assertEquals(3, grids[1].getHeight());
		assertEquals(1.0, grids[1].getCell(0, 0).getValue(0));
		assertEquals(2.0, grids[1].getCell(0, 0).getValue(1));
		assertEquals(3.0, grids[1].getCell(1, 0).getValue(0));
		assertEquals(4.0, grids[1].getCell(1, 0).getValue(1));
		assertEquals(5.0, grids[1].getCell(0, 1).getValue(0));
		assertEquals(6.0, grids[1].getCell(0, 1).getValue(1));
		assertEquals(7.0, grids[1].getCell(1, 1).getValue(0));
		assertEquals(8.0, grids[1].getCell(1, 1).getValue(1));
		assertEquals(9.0, grids[1].getCell(0, 2).getValue(0));
		assertEquals(10.0, grids[1].getCell(0, 2).getValue(1));
		assertEquals(11.0, grids[1].getCell(1, 2).getValue(0));
		assertEquals(12.0, grids[1].getCell(1, 2).getValue(1));

		//Generation 3
		assertEquals('H', grids[2].getType());
		assertEquals(2, grids[2].getWidth());
		assertEquals(3, grids[2].getHeight());
		assertEquals(2.0, grids[2].getCell(0, 0).getValue(0));
		assertEquals(3.0, grids[2].getCell(0, 0).getValue(1));
		assertEquals(4.0, grids[2].getCell(1, 0).getValue(0));
		assertEquals(5.0, grids[2].getCell(1, 0).getValue(1));
		assertEquals(6.0, grids[2].getCell(0, 1).getValue(0));
		assertEquals(7.0, grids[2].getCell(0, 1).getValue(1));
		assertEquals(8.0, grids[2].getCell(1, 1).getValue(0));
		assertEquals(9.0, grids[2].getCell(1, 1).getValue(1));
		assertEquals(10.0, grids[2].getCell(0, 2).getValue(0));
		assertEquals(11.0, grids[2].getCell(0, 2).getValue(1));
		assertEquals(12.0, grids[2].getCell(1, 2).getValue(0));
		assertEquals(13.0, grids[2].getCell(1, 2).getValue(1));
	}
}
