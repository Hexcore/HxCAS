package com.hexcore.cas.rulesystems.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.hexcore.cas.model.Cell;
import com.hexcore.cas.rulesystems.CALCompiler;
import com.hexcore.cas.rulesystems.Parser;
import com.hexcore.cas.rulesystems.Rule;
import com.hexcore.cas.rulesystems.RuleLoader;

public class TestLoops
{
	@Test
	public void testSingleStatementInLoop()
	{
		CALCompiler compiler = new CALCompiler();
		compiler.compileFile("Test Data/rules/testSet13.cal");		
		assertTrue(Parser.getErrorCount() == 0);
		
		RuleLoader rl = new RuleLoader();
		
		Rule rule = rl.loadRule(compiler.getCode());
		
		Cell c0 = new Cell(new double[]{0,0});
		rule.run(c0, null);
		
		//Post run test
		assertEquals(0.0, c0.getValue(0), 0.0);
		assertEquals(10.0, c0.getValue(1), 0.0);
	}
	
	@Test
	public void testMultiStatementInLoop()
	{
		CALCompiler compiler = new CALCompiler();
		compiler.compileFile("Test Data/rules/testSet14.cal");		
		assertTrue(Parser.getErrorCount() == 0);
		
		RuleLoader rl = new RuleLoader();
		
		Rule rule = rl.loadRule(compiler.getCode());
		
		Cell c0 = new Cell(new double[]{0,0,0});
		rule.run(c0, null);
		
		//Post run test
		assertEquals(0.0, c0.getValue(0), 0.0);
		assertEquals(20.0, c0.getValue(1), 0.0);		
		assertEquals(10.0, c0.getValue(2), 0.0);
	}	
	
}
