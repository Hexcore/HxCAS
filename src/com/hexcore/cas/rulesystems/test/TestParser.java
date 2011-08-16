package com.hexcore.cas.rulesystems.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import junit.framework.TestCase;

import org.junit.Test;

import com.hexcore.cas.rulesystems.HexcoreVM;

public class TestParser extends TestCase
{
	private ByteArrayOutputStream errStream = null;
	boolean streamRestored = false;
	
	public void testPreconditions()
	{
		File in = new File("Test Data/testRules.cal");
		assertTrue(in.exists());
	}
	
	@Override
	public void setUp()
	{
		errStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(errStream));
	}
	
	public void testParsing()
	{
		HexcoreVM vm = new HexcoreVM();
		vm.loadRules("Test Data/testRules.cal");
		String out = errStream.toString().trim();
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
		streamRestored = true;
		System.out.print(out);
		assertTrue(out, out.equals("Parsed correctly"));
	}
	
	public void tearDown()
	{
		if(!streamRestored)
			System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
	}
	
	
}
