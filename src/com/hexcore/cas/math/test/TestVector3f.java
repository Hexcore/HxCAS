package com.hexcore.cas.math.test;

import static org.junit.Assert.*;

import java.nio.FloatBuffer;

import org.junit.Test;

import com.hexcore.cas.math.Vector2f;
import com.hexcore.cas.math.Vector3f;

public class TestVector3f
{
	static final float epsilon = 0.0001f;
	
	@Test
	public void testVector3f()
	{
		Vector3f point = new Vector3f();
		assertEquals(0.0f, point.x, epsilon);
		assertEquals(0.0f, point.y, epsilon);
	}

	
	@Test
	public void testVector3fFloatFloatFloat()
	{
		Vector3f point = new Vector3f(7.7f, 9.9f, 13.13f);
		assertEquals(7.7f, point.x, epsilon);
		assertEquals(9.9f, point.y, epsilon);
		assertEquals(13.13f, point.z, epsilon);
	}
	
	
	@Test
	public void testVector3fVector2fFloat()
	{
		Vector2f point = new Vector2f(3.3f, 4.4f);
		Vector3f copy = new Vector3f(point, 5.5f);
		assertEquals(3.3f, copy.x, epsilon);
		assertEquals(4.4f, copy.y, epsilon);
		assertEquals(5.5f, copy.z, epsilon);
	}	

	
	@Test
	public void testVector3fVector3f()
	{
		Vector3f point = new Vector3f(3.3f, 4.4f, 5.5f);
		Vector3f copy = new Vector3f(point);
		assertEquals(3.3f, copy.x, epsilon);
		assertEquals(4.4f, copy.y, epsilon);
		assertEquals(5.5f, copy.z, epsilon);
	}

	
	@Test
	public void testGet()
	{
		Vector3f point = new Vector3f(7.7f, 9.9f, 13.13f);
		assertEquals(7.7f, point.get(0), epsilon);
		assertEquals(9.9f, point.get(1), epsilon);
		assertEquals(13.13f, point.get(2), epsilon);
	}
	
	
	@Test
	public void testEqualsVector3f()
	{
		Vector3f a, b;
		
		a = new Vector3f(7.7f, 9.9f, 11.11f);
		b = new Vector3f(7.7f, 9.9f, 11.11f);
		assertTrue(a.equals(b));
		
		a = new Vector3f(6.6f, 8.8f, 7.7f);
		b = new Vector3f(7.7f, 9.9f, 8.8f);
		assertFalse(a.equals(b));
		
		a = new Vector3f(7.7f, 8.8f, 7.7f);
		b = new Vector3f(7.7f, 9.9f, 7.7f);
		assertFalse(a.equals(b));
		
		a = new Vector3f(6.6f, 9.9f, 9.9f);
		b = new Vector3f(7.7f, 9.9f, 9.9f);
		assertFalse(a.equals(b));
		
		a = new Vector3f(7.7f, 9.9f, 9.9f);
		b = new Vector3f(7.7f, 9.9f, 11.11f);
		assertFalse(a.equals(b));	
	}
	
	
	@Test
	public void testSetFloatFloatFloat()
	{
		Vector3f a = new Vector3f(1.1f, 2.2f, 4.4f);
		a.set(3.3f, 4.4f, 6.6f);
		
		assertEquals(3.3f, a.x, epsilon);
		assertEquals(4.4f, a.y, epsilon);
		assertEquals(6.6f, a.z, epsilon);
	}
	
	
	@Test
	public void testSetVector2fFloat()
	{
		Vector3f a = new Vector3f(1.1f, 2.2f, 4.4f);
		a.set(new Vector2f(3.3f, 4.4f), 6.6f);
		
		assertEquals(3.3f, a.x, epsilon);
		assertEquals(4.4f, a.y, epsilon);
		assertEquals(6.6f, a.z, epsilon);
	}	
	
	
	@Test
	public void testSetVector3f()
	{
		Vector3f a = new Vector3f(1.1f, 2.2f, 3.3f);
		Vector3f b = new Vector3f(5.5f, 6.6f, 7.7f);
		a.set(b);
		
		assertEquals(5.5f, a.x, epsilon);
		assertEquals(6.6f, a.y, epsilon);	
		assertEquals(7.7f, a.z, epsilon);
		
		// Verify a copy was made
		b.set(7.7f, 8.8f, 9.9f);
		
		assertEquals(5.5f, a.x, epsilon);
		assertEquals(6.6f, a.y, epsilon);
		assertEquals(7.7f, a.z, epsilon);
	}	

	
	@Test
	public void testAddVector3f()
	{
		Vector3f a = new Vector3f(1.1f, 5.5f, 10.0f);
		Vector3f b = new Vector3f(2.2f, 4.4f, 6.6f);
		Vector3f c = a.add(b);
		
		assertEquals(3.3f, c.x, epsilon);
		assertEquals(9.9f, c.y, epsilon);
		assertEquals(16.6f, c.z, epsilon);
	}

	
	@Test
	public void testAddFloatFloatFloat()
	{
		Vector3f a = new Vector3f(1.1f, 5.5f, 10.0f);
		Vector3f c = a.add(2.2f, 4.4f, 6.6f);
		
		assertEquals(3.3f, c.x, epsilon);
		assertEquals(9.9f, c.y, epsilon);
		assertEquals(16.6f, c.z, epsilon);
	}

	
	@Test
	public void testSubtractVector3f()
	{
		Vector3f a = new Vector3f(1.1f, 5.5f, 10.0f);
		Vector3f b = new Vector3f(2.2f, 4.4f, 6.6f);
		Vector3f c = a.subtract(b);
		
		assertEquals(-1.1f, c.x, epsilon);
		assertEquals(1.1f, c.y, epsilon);
		assertEquals(3.4f, c.z, epsilon);
	}

	
	@Test
	public void testSubtractFloatFloatFloat()
	{
		Vector3f a = new Vector3f(1.1f, 5.5f, 10.0f);
		Vector3f c = a.subtract(2.2f, 4.4f, 6.6f);
		
		assertEquals(-1.1f, c.x, epsilon);
		assertEquals(1.1f, c.y, epsilon);
		assertEquals(3.4f, c.z, epsilon);
	}

	
	@Test
	public void testToString()
	{
		Vector3f a = new Vector3f(1.1f, 5.5f, 10.9f);
		Vector3f b = new Vector3f(-1.1f, -5.5f, -12.4f);
		
		assertEquals("Vector3f<1.1, 5.5, 10.9>", a.toString());
		assertEquals("Vector3f<-1.1, -5.5, -12.4>", b.toString());
	}
	
	
	@Test
	public void testToFloatBuffer()
	{
		Vector3f a = new Vector3f(1.1f, 5.5f, 10.9f);
		FloatBuffer buf = a.toFloatBuffer();
		
		assertEquals(3, buf.capacity());
		assertEquals(1.1f, buf.get(0), epsilon);
		assertEquals(5.5f, buf.get(1), epsilon);
		assertEquals(10.9f, buf.get(2), epsilon);
	}	
	
	
	@Test
	public void testToFloatBufferW()
	{
		Vector3f a = new Vector3f(1.1f, 5.5f, 10.9f);
		FloatBuffer buf = a.toFloatBuffer(1.7f);
		
		assertEquals(4, buf.capacity());
		assertEquals(1.1f, buf.get(0), epsilon);
		assertEquals(5.5f, buf.get(1), epsilon);
		assertEquals(10.9f, buf.get(2), epsilon);
		assertEquals(1.7f, buf.get(3), epsilon);
	}
		
	
	@Test
	public void testIncVector3f()
	{
		Vector3f a = new Vector3f(1.1f, 5.5f, 10.0f);
		Vector3f b = new Vector3f(2.2f, 4.4f, 6.6f);
		a.inc(b);
		
		assertEquals(3.3f, a.x, epsilon);
		assertEquals(9.9f, a.y, epsilon);
		assertEquals(16.6f, a.z, epsilon);
	}

	
	@Test
	public void testIncFloatFloatFloat()
	{
		Vector3f a = new Vector3f(1.1f, 5.5f, 10.0f);
		a.inc(2.2f, 4.4f, 6.6f);
		
		assertEquals(3.3f, a.x, epsilon);
		assertEquals(9.9f, a.y, epsilon);
		assertEquals(16.6f, a.z, epsilon);
	}

	
	@Test
	public void testDecVector3f()
	{
		Vector3f a = new Vector3f(1.1f, 5.5f, 10.0f);
		Vector3f b = new Vector3f(2.2f, 4.4f, 6.6f);
		a.dec(b);
		
		assertEquals(-1.1f, a.x, epsilon);
		assertEquals(1.1f, a.y, epsilon);
		assertEquals(3.4f, a.z, epsilon);
	}

	
	@Test
	public void testDecFloatFloatFloat()
	{
		Vector3f a = new Vector3f(1.1f, 5.5f, 10.0f);
		a.dec(2.2f, 4.4f, 6.6f);
		
		assertEquals(-1.1f, a.x, epsilon);
		assertEquals(1.1f, a.y, epsilon);
		assertEquals(3.4f, a.z, epsilon);
	}
}
