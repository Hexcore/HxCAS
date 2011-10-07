package com.hexcore.cas.rulesystems.test;

import java.io.File;

import com.hexcore.cas.rulesystems.CALCompiler;
import com.hexcore.cas.rulesystems.Parser;
import com.hexcore.cas.rulesystems.RuleLoader;

import junit.framework.TestCase;

public class TestByteCode extends TestCase
{
	public void testPreconditions()
	{
		File in = new File("Test Data/testRules.cal");
		assertTrue(in.exists());
	}

	
	public void testPropertyAssignment()
	{
		CALCompiler.loadRules("Test Data/testRules.cal");
		RuleLoader rl = new RuleLoader();
		
		//rl.loadRule(b);
	
	}
}
