package com.hexcore.cas.math.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.hexcore.cas.math.Recti;
import com.hexcore.cas.math.Vector2i;

public class TestRecti
{
	@Test
	public void testRecti()
	{
		Vector2i 	zero = new Vector2i(0, 0);
		Recti 		rect = new Recti();
		
		assertTrue(zero.equals(rect.position));
		assertTrue(zero.equals(rect.size));
	}
	
	@Test
	public void testRectiVector2i()
	{
		Vector2i 	zero = new Vector2i(0, 0);
		Vector2i	size = new Vector2i(3, 7);
		Recti 		rect = new Recti(size);
		
		assertTrue(zero.equals(rect.position));
		assertTrue(size.equals(rect.size));
	}
	
	@Test
	public void testRectiVector2iVector2i()
	{
		Vector2i	size = new Vector2i(3, 7);
		Vector2i	position = new Vector2i(5, 9);
		Recti 		rect = new Recti(position, size);
		
		assertTrue(position.equals(rect.position));
		assertTrue(size.equals(rect.size));
	}
	
	@Test
	public void testGetters()
	{
		Vector2i	size = new Vector2i(3, 7);
		Vector2i	position = new Vector2i(5, 9);
		Recti 		rect = new Recti(position, size);
		
		assertTrue(position.equals(rect.getPosition()));
		assertTrue(size.equals(rect.getSize()));
		
		assertEquals(position.x, rect.getX());
		assertEquals(position.y, rect.getY());
		assertEquals(size.x, rect.getWidth());
		assertEquals(size.y, rect.getHeight());
	}
	
	@Test
	public void testSetters()
	{
		Vector2i	size = new Vector2i(3, 7);
		Vector2i	position = new Vector2i(5, 9);
		Recti 		rect = new Recti(position, size);
		
		size = new Vector2i(13, 17);
		position = new Vector2i(15, 19);
		
		rect.setSize(size);
		rect.setPosition(position);
		
		assertEquals(position.x, rect.getX());
		assertEquals(position.y, rect.getY());
		assertEquals(size.x, rect.getWidth());
		assertEquals(size.y, rect.getHeight());
	}
	
	@Test
	public void testGetBoundingBox()
	{
		// Test 1
		Vector2i[] points = new Vector2i[5];
		points[0] = new Vector2i(4, 6);
		points[1] = new Vector2i(3, 7);
		points[2] = new Vector2i(3, 8);
		points[3] = new Vector2i(6, 11);
		points[4] = new Vector2i(7, 9);
		
		Recti box = Recti.getBoundingBox(points);
		assertEquals(3, box.position.x);
		assertEquals(6, box.position.y);
		assertEquals(4, box.size.x);
		assertEquals(5, box.size.y);	
		
		// Test 2
		points = new Vector2i[4];
		points[0] = new Vector2i(4,  0);
		points[1] = new Vector2i(8,  0);
		points[2] = new Vector2i(12, 8);
		points[3] = new Vector2i(0,  8);
		
		box = Recti.getBoundingBox(points);
		assertEquals(0, box.position.x);
		assertEquals(0, box.position.y);
		assertEquals(12, box.size.x);
		assertEquals(8, box.size.y);	
	}
}
