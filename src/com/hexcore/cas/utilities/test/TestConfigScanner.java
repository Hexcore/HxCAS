package com.hexcore.cas.utilities.test;

import junit.framework.TestCase;

import com.hexcore.cas.utilities.ConfigScanner;
import com.hexcore.cas.utilities.ConfigScanner.Symbol;

public class TestConfigScanner extends TestCase
{
	private final static float EPSILON = 0.0001f;
	
	public void test1()
	{
		String		test1 = "10 hello there 6 : 10";
		String[]	test1Ans = new String[] {"10", "hello", "there", "6", ":", "10"};
		
		ConfigScanner scanner = new ConfigScanner();
		scanner.readString(test1);
		
		Symbol 	symbol;
		int		index = 0;
		while ((symbol = scanner.getSymbol()) != null)
		{
			assertEquals(test1Ans[index++], symbol.text);
		}
		
		symbol = scanner.getSymbol();
		assertSame(null, symbol);
	}
		
	public void test2()
	{
		String		test1 = "string 10 5.6";

		ConfigScanner scanner = new ConfigScanner();
		scanner.readString(test1);
		
		Symbol 	symbol;
		
		symbol = scanner.getSymbol();
		assertEquals("string", symbol.text);
		assertEquals(Symbol.Type.STRING, symbol.type);
		
		symbol = scanner.getSymbol();
		assertEquals("10", symbol.text);
		assertEquals(Symbol.Type.INTEGER, symbol.type);
		assertEquals(10, symbol.integer);
		
		symbol = scanner.getSymbol();
		assertEquals("5.6", symbol.text);
		assertEquals(5.6, symbol.decimal, EPSILON);
		
		symbol = scanner.getSymbol();
		assertSame(null, symbol);	
	}
	
	
	public void test3()
	{
		String		test1 = "string{10}5.6;{}10.";

		ConfigScanner scanner = new ConfigScanner();
		scanner.addSymbol(';');
		scanner.addSymbols(new char[] {'{', '}'});
		scanner.readString(test1);
		
		Symbol 	symbol;
		
		symbol = scanner.getSymbol();
		assertEquals("string", symbol.text);
		assertEquals(Symbol.Type.STRING, symbol.type);
		
		symbol = scanner.getSymbol();
		assertEquals("{", symbol.text);
		assertEquals(Symbol.Type.STRING, symbol.type);		
		
		symbol = scanner.getSymbol();
		assertEquals("10", symbol.text);
		assertEquals(Symbol.Type.INTEGER, symbol.type);
		assertEquals(10, symbol.integer);
		
		symbol = scanner.getSymbol();
		assertEquals("}", symbol.text);
		assertEquals(Symbol.Type.STRING, symbol.type);
		
		symbol = scanner.getSymbol();
		assertEquals("5.6", symbol.text);
		assertEquals(5.6, symbol.decimal, EPSILON);
		
		symbol = scanner.getSymbol();
		assertEquals(";", symbol.text);
		assertEquals(Symbol.Type.STRING, symbol.type);
		
		symbol = scanner.getSymbol();
		assertEquals("{", symbol.text);
		assertEquals(Symbol.Type.STRING, symbol.type);	
		
		symbol = scanner.getSymbol();
		assertEquals("}", symbol.text);
		assertEquals(Symbol.Type.STRING, symbol.type);
		
		symbol = scanner.getSymbol();
		assertEquals("10.", symbol.text);
		assertEquals(10.0, symbol.decimal, EPSILON);
		
		symbol = scanner.getSymbol();
		assertSame(null, symbol);	
	}
	
	
	public void test4()
	{
		String		test1 = "string\n10  5\n 5.6";

		ConfigScanner scanner = new ConfigScanner();
		scanner.readString(test1);
		
		Symbol 	symbol;
		
		symbol = scanner.getSymbol();		
		assertEquals("string", symbol.text);
		assertEquals(Symbol.Type.STRING, symbol.type);
		assertEquals(1, symbol.line);
		
		symbol = scanner.getSymbol();
		assertEquals("10", symbol.text);
		assertEquals(Symbol.Type.INTEGER, symbol.type);
		assertEquals(10, symbol.integer);
		assertEquals(2, symbol.line);
		
		symbol = scanner.getSymbol();
		assertEquals("5", symbol.text);
		assertEquals(Symbol.Type.INTEGER, symbol.type);
		assertEquals(5, symbol.integer);	
		assertEquals(2, symbol.line);
		
		symbol = scanner.getSymbol();
		assertEquals("5.6", symbol.text);
		assertEquals(5.6, symbol.decimal, EPSILON);
		assertEquals(3, symbol.line);
		
		symbol = scanner.getSymbol();
		assertSame(null, symbol);	
	}
	
	
	public void test5()
	{
		String		test1 = "string\n\"Hello\nWorld!\"\n 5.6\n\"\"";

		ConfigScanner scanner = new ConfigScanner();
		scanner.readString(test1);
		
		Symbol 	symbol;
		
		symbol = scanner.getSymbol();		
		assertEquals("string", symbol.text);
		assertEquals(Symbol.Type.STRING, symbol.type);
		assertEquals(1, symbol.line);
		
		symbol = scanner.getSymbol();
		assertEquals("Hello\nWorld!", symbol.text);
		assertEquals(Symbol.Type.STRING, symbol.type);
		assertEquals(2, symbol.line);
		
		symbol = scanner.getSymbol();
		assertEquals("5.6", symbol.text);
		assertEquals(5.6, symbol.decimal, EPSILON);
		assertEquals(4, symbol.line);
		
		symbol = scanner.getSymbol();
		assertEquals("", symbol.text);
		assertEquals(Symbol.Type.STRING, symbol.type);
		assertEquals(5, symbol.line);		
		
		symbol = scanner.getSymbol();
		assertSame(null, symbol);	
	}
	
	
	public void test6()
	{
		ConfigScanner scanner = new ConfigScanner();
		scanner.readFile("Test Data/ConfigScanner/test.txt");
		
		assertTrue(scanner.isValid());
		
		Symbol 	symbol;
		
		symbol = scanner.getSymbol();
		assertEquals("string", symbol.text);
		assertEquals(Symbol.Type.STRING, symbol.type);
		
		symbol = scanner.getSymbol();
		assertEquals("10", symbol.text);
		assertEquals(Symbol.Type.INTEGER, symbol.type);
		assertEquals(10, symbol.integer);
		
		symbol = scanner.getSymbol();
		assertEquals("5.6", symbol.text);
		assertEquals(5.6, symbol.decimal, EPSILON);
		
		symbol = scanner.getSymbol();
		assertSame(null, symbol);	
	}
	
	
	public void test7()
	{
		ConfigScanner scanner = new ConfigScanner();
		scanner.readFile("Test Data/ConfigScanner/notfound.invalid");
		
		assertFalse(scanner.isValid());
	}
}
