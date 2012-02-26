package com.hexcore.cas.rulesystems.test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.hexcore.cas.model.Cell;
import com.hexcore.cas.rulesystems.Rule;
import com.hexcore.cas.rulesystems.RuleLoader;

public class TestRuleLoader
{
	
	public void test()
	{
		try
		{
			byte[] bytes = readBytes("Test Data/bytecode/GameOfLifeRule.class");
			
			RuleLoader program = new RuleLoader();
			Rule rule = program.loadRule(bytes);
						
			Cell target = new Cell(new double[] {0.0f});
			Cell[] neighbours = new Cell[4];
			
			neighbours[0] = new Cell(new double[] {1.0f});
			neighbours[1] = new Cell(new double[] {1.0f});
			neighbours[2] = new Cell(new double[] {1.0f});
			neighbours[3] = new Cell(new double[] {1.0f});
			
			rule.run(target, neighbours);
			assertEquals(0.0f, target.getValue(0), 0.1f);
			
			neighbours[0] = new Cell(new double[] {0.0f});
			neighbours[1] = new Cell(new double[] {1.0f});
			neighbours[2] = new Cell(new double[] {1.0f});
			neighbours[3] = new Cell(new double[] {1.0f});
			
			rule.run(target, neighbours);
			assertEquals(1.0f, target.getValue(0), 0.1f);	
						
			neighbours[0] = new Cell(new double[] {0.0f});
			neighbours[1] = new Cell(new double[] {1.0f});
			neighbours[2] = new Cell(new double[] {1.0f});
			neighbours[3] = new Cell(new double[] {0.0f});
			
			rule.run(target, neighbours);
			assertEquals(1.0f, target.getValue(0), 0.1f);
			
			neighbours[0] = new Cell(new double[] {0.0f});
			neighbours[1] = new Cell(new double[] {0.0f});
			neighbours[2] = new Cell(new double[] {0.0f});
			neighbours[3] = new Cell(new double[] {0.0f});
			
			rule.run(target, neighbours);
			assertEquals(0.0f, target.getValue(0), 0.1f);			
		}
		catch (Exception e)
		{
			fail(e.getMessage());
		}
	}
	
	private byte[] readBytes(String filename) throws Exception
	{
		File file = new File(filename);
		
		if (!file.exists()) throw new Exception("Test bytecode could not be found");
		
		InputStream stream = new FileInputStream(file);
		
		int length = (int)file.length();
		byte[] bytes = new byte[length];
		
		System.out.println("Bytecode - File: " + filename + " - Size: " + length);
		
		int index = 0;
	    int numRead = 0;
	    while (index < bytes.length)
	    {
	    	numRead = stream.read(bytes, index, bytes.length-index);
	    	if (numRead <= 0) break;
	    	index += numRead;
	    }
	    
	    if (index < length) throw new Exception("Not all bytes were read in");

	    return bytes;
	}
}
