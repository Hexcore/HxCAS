package com.hexcore.cas.rulesystems.test;

import java.io.File;

import junit.framework.TestCase;

import com.hexcore.cas.model.Cell;
import com.hexcore.cas.rulesystems.CALCompiler;
import com.hexcore.cas.rulesystems.Rule;
import com.hexcore.cas.rulesystems.RuleLoader;

public class TestByteCode extends TestCase
{
	public void testPreconditions()
	{
		File in = new File("Test Data/testRules.cal");
		assertTrue(in.exists());
	}

	
	public void testPropertyAssignment()
	{
		CALCompiler compiler = new CALCompiler();
		compiler.loadRules("Test Data/rules/testSet1.cal");
		RuleLoader rl = new RuleLoader();
		
		Rule rule = rl.loadRule(compiler.getCode());
		
		
		Cell c = new Cell(new double[]{0,0,0});
		
		//New Cell Test
		assertEquals(0.0, c.getValue(0));
		assertEquals(0.0, c.getValue(1));
		assertEquals(0.0, c.getValue(2));
		
		//Run once Test
		rule.run(c, null);		
		assertEquals(0.0, c.getValue(0));
		assertEquals(1.0, c.getValue(1));
		assertEquals(2.0, c.getValue(2));
		
		//Run again Test
		rule.run(c, null);		
		assertEquals(0.0, c.getValue(0));
		assertEquals(2.0, c.getValue(1));
		assertEquals(3.0, c.getValue(2));
	}
}
