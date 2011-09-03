package com.hexcore.cas.control.protocol.test;

import java.io.IOException;
import java.util.ArrayList;

import junit.framework.TestCase;

import com.hexcore.cas.control.client.ClientOverseer;
import com.hexcore.cas.control.server.ServerOverseer;
import com.hexcore.cas.math.Recti;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.RectangleGrid;

public class TestDistributionSystem extends TestCase
{
	ClientOverseer co = null;
	ServerOverseer so = null;

	private void testClientGridsAfterRectangleVersion(Grid[] cG)
	{
		System.out.println("Testing clientGrids was altered correctly");
		assertEquals(1, cG.length);
		assertEquals(4, cG[0].getWidth());
		assertEquals(6, cG[0].getHeight());
		assertEquals(0.0, cG[0].getCell(0, 0).getValue(0));
		assertEquals(0.0, cG[0].getCell(1, 0).getValue(0));
		assertEquals(1.0, cG[0].getCell(2, 0).getValue(0));
		assertEquals(0.0, cG[0].getCell(3, 0).getValue(0));
		assertEquals(0.0, cG[0].getCell(0, 1).getValue(0));
		assertEquals(0.0, cG[0].getCell(1, 1).getValue(0));
		assertEquals(0.0, cG[0].getCell(2, 1).getValue(0));
		assertEquals(0.0, cG[0].getCell(3, 1).getValue(0));
		assertEquals(0.0, cG[0].getCell(0, 2).getValue(0));
		assertEquals(0.0, cG[0].getCell(1, 2).getValue(0));
		assertEquals(0.0, cG[0].getCell(2, 2).getValue(0));
		assertEquals(0.0, cG[0].getCell(3, 2).getValue(0));
		assertEquals(0.0, cG[0].getCell(0, 3).getValue(0));
		assertEquals(0.0, cG[0].getCell(1, 3).getValue(0));
		assertEquals(1.0, cG[0].getCell(2, 3).getValue(0));
		assertEquals(1.0, cG[0].getCell(3, 3).getValue(0));
		assertEquals(0.0, cG[0].getCell(0, 4).getValue(0));
		assertEquals(0.0, cG[0].getCell(1, 4).getValue(0));
		assertEquals(0.0, cG[0].getCell(2, 4).getValue(0));
		assertEquals(0.0, cG[0].getCell(3, 4).getValue(0));
		assertEquals(0.0, cG[0].getCell(0, 5).getValue(0));
		assertEquals(0.0, cG[0].getCell(1, 5).getValue(0));
		assertEquals(0.0, cG[0].getCell(2, 5).getValue(0));
		assertEquals(0.0, cG[0].getCell(3, 5).getValue(0));
	}
	
	private void testClientGridsRectangleVersion(Grid[] cG)
	{
		System.out.println("Testing clientGrids was set correctly");
		assertEquals(1, cG.length);
		assertEquals(4, cG[0].getWidth());
		assertEquals(6, cG[0].getHeight());
		assertEquals(0.0, cG[0].getCell(0, 0).getValue(0));
		assertEquals(0.0, cG[0].getCell(1, 0).getValue(0));
		assertEquals(1.0, cG[0].getCell(2, 0).getValue(0));
		assertEquals(0.0, cG[0].getCell(3, 0).getValue(0));
		assertEquals(0.0, cG[0].getCell(0, 1).getValue(0));
		assertEquals(0.0, cG[0].getCell(1, 1).getValue(0));
		assertEquals(0.0, cG[0].getCell(2, 1).getValue(0));
		assertEquals(0.0, cG[0].getCell(3, 1).getValue(0));
		assertEquals(0.0, cG[0].getCell(0, 2).getValue(0));
		assertEquals(0.0, cG[0].getCell(1, 2).getValue(0));
		assertEquals(1.0, cG[0].getCell(2, 2).getValue(0));
		assertEquals(0.0, cG[0].getCell(3, 2).getValue(0));
		assertEquals(0.0, cG[0].getCell(0, 3).getValue(0));
		assertEquals(0.0, cG[0].getCell(1, 3).getValue(0));
		assertEquals(0.0, cG[0].getCell(2, 3).getValue(0));
		assertEquals(1.0, cG[0].getCell(3, 3).getValue(0));
		assertEquals(0.0, cG[0].getCell(0, 4).getValue(0));
		assertEquals(0.0, cG[0].getCell(1, 4).getValue(0));
		assertEquals(1.0, cG[0].getCell(2, 4).getValue(0));
		assertEquals(0.0, cG[0].getCell(3, 4).getValue(0));
		assertEquals(0.0, cG[0].getCell(0, 5).getValue(0));
		assertEquals(0.0, cG[0].getCell(1, 5).getValue(0));
		assertEquals(0.0, cG[0].getCell(2, 5).getValue(0));
		assertEquals(0.0, cG[0].getCell(3, 5).getValue(0));
	}
	
	private void testGridRectangleVersion(Grid grid)
	{
		System.out.println("Testing grid was set correctly");
		assertEquals('R', grid.getType());
		assertEquals(4, grid.getWidth());
		assertEquals(4, grid.getHeight());
		assertEquals(0.0, grid.getCell(0, 0).getValue(0));
		assertEquals(0.0, grid.getCell(0, 1).getValue(0));
		assertEquals(0.0, grid.getCell(0, 2).getValue(0));
		assertEquals(0.0, grid.getCell(0, 3).getValue(0));
		assertEquals(0.0, grid.getCell(1, 0).getValue(0));
		assertEquals(1.0, grid.getCell(1, 1).getValue(0));
		assertEquals(0.0, grid.getCell(1, 2).getValue(0));
		assertEquals(1.0, grid.getCell(1, 3).getValue(0));
		assertEquals(0.0, grid.getCell(2, 0).getValue(0));
		assertEquals(0.0, grid.getCell(2, 1).getValue(0));
		assertEquals(1.0, grid.getCell(2, 2).getValue(0));
		assertEquals(0.0, grid.getCell(2, 3).getValue(0));
		assertEquals(0.0, grid.getCell(3, 0).getValue(0));
		assertEquals(0.0, grid.getCell(3, 1).getValue(0));
		assertEquals(0.0, grid.getCell(3, 2).getValue(0));
		assertEquals(0.0, grid.getCell(3, 3).getValue(0));
	}
	
	private void testNameListRectangleVersion(ArrayList<String> nameList)
	{
		System.out.println("Testing nameList was set correctly");
		for(int i = 0; i < nameList.size(); i++)
			assertEquals("localhost", nameList.get(i));
	}
	
	private void testWorkablesRectangleVersion(Recti[] aw, Recti[] uw, int NOC)
	{
		System.out.println("Testing workables was set correctly");
		System.out.println("--numOfclients");
		assertEquals(1, NOC);
		
		System.out.println("--unalteredWorkables");
		assertEquals(0, uw[0].getPosition().x);
		assertEquals(0, uw[0].getPosition().y);
		assertEquals(2, uw[1].getPosition().x);
		assertEquals(0, uw[1].getPosition().y);
		assertEquals(2, uw[0].getSize().x);
		assertEquals(4, uw[0].getSize().y);
		assertEquals(2, uw[1].getSize().x);
		assertEquals(4, uw[1].getSize().y);

		System.out.println("--alteredWorkables");
		assertEquals(1, aw[0].getPosition().x);
		assertEquals(1, aw[0].getPosition().y);
		assertEquals(2, aw[1].getPosition().x);
		assertEquals(0, aw[1].getPosition().y);
		assertEquals(2, aw[0].getSize().x);
		assertEquals(4, aw[0].getSize().y);
		assertEquals(2, aw[1].getSize().x);
		assertEquals(4, aw[1].getSize().y);
	}
	
	/*
	@SuppressWarnings("unchecked")
	private void testRectangleVersion()
		throws IOException, InterruptedException
	{
		System.out.println("===============================================");
		System.out.println("TESTING DISTRIBUTION SYSTEM WITH RECTANGLE GRID");
		System.out.println("===============================================");
		RectangleGrid g = new RectangleGrid(new Vector2i(4, 4), new Cell(1));
		for(int y = 0; y < 4; y++)
			for(int x = 0; x < 4; x++)
				g.getCell(x, y).setValue(0, 0.0);
		g.getCell(1, 1).setValue(0, 1.0);
		g.getCell(2, 2).setValue(0, 1.0);
		g.getCell(1, 3).setValue(0, 1.0);

		co = new ClientOverseer();
		{
			so = new ServerOverseer(g);
			
			Grid grid = so.getGrid();
			testGridRectangleVersion(grid);
		}
		
		{
			ArrayList<String> nameList = new ArrayList<String>();
			nameList.add("localhost");
			so.setClientNames(nameList);
			
			nameList = so.getClientNames();
			testNameListRectangleVersion(nameList);
		}
		
		so.start();
		
		{
			Recti[] workables = new Recti[2];
			workables[0] = new Recti(new Vector2i(0, 0), new Vector2i(2, 4));
			workables[1] = new Recti(new Vector2i(2, 0), new Vector2i(2, 4));
			so.setClientWorkables(workables);
			
			int NOC = so.getNumberOfClients();
			Recti[] aw = so.getClientWorkablesAltered();
			Recti[] uw = so.getClientWorkablesUnaltered();
			testWorkablesRectangleVersion(aw, uw, NOC);
		
			Grid[] cG = so.getClientGrids();
			testClientGridsRectangleVersion(cG);
		}
		
		{
			Thread.sleep(1000);
			so.send();
			Thread.sleep(5000);
			Grid[] cG = so.getClientGrids();
			testClientGridsAfterRectangleVersion(cG);
		}
		
		System.out.println("Testing Distribution System complete");
		
		so.disconnect();
	}
	*/
}
