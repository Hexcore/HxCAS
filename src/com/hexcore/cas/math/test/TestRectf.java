package com.hexcore.cas.math.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.hexcore.cas.math.Rectf;
import com.hexcore.cas.math.Vector2f;

public class TestRectf
{
	static final float epsilon = 0.0001f;
	
	@Test
	public void testRectf()
	{
		Vector2f 	zero = new Vector2f(0.0f, 0.0f);
		Rectf 		rect = new Rectf();
		
		assertTrue(zero.equals(rect.position));
		assertTrue(zero.equals(rect.size));
	}
	
	
	@Test
	public void testRectfVector2f()
	{
		Vector2f 	zero = new Vector2f(0.0f, 0.0f);
		Vector2f	size = new Vector2f(3.3f, 7.7f);
		Rectf 		rect = new Rectf(size);
		
		assertTrue(zero.equals(rect.position));
		assertTrue(size.equals(rect.size));
	}
	
	
	@Test
	public void testRectfVector2fVector2f()
	{
		Vector2f	size = new Vector2f(3.3f, 7.7f);
		Vector2f	position = new Vector2f(5.5f, 9.9f);
		Rectf 		rect = new Rectf(position, size);
		
		assertTrue(position.equals(rect.position));
		assertTrue(size.equals(rect.size));
	}
	
	
	@Test
	public void testGetters()
	{
		Vector2f	size = new Vector2f(3.3f, 7.7f);
		Vector2f	position = new Vector2f(5.5f, 9.9f);
		Rectf 		rect = new Rectf(position, size);
		
		assertTrue(position.equals(rect.getPosition()));
		assertTrue(size.equals(rect.getSize()));
		
		assertEquals(position.x, rect.getX(), epsilon);
		assertEquals(position.y, rect.getY(), epsilon);
		assertEquals(size.x, rect.getWidth(), epsilon);
		assertEquals(size.y, rect.getHeight(), epsilon);
	}
	
	
	@Test
	public void testSetters()
	{
		Vector2f	size = new Vector2f(3.3f, 7.7f);
		Vector2f	position = new Vector2f(5.5f, 9.9f);
		Rectf 		rect = new Rectf(position, size);
		
		size = new Vector2f(13.13f, 17.17f);
		position = new Vector2f(15.15f, 19.19f);
		
		rect.setSize(size);
		rect.setPosition(position);
		
		assertEquals(position.x, rect.getX(), epsilon);
		assertEquals(position.y, rect.getY(), epsilon);
		assertEquals(size.x, rect.getWidth(), epsilon);
		assertEquals(size.y, rect.getHeight(), epsilon);
	}
	
	
	@Test
	public void testGetBoundingBox()
	{
		// Test 1
		Vector2f[] points = new Vector2f[0];
		
		Rectf box = Rectf.getBoundingBox(points);
		assertEquals(null, box);
		
		// Test 2
		points = new Vector2f[5];
		points[0] = new Vector2f(4.4f, 6.6f);
		points[1] = new Vector2f(3.3f, 7.7f);
		points[2] = new Vector2f(3.3f, 8.8f);
		points[3] = new Vector2f(6.6f, 11.11f);
		points[4] = new Vector2f(7.7f, 9.9f);
		points[4] = new Vector2f(7.7f, 2.2f);
		
		box = Rectf.getBoundingBox(points);
		assertEquals(3.3f, box.position.x, epsilon);
		assertEquals(2.2f, box.position.y, epsilon);
		assertEquals(4.4f, box.size.x, epsilon);
		assertEquals(8.91f, box.size.y, epsilon);	
		
		// Test 3
		points = new Vector2f[4];
		points[0] = new Vector2f(4.4f,  0.0f);
		points[1] = new Vector2f(8.8f,  0.0f);
		points[2] = new Vector2f(12.12f, 8.8f);
		points[3] = new Vector2f(0.0f,  8.8f);
		
		box = Rectf.getBoundingBox(points);
		assertEquals(0.0f, box.position.x, epsilon);
		assertEquals(0.0f, box.position.y, epsilon);
		assertEquals(12.12f, box.size.x, epsilon);
		assertEquals(8.8f, box.size.y, epsilon);	
	}
}
