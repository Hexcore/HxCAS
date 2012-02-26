package com.hexcore.cas.model.test;

import junit.framework.TestCase;

import com.hexcore.cas.model.ColourRule;
import com.hexcore.cas.ui.toolkit.Colour;

public class TestColourRule extends TestCase
{
	
	public void testRangeSolid()
	{
		Colour colour = new Colour(0.8f, 0.5f, 0.2f);
		ColourRule.Range range = new ColourRule.Range(10.0, 48.0, colour);
		
		assertEquals(10.0, range.from);
		assertEquals(48.0, range.to);
		assertEquals(ColourRule.Range.Type.SOLID, range.getType());
		assertTrue(range.getColour(0).equals(colour));
		
		assertTrue(range.getColourAt(5.0f).equals(colour));
		assertTrue(range.getColourAt(30.0f).equals(colour));
		assertTrue(range.getColourAt(55.0f).equals(colour));
	}	
	
	
	public void testRangeGradient()
	{
		Colour firstColour = new Colour(0.8f, 0.5f, 0.2f);
		Colour midColour = new Colour(0.7f, 0.4f, 0.1f);
		Colour secondColour = new Colour(0.6f, 0.3f, 0.0f);
		ColourRule.Range range = new ColourRule.Range(10.0, 60.0, firstColour, secondColour);
		
		assertEquals(10.0, range.from);
		assertEquals(60.0, range.to);
		assertEquals(ColourRule.Range.Type.GRADIENT, range.getType());
		assertTrue(range.getColour(0).equals(firstColour));
		assertTrue(range.getColour(1).equals(secondColour));
		
		assertTrue(range.getColourAt(5.0f).equals(firstColour));
		assertTrue(range.getColourAt(10.0f).equals(firstColour));
		
		assertEquals(range.getColourAt(35.0f).r, midColour.r, 0.0001f);
		assertEquals(range.getColourAt(35.0f).g, midColour.g, 0.0001f);
		assertEquals(range.getColourAt(35.0f).b, midColour.b, 0.0001f);
		assertEquals(range.getColourAt(35.0f).a, midColour.a, 0.0001f);
		
		assertTrue(range.getColourAt(60.0f).equals(secondColour));
		assertTrue(range.getColourAt(100.0f).equals(secondColour));		
	}		
	
	
	public void testColourRule()
	{
		Colour a = new Colour(0.4f, 0.9f, 0.1f);
		Colour b = new Colour(0.8f, 0.5f, 0.2f);
		Colour bc = new Colour(0.7f, 0.4f, 0.1f);
		Colour c = new Colour(0.6f, 0.3f, 0.0f);	
		
		ColourRule.Range rangeA = new ColourRule.Range(10.0, 40.0, a);
		ColourRule.Range rangeB = new ColourRule.Range(40.0, 60.0, b, c);
		
		ColourRule rule = new ColourRule();
		rule.addRange(rangeA);
		rule.addRange(rangeB);
		
		assertTrue(rule.getColour(5.0f).equals(Colour.BLACK));
		assertTrue(rule.getColour(10.0f).equals(a));
		assertTrue(rule.getColour(20.0f).equals(a));
		assertTrue(rule.getColour(39.9f).equals(a));
		
		assertTrue(rule.getColour(40.0f).equals(b));
		
		assertEquals(rule.getColour(50.0f).r, bc.r, 0.0001f);
		assertEquals(rule.getColour(50.0f).g, bc.g, 0.0001f);
		assertEquals(rule.getColour(50.0f).b, bc.b, 0.0001f);
		assertEquals(rule.getColour(50.0f).a, bc.a, 0.0001f);
		
		assertTrue(rule.getColour(60.0f).equals(Colour.BLACK));	
	}
	
	
	public void testColourRuleClosestRange()
	{
		Colour a = new Colour(0.4f, 0.9f, 0.1f);
		Colour b = new Colour(0.8f, 0.5f, 0.2f);
		Colour bc = new Colour(0.7f, 0.4f, 0.1f);
		Colour c = new Colour(0.6f, 0.3f, 0.0f);	
		
		ColourRule.Range rangeA = new ColourRule.Range(10.0, 30.0, a);
		ColourRule.Range rangeB = new ColourRule.Range(40.0, 60.0, b, c);
		
		ColourRule rule = new ColourRule();
		rule.addRange(rangeA);
		rule.addRange(rangeB);
		rule.useClosestRange(true);
				
		assertTrue(rule.getColour(5.0f).equals(a));
		assertTrue(rule.getColour(10.0f).equals(a));
		assertTrue(rule.getColour(20.0f).equals(a));
		assertTrue(rule.getColour(29.9f).equals(a));
		assertTrue(rule.getColour(33.0f).equals(a));
		assertTrue(rule.getColour(35.0f).equals(b));
		assertTrue(rule.getColour(38.0f).equals(b));
		assertTrue(rule.getColour(40.0f).equals(b));
		
		assertEquals(rule.getColour(50.0f).r, bc.r, 0.0001f);
		assertEquals(rule.getColour(50.0f).g, bc.g, 0.0001f);
		assertEquals(rule.getColour(50.0f).b, bc.b, 0.0001f);
		assertEquals(rule.getColour(50.0f).a, bc.a, 0.0001f);
		
		assertTrue(rule.getColour(60.0f).equals(c));	
	}
}
