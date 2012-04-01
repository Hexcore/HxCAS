package com.hexcore.cas.model.test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipException;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.HexagonGrid;
import com.hexcore.cas.model.TriangleGrid;
import com.hexcore.cas.model.World;
import com.hexcore.cas.model.WorldStreamer;

import junit.framework.TestCase;

public class TestWorldStreamer extends TestCase
{
	private World world = new World();
	
	public void testStartAndStop()
		throws ZipException, IOException
	{
		world.setFileName("Test Data/world/world.caw");
		world.load();
		
		WorldStreamer streamer = new WorldStreamer();
		streamer.start(world);
		
		String ruleCode = world.getRuleCode();
		assertNotNull(ruleCode);
		assertTrue(ruleCode.startsWith("rules"));
		
		String colourCode = world.getColourCode();
		assertNotNull(colourCode);
		assertTrue(colourCode.startsWith("colours"));
		
		streamer.stop();
		
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
	
	public void testReset()
	{
		world.setFileName("Test Data/world/world.caw");
		world.load();
		
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
		
		WorldStreamer streamer = new WorldStreamer();
		streamer.start(world);
		streamer.reset(secondWorld);
		
		List<Grid> grids = secondWorld.getGenerations();
		Grid grid;
		
		streamer.stop();
		
		//Generation 1
		grid = grids.get(0);
		assertEquals('T', grid.getTypeSymbol());
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
		assertEquals('T', grid.getTypeSymbol());
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
		assertEquals('T', grid.getTypeSymbol());
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
	
	public void testStreamingFromDisk()
	{
		world.setFileName("Test Data/world/world.caw");
		world.load();
		
		WorldStreamer streamer = new WorldStreamer();
		streamer.start(world);
		
		Grid grid = streamer.streamGeneration(2);
		
		streamer.stop();

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
	
	public void testStreamingToDisk()
	{
		world.setFileName("Test Data/world/world.caw");
		world.load();
		
		WorldStreamer streamer = new WorldStreamer();
		streamer.start(world);
		
		int nextGen = world.getNumGenerations();
		
		HexagonGrid gen = new HexagonGrid(world.getInitialGeneration());
		gen.setCell(0, 0, new double[] {3.0, 4.0});
		gen.setCell(1, 0, new double[] {5.0, 6.0});
		gen.setCell(0, 1, new double[] {7.0, 8.0});
		gen.setCell(1, 1, new double[] {9.0, 10.0});
		gen.setCell(0, 2, new double[] {11.0, 12.0});
		gen.setCell(1, 2, new double[] {13.0, 14.0});
		
		streamer.streamGeneration(gen, nextGen);
		Grid grid = streamer.streamGeneration(nextGen);

		streamer.stop();
		
		assertEquals('H', grid.getTypeSymbol());
		assertEquals(2, grid.getWidth());
		assertEquals(3, grid.getHeight());
		assertEquals(3.0, grid.getCell(0, 0).getValue(0));
		assertEquals(4.0, grid.getCell(0, 0).getValue(1));
		assertEquals(5.0, grid.getCell(1, 0).getValue(0));
		assertEquals(6.0, grid.getCell(1, 0).getValue(1));
		assertEquals(7.0, grid.getCell(0, 1).getValue(0));
		assertEquals(8.0, grid.getCell(0, 1).getValue(1));
		assertEquals(9.0, grid.getCell(1, 1).getValue(0));
		assertEquals(10.0, grid.getCell(1, 1).getValue(1));
		assertEquals(11.0, grid.getCell(0, 2).getValue(0));
		assertEquals(12.0, grid.getCell(0, 2).getValue(1));
		assertEquals(13.0, grid.getCell(1, 2).getValue(0));
		assertEquals(14.0, grid.getCell(1, 2).getValue(1));
	}
	
	public void testStreamToFromToDisk()
	{
		world.setFileName("Test Data/world/world.caw");
		world.load();
		
		WorldStreamer streamer = new WorldStreamer();
		streamer.start(world);
		
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
		
		streamer.streamGeneration(gen1, nextGen);
		Grid grid = streamer.streamGeneration(nextGen);
		
		nextGen++;
		streamer.streamGeneration(gen2, nextGen);
		grid = streamer.streamGeneration(nextGen);

		streamer.stop();
		
		assertEquals('H', grid.getTypeSymbol());
		assertEquals(2, grid.getWidth());
		assertEquals(3, grid.getHeight());
		assertEquals(4.0, grid.getCell(0, 0).getValue(0));
		assertEquals(5.0, grid.getCell(0, 0).getValue(1));
		assertEquals(6.0, grid.getCell(1, 0).getValue(0));
		assertEquals(7.0, grid.getCell(1, 0).getValue(1));
		assertEquals(8.0, grid.getCell(0, 1).getValue(0));
		assertEquals(9.0, grid.getCell(0, 1).getValue(1));
		assertEquals(10.0, grid.getCell(1, 1).getValue(0));
		assertEquals(11.0, grid.getCell(1, 1).getValue(1));
		assertEquals(12.0, grid.getCell(0, 2).getValue(0));
		assertEquals(13.0, grid.getCell(0, 2).getValue(1));
		assertEquals(14.0, grid.getCell(1, 2).getValue(0));
		assertEquals(15.0, grid.getCell(1, 2).getValue(1));
	}
	
	public void testGetGenerationNum()
	{
		world.setFileName("Test Data/world/world.caw");
		world.load();
		
		WorldStreamer streamer = new WorldStreamer();
		streamer.start(world);
		
		int genAmount = streamer.getNumGenerations();
		
		streamer.stop();

		assertEquals(3, genAmount);
	}
	
	public void testGetGenerations()
	{
		world.setFileName("Test Data/world/world.caw");
		world.load();
		
		WorldStreamer streamer = new WorldStreamer();
		streamer.start(world);
		
		List<Grid> grids = streamer.getGenerations();
		
		streamer.stop();

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
	
	public void testGetLastGeneration()
	{
		world.setFileName("Test Data/world/world.caw");
		world.load();
		
		WorldStreamer streamer = new WorldStreamer();
		streamer.start(world);
		
		Grid grid = streamer.getLastGeneration();
		
		streamer.stop();
		
		//Generation 3
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
	
	//Not actually a test. Used to remove all the extra files created by the tests.
	public void testRemoveAllExtraFile()
	{
		boolean deletionSuccess = true;
		File fileToDelete = null;
		
		String secondWorld = "Test Data/world/secondWorld";
		
		int worldNum = 1;
		while(true)
		{
			fileToDelete = new File(secondWorld + "_" + worldNum + ".caw");
			if(fileToDelete.exists())
			{
				System.out.println("Deleting file " + secondWorld + "_" + worldNum + ".caw" + "...");
				deletionSuccess = fileToDelete.delete();
				assertTrue(deletionSuccess);
				worldNum++;
			}
			else
				break;
		}
		
		String worldBegin = "Test Data/world/world";
		worldNum = 1;
		while(true)
		{
			fileToDelete = new File(worldBegin + "_" + worldNum + ".caw");
			if(fileToDelete.exists())
			{
				System.out.println("Deleting file " + worldBegin + "_" + worldNum + ".caw" + "...");
				deletionSuccess = fileToDelete.delete();
				assertTrue(deletionSuccess);
				worldNum++;
			}
			else
				break;
		}
	}
}
