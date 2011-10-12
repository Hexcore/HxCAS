package com.hexcore.cas.rulesystems.test;

import java.io.File;

import org.junit.Ignore;
import org.junit.experimental.theories.suppliers.TestedOn;

import junit.framework.TestCase;

import com.hexcore.cas.model.Cell;
import com.hexcore.cas.rulesystems.CALCompiler;
import com.hexcore.cas.rulesystems.Rule;
import com.hexcore.cas.rulesystems.RuleLoader;

public class TestByteCode extends TestCase
{
	public void testPreconditions()
	{
		File in = new File("Test Data/rules/testSet1.cal");
		assertTrue(in.exists());
		
		in = new File("Test Data/rules/testSet2.cal");
		assertTrue(in.exists());
		
		in = new File("Test Data/rules/testSet3.cal");
		assertTrue(in.exists());

		in = new File("Test Data/rules/testSet4.cal");
		assertTrue(in.exists());
		
		in = new File("Test Data/rules/testSet5.cal");
		assertTrue(in.exists());
		
		in = new File("Test Data/rules/testSet6.cal");
		assertTrue(in.exists());
		
		in = new File("Test Data/rules/testSet7.cal");
		assertTrue(in.exists());
	}

	
	public void testPropertyAssignment()
	{
		CALCompiler compiler = new CALCompiler();
		compiler.loadRules("Test Data/rules/testSet1.cal");
		assertEquals(0, compiler.getErrorCount());
		
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
	
	public void testVariablesPostOps()
	{
		CALCompiler compiler = new CALCompiler();
		compiler.loadRules("Test Data/rules/testSet2.cal");
		assertEquals(0, compiler.getErrorCount());
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
		assertEquals(11.0, c.getValue(2));
		
		//Run again Test
		rule.run(c, null);		
		assertEquals(0.0, c.getValue(0));
		assertEquals(2.0, c.getValue(1));
		assertEquals(12.0, c.getValue(2));
	}
	
	public void testIfStatementsBasic()
	{
		CALCompiler compiler = new CALCompiler();
		compiler.loadRules("Test Data/rules/testSet3.cal");
		assertEquals(0, compiler.getErrorCount());
		RuleLoader rl = new RuleLoader();
		
		Rule rule = rl.loadRule(compiler.getCode());
		
		
		Cell c = new Cell(new double[]{0,0,0});
		
		
		//New Cell Test
		assertEquals(0.0, c.getValue(0));
		assertEquals(0.0, c.getValue(1));
		assertEquals(0.0, c.getValue(2));
		
		//Run once
		rule.run(c, null);		
		assertEquals(0.0, c.getValue(0));
		assertEquals(1.0, c.getValue(1));
		assertEquals(2.0, c.getValue(2));
	}
	
	public void testPropertyCount()
	{
		CALCompiler compiler0 = new CALCompiler();
		CALCompiler compiler1 = new CALCompiler();
		CALCompiler compiler2 = new CALCompiler();
		CALCompiler compiler3 = new CALCompiler();	
		RuleLoader rl0 = new RuleLoader();
		RuleLoader rl1 = new RuleLoader();
		RuleLoader rl2 = new RuleLoader();
		RuleLoader rl3 = new RuleLoader();
		
		compiler0.loadRules("Test Data/rules/testSet4.cal");
		assertEquals(0, compiler0.getErrorCount());
		Rule rule0 = rl0.loadRule(compiler0.getCode());		
		
		
		compiler1.loadRules("Test Data/rules/testSet5.cal");
		assertEquals(0, compiler1.getErrorCount());
		Rule rule1 = rl1.loadRule(compiler1.getCode());
		
		compiler2.loadRules("Test Data/rules/testSet6.cal");
		assertEquals(0, compiler2.getErrorCount());
		Rule rule2 = rl2.loadRule(compiler2.getCode());
		
		
		compiler3.loadRules("Test Data/rules/testSet7.cal");		
		assertEquals(0, compiler3.getErrorCount());		
		Rule rule3 = rl3.loadRule(compiler3.getCode());
		
		assertEquals(2, rule0.getNumProperties());
		assertEquals(3, rule1.getNumProperties());
		assertEquals(4, rule2.getNumProperties());
		assertEquals(5, rule3.getNumProperties());	
		
	}
}
