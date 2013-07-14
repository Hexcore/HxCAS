package com.hexcore.cas.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipException;

import org.junit.After;
import org.junit.Test;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.HexagonGrid;
import com.hexcore.cas.model.TriangleGrid;
import com.hexcore.cas.model.World;


public class TestWorld
{
	private World world = new World();
	
	@Test
	public void testSetAndGetFileName()
	{
		world.setFileName("Test Data/world/world.caw");
		assertTrue(world.getFilename().equals("Test Data/world/world.caw"));
		assertTrue(world.getWorldName().equals("world.caw"));
	}
	
	@Test
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
		assertEquals('T', grid.getTypeSymbol());
		assertEquals(2, grid.getWidth());
		assertEquals(3, grid.getHeight());
		assertEquals(0.0, grid.getCell(0, 0).getValue(0), 0.0);
		assertEquals(1.0, grid.getCell(0, 0).getValue(1), 0.0);
		assertEquals(2.0, grid.getCell(1, 0).getValue(0), 0.0);
		assertEquals(3.0, grid.getCell(1, 0).getValue(1), 0.0);
		assertEquals(4.0, grid.getCell(0, 1).getValue(0), 0.0);
		assertEquals(5.0, grid.getCell(0, 1).getValue(1), 0.0);
		assertEquals(6.0, grid.getCell(1, 1).getValue(0), 0.0);
		assertEquals(7.0, grid.getCell(1, 1).getValue(1), 0.0);
		assertEquals(8.0, grid.getCell(0, 2).getValue(0), 0.0);
		assertEquals(9.0, grid.getCell(0, 2).getValue(1), 0.0);
		assertEquals(10.0, grid.getCell(1, 2).getValue(0), 0.0);
		assertEquals(11.0, grid.getCell(1, 2).getValue(1), 0.0);
		
		//Generation 2
		grid = grids.get(1);
		assertEquals('T', grid.getTypeSymbol());
		assertEquals(2, grid.getWidth());
		assertEquals(3, grid.getHeight());
		assertEquals(1.0, grid.getCell(0, 0).getValue(0), 0.0);
		assertEquals(2.0, grid.getCell(0, 0).getValue(1), 0.0);
		assertEquals(3.0, grid.getCell(1, 0).getValue(0), 0.0);
		assertEquals(4.0, grid.getCell(1, 0).getValue(1), 0.0);
		assertEquals(5.0, grid.getCell(0, 1).getValue(0), 0.0);
		assertEquals(6.0, grid.getCell(0, 1).getValue(1), 0.0);
		assertEquals(7.0, grid.getCell(1, 1).getValue(0), 0.0);
		assertEquals(8.0, grid.getCell(1, 1).getValue(1), 0.0);
		assertEquals(9.0, grid.getCell(0, 2).getValue(0), 0.0);
		assertEquals(10.0, grid.getCell(0, 2).getValue(1), 0.0);
		assertEquals(11.0, grid.getCell(1, 2).getValue(0), 0.0);
		assertEquals(12.0, grid.getCell(1, 2).getValue(1), 0.0);
		
		//Generation 3
		grid = grids.get(2);
		assertEquals('T', grid.getTypeSymbol());
		assertEquals(2, grid.getWidth());
		assertEquals(3, grid.getHeight());
		assertEquals(2.0, grid.getCell(0, 0).getValue(0), 0.0);
		assertEquals(3.0, grid.getCell(0, 0).getValue(1), 0.0);
		assertEquals(4.0, grid.getCell(1, 0).getValue(0), 0.0);
		assertEquals(5.0, grid.getCell(1, 0).getValue(1), 0.0);
		assertEquals(6.0, grid.getCell(0, 1).getValue(0), 0.0);
		assertEquals(7.0, grid.getCell(0, 1).getValue(1), 0.0);
		assertEquals(8.0, grid.getCell(1, 1).getValue(0), 0.0);
		assertEquals(9.0, grid.getCell(1, 1).getValue(1), 0.0);
		assertEquals(10.0, grid.getCell(0, 2).getValue(0), 0.0);
		assertEquals(11.0, grid.getCell(0, 2).getValue(1), 0.0);
		assertEquals(12.0, grid.getCell(1, 2).getValue(0), 0.0);
		assertEquals(13.0, grid.getCell(1, 2).getValue(1), 0.0);
	}
	
	@Test
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
		
		assertEquals(0.0, grid.getCell(0, 0).getValue(0), 0.0);
		assertEquals(1.0, grid.getCell(0, 0).getValue(1), 0.0);
		assertEquals(2.0, grid.getCell(1, 0).getValue(0), 0.0);
		assertEquals(3.0, grid.getCell(1, 0).getValue(1), 0.0);
		assertEquals(4.0, grid.getCell(0, 1).getValue(0), 0.0);
		assertEquals(5.0, grid.getCell(0, 1).getValue(1), 0.0);
		assertEquals(6.0, grid.getCell(1, 1).getValue(0), 0.0);
		assertEquals(7.0, grid.getCell(1, 1).getValue(1), 0.0);
		assertEquals(8.0, grid.getCell(0, 2).getValue(0), 0.0);
		assertEquals(9.0, grid.getCell(0, 2).getValue(1), 0.0);
		assertEquals(10.0, grid.getCell(1, 2).getValue(0), 0.0);
		assertEquals(11.0, grid.getCell(1, 2).getValue(1), 0.0);
		
		//Generation 2
		grid = grids.get(1);
		assertEquals('H', grid.getTypeSymbol());
		assertEquals(2, grid.getWidth());
		assertEquals(3, grid.getHeight());

		assertEquals(1.0, grid.getCell(0, 0).getValue(0), 0.0);
		assertEquals(2.0, grid.getCell(0, 0).getValue(1), 0.0);
		assertEquals(3.0, grid.getCell(1, 0).getValue(0), 0.0);
		assertEquals(4.0, grid.getCell(1, 0).getValue(1), 0.0);
		assertEquals(5.0, grid.getCell(0, 1).getValue(0), 0.0);
		assertEquals(6.0, grid.getCell(0, 1).getValue(1), 0.0);
		assertEquals(7.0, grid.getCell(1, 1).getValue(0), 0.0);
		assertEquals(8.0, grid.getCell(1, 1).getValue(1), 0.0);
		assertEquals(9.0, grid.getCell(0, 2).getValue(0), 0.0);
		assertEquals(10.0, grid.getCell(0, 2).getValue(1), 0.0);
		assertEquals(11.0, grid.getCell(1, 2).getValue(0), 0.0);
		assertEquals(12.0, grid.getCell(1, 2).getValue(1), 0.0);
		
		//Generation 3
		grid = grids.get(2);
		assertEquals('H', grid.getTypeSymbol());
		assertEquals(2, grid.getWidth());
		assertEquals(3, grid.getHeight());
		assertEquals(2.0, grid.getCell(0, 0).getValue(0), 0.0);
		assertEquals(3.0, grid.getCell(0, 0).getValue(1), 0.0);
		assertEquals(4.0, grid.getCell(1, 0).getValue(0), 0.0);
		assertEquals(5.0, grid.getCell(1, 0).getValue(1), 0.0);
		assertEquals(6.0, grid.getCell(0, 1).getValue(0), 0.0);
		assertEquals(7.0, grid.getCell(0, 1).getValue(1), 0.0);
		assertEquals(8.0, grid.getCell(1, 1).getValue(0), 0.0);
		assertEquals(9.0, grid.getCell(1, 1).getValue(1), 0.0);
		assertEquals(10.0, grid.getCell(0, 2).getValue(0), 0.0);
		assertEquals(11.0, grid.getCell(0, 2).getValue(1), 0.0);
		assertEquals(12.0, grid.getCell(1, 2).getValue(0), 0.0);
		assertEquals(13.0, grid.getCell(1, 2).getValue(1), 0.0);
		
		File file = new File("Test Data/world/savedWorld.caw");
		file.delete();
	}
	@Test
	public void testStartAndStop()
	throws ZipException, IOException
	{
		System.out.println("\n== Test : testStartAndStop ==");
		
		world.setFileName("Test Data/world/world.caw");
		world.load();
		
		world.setKeepHistory(2);
		
		world.start();
		
		String ruleCode = world.getRuleCode();

		assertNotNull(ruleCode);
		assertTrue(ruleCode.startsWith("rules"));
		
		String colourCode = world.getColourCode();
		assertNotNull(colourCode);
		assertTrue(colourCode.startsWith("colours"));
		
		List<Grid> grids = world.getGenerations();
		Grid grid;
		
		world.stop();
		
		//Generation 1
		grid = grids.get(0);
		assertEquals('T', grid.getTypeSymbol());
		assertEquals(2, grid.getWidth());
		assertEquals(3, grid.getHeight());
		assertEquals(0.0, grid.getCell(0, 0).getValue(0), 0.0);
		assertEquals(1.0, grid.getCell(0, 0).getValue(1), 0.0);
		assertEquals(2.0, grid.getCell(1, 0).getValue(0), 0.0);
		assertEquals(3.0, grid.getCell(1, 0).getValue(1), 0.0);
		assertEquals(4.0, grid.getCell(0, 1).getValue(0), 0.0);
		assertEquals(5.0, grid.getCell(0, 1).getValue(1), 0.0);
		assertEquals(6.0, grid.getCell(1, 1).getValue(0), 0.0);
		assertEquals(7.0, grid.getCell(1, 1).getValue(1), 0.0);
		assertEquals(8.0, grid.getCell(0, 2).getValue(0), 0.0);
		assertEquals(9.0, grid.getCell(0, 2).getValue(1), 0.0);
		assertEquals(10.0, grid.getCell(1, 2).getValue(0), 0.0);
		assertEquals(11.0, grid.getCell(1, 2).getValue(1), 0.0);
	
		//Generation 2
		grid = grids.get(1);
		assertEquals('T', grid.getTypeSymbol());
		assertEquals(2, grid.getWidth());
		assertEquals(3, grid.getHeight());
		assertEquals(1.0, grid.getCell(0, 0).getValue(0), 0.0);
		assertEquals(2.0, grid.getCell(0, 0).getValue(1), 0.0);
		assertEquals(3.0, grid.getCell(1, 0).getValue(0), 0.0);
		assertEquals(4.0, grid.getCell(1, 0).getValue(1), 0.0);
		assertEquals(5.0, grid.getCell(0, 1).getValue(0), 0.0);
		assertEquals(6.0, grid.getCell(0, 1).getValue(1), 0.0);
		assertEquals(7.0, grid.getCell(1, 1).getValue(0), 0.0);
		assertEquals(8.0, grid.getCell(1, 1).getValue(1), 0.0);
		assertEquals(9.0, grid.getCell(0, 2).getValue(0), 0.0);
		assertEquals(10.0, grid.getCell(0, 2).getValue(1), 0.0);
		assertEquals(11.0, grid.getCell(1, 2).getValue(0), 0.0);
		assertEquals(12.0, grid.getCell(1, 2).getValue(1), 0.0);
	
		//Generation 3
		grid = grids.get(2);
		assertEquals('T', grid.getTypeSymbol());
		assertEquals(2, grid.getWidth());
		assertEquals(3, grid.getHeight());
		assertEquals(2.0, grid.getCell(0, 0).getValue(0), 0.0);
		assertEquals(3.0, grid.getCell(0, 0).getValue(1), 0.0);
		assertEquals(4.0, grid.getCell(1, 0).getValue(0), 0.0);
		assertEquals(5.0, grid.getCell(1, 0).getValue(1), 0.0);
		assertEquals(6.0, grid.getCell(0, 1).getValue(0), 0.0);
		assertEquals(7.0, grid.getCell(0, 1).getValue(1), 0.0);
		assertEquals(8.0, grid.getCell(1, 1).getValue(0), 0.0);
		assertEquals(9.0, grid.getCell(1, 1).getValue(1), 0.0);
		assertEquals(10.0, grid.getCell(0, 2).getValue(0), 0.0);
		assertEquals(11.0, grid.getCell(0, 2).getValue(1), 0.0);
		assertEquals(12.0, grid.getCell(1, 2).getValue(0), 0.0);
		assertEquals(13.0, grid.getCell(1, 2).getValue(1), 0.0);
	}
	
	@Test
	public void testReset()
	{
		System.out.println("\n== Test : testReset() ==");
		
		world.setFileName("Test Data/world/world.caw");
		world.load();
		
		world.setKeepHistory(2);
		
		world.start();
		System.out.println("== File 1 : " + world.getFilename() + " ==");
		
		World secondWorld = new World();
		
		secondWorld.setFileName("Test Data/world/secondWorld.caw");
		
		TriangleGrid[] worlds = new TriangleGrid[3];
		
		worlds[0] = new TriangleGrid(new Vector2i(2, 3), 2);
		worlds[0].setCell(0, 0, new double[] {0.0, 1.0});
		worlds[0].setCell(1, 0, new double[] {2.0, 3.0});
		worlds[0].setCell(0, 1, new double[] {4.0, 5.0});
		worlds[0].setCell(1, 1, new double[] {6.0, 7.0});
		worlds[0].setCell(0, 2, new double[] {8.0, 9.0});
		worlds[0].setCell(1, 2, new double[] {10.0, 11.0});
		
		worlds[1] = new TriangleGrid(new Vector2i(2, 3), 2);
		worlds[1].setCell(0, 0, new double[] {1.0, 2.0});
		worlds[1].setCell(1, 0, new double[] {3.0, 4.0});
		worlds[1].setCell(0, 1, new double[] {5.0, 6.0});
		worlds[1].setCell(1, 1, new double[] {7.0, 8.0});
		worlds[1].setCell(0, 2, new double[] {9.0, 10.0});
		worlds[1].setCell(1, 2, new double[] {11.0, 12.0});
		
		worlds[2] = new TriangleGrid(new Vector2i(2, 3), 2);
		worlds[2].setCell(0, 0, new double[] {2.0, 3.0});
		worlds[2].setCell(1, 0, new double[] {4.0, 5.0});
		worlds[2].setCell(0, 1, new double[] {6.0, 7.0});
		worlds[2].setCell(1, 1, new double[] {8.0, 9.0});
		worlds[2].setCell(0, 2, new double[] {10.0, 11.0});
		worlds[2].setCell(1, 2, new double[] {12.0, 13.0});
		
		for(int i = 0; i < 3; i++)
			secondWorld.addGeneration(worlds[i]);
		
		secondWorld.setRuleCode("rules");
		secondWorld.setColourCode("colours");
		try
		{
			secondWorld.save();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		world.resetTo(secondWorld);
		System.out.println("== File 2 : " + world.getFilename() + " ==");
		
		List<Grid> grids = secondWorld.getGenerations();
		Grid grid;
		
		world.stop();
		
		//Generation 1
		grid = grids.get(0);
		assertEquals('T', grid.getTypeSymbol());
		assertEquals(2, grid.getWidth());
		assertEquals(3, grid.getHeight());
		assertEquals(0.0, grid.getCell(0, 0).getValue(0), 0.0);
		assertEquals(1.0, grid.getCell(0, 0).getValue(1), 0.0);
		assertEquals(2.0, grid.getCell(1, 0).getValue(0), 0.0);
		assertEquals(3.0, grid.getCell(1, 0).getValue(1), 0.0);
		assertEquals(4.0, grid.getCell(0, 1).getValue(0), 0.0);
		assertEquals(5.0, grid.getCell(0, 1).getValue(1), 0.0);
		assertEquals(6.0, grid.getCell(1, 1).getValue(0), 0.0);
		assertEquals(7.0, grid.getCell(1, 1).getValue(1), 0.0);
		assertEquals(8.0, grid.getCell(0, 2).getValue(0), 0.0);
		assertEquals(9.0, grid.getCell(0, 2).getValue(1), 0.0);
		assertEquals(10.0, grid.getCell(1, 2).getValue(0), 0.0);
		assertEquals(11.0, grid.getCell(1, 2).getValue(1), 0.0);
		
		//Generation 2
		grid = grids.get(1);
		assertEquals('T', grid.getTypeSymbol());
		assertEquals(2, grid.getWidth());
		assertEquals(3, grid.getHeight());
		assertEquals(1.0, grid.getCell(0, 0).getValue(0), 0.0);
		assertEquals(2.0, grid.getCell(0, 0).getValue(1), 0.0);
		assertEquals(3.0, grid.getCell(1, 0).getValue(0), 0.0);
		assertEquals(4.0, grid.getCell(1, 0).getValue(1), 0.0);
		assertEquals(5.0, grid.getCell(0, 1).getValue(0), 0.0);
		assertEquals(6.0, grid.getCell(0, 1).getValue(1), 0.0);
		assertEquals(7.0, grid.getCell(1, 1).getValue(0), 0.0);
		assertEquals(8.0, grid.getCell(1, 1).getValue(1), 0.0);
		assertEquals(9.0, grid.getCell(0, 2).getValue(0), 0.0);
		assertEquals(10.0, grid.getCell(0, 2).getValue(1), 0.0);
		assertEquals(11.0, grid.getCell(1, 2).getValue(0), 0.0);
		assertEquals(12.0, grid.getCell(1, 2).getValue(1), 0.0);
		
		//Generation 3
		grid = grids.get(2);
		assertEquals('T', grid.getTypeSymbol());
		assertEquals(2, grid.getWidth());
		assertEquals(3, grid.getHeight());
		assertEquals(2.0, grid.getCell(0, 0).getValue(0), 0.0);
		assertEquals(3.0, grid.getCell(0, 0).getValue(1), 0.0);
		assertEquals(4.0, grid.getCell(1, 0).getValue(0), 0.0);
		assertEquals(5.0, grid.getCell(1, 0).getValue(1), 0.0);
		assertEquals(6.0, grid.getCell(0, 1).getValue(0), 0.0);
		assertEquals(7.0, grid.getCell(0, 1).getValue(1), 0.0);
		assertEquals(8.0, grid.getCell(1, 1).getValue(0), 0.0);
		assertEquals(9.0, grid.getCell(1, 1).getValue(1), 0.0);
		assertEquals(10.0, grid.getCell(0, 2).getValue(0), 0.0);
		assertEquals(11.0, grid.getCell(0, 2).getValue(1), 0.0);
		assertEquals(12.0, grid.getCell(1, 2).getValue(0), 0.0);
		assertEquals(13.0, grid.getCell(1, 2).getValue(1), 0.0);
	}
	
	@Test
	public void testStreamingFromDisk()
	{
		System.out.println("\n== Test : testStreamingFromDisk() ==");
		
		world.setFileName("Test Data/world/world.caw");
		world.load();
		
		world.setKeepHistory(2);
		
		world.start();
		
		Grid grid = world.getGeneration(2);
		
		world.stop();
		
		assertEquals('T', grid.getTypeSymbol());
		assertEquals(2, grid.getWidth());
		assertEquals(3, grid.getHeight());
		assertEquals(2.0, grid.getCell(0, 0).getValue(0), 0.0);
		assertEquals(3.0, grid.getCell(0, 0).getValue(1), 0.0);
		assertEquals(4.0, grid.getCell(1, 0).getValue(0), 0.0);
		assertEquals(5.0, grid.getCell(1, 0).getValue(1), 0.0);
		assertEquals(6.0, grid.getCell(0, 1).getValue(0), 0.0);
		assertEquals(7.0, grid.getCell(0, 1).getValue(1), 0.0);
		assertEquals(8.0, grid.getCell(1, 1).getValue(0), 0.0);
		assertEquals(9.0, grid.getCell(1, 1).getValue(1), 0.0);
		assertEquals(10.0, grid.getCell(0, 2).getValue(0), 0.0);
		assertEquals(11.0, grid.getCell(0, 2).getValue(1), 0.0);
		assertEquals(12.0, grid.getCell(1, 2).getValue(0), 0.0);
		assertEquals(13.0, grid.getCell(1, 2).getValue(1), 0.0);
	}
	
	@Test
	public void testStreamingToDisk()
	{
		System.out.println("\n== Test : testStreamingToDisk() ==");
		
		world.setFileName("Test Data/world/world.caw");
		world.load();
		
		world.setKeepHistory(2);
		
		world.start();
		
		int nextGen = world.getNumGenerations();
		
		HexagonGrid gen = new HexagonGrid(world.getInitialGeneration());
		gen.setCell(0, 0, new double[] {3.0, 4.0});
		gen.setCell(1, 0, new double[] {5.0, 6.0});
		gen.setCell(0, 1, new double[] {7.0, 8.0});
		gen.setCell(1, 1, new double[] {9.0, 10.0});
		gen.setCell(0, 2, new double[] {11.0, 12.0});
		gen.setCell(1, 2, new double[] {13.0, 14.0});
		
		world.addGeneration(gen);
		Grid grid = world.getGeneration(nextGen);
		
		world.stop();
		
		assertEquals('H', grid.getTypeSymbol());
		assertEquals(2, grid.getWidth());
		assertEquals(3, grid.getHeight());
		assertEquals(3.0, grid.getCell(0, 0).getValue(0), 0.0);
		assertEquals(4.0, grid.getCell(0, 0).getValue(1), 0.0);
		assertEquals(5.0, grid.getCell(1, 0).getValue(0), 0.0);
		assertEquals(6.0, grid.getCell(1, 0).getValue(1), 0.0);
		assertEquals(7.0, grid.getCell(0, 1).getValue(0), 0.0);
		assertEquals(8.0, grid.getCell(0, 1).getValue(1), 0.0);
		assertEquals(9.0, grid.getCell(1, 1).getValue(0), 0.0);
		assertEquals(10.0, grid.getCell(1, 1).getValue(1), 0.0);
		assertEquals(11.0, grid.getCell(0, 2).getValue(0), 0.0);
		assertEquals(12.0, grid.getCell(0, 2).getValue(1), 0.0);
		assertEquals(13.0, grid.getCell(1, 2).getValue(0), 0.0);
		assertEquals(14.0, grid.getCell(1, 2).getValue(1), 0.0);
	}
	
	@Test
	public void testStreamToFromToDisk()
	{
		System.out.println("\n== Test : testStreamToFromToDisk ==");
		
		world.setFileName("Test Data/world/world.caw");
		world.load();
		
		world.setKeepHistory(2);
		
		world.start();
		
		int nextGen = world.getNumGenerations();
		
		HexagonGrid gen1 = new HexagonGrid(world.getInitialGeneration());
		gen1.setCell(0, 0, new double[] {3.0, 4.0});
		gen1.setCell(1, 0, new double[] {5.0, 6.0});
		gen1.setCell(0, 1, new double[] {7.0, 8.0});
		gen1.setCell(1, 1, new double[] {9.0, 10.0});
		gen1.setCell(0, 2, new double[] {11.0, 12.0});
		gen1.setCell(1, 2, new double[] {13.0, 14.0});
		
		HexagonGrid gen2 = new HexagonGrid(world.getInitialGeneration());
		gen2.setCell(0, 0, new double[] {4.0, 5.0});
		gen2.setCell(1, 0, new double[] {6.0, 7.0});
		gen2.setCell(0, 1, new double[] {8.0, 9.0});
		gen2.setCell(1, 1, new double[] {10.0, 11.0});
		gen2.setCell(0, 2, new double[] {12.0, 13.0});
		gen2.setCell(1, 2, new double[] {14.0, 15.0});
		
		world.addGeneration(gen1);
		Grid grid = world.getGeneration(nextGen);
		
		nextGen++;
		
		world.addGeneration(gen2);
		grid = world.getGeneration(nextGen);
		
		world.stop();
		
		assertEquals('H', grid.getTypeSymbol());
		assertEquals(2, grid.getWidth());
		assertEquals(3, grid.getHeight());
		assertEquals(4.0, grid.getCell(0, 0).getValue(0), 0.0);
		assertEquals(5.0, grid.getCell(0, 0).getValue(1), 0.0);
		assertEquals(6.0, grid.getCell(1, 0).getValue(0), 0.0);
		assertEquals(7.0, grid.getCell(1, 0).getValue(1), 0.0);
		assertEquals(8.0, grid.getCell(0, 1).getValue(0), 0.0);
		assertEquals(9.0, grid.getCell(0, 1).getValue(1), 0.0);
		assertEquals(10.0, grid.getCell(1, 1).getValue(0), 0.0);
		assertEquals(11.0, grid.getCell(1, 1).getValue(1), 0.0);
		assertEquals(12.0, grid.getCell(0, 2).getValue(0), 0.0);
		assertEquals(13.0, grid.getCell(0, 2).getValue(1), 0.0);
		assertEquals(14.0, grid.getCell(1, 2).getValue(0), 0.0);
		assertEquals(15.0, grid.getCell(1, 2).getValue(1), 0.0);
	}
	
	@After
	public void testDelete()
	{
		//System.out.println("\n== Deleting tester files ==");
		
		String worldName = "Test Data/world/world";
		String secondWorldName = "Test Data/world/secondWorld";
		
		int worldNum = 1;
		
		while(true)
		{
			File caw = new File(worldName + "_" + worldNum + ".caw");
			boolean success = false;
			
			if(caw.exists())
				success = caw.delete();
			else
				break;
			
			System.out.println("Deletion of file " + worldName + "_" + worldNum + ".caw : " + (success ? "SUCCESS" : "FAILED"));
			worldNum++;
		}
		
		{
			File caw = new File(secondWorldName + ".caw");
			boolean success = false;
			
			if(caw.exists())
				success = caw.delete();
			
			System.out.println("Deletion of file " + secondWorldName + ".caw : " + (success ? "SUCCESS" : "FAILED"));
		}
		
		worldNum = 1;
		
		while(true)
		{
			File caw = new File(secondWorldName + "_" + worldNum + ".caw");
			boolean success = false;
			
			if(caw.exists())
				success = caw.delete();
			else
				break;
			
			System.out.println("Deletion of file " + secondWorldName + "_" + worldNum + ".caw : " + (success ? "SUCCESS" : "FAILED"));
			worldNum++;
		}
	}
}
