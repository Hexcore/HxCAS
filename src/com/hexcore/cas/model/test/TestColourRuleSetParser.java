package com.hexcore.cas.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.hexcore.cas.model.ColourRule;
import com.hexcore.cas.model.ColourRuleSet;
import com.hexcore.cas.model.ColourRuleSetParser;
import com.hexcore.cas.ui.toolkit.Colour;

public class TestColourRuleSetParser
{
	private static final double EPSILON = 0.0001f;
	
	@Test
	public void test()
	{
		List<String> properties = new ArrayList<String>();
		properties.add("water");
		properties.add("land");
		
		ColourRuleSetParser parser = new ColourRuleSetParser();
		ColourRuleSet ruleSet = parser.parse("Test Data/colourruleset/test.crs", properties);
		
		assertEquals(ruleSet.getNumProperties(), 2);
		
		ColourRule waterRule = ruleSet.getColourRule(0);
		assertEquals(waterRule.ranges.size(), 3);
		assertEquals(waterRule.ranges.get(0).getType(), ColourRule.Range.Type.SOLID);
		assertEquals(waterRule.ranges.get(0).from, 0, EPSILON);
		assertEquals(waterRule.ranges.get(0).to, 4, EPSILON);
		assertTrue(waterRule.ranges.get(0).getColour(0).equals(new Colour(0.3f, 0.7f, 0.2f)));
		assertEquals(waterRule.ranges.get(1).getType(), ColourRule.Range.Type.GRADIENT);
		assertEquals(waterRule.ranges.get(1).from, 4, EPSILON);
		assertEquals(waterRule.ranges.get(1).to, 8, EPSILON);
		assertTrue(waterRule.ranges.get(1).getColour(0).equals(new Colour(0.3f, 0.7f, 0.2f)));
		assertTrue(waterRule.ranges.get(1).getColour(1).equals(new Colour(0.2f, 0.1f, 0.4f)));
		assertEquals(waterRule.ranges.get(2).getType(), ColourRule.Range.Type.SOLID);
		assertEquals(waterRule.ranges.get(2).from, 8, EPSILON);
		assertEquals(waterRule.ranges.get(2).to, 10, EPSILON);
		
		ColourRule landRule = ruleSet.getColourRule(1);
		assertEquals(landRule.ranges.size(), 2);
		assertEquals(landRule.ranges.get(0).getType(), ColourRule.Range.Type.SOLID);
		assertEquals(landRule.ranges.get(0).from, 0, EPSILON);
		assertEquals(landRule.ranges.get(0).to, 5, EPSILON);
		assertEquals(landRule.ranges.get(1).getType(), ColourRule.Range.Type.SOLID);
		assertEquals(landRule.ranges.get(1).from, 5, EPSILON);
		assertEquals(landRule.ranges.get(1).to, 10, EPSILON);
	}
}
