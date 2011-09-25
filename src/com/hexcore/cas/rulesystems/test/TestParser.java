package com.hexcore.cas.rulesystems.test;

import java.io.File;

import junit.framework.TestCase;

import com.hexcore.cas.rulesystems.HexcoreVM;
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
		HexcoreVM.loadRules("Test Data/testRules.cal");
		
		System.out.print(Parser.getResult());
		
		HexcoreVM.loadRules("Test Data/testRules.cal");
		
		System.out.print(Parser.getResult());
	}

	
	
}
