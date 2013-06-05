package com.hexcore.cas.rulesystems.test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.hexcore.cas.rulesystems.CALCompiler;
import com.hexcore.cas.rulesystems.Parser;

public class TestParser
{

	@Test
	public void testPreconditions()
	{
		File in = new File("Test Data/testRules.cal");
		assertTrue(in.exists());
	}

	@Test
	public void testParsing()
	{
		CALCompiler compiler = new CALCompiler();
		compiler.compileFile("Test Data/testRules.cal");
		
		assertTrue(Parser.getErrorCount() == 0);
	}

	
	
}
