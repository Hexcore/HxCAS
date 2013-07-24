package com.hexcore.cas.rulesystems.test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.hexcore.cas.rulesystems.CALCompiler;
import com.hexcore.cas.rulesystems.Parser;

public class TestParser
{

	@Test
	public void testSimpleParsing()
	{
		CALCompiler compiler = new CALCompiler();
		compiler.compileFile("Test Data/testRules.cal");
		
		assertTrue(Parser.getErrorCount() == 0);
	}
	
	@Test
	public void testNStepParsing()
	{
		CALCompiler compiler = new CALCompiler();
		compiler.compileFile("Test Data/rules/testNStepParsing.cal");
		
		assertTrue(Parser.getErrorCount() == 0);
	}
	
	@Test
	public void testNStepParsingRestrictions()
	{
		CALCompiler compiler = new CALCompiler();
		
		compiler.compileFile("Test Data/rules/testNStepParsingRestrictions0.cal");		
		assertTrue(Parser.getErrorCount() != 0);
		
		compiler.compileFile("Test Data/rules/testNStepParsingRestrictions1.cal");
		assertTrue(Parser.getErrorCount() != 0);
		
		compiler.compileFile("Test Data/rules/testNStepParsingRestrictions2.cal");
		assertTrue(Parser.getErrorCount() != 0);
		
		compiler.compileFile("Test Data/rules/testNStepParsingRestrictions3.cal");
		assertTrue(Parser.getErrorCount() != 0);
		
		compiler.compileFile("Test Data/rules/testNStepParsingRestrictions4.cal");
		assertTrue(Parser.getErrorCount() != 0);
	}
	
	@Test
	public void testNStepParsingWithIndependentCode()
	{
CALCompiler compiler = new CALCompiler();
		
		compiler.compileFile("Test Data/rules/testNStepParsingWithIndependentCode.cal");		
		assertTrue(compiler.getErrorCount() != 0);
	}
	
	@Test
	public void testFunctionParameterChecking()
	{
		CALCompiler compiler = new CALCompiler();
		
		compiler.compileFile("Test Data/rules/testFunctionParameterChecking.cal");		
		assertTrue(Parser.getErrorCount() == 0);
	}
	
	@Test
	public void testFunctionParameterCheckingRestrictions()
	{
		CALCompiler compiler = new CALCompiler();
		
		compiler.compileFile("Test Data/rules/testFunctionParameterCheckingRestrictions.cal");		
		assertTrue(Parser.getErrorCount() != 0);
	}
	
	@Test
	public void testIfElseAdvanced()
	{
		CALCompiler compiler = new CALCompiler();
		
		compiler.compileFile("Test Data/rules/testIfElseAdvanced.cal");		
		assertTrue(compiler.getErrorCount() == 0);
	}
	
	@Test
	public void testArrayParsing()
	{
		CALCompiler compiler = new CALCompiler();
		
		compiler.compileFile("Test Data/rules/testArrayParsing.cal");		
		assertTrue(compiler.getErrorCount() == 0);
	}
	
	@Test
	public void testArrayParsingRestrictions()
	{
		CALCompiler compiler = new CALCompiler();
		
		compiler.compileFile("Test Data/rules/testArrayParsingRestrictions0.cal");		
		assertTrue(compiler.getErrorCount() != 0);
		
		compiler.compileFile("Test Data/rules/testArrayParsingRestrictions1.cal");		
		assertTrue(compiler.getErrorCount() != 0);
	}
}
