package com.hexcore.cas.rulesystems.test;

import com.hexcore.cas.rulesystems.StdLib;

import junit.framework.TestCase;

public class TestStdLib extends TestCase
{
	public void testMax()
	{
		double[] vals = {3, -2.47, 0, 1, 4};
		
		double result = StdLib.max(vals);
		assertEquals(4.0, result);
	}
	
	public void testMin()
	{
		double[] vals = {3, -2.47, 0, 1, 4};
		
		double result = StdLib.min(vals);
		assertEquals(-2.47, result);
	}
	
	public void testLog()
	{
		double ident = StdLib.log(10.0);
		assertEquals(1.0, ident);
		
		double result = StdLib.log(122);
		assertEquals(2.0863598306747482290994874084818, result);
	}
	
	public void testLn()
	{
		double ident = StdLib.ln(Math.E);
		assertEquals(1.0, ident);
		
		double result = StdLib.ln(122);
		assertEquals(4.8040210447332565581686212248838, result);
	}
	
	public void testSum()
	{
		double[] vals = {3, -2.47, 0, 1, 4};
		
		double result = StdLib.sum(vals);
		assertEquals(5.53, result, 0.000000000000001);
	}
	
	public void testCount()
	{
		double[] vals = {3, -2.47, 0, 1, 4};
		
		int result = StdLib.count(vals);
		assertEquals(5, result);
		
		result = StdLib.count(new double[]{});
		assertEquals(0, result);
	}
}
