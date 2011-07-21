package com.hexcore.cas.test;

import junit.framework.TestCase;


public class TestPrivateAccessor extends TestCase
{
	private class PrivateTestClass
	{
		private String privateString1;
		private String privateString2;
		
		public PrivateTestClass()
		{
			privateString1 = "This is String 1";
			privateString2 = "This is String 2";
		}
		
		private int getPrivateNumber(int num)
		{
			return num;
		}
	}
	
	
	public void testPrivateAccessor()
	{
		PrivateAccessor pa = new PrivateAccessor(new PrivateTestClass());
		
		assertTrue(pa.getFieldValue("privateString1").equals("This is String 1"));
		assertTrue(pa.getFieldValue("privateString2").equals("This is String 2"));
		assertTrue(pa.invokeMethod("getPrivateNumber", 10).equals(10));
	}
}
