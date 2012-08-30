package com.hexcore.cas.rulesystems.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.hexcore.cas.rulesystems.CALCompiler;
import com.hexcore.cas.rulesystems.Parser;

public class TestLoops
{
	@Test
	public void testSingleStatementInLoop()
	{
		CALCompiler compiler = new CALCompiler();
		compiler.compileFile("Test Data/rules/testSet13.cal");		
		assertTrue(Parser.getErrorCount() == 0);
	}
	
	@Test
	public void testMultiStatementInLoop()
	{
		CALCompiler compiler = new CALCompiler();
		compiler.compileFile("Test Data/rules/testSet14.cal");		
		assertTrue(Parser.getErrorCount() == 0);
	}	
	
}
