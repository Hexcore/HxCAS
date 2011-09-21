package com.hexcore.cas.rulesystems.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Test;

import com.hexcore.cas.model.Cell;
import com.hexcore.cas.rulesystems.RuleProgram;

public class TestRuleProgram
{
	@Test
	public void test()
	{
		try
		{
			byte[] bytes = readBytes("Test Data/bytecode/GameOfLifeRule.class");
			
			RuleProgram program = new RuleProgram();
			program.loadBytecode(bytes);
			
			assertTrue(program.isValid());
			
			Cell target = new Cell(new double[] {0.0f});
			Cell[] neighbours = new Cell[4];
			
			neighbours[0] = new Cell(new double[] {1.0f});
			neighbours[1] = new Cell(new double[] {1.0f});
			neighbours[2] = new Cell(new double[] {1.0f});
			neighbours[3] = new Cell(new double[] {1.0f});
			
			program.run(target, neighbours);
			assertEquals(0.0f, target.getValue(0), 0.1f);
			
			neighbours[0] = new Cell(new double[] {0.0f});
			neighbours[1] = new Cell(new double[] {1.0f});
			neighbours[2] = new Cell(new double[] {1.0f});
			neighbours[3] = new Cell(new double[] {1.0f});
			
			program.run(target, neighbours);
			assertEquals(1.0f, target.getValue(0), 0.1f);	
						
			neighbours[0] = new Cell(new double[] {0.0f});
			neighbours[1] = new Cell(new double[] {1.0f});
			neighbours[2] = new Cell(new double[] {1.0f});
			neighbours[3] = new Cell(new double[] {0.0f});
			
			program.run(target, neighbours);
			assertEquals(1.0f, target.getValue(0), 0.1f);
			
			neighbours[0] = new Cell(new double[] {0.0f});
			neighbours[1] = new Cell(new double[] {0.0f});
			neighbours[2] = new Cell(new double[] {0.0f});
			neighbours[3] = new Cell(new double[] {0.0f});
			
			program.run(target, neighbours);
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
		InputStream stream = new FileInputStream(file);
		
		int length = (int)file.length();
		byte[] bytes = new byte[length];
		
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
