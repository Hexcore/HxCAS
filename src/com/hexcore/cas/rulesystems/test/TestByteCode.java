package com.hexcore.cas.rulesystems.test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.hexcore.cas.model.Cell;
import com.hexcore.cas.rulesystems.CALCompiler;
import com.hexcore.cas.rulesystems.Rule;
import com.hexcore.cas.rulesystems.RuleLoader;

public class TestByteCode
{
	@Test
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
		
		in = new File("Test Data/rules/testSet8.cal");
		assertTrue(in.exists());
		
		in = new File("Test Data/rules/testSet9.cal");
		assertTrue(in.exists());
		
		in = new File("Test Data/rules/testSet10.cal");
		assertTrue(in.exists());
		
		in = new File("Test Data/rules/testSet11.cal");
		assertTrue(in.exists());
		
		in = new File("Test Data/rules/testSet12.cal");
		assertTrue(in.exists());
		
		in = new File("Test Data/rules/testSet13.cal");
		assertTrue(in.exists());
	}

	@Test
	public void testPropertyAssignment()
	{
		CALCompiler compiler = new CALCompiler();
		compiler.compileFile("Test Data/rules/testSet1.cal");
		assertEquals(0, compiler.getErrorCount());
		
		RuleLoader rl = new RuleLoader();
		
		Rule rule = rl.loadRule(compiler.getCode());
		
		
		Cell c = new Cell(new double[]{0,0,0});
		
		//New Cell Test
		assertEquals(0.0, c.getValue(0), 0.0);
		assertEquals(0.0, c.getValue(1), 0.0);
		assertEquals(0.0, c.getValue(2), 0.0);
		
		//Run once Test
		rule.run(c, null);		
		assertEquals(0.0, c.getValue(0), 0.0);
		assertEquals(1.0, c.getValue(1), 0.0);
		assertEquals(2.0, c.getValue(2), 0.0);
		
		//Run again Test
		rule.run(c, null);		
		assertEquals(0.0, c.getValue(0), 0.0);
		assertEquals(2.0, c.getValue(1), 0.0);
		assertEquals(3.0, c.getValue(2), 0.0);
	}
	
	@Test
	public void testVariablesPostOps()
	{
		CALCompiler compiler = new CALCompiler();
		compiler.compileFile("Test Data/rules/testSet2.cal");
		assertEquals(0, compiler.getErrorCount());
		RuleLoader rl = new RuleLoader();
		
		Rule rule = rl.loadRule(compiler.getCode());
		
		
		Cell c = new Cell(new double[]{0,0,0});
		
		//New Cell Test
		assertEquals(0.0, c.getValue(0), 0.0);
		assertEquals(0.0, c.getValue(1), 0.0);
		assertEquals(0.0, c.getValue(2), 0.0);
		
		//Run once Test
		rule.run(c, null);		
		assertEquals(0.0, c.getValue(0), 0.0);
		assertEquals(1.0, c.getValue(1), 0.0);
		assertEquals(11.0, c.getValue(2), 0.0);
		
		//Run again Test
		rule.run(c, null);		
		assertEquals(0.0, c.getValue(0), 0.0);
		assertEquals(2.0, c.getValue(1), 0.0);
		assertEquals(12.0, c.getValue(2), 0.0);
	}
	
	@Test
	public void testIfStatementsBasic()
	{
		CALCompiler compiler = new CALCompiler();
		compiler.compileFile("Test Data/rules/testSet3.cal");
		assertEquals(0, compiler.getErrorCount());
		RuleLoader rl = new RuleLoader();
		
		Rule rule = rl.loadRule(compiler.getCode());
		
		
		Cell c = new Cell(new double[]{0,0,0});
		
		
		//New Cell Test
		assertEquals(0.0, c.getValue(0), 0.0);
		assertEquals(0.0, c.getValue(1), 0.0);
		assertEquals(0.0, c.getValue(2), 0.0);
		
		//Run once
		rule.run(c, null);		
		assertEquals(0.0, c.getValue(0), 0.0);
		assertEquals(1.0, c.getValue(1), 0.0);
		assertEquals(2.0, c.getValue(2), 0.0);
	}
	
	@Test
	public void testIfStatementsComplex()
	{
		CALCompiler compiler = new CALCompiler();
		compiler.compileFile("Test Data/rules/testSet8.cal");
		assertEquals(0, compiler.getErrorCount());
		RuleLoader rl = new RuleLoader();
		
		Rule rule = rl.loadRule(compiler.getCode());
		
		
		Cell c = new Cell(new double[]{0,0,0,0,0});
		
		
		//New Cell Test
		assertEquals(0.0, c.getValue(0), 0.0);
		assertEquals(0.0, c.getValue(1), 0.0);
		assertEquals(0.0, c.getValue(2), 0.0);
		assertEquals(0.0, c.getValue(3), 0.0);
		assertEquals(0.0, c.getValue(4), 0.0);
		
		//Run once
		rule.run(c, null);		
		assertEquals(0.0, c.getValue(0), 0.0);
		assertEquals(1.0, c.getValue(1), 0.0);
		assertEquals(2.0, c.getValue(2), 0.0);
		assertEquals(3.0, c.getValue(3), 0.0);
		assertEquals(4.0, c.getValue(4), 0.0);
	}
	
	@Test
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
		
		compiler0.compileFile("Test Data/rules/testSet4.cal");
		assertEquals(0, compiler0.getErrorCount());
		Rule rule0 = rl0.loadRule(compiler0.getCode());		
		
		
		compiler1.compileFile("Test Data/rules/testSet5.cal");
		assertEquals(0, compiler1.getErrorCount());
		Rule rule1 = rl1.loadRule(compiler1.getCode());
		
		compiler2.compileFile("Test Data/rules/testSet6.cal");
		assertEquals(0, compiler2.getErrorCount());
		Rule rule2 = rl2.loadRule(compiler2.getCode());
		
		
		compiler3.compileFile("Test Data/rules/testSet7.cal");		
		assertEquals(0, compiler3.getErrorCount());		
		Rule rule3 = rl3.loadRule(compiler3.getCode());
		
		assertEquals(2, rule0.getNumProperties());
		assertEquals(3, rule1.getNumProperties());
		assertEquals(4, rule2.getNumProperties());
		assertEquals(5, rule3.getNumProperties());	
		
	}
	
	@Test
	public void testNeighboursAccess()
	{
		CALCompiler compiler = new CALCompiler();
		compiler.compileFile("Test Data/rules/testSet9.cal");
		assertEquals(0, compiler.getErrorCount());
		RuleLoader rl = new RuleLoader();
		
		Rule rule = rl.loadRule(compiler.getCode());
		
		
		Cell c = new Cell(new double[]{0,0});
		Cell[] n = new Cell[2];
		
		n[0] = new Cell(new double[]{0,5});
		n[1] = new Cell(new double[]{0,10});
		
		rule.run(c, n);
		
		assertEquals(6.0, c.getValue(1), 0.0);
	}
	
	@Test
	public void testStdLibFunctionCalls()
	{
		CALCompiler compiler = new CALCompiler();
		compiler.compileFile("Test Data/rules/testSet10.cal");
		assertEquals(0, compiler.getErrorCount());
		RuleLoader rl = new RuleLoader();
		
		Rule rule = rl.loadRule(compiler.getCode());		
		
		Cell c = new Cell(new double[]{0,0});
		Cell[] n = new Cell[5];
		
		n[0] = new Cell(new double[]{0,5});
		n[1] = new Cell(new double[]{0,10});
		n[2] = new Cell(new double[]{0,-1});
		n[3] = new Cell(new double[]{0,-300});
		n[4] = new Cell(new double[]{0,55});
		
		rule.run(c, n);
		
		assertEquals(55.0, c.getValue(1), 0.0);
	}
	
	
	
	@Test
	public void testSet11Test()
	{
		CALCompiler compiler = new CALCompiler();
		compiler.compileFile("Test Data/rules/testSet11.cal");
		assertEquals(0, compiler.getErrorCount());
		RuleLoader rl = new RuleLoader();
		
		Rule rule = rl.loadRule(compiler.getCode());

		
		
		Cell c = new Cell(new double[]{0,0,0});
		Cell[] n = new Cell[5];
		
		n[0] = new Cell(new double[]{0,1,0});
		n[1] = new Cell(new double[]{0,1,0});
		n[2] = new Cell(new double[]{0,0,0});
		n[3] = new Cell(new double[]{0,0,0});
		n[4] = new Cell(new double[]{0,0,0});
		
		for(int i = 0; i < 20 ; i++)
			rule.run(c, n);
		
		assertEquals(1.0, c.getValue(1), 0.0);
		assertEquals(20.0, c.getValue(2), 0.0);
		
		rule.run(c, n);
		assertEquals(0.0, c.getValue(1), 0.0);
		assertEquals(0.0, c.getValue(2), 0.0);

	}
	
	@Test
	public void testMultipleTypeDeclarations()
	{
		CALCompiler compiler = new CALCompiler();
		compiler.compileFile("Test Data/rules/testSet12.cal");
		assertEquals(0, compiler.getErrorCount());
		RuleLoader rl = new RuleLoader();
		
		Rule rule = rl.loadRule(compiler.getCode());
		
		Cell c0 = new Cell(new double[]{0,0});
		Cell c1 = new Cell(new double[]{1,0});
		Cell c2 = new Cell(new double[]{2,0});
		
		//New Cell Test
		assertEquals(0.0, c0.getValue(0), 0.0);
		assertEquals(0.0, c0.getValue(1), 0.0);
		assertEquals(1.0, c1.getValue(0), 0.0);
		assertEquals(0.0, c1.getValue(1), 0.0);
		assertEquals(2.0, c2.getValue(0), 0.0);
		assertEquals(0.0, c2.getValue(1), 0.0);
		
		//Run once
		rule.run(c0, null);
		rule.run(c1, null);
		rule.run(c2, null);
		
		//Post run test
		assertEquals(0.0, c0.getValue(0), 0.0);
		assertEquals(15.0, c0.getValue(1), 0.0);
		assertEquals(1.0, c1.getValue(0), 0.0);
		assertEquals(20.0, c1.getValue(1), 0.0);
		assertEquals(0.0, c2.getValue(0), 0.0);
		assertEquals(30.0, c2.getValue(1), 0.0);
		
		//Run again
		rule.run(c0, null);
		rule.run(c2, null);
		rule.run(c1, null);
		
		assertEquals(0.0, c0.getValue(0), 0.0);
		assertEquals(15.0, c0.getValue(1), 0.0);
		assertEquals(1.0, c1.getValue(0), 0.0);
		assertEquals(20.0, c1.getValue(1), 0.0);
		assertEquals(0.0, c2.getValue(0), 0.0);
		assertEquals(15.0, c2.getValue(1), 0.0);
		
	}
	
	@Test
	public void testTypeNames()
	{
		CALCompiler compiler = new CALCompiler();
		compiler.compileFile("Test Data/rules/testSet15.cal");
		assertEquals(0, compiler.getErrorCount());
		RuleLoader rl = new RuleLoader();
		
		Rule rule = rl.loadRule(compiler.getCode());
		
		Cell c0 = new Cell(new double[]{0,0});
		
		//New Cell Test
		assertEquals(0.0, c0.getValue(0), 0.0);
		assertEquals(0.0, c0.getValue(1), 0.0);
		
		//Run once
		rule.run(c0, null);

		
		//Post run test
		assertEquals(2.0, c0.getValue(0), 0.0);
		assertEquals(0.0, c0.getValue(1), 0.0);

		
		//Run again
		rule.run(c0, null);
		
		
		//Post run test
		assertEquals(1.0, c0.getValue(0), 0.0);
		assertEquals(0.0, c0.getValue(1), 0.0);
		
		
		//Run again
		rule.run(c0, null);
				
				
		//Post run test
		assertEquals(0.0, c0.getValue(0), 0.0);
		assertEquals(0.0, c0.getValue(1), 0.0);	

		
	}
	
	
	@Test
	public void testTypeNameRestrictions()
	{
		CALCompiler compiler = new CALCompiler();
		compiler.compileFile("Test Data/rules/testSet16.cal");
		
		int errors = compiler.getErrorCount();		
		assertEquals(true, errors > 0);
	}
	
	@Test
	public void testLoops()
	{
		CALCompiler compiler = new CALCompiler();
		compiler.compileFile("Test Data/rules/testSet13.cal");
		assertEquals(0, compiler.getErrorCount());
		RuleLoader rl = new RuleLoader();
		
		Rule rule = rl.loadRule(compiler.getCode());
		
		Cell c0 = new Cell(new double[]{0,0});
		rule.run(c0, null);
		
		//Post run test
		assertEquals(0.0, c0.getValue(0), 0.0);
		assertEquals(10.0, c0.getValue(1), 0.0);
	}
	
	@Test
	public void testNStepEngine()
	{
		CALCompiler compiler = new CALCompiler();
		compiler.compileFile("Test Data/rules/testNStepEngine.cal");
		assertEquals(0, compiler.getErrorCount());
		
		RuleLoader rl = new RuleLoader();
		
		Rule rule = rl.loadRule(compiler.getCode());
		
		
		Cell c0 = new Cell(new double[]{0,0});
		Cell c1 = new Cell(new double[]{0,0});
		
		rule.run(c0, null);
		rule.run(c1, null);
		rule.step();		
		assertEquals(0.0, c0.getValue(1), 0.0);
		assertEquals(0.0, c1.getValue(1), 0.0);
		
		
		rule.run(c0, null);
		rule.run(c1, null);
		rule.step();		
		assertEquals(1.0, c0.getValue(1), 0.0);
		assertEquals(1.0, c1.getValue(1), 0.0);
		
		rule.run(c0, null);
		rule.run(c1, null);
		rule.step();		
		assertEquals(2.0, c0.getValue(1), 0.0);
		assertEquals(2.0, c1.getValue(1), 0.0);
		
		rule.run(c0, null);
		rule.run(c1, null);
		rule.step();		
		assertEquals(0.0, c0.getValue(1), 0.0);
		assertEquals(0.0, c1.getValue(1), 0.0);
		
		rule.run(c0, null);
		rule.run(c1, null);
		rule.step();		
		assertEquals(1.0, c0.getValue(1), 0.0);
		assertEquals(1.0, c1.getValue(1), 0.0);
		
		rule.resetStep();
		rule.run(c0, null);
		rule.run(c1, null);
		rule.step();		
		assertEquals(0.0, c0.getValue(1), 0.0);
		assertEquals(0.0, c1.getValue(1), 0.0);
		
		
		rule.setStepForGen(27);
		rule.run(c0, null);
		rule.run(c1, null);
		rule.step();
		assertEquals(0.0, c0.getValue(1), 0.0);
		assertEquals(0.0, c1.getValue(1), 0.0);
		
		rule.setStepForGen(59);
		rule.run(c0, null);
		rule.run(c1, null);
		rule.step();
		assertEquals(2.0, c0.getValue(1), 0.0);
		assertEquals(2.0, c1.getValue(1), 0.0);
	}
	
	@Test
	public void testNStepEngineWithIndependentCode()
	{
		CALCompiler compiler = new CALCompiler();
		compiler.compileFile("Test Data/rules/testNStepEngineWithIndependentCode.cal");
		assertEquals(0, compiler.getErrorCount());
		

		RuleLoader rl = new RuleLoader();		
		Rule rule = rl.loadRule(compiler.getCode());
		
		
		Cell c0 = new Cell(new double[]{0,0,0});
		
		rule.run(c0, null);
		rule.step();		
		assertEquals(0.0, c0.getValue(1), 0.0);
		assertEquals(-1.0, c0.getValue(2), 0.0);
		
		rule.run(c0, null);
		rule.step();		
		assertEquals(1.0, c0.getValue(1), 0.0);
		assertEquals(-1.0, c0.getValue(2), 0.0);
		
		rule.run(c0, null);
		rule.step();		
		assertEquals(2.0, c0.getValue(1), 0.0);
		assertEquals(-1.0, c0.getValue(2), 0.0);
		
		rule.run(c0, null);
		rule.step();		
		assertEquals(0.0, c0.getValue(1), 0.0);
		assertEquals(-1.0, c0.getValue(2), 0.0);
	}
	
	@Test
	public void testFunctionResultDiscarding()
	{
		CALCompiler compiler = new CALCompiler();
		compiler.compileFile("Test Data/rules/testFunctionResultDiscarding.cal");
		assertEquals(0, compiler.getErrorCount());
	}
	
	@Test
	public void testIfElseAdvanced()
	{
		CALCompiler compiler = new CALCompiler();
		
		compiler.compileFile("Test Data/rules/testIfElseAdvanced.cal");		
		assertTrue(compiler.getErrorCount() == 0);
		
		RuleLoader rl = new RuleLoader();
		
		Rule rule = rl.loadRule(compiler.getCode());
		
		
		Cell c0 = new Cell(new double[]{0,0});
		
		rule.run(c0, null);		
		assertEquals(1.0, c0.getValue(1), 0.0);
		
		rule.run(c0, null);		
		assertEquals(2.0, c0.getValue(1), 0.0);
	}
	
	@Test
	public void testArrayCode()
	{
		CALCompiler compiler = new CALCompiler();
		
		compiler.compileFile("Test Data/rules/testArrayCode.cal");		
		assertTrue(compiler.getErrorCount() == 0);
		
		RuleLoader rl = new RuleLoader();		
		Rule rule = rl.loadRule(compiler.getCode());
		
		
		Cell c0 = new Cell(new double[]{0,0,0,0,0,0});
		
		rule.run(c0, null);
		
		assertEquals(5.0, c0.getValue(1), 0.0);
		assertEquals(4.0, c0.getValue(2), 0.0);
		assertEquals(3.0, c0.getValue(3), 0.0);
		assertEquals(99.5, c0.getValue(4), 0.0);
		assertEquals(1.0, c0.getValue(5), 0.0);
	}
	
}
