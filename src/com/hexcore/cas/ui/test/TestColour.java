package com.hexcore.cas.ui.test;

import junit.framework.TestCase;
import org.junit.Test;

import com.hexcore.cas.ui.toolkit.Colour;

public class TestColour extends TestCase
{
	@Test
	public void testColourFloatFloatFloat()
	{
		Colour colour = new Colour(0.1f, 0.2f, 0.3f);
		
		assertEquals(colour.r, 0.1f);
		assertEquals(colour.g, 0.2f);
		assertEquals(colour.b, 0.3f);
		assertEquals(colour.a, 1.0f);
	}

	@Test
	public void testColourFloatFloatFloatFloat()
	{
		Colour colour = new Colour(0.1f, 0.2f, 0.3f, 0.4f);
		
		assertEquals(colour.r, 0.1f);
		assertEquals(colour.g, 0.2f);
		assertEquals(colour.b, 0.3f);
		assertEquals(colour.a, 0.4f);
	}

	@Test
	public void testColourColour()
	{
		Colour a = new Colour(0.1f, 0.2f, 0.3f, 0.4f);
		Colour b = new Colour(a);
		
		// Verify that a deep-copy was made
		a.r = 0.5f;
		a.g = 0.6f;
		a.b = 0.7f;
		a.a = 0.8f;
		
		assertEquals(b.r, 0.1f, 0.0001f);
		assertEquals(b.g, 0.2f, 0.0001f);
		assertEquals(b.b, 0.3f, 0.0001f);
		assertEquals(b.a, 0.4f, 0.0001f);	
	}
	
	@Test
	public void testMixColour()
	{
		Colour a = new Colour(0.1f, 0.2f, 0.3f, 0.4f);
		Colour b = new Colour(0.5f, 0.6f, 0.7f, 0.8f);
		Colour c = a.mix(b);
				
		assertEquals(c.r, 0.3f, 0.0001f);
		assertEquals(c.g, 0.4f, 0.0001f);
		assertEquals(c.b, 0.5f, 0.0001f);
		assertEquals(c.a, 0.6f, 0.0001f);			
	}

	@Test
	public void testMixColourFloat()
	{
		Colour a = new Colour(0.1f, 0.2f, 0.3f, 0.4f);
		Colour b = new Colour(0.5f, 0.6f, 0.7f, 0.8f);
		
		Colour c = a.mix(b, 0.5f);
		assertEquals(c.r, 0.3f, 0.0001f);
		assertEquals(c.g, 0.4f, 0.0001f);
		assertEquals(c.b, 0.5f, 0.0001f);
		assertEquals(c.a, 0.6f, 0.0001f);
		
		c = a.mix(b, 0.25f);
		assertEquals(c.r, 0.2f, 0.0001f);
		assertEquals(c.g, 0.3f, 0.0001f);
		assertEquals(c.b, 0.4f, 0.0001f);
		assertEquals(c.a, 0.5f, 0.0001f);
	}

	@Test
	public void testToString()
	{
		Colour a = new Colour(0.1f, 0.2f, 0.3f, 0.4f);
		assertTrue(a.toString().equals("Colour<0.1, 0.2, 0.3, 0.4>"));
	}

}
