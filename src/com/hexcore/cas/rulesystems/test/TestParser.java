package com.hexcore.cas.rulesystems.test;

import java.io.File;

import junit.framework.TestCase;

import com.hexcore.cas.rulesystems.CALCompiler;
import com.hexcore.cas.rulesystems.Parser;

public class TestParser extends TestCase
{

	
	public void testPreconditions()
	{
		File in = new File("Test Data/testRules.cal");
		assertTrue(in.exists());
	}

	
	public void testParsing()
	{
		CALCompiler compiler = new CALCompiler();
		compiler.compileFile("Test Data/testRules.cal");
		
		assertTrue(Parser.getErrorCount() == 0);
	}

	
	
}
