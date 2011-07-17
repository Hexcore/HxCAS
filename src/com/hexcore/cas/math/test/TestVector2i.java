package com.hexcore.cas.math.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.hexcore.cas.math.Vector2i;

public class TestVector2i
{

	@Test
	public void testVector2i()
	{
		Vector2i point = new Vector2i();
		assertEquals(0, point.x);
		assertEquals(0, point.y);
	}

	@Test
	public void testVector2iIntInt()
	{
		Vector2i point = new Vector2i(7, 9);
		assertEquals(7, point.x);
		assertEquals(9, point.y);
	}

	@Test
	public void testVector2iVector2i()
	{
		Vector2i point = new Vector2i(3, 4);
		Vector2i copy = new Vector2i(point);
		assertEquals(3, copy.x);
		assertEquals(4, copy.y);
	}

	@Test
	public void testGet()
	{
		Vector2i point = new Vector2i(7, 9);
		assertEquals(7, point.get(0));
		assertEquals(9, point.get(1));
	}

	@Test
	public void testEqualsVector2i()
	{
		Vector2i a, b;
		
		a = new Vector2i(7, 9);
		b = new Vector2i(7, 9);
		assertTrue(a.equals(b));
		
		a = new Vector2i(6, 8);
		b = new Vector2i(7, 9);
		assertFalse(a.equals(b));
		
		a = new Vector2i(7, 8);
		b = new Vector2i(7, 9);
		assertFalse(a.equals(b));
		
		a = new Vector2i(6, 9);
		b = new Vector2i(7, 9);
		assertFalse(a.equals(b));
	}
	
	@Test
	public void testSetIntInt()
	{
		Vector2i a = new Vector2i(1, 2);
		a.set(3, 4);
		
		assertEquals(3, a.x);
		assertEquals(4, a.y);	
	}
	
	public void testSetVector2i()
	{
		Vector2i a = new Vector2i(1, 2);
		Vector2i b = new Vector2i(5, 6);
		a.set(b);
		
		assertEquals(5, a.x);
		assertEquals(6, a.y);	
		
		// Verify a copy was made
		b.set(7, 8);
		
		assertEquals(5, a.x);
		assertEquals(6, a.y);		
	}	

	@Test
	public void testAddVector2i()
	{
		Vector2i a = new Vector2i(1, 5);
		Vector2i b = new Vector2i(2, 4);
		Vector2i c = a.add(b);
		
		assertEquals(3, c.x);
		assertEquals(9, c.y);
	}

	@Test
	public void testAddIntInt()
	{
		Vector2i a = new Vector2i(1, 5);
		Vector2i c = a.add(2, 4);
		
		assertEquals(3, c.x);
		assertEquals(9, c.y);
	}

	@Test
	public void testSubtractVector2i()
	{
		Vector2i a = new Vector2i(1, 5);
		Vector2i b = new Vector2i(2, 4);
		Vector2i c = a.subtract(b);
		
		assertEquals(-1, c.x);
		assertEquals(1, c.y);
	}

	@Test
	public void testSubtractIntInt()
	{
		Vector2i a = new Vector2i(1, 5);
		Vector2i c = a.subtract(2, 4);
		
		assertEquals(-1, c.x);
		assertEquals(1, c.y);
	}

	@Test
	public void testToString()
	{
		Vector2i a = new Vector2i(1, 5);
		Vector2i b = new Vector2i(-1, -5);
		
		assertEquals("Vector2i<1, 5>", a.toString());
		assertEquals("Vector2i<-1, -5>", b.toString());
	}
		
	@Test
	public void testIncVector2i()
	{
		Vector2i a = new Vector2i(1, 5);
		Vector2i b = new Vector2i(2, 4);
		a.inc(b);
		
		assertEquals(3, a.x);
		assertEquals(9, a.y);
	}

	@Test
	public void testIncIntInt()
	{
		Vector2i a = new Vector2i(1, 5);
		a.inc(2, 4);
		
		assertEquals(3, a.x);
		assertEquals(9, a.y);
	}

	@Test
	public void testDecVector2i()
	{
		Vector2i a = new Vector2i(1, 5);
		Vector2i b = new Vector2i(2, 4);
		a.dec(b);
		
		assertEquals(-1, a.x);
		assertEquals(1, a.y);
	}

	@Test
	public void testDecIntInt()
	{
		Vector2i a = new Vector2i(1, 5);
		a.dec(2, 4);
		
		assertEquals(-1, a.x);
		assertEquals(1, a.y);
	}
}
