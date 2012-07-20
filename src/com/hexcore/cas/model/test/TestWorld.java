package com.hexcore.cas.model.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
		world.setFileName("Test Data/world/world.caw");
		assertTrue(world.getFilename().equals("Test Data/world/world.caw"));
		assertTrue(world.getWorldName().equals("world.caw"));
	}
	
	public void testReadWorld()
		throws IOException
	{
		world.setFileName("Test Data/world/world.caw");
		world.load();
		
		String ruleCode = world.getRuleCode();
		assertNotNull(ruleCode);
		assertTrue(ruleCode.startsWith("rules"));
		
		String colourCode = world.getColourCode();
		assertNotNull(colourCode);
		assertTrue(colourCode.startsWith("colours"));
		
		List<Grid> grids = world.getGenerations();
		Grid grid;
		
		//Generation 1
		grid = grids.get(0);
		assertEquals('H', grid.getTypeSymbol());
		assertEquals(2, grid.getWidth());
		assertEquals(3, grid.getHeight());
		assertEquals(0.0, grid.getCell(0, 0).getValue(0));
		assertEquals(1.0, grid.getCell(0, 0).getValue(1));
		assertEquals(2.0, grid.getCell(1, 0).getValue(0));
		assertEquals(3.0, grid.getCell(1, 0).getValue(1));
		assertEquals(4.0, grid.getCell(0, 1).getValue(0));
		assertEquals(5.0, grid.getCell(0, 1).getValue(1));
		assertEquals(6.0, grid.getCell(1, 1).getValue(0));
		assertEquals(7.0, grid.getCell(1, 1).getValue(1));
		assertEquals(8.0, grid.getCell(0, 2).getValue(0));
		assertEquals(9.0, grid.getCell(0, 2).getValue(1));
		assertEquals(10.0, grid.getCell(1, 2).getValue(0));
		assertEquals(11.0, grid.getCell(1, 2).getValue(1));
		
		//Generation 2
		grid = grids.get(1);
		assertEquals('H', grid.getTypeSymbol());
		assertEquals(2, grid.getWidth());
		assertEquals(3, grid.getHeight());
		assertEquals(1.0, grid.getCell(0, 0).getValue(0));
		assertEquals(2.0, grid.getCell(0, 0).getValue(1));
		assertEquals(3.0, grid.getCell(1, 0).getValue(0));
		assertEquals(4.0, grid.getCell(1, 0).getValue(1));
		assertEquals(5.0, grid.getCell(0, 1).getValue(0));
		assertEquals(6.0, grid.getCell(0, 1).getValue(1));
		assertEquals(7.0, grid.getCell(1, 1).getValue(0));
		assertEquals(8.0, grid.getCell(1, 1).getValue(1));
		assertEquals(9.0, grid.getCell(0, 2).getValue(0));
		assertEquals(10.0, grid.getCell(0, 2).getValue(1));
		assertEquals(11.0, grid.getCell(1, 2).getValue(0));
		assertEquals(12.0, grid.getCell(1, 2).getValue(1));
		
		//Generation 3
		grid = grids.get(2);
		assertEquals('H', grid.getTypeSymbol());
		assertEquals(2, grid.getWidth());
		assertEquals(3, grid.getHeight());
		assertEquals(2.0, grid.getCell(0, 0).getValue(0));
		assertEquals(3.0, grid.getCell(0, 0).getValue(1));
		assertEquals(4.0, grid.getCell(1, 0).getValue(0));
		assertEquals(5.0, grid.getCell(1, 0).getValue(1));
		assertEquals(6.0, grid.getCell(0, 1).getValue(0));
		assertEquals(7.0, grid.getCell(0, 1).getValue(1));
		assertEquals(8.0, grid.getCell(1, 1).getValue(0));
		assertEquals(9.0, grid.getCell(1, 1).getValue(1));
		assertEquals(10.0, grid.getCell(0, 2).getValue(0));
		assertEquals(11.0, grid.getCell(0, 2).getValue(1));
		assertEquals(12.0, grid.getCell(1, 2).getValue(0));
		assertEquals(13.0, grid.getCell(1, 2).getValue(1));
	}
	
	public void testSaveWorld()
		throws IOException
	{
		world.setFileName("Test Data/world/savedWorld.caw");
		HexagonGrid[] worlds = new HexagonGrid[3];
		
		worlds[0] = new HexagonGrid(new Vector2i(2, 3), 2);
		worlds[0].setCell(0, 0, new double[] {0.0, 1.0});
		worlds[0].setCell(1, 0, new double[] {2.0, 3.0});
		worlds[0].setCell(0, 1, new double[] {4.0, 5.0});
		worlds[0].setCell(1, 1, new double[] {6.0, 7.0});
		worlds[0].setCell(0, 2, new double[] {8.0, 9.0});
		worlds[0].setCell(1, 2, new double[] {10.0, 11.0});
		
		worlds[1] = new HexagonGrid(new Vector2i(2, 3), 2);
		worlds[1].setCell(0, 0, new double[] {1.0, 2.0});
		worlds[1].setCell(1, 0, new double[] {3.0, 4.0});
		worlds[1].setCell(0, 1, new double[] {5.0, 6.0});
		worlds[1].setCell(1, 1, new double[] {7.0, 8.0});
		worlds[1].setCell(0, 2, new double[] {9.0, 10.0});
		worlds[1].setCell(1, 2, new double[] {11.0, 12.0});
		
		worlds[2] = new HexagonGrid(new Vector2i(2, 3), 2);
		worlds[2].setCell(0, 0, new double[] {2.0, 3.0});
		worlds[2].setCell(1, 0, new double[] {4.0, 5.0});
		worlds[2].setCell(0, 1, new double[] {6.0, 7.0});
		worlds[2].setCell(1, 1, new double[] {8.0, 9.0});
		worlds[2].setCell(0, 2, new double[] {10.0, 11.0});
		worlds[2].setCell(1, 2, new double[] {12.0, 13.0});
		
		for(int i = 0; i < 3; i++)
			world.addGeneration(worlds[i]);
		
		world.setRuleCode("rules");
		world.setColourCode("colours");
		
		world.save();
		
		assertTrue(world.load());
		
		String ruleCode = world.getRuleCode();
		assertNotNull(ruleCode);
		assertTrue(ruleCode.startsWith("rules"));
		
		String colourCode = world.getColourCode();
		assertNotNull(colourCode);
		assertTrue(colourCode.startsWith("colours"));
		
		List<Grid> grids = world.getGenerations();
		Grid grid;
		
		//Generation 1
		grid = grids.get(0);
		assertEquals('H', grid.getTypeSymbol());
		assertEquals(2, grid.getWidth());
		assertEquals(3, grid.getHeight());
		assertEquals(0.0, grid.getCell(0, 0).getValue(0));
		assertEquals(1.0, grid.getCell(0, 0).getValue(1));
		assertEquals(2.0, grid.getCell(1, 0).getValue(0));
		assertEquals(3.0, grid.getCell(1, 0).getValue(1));
		assertEquals(4.0, grid.getCell(0, 1).getValue(0));
		assertEquals(5.0, grid.getCell(0, 1).getValue(1));
		assertEquals(6.0, grid.getCell(1, 1).getValue(0));
		assertEquals(7.0, grid.getCell(1, 1).getValue(1));
		assertEquals(8.0, grid.getCell(0, 2).getValue(0));
		assertEquals(9.0, grid.getCell(0, 2).getValue(1));
		assertEquals(10.0, grid.getCell(1, 2).getValue(0));
		assertEquals(11.0, grid.getCell(1, 2).getValue(1));
		
		//Generation 2
		grid = grids.get(1);
		assertEquals('H', grid.getTypeSymbol());
		assertEquals(2, grid.getWidth());
		assertEquals(3, grid.getHeight());
		assertEquals(1.0, grid.getCell(0, 0).getValue(0));
		assertEquals(2.0, grid.getCell(0, 0).getValue(1));
		assertEquals(3.0, grid.getCell(1, 0).getValue(0));
		assertEquals(4.0, grid.getCell(1, 0).getValue(1));
		assertEquals(5.0, grid.getCell(0, 1).getValue(0));
		assertEquals(6.0, grid.getCell(0, 1).getValue(1));
		assertEquals(7.0, grid.getCell(1, 1).getValue(0));
		assertEquals(8.0, grid.getCell(1, 1).getValue(1));
		assertEquals(9.0, grid.getCell(0, 2).getValue(0));
		assertEquals(10.0, grid.getCell(0, 2).getValue(1));
		assertEquals(11.0, grid.getCell(1, 2).getValue(0));
		assertEquals(12.0, grid.getCell(1, 2).getValue(1));
		
		//Generation 3
		grid = grids.get(2);
		assertEquals('H', grid.getTypeSymbol());
		assertEquals(2, grid.getWidth());
		assertEquals(3, grid.getHeight());
		assertEquals(2.0, grid.getCell(0, 0).getValue(0));
		assertEquals(3.0, grid.getCell(0, 0).getValue(1));
		assertEquals(4.0, grid.getCell(1, 0).getValue(0));
		assertEquals(5.0, grid.getCell(1, 0).getValue(1));
		assertEquals(6.0, grid.getCell(0, 1).getValue(0));
		assertEquals(7.0, grid.getCell(0, 1).getValue(1));
		assertEquals(8.0, grid.getCell(1, 1).getValue(0));
		assertEquals(9.0, grid.getCell(1, 1).getValue(1));
		assertEquals(10.0, grid.getCell(0, 2).getValue(0));
		assertEquals(11.0, grid.getCell(0, 2).getValue(1));
		assertEquals(12.0, grid.getCell(1, 2).getValue(0));
		assertEquals(13.0, grid.getCell(1, 2).getValue(1));
		
		File file = new File("Test Data/world/savedWorld.caw");
		file.delete();
	}
}
