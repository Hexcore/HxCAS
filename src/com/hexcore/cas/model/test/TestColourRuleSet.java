package com.hexcore.cas.model.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.ColourRule;
import com.hexcore.cas.model.ColourRuleSet;
import com.hexcore.cas.ui.Colour;

public class TestColourRuleSet
{
	@Test
	public void testColourRuleSet()
	{
		ColourRuleSet ruleSet = new ColourRuleSet(3);
		assertEquals(3, ruleSet.colourRules.length);
	}

	@Test
	public void testGetNumProperties()
	{
		ColourRuleSet ruleSet = new ColourRuleSet(3);
		assertEquals(3, ruleSet.getNumProperties());
	}
	
	@Test
	public void testSetColourRule()
	{
		ColourRuleSet ruleSet = new ColourRuleSet(3);
		
		ColourRule rule1 = new ColourRule();
		rule1.addRange(new ColourRule.Range(0.0, 10.0, Colour.RED));
		ruleSet.setColourRule(0, rule1);
		
		ColourRule rule2 = new ColourRule();
		rule2.addRange(new ColourRule.Range(20.0, 30.0, Colour.BLUE));
		ruleSet.setColourRule(1, rule2);		
		
		assertSame(rule1, ruleSet.colourRules[0]);
		assertSame(rule2, ruleSet.colourRules[1]);
	}

	@Test
	public void testGetColourRule()
	{
		ColourRuleSet ruleSet = new ColourRuleSet(3);
		
		ColourRule rule1 = new ColourRule();
		rule1.addRange(new ColourRule.Range(0.0, 10.0, Colour.RED));
		ruleSet.setColourRule(0, rule1);
		
		ColourRule rule2 = new ColourRule();
		rule2.addRange(new ColourRule.Range(20.0, 30.0, Colour.BLUE));
		ruleSet.setColourRule(1, rule2);		
		
		assertSame(rule1, ruleSet.getColourRule(0));
		assertSame(rule2, ruleSet.getColourRule(1));		
	}

	@Test
	public void testGetColourIntDouble()
	{
		ColourRuleSet ruleSet = new ColourRuleSet(3);
		
		ColourRule rule1 = new ColourRule();
		rule1.addRange(new ColourRule.Range(0.0, 10.0, Colour.RED));
		ruleSet.setColourRule(0, rule1);
		
		ColourRule rule2 = new ColourRule();
		rule2.addRange(new ColourRule.Range(20.0, 30.0, Colour.BLUE));
		ruleSet.setColourRule(1, rule2);		
		
		assertTrue(Colour.RED.equals(ruleSet.getColour(0, 5.0f)));
		assertTrue(Colour.BLUE.equals(ruleSet.getColour(1, 25.0f)));
	}

	@Test
	public void testGetColourCellInt()
	{
		ColourRuleSet ruleSet = new ColourRuleSet(3);
		
		ColourRule rule1 = new ColourRule();
		rule1.addRange(new ColourRule.Range(0.0, 10.0, Colour.RED));
		ruleSet.setColourRule(0, rule1);
		
		ColourRule rule2 = new ColourRule();
		rule2.addRange(new ColourRule.Range(20.0, 30.0, Colour.BLUE));
		ruleSet.setColourRule(1, rule2);
		
		Cell	cell1 = new Cell(new double[] {5.0f, 100.0f, 100.0f});
		Cell	cell2 = new Cell(new double[] {100.0f, 25.0f, 100.0f});
		
		assertTrue(Colour.RED.equals(ruleSet.getColour(cell1, 0)));
		assertTrue(Colour.BLUE.equals(ruleSet.getColour(cell2, 1)));
	}
}
