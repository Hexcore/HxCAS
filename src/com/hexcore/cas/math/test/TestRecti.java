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
}
