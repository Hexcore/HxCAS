package com.hexcore.cas.rulesystems.test;

import java.util.Stack;

import junit.framework.TestCase;

import com.hexcore.cas.rulesystems.CALCompiler;
import com.hexcore.cas.test.PrivateAccessor;

@SuppressWarnings("unchecked")
public class TestVM extends TestCase
{
	CALCompiler 			vm;
	PrivateAccessor		pa;
	
	@Override
	public void setUp()
	{
		vm = new CALCompiler();
		pa = new PrivateAccessor(vm);
	}
	
	public void testLDC()
	{
		pa.setFieldValue("IR", "LDC 5");
		pa.invokeMethod("execute", (Object[])null);
		
		Stack<Integer> stack = (Stack<Integer>)pa.getFieldValue("oStack");
		Integer SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.size() == 1);
		assertTrue(stack.pop().equals(new Integer(5)));
		assertTrue(SP == 1);
	}
	
	public void testLDA()
	{
		pa.setFieldValue("IR", "LDA 0");		
		pa.invokeMethod("execute", (Object[])null);
		pa.setFieldValue("IR", "LDA 1");		
		pa.invokeMethod("execute", (Object[])null);
		
		
		Stack<Integer> stack = (Stack<Integer>)pa.getFieldValue("oStack");
		Integer SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.size() == 2);
		assertTrue(stack.pop().equals(new Integer(1)));
		assertTrue(stack.pop().equals(new Integer(0)));
		assertTrue(SP == 2);
	}
	
	public void testLDV()
	{
		Stack<Integer> cookedStack = new Stack<Integer>();
		cookedStack.push(123);
		cookedStack.push(198);
		cookedStack.push(456);
		cookedStack.push(1);		
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 4);
		
		
		pa.setFieldValue("IR", "LDV");
		pa.invokeMethod("execute", (Object[])null);
		
		Stack<Integer> stack = (Stack<Integer>)pa.getFieldValue("oStack");
		Integer SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.pop().equals(new Integer(198)));
		assertTrue(stack.pop().equals(new Integer(456)));
		assertTrue(stack.pop().equals(new Integer(198)));
		assertTrue(stack.pop().equals(new Integer(123)));
		assertTrue(SP == 4);
	}
	
	public void testSTO()
	{
		Stack<Integer> cookedStack = new Stack<Integer>();
		cookedStack.push(12);
		cookedStack.push(5);
		cookedStack.push(1);
		cookedStack.push(99);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 4);
		
		pa.setFieldValue("IR", "STO");
		pa.invokeMethod("execute", (Object[])null);
		
		Stack<Integer> stack = (Stack<Integer>)pa.getFieldValue("oStack");
		Integer SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.pop().equals(new Integer(99)));
		assertTrue(stack.pop().equals(new Integer(12)));
		assertTrue(SP == 2);		
	}
	
	public void testDSP()
	{
		pa.setFieldValue("IR", "DSP 5");
		pa.invokeMethod("execute", (Object[])null);
		
		Stack<Integer> stack = (Stack<Integer>)pa.getFieldValue("oStack");
		Integer SP = (Integer)pa.getFieldValue("SP");
		
		for(int i = 0; i < 5; i++)
			assertTrue(stack.pop().equals(new Integer(0)));
		
		assertTrue(SP == 5);
	}
	
	public void testADD()
	{
		Stack<Integer> cookedStack = new Stack<Integer>();
		cookedStack.push(10);
		cookedStack.push(5);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 2);
		
		pa.setFieldValue("IR", "ADD");
		pa.invokeMethod("execute", (Object[])null);
		
		
		Stack<Integer> stack = (Stack<Integer>)pa.getFieldValue("oStack");
		Integer SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.pop().equals(new Integer(15)));
		assertTrue(SP == 1);
	}
	
	public void testSUB()
	{
		Stack<Integer> cookedStack = new Stack<Integer>();
		cookedStack.push(10);
		cookedStack.push(5);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 2);
		
		pa.setFieldValue("IR", "SUB");
		pa.invokeMethod("execute", (Object[])null);
		
		
		Stack<Integer> stack = (Stack<Integer>)pa.getFieldValue("oStack");
		Integer SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.pop().equals(new Integer(5)));
		assertTrue(SP == 1);
	}
	
	public void testMUL()
	{
		Stack<Integer> cookedStack = new Stack<Integer>();
		cookedStack.push(10);
		cookedStack.push(5);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 2);
		
		pa.setFieldValue("IR", "MUL");
		pa.invokeMethod("execute", (Object[])null);
		
		
		Stack<Integer> stack = (Stack<Integer>)pa.getFieldValue("oStack");
		Integer SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.pop().equals(new Integer(50)));
		assertTrue(SP == 1);
	}
	
	public void testDIV()
	{
		Stack<Integer> cookedStack = new Stack<Integer>();
		cookedStack.push(10);
		cookedStack.push(5);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 2);
		
		pa.setFieldValue("IR", "DIV");
		pa.invokeMethod("execute", (Object[])null);
		
		
		Stack<Integer> stack = (Stack<Integer>)pa.getFieldValue("oStack");
		Integer SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.pop().equals(new Integer(2)));
		assertTrue(SP == 1);
	}
	
	public void testREM()
	{
		Stack<Integer> cookedStack = new Stack<Integer>();
		cookedStack.push(10);
		cookedStack.push(5);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 2);
		
		pa.setFieldValue("IR", "REM");
		pa.invokeMethod("execute", (Object[])null);
		
		
		Stack<Integer> stack = (Stack<Integer>)pa.getFieldValue("oStack");
		Integer SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.pop().equals(new Integer(0)));
		assertTrue(SP == 1);
	}
	
	public void testCEQ()
	{
		Stack<Integer> cookedStack = new Stack<Integer>();
		
		//Equals test
		cookedStack.push(2);
		cookedStack.push(2);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 2);
		
		pa.setFieldValue("IR", "CEQ");
		pa.invokeMethod("execute", (Object[])null);
		
		Stack<Integer> stack = (Stack<Integer>)pa.getFieldValue("oStack");
		Integer SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.pop().equals(new Integer(1)));
		assertTrue(SP == 1);
		
		//Less Test
		cookedStack.clear();
		cookedStack.push(1);
		cookedStack.push(2);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 3);
		
		pa.setFieldValue("IR", "CEQ");
		pa.invokeMethod("execute", (Object[])null);
		
		stack = (Stack<Integer>)pa.getFieldValue("oStack");
		SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.pop().equals(new Integer(0)));
		assertTrue(SP == 2);
		
		//Greater Test
		cookedStack.clear();
		cookedStack.push(2);
		cookedStack.push(1);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 4);
		
		pa.setFieldValue("IR", "CEQ");
		pa.invokeMethod("execute", (Object[])null);
		
		stack = (Stack<Integer>)pa.getFieldValue("oStack");
		SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.pop().equals(new Integer(0)));
		assertTrue(SP == 3);		
	}
	
	public void testCNE()
	{
		Stack<Integer> cookedStack = new Stack<Integer>();
		
		//Equals test
		cookedStack.push(2);
		cookedStack.push(2);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 2);
		
		pa.setFieldValue("IR", "CNE");
		pa.invokeMethod("execute", (Object[])null);
		
		Stack<Integer> stack = (Stack<Integer>)pa.getFieldValue("oStack");
		Integer SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.pop().equals(new Integer(0)));
		assertTrue(SP == 1);
		
		//Less Test
		cookedStack.clear();
		cookedStack.push(1);
		cookedStack.push(2);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 3);
		
		pa.setFieldValue("IR", "CNE");
		pa.invokeMethod("execute", (Object[])null);
		
		stack = (Stack<Integer>)pa.getFieldValue("oStack");
		SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.pop().equals(new Integer(1)));
		assertTrue(SP == 2);
		
		//Greater Test
		cookedStack.clear();
		cookedStack.push(2);
		cookedStack.push(1);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 4);
		
		pa.setFieldValue("IR", "CNE");
		pa.invokeMethod("execute", (Object[])null);
		
		stack = (Stack<Integer>)pa.getFieldValue("oStack");
		SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.pop().equals(new Integer(1)));
		assertTrue(SP == 3);		
	}
	
	public void testCGT()
	{
		Stack<Integer> cookedStack = new Stack<Integer>();
		
		//Equals test
		cookedStack.push(2);
		cookedStack.push(2);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 2);
		
		pa.setFieldValue("IR", "CGT");
		pa.invokeMethod("execute", (Object[])null);
		
		Stack<Integer> stack = (Stack<Integer>)pa.getFieldValue("oStack");
		Integer SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.pop().equals(new Integer(0)));
		assertTrue(SP == 1);
		
		//Less Test
		cookedStack.clear();
		cookedStack.push(1);
		cookedStack.push(2);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 3);
		
		pa.setFieldValue("IR", "CGT");
		pa.invokeMethod("execute", (Object[])null);
		
		stack = (Stack<Integer>)pa.getFieldValue("oStack");
		SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.pop().equals(new Integer(0)));
		assertTrue(SP == 2);
		
		//Greater Test
		cookedStack.clear();
		cookedStack.push(2);
		cookedStack.push(1);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 4);
		
		pa.setFieldValue("IR", "CGT");
		pa.invokeMethod("execute", (Object[])null);
		
		stack = (Stack<Integer>)pa.getFieldValue("oStack");
		SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.pop().equals(new Integer(1)));
		assertTrue(SP == 3);		
	}
	
	public void testCLT()
	{
		Stack<Integer> cookedStack = new Stack<Integer>();
		
		//Equals test
		cookedStack.push(2);
		cookedStack.push(2);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 2);
		
		pa.setFieldValue("IR", "CLT");
		pa.invokeMethod("execute", (Object[])null);
		
		Stack<Integer> stack = (Stack<Integer>)pa.getFieldValue("oStack");
		Integer SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.pop().equals(new Integer(0)));
		assertTrue(SP == 1);
		
		//Less Test
		cookedStack.clear();
		cookedStack.push(1);
		cookedStack.push(2);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 3);
		
		pa.setFieldValue("IR", "CLT");
		pa.invokeMethod("execute", (Object[])null);
		
		stack = (Stack<Integer>)pa.getFieldValue("oStack");
		SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.pop().equals(new Integer(1)));
		assertTrue(SP == 2);
		
		//Greater Test
		cookedStack.clear();
		cookedStack.push(2);
		cookedStack.push(1);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 4);
		
		pa.setFieldValue("IR", "CLT");
		pa.invokeMethod("execute", (Object[])null);
		
		stack = (Stack<Integer>)pa.getFieldValue("oStack");
		SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.pop().equals(new Integer(0)));
		assertTrue(SP == 3);		
	}
	
	public void testCLE()
	{
		Stack<Integer> cookedStack = new Stack<Integer>();
		
		//Equals test
		cookedStack.push(2);
		cookedStack.push(2);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 2);
		
		pa.setFieldValue("IR", "CLE");
		pa.invokeMethod("execute", (Object[])null);
		
		Stack<Integer> stack = (Stack<Integer>)pa.getFieldValue("oStack");
		Integer SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.pop().equals(new Integer(1)));
		assertTrue(SP == 1);
		
		//Less Test
		cookedStack.clear();
		cookedStack.push(1);
		cookedStack.push(2);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 3);
		
		pa.setFieldValue("IR", "CLE");
		pa.invokeMethod("execute", (Object[])null);
		
		stack = (Stack<Integer>)pa.getFieldValue("oStack");
		SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.pop().equals(new Integer(1)));
		assertTrue(SP == 2);
		
		//Greater Test
		cookedStack.clear();
		cookedStack.push(2);
		cookedStack.push(1);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 4);
		
		pa.setFieldValue("IR", "CLE");
		pa.invokeMethod("execute", (Object[])null);
		
		stack = (Stack<Integer>)pa.getFieldValue("oStack");
		SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.pop().equals(new Integer(0)));
		assertTrue(SP == 3);		
	}
	
	public void testCGE()
	{
		Stack<Integer> cookedStack = new Stack<Integer>();
		
		//Equals test
		cookedStack.push(2);
		cookedStack.push(2);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 2);
		
		pa.setFieldValue("IR", "CGE");
		pa.invokeMethod("execute", (Object[])null);
		
		Stack<Integer> stack = (Stack<Integer>)pa.getFieldValue("oStack");
		Integer SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.pop().equals(new Integer(1)));
		assertTrue(SP == 1);
		
		//Less Test
		cookedStack.clear();
		cookedStack.push(1);
		cookedStack.push(2);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 3);
		
		pa.setFieldValue("IR", "CGE");
		pa.invokeMethod("execute", (Object[])null);
		
		stack = (Stack<Integer>)pa.getFieldValue("oStack");
		SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.pop().equals(new Integer(0)));
		assertTrue(SP == 2);
		
		//Greater Test
		cookedStack.clear();
		cookedStack.push(2);
		cookedStack.push(1);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 4);
		
		pa.setFieldValue("IR", "CGE");
		pa.invokeMethod("execute", (Object[])null);
		
		stack = (Stack<Integer>)pa.getFieldValue("oStack");
		SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.pop().equals(new Integer(1)));
		assertTrue(SP == 3);		
	}
	
	public void testAND()
	{
		Stack<Integer> cookedStack = new Stack<Integer>();		

		cookedStack.push(3);
		cookedStack.push(6);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 2);
		
		pa.setFieldValue("IR", "AND");
		pa.invokeMethod("execute", (Object[])null);
		
		Stack<Integer> stack = (Stack<Integer>)pa.getFieldValue("oStack");
		Integer SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.pop().equals(new Integer(2)));
		assertTrue(SP == 1);
	}
	
	public void testOR()
	{
		Stack<Integer> cookedStack = new Stack<Integer>();		

		cookedStack.push(3);
		cookedStack.push(6);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 2);
		
		pa.setFieldValue("IR", "OR");
		pa.invokeMethod("execute", (Object[])null);
		
		Stack<Integer> stack = (Stack<Integer>)pa.getFieldValue("oStack");
		Integer SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.pop().equals(new Integer(7)));
		assertTrue(SP == 1);
	}
	
	public void testNOT()
	{
		Stack<Integer> cookedStack = new Stack<Integer>();		

		cookedStack.push(3);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 1);
		
		pa.setFieldValue("IR", "NOT");
		pa.invokeMethod("execute", (Object[])null);
		
		Stack<Integer> stack = (Stack<Integer>)pa.getFieldValue("oStack");
		Integer SP = (Integer)pa.getFieldValue("SP");
		
		assertTrue(stack.pop().equals(new Integer(0)));
		assertTrue(SP == 1);
		
		
		cookedStack.push(0);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 1);
		pa.setFieldValue("IR", "NOT");
		pa.invokeMethod("execute", (Object[])null);
		stack = (Stack<Integer>)pa.getFieldValue("oStack");
		SP = (Integer)pa.getFieldValue("SP");
		assertTrue(stack.pop().equals(new Integer(1)));
		assertTrue(SP == 1);
	}
	
	public void testBRN()
	{		
		pa.setFieldValue("IR", "BRN 23");
		pa.invokeMethod("execute", (Object[])null);
		
		Integer PC = (Integer)pa.getFieldValue("PC");
		assertTrue(PC == 23);
	}
	
	public void testBZE()
	{
		Stack<Integer> cookedStack = new Stack<Integer>();		

		cookedStack.push(3);
		cookedStack.push(0);
		pa.setFieldValue("oStack", cookedStack);
		pa.setFieldValue("SP", 2);		
		
		pa.setFieldValue("IR", "BZE 23");
		pa.invokeMethod("execute", (Object[])null);
		
		
		Stack<Integer> stack = (Stack<Integer>)pa.getFieldValue("oStack");
		Integer SP = (Integer)pa.getFieldValue("SP");
		Integer PC = (Integer)pa.getFieldValue("PC");		
		
		assertTrue(PC == 23);
		assertTrue(stack.peek().equals(3));
		assertTrue(SP == 1);
		
		pa.setFieldValue("IR", "BZE 45");
		pa.invokeMethod("execute", (Object[])null);
		
		assertTrue(PC == 23);
		assertTrue(stack.empty());
		assertTrue(SP == 1);	
	}
}
