package com.hexcore.cas.model;

import com.hexcore.cas.ui.toolkit.Colour;

/**
 * Class ColorRuleSet
 * Use this class to assign colourRules to
 * all properties.
 * 
 * @author Divan
 *
 */

public class ColourRuleSet
{
	public ColourRule[]	colourRules;
	
	public ColourRuleSet(int numProperties)
	{
		colourRules = new ColourRule[numProperties];
	}
	
	public int getNumProperties() {return colourRules.length;}
	
	public ColourRule getColourRule(int propertyIndex)
	{
		if ((propertyIndex < 0) || (propertyIndex >= colourRules.length)) return null;
		return colourRules[propertyIndex];
	}	
	
	public Colour getColour(int propertyIndex, double value)
	{
		if ((propertyIndex < 0) || (propertyIndex >= colourRules.length)) return Colour.BLACK;
		if (colourRules[propertyIndex] == null) return Colour.BLACK;
		return colourRules[propertyIndex].getColour(value);
	}
	
	public Colour getColour(Cell cell, int propertyIndex)
	{
		if ((propertyIndex < 0) || (propertyIndex >= colourRules.length)) return Colour.BLACK;
		if (colourRules[propertyIndex] == null) return Colour.BLACK;
		return colourRules[propertyIndex].getColour(cell.getValue(propertyIndex));
	}	
	
	public void setColourRule(int propertyIndex, ColourRule colourRule)
	{
		if ((propertyIndex < 0) || (propertyIndex >= colourRules.length)) return;
		colourRules[propertyIndex] = colourRule;
	}
}
