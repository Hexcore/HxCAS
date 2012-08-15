package com.hexcore.cas.math.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.hexcore.cas.math.Vector2f;

public class TestVector2f
{
	static final float epsilon = 0.0001f;
	
	@Test
	public void testVector2f()
	{
		Vector2f point = new Vector2f();
		assertEquals(0.0f, point.x, epsilon);
		assertEquals(0.0f, point.y, epsilon);
	}

	
	@Test
	public void testVector2fIntInt()
	{
		Vector2f point = new Vector2f(7.7f, 9.9f);
		assertEquals(7.7f, point.x, epsilon);
		assertEquals(9.9f, point.y, epsilon);
	}

	
	@Test
	public void testVector2fVector2f()
	{
		Vector2f point = new Vector2f(3.3f, 4.4f);
		Vector2f copy = new Vector2f(point);
		assertEquals(3.3f, copy.x, epsilon);
		assertEquals(4.4f, copy.y, epsilon);
	}
	
	
	@Test
	public void testGet()
	{
		Vector2f point = new Vector2f(7.7f, 9.9f);
		assertEquals(7.7f, point.get(0), epsilon);
		assertEquals(9.9f, point.get(1), epsilon);
	}

	
	@Test
	public void testEqualsVector2f()
	{
		Vector2f a, b;
		
		a = new Vector2f(7.7f, 9.9f);
		b = new Vector2f(7.7f, 9.9f);
		assertTrue(a.equals(b));
		
		a = new Vector2f(6.6f, 8.8f);
		b = new Vector2f(7.7f, 9.9f);
		assertFalse(a.equals(b));
		
		a = new Vector2f(7.7f, 8.8f);
		b = new Vector2f(7.7f, 9.9f);
		assertFalse(a.equals(b));
		
		a = new Vector2f(6.6f, 9.9f);
		b = new Vector2f(7.7f, 9.9f);
		assertFalse(a.equals(b));
	}
	
	
	@Test
	public void testSetIntInt()
	{
		Vector2f a = new Vector2f(1.1f, 2.2f);
		a.set(3.3f, 4.4f);
		
		assertEquals(3.3f, a.x, epsilon);
		assertEquals(4.4f, a.y, epsilon);	
	}
	
	
	@Test
	public void testSetVector2f()
	{
		Vector2f a = new Vector2f(1.1f, 2.2f);
		Vector2f b = new Vector2f(5.5f, 6.6f);
		a.set(b);
		
		assertEquals(5.5f, a.x, epsilon);
		assertEquals(6.6f, a.y, epsilon);	
		
		// Verify a copy was made
		b.set(7.7f, 8.8f);
		
		assertEquals(5.5f, a.x, epsilon);
		assertEquals(6.6f, a.y, epsilon);		
	}	

	
	@Test
	public void testAddVector2f()
	{
		Vector2f a = new Vector2f(1.1f, 5.5f);
		Vector2f b = new Vector2f(2.2f, 4.4f);
		Vector2f c = a.add(b);
		
		assertEquals(3.3f, c.x, epsilon);
		assertEquals(9.9f, c.y, epsilon);
	}

	
	@Test
	public void testAddIntInt()
	{
		Vector2f a = new Vector2f(1.1f, 5.5f);
		Vector2f c = a.add(2.2f, 4.4f);
		
		assertEquals(3.3f, c.x, epsilon);
		assertEquals(9.9f, c.y, epsilon);
	}

	
	@Test
	public void testSubtractVector2f()
	{
		Vector2f a = new Vector2f(1.1f, 5.5f);
		Vector2f b = new Vector2f(2.2f, 4.4f);
		Vector2f c = a.subtract(b);
		
		assertEquals(-1.1f, c.x, epsilon);
		assertEquals(1.1f, c.y, epsilon);
	}

	
	@Test
	public void testSubtractIntInt()
	{
		Vector2f a = new Vector2f(1.1f, 5.5f);
		Vector2f c = a.subtract(2.2f, 4.4f);
		
		assertEquals(-1.1f, c.x, epsilon);
		assertEquals(1.1f, c.y, epsilon);
	}

	
	@Test
	public void testToString()
	{
		Vector2f a = new Vector2f(1.1f, 5.5f);
		Vector2f b = new Vector2f(-1.1f, -5.5f);
		
		assertEquals("Vector2f<1.1, 5.5>", a.toString());
		assertEquals("Vector2f<-1.1, -5.5>", b.toString());
	}
		
	
	@Test
	public void testIncVector2f()
	{
		Vector2f a = new Vector2f(1.1f, 5.5f);
		Vector2f b = new Vector2f(2.2f, 4.4f);
		a.inc(b);
		
		assertEquals(3.3f, a.x, epsilon);
		assertEquals(9.9f, a.y, epsilon);
	}

	
	@Test
	public void testIncIntInt()
	{
		Vector2f a = new Vector2f(1.1f, 5.5f);
		a.inc(2.2f, 4.4f);
		
		assertEquals(3.3f, a.x, epsilon);
		assertEquals(9.9f, a.y, epsilon);
	}

	
	@Test
	public void testDecVector2f()
	{
		Vector2f a = new Vector2f(1.1f, 5.5f);
		Vector2f b = new Vector2f(2.2f, 4.4f);
		a.dec(b);
		
		assertEquals(-1.1f, a.x, epsilon);
		assertEquals(1.1f, a.y, epsilon);
	}

	
	@Test
	public void testDecIntInt()
	{
		Vector2f a = new Vector2f(1.1f, 5.5f);
		a.dec(2.2f, 4.4f);
		
		assertEquals(-1.1f, a.x, epsilon);
		assertEquals(1.1f, a.y, epsilon);
	}
	
	
	@Test
	public void testNormalize()
	{
		Vector2f a = new Vector2f(1.1f, 5.5f);
		a.normalise();
		
		assertEquals(0.196116135f, a.x, epsilon);
		assertEquals(0.980580676f, a.y, epsilon);
	}	
	
	
	@Test
	public void testGetNormalized()
	{
		Vector2f a = new Vector2f(1.1f, 5.5f);
		Vector2f b = a.getNormalised();
		
		assertEquals(0.196116135f, b.x, epsilon);
		assertEquals(0.980580676f, b.y, epsilon);
	}
	
	
	@Test
	public void testGetPerpendicular()
	{
		Vector2f a = new Vector2f(1.1f, 5.5f);
		Vector2f b = a.getPerpendicular();
	
		assertEquals(5.5f, b.x, epsilon);
		assertEquals(-1.1f, b.y, epsilon);
		assertEquals(0.0f, a.x*b.x + a.y*b.y, epsilon);
	}	
}
