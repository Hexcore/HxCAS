package com.hexcore.cas.model;

import java.util.ArrayList;

import com.hexcore.cas.ui.toolkit.Colour;

public class ColourRule
{
	/*
	 *  A Range doesn't not include the end of the range (the value of 'to')
	 */
	public static class Range
	{
		public enum Type {SOLID, GRADIENT};
		
		public double from;
		public double to;
		
		private Type		type;
		private Colour[] 	colours;
		
		public Range(double from, double to, Colour colour)
		{
			this.from = from;
			this.to = to;
			this.type = Type.SOLID;
			colours = new Colour[1];
			colours[0] = new Colour(colour);
		}
		
		public Range(double from, double to, Colour fromColour, Colour toColour)
		{
			this.from = from;
			this.to = to;
			this.type = Type.GRADIENT;
			colours = new Colour[2];
			colours[0] = new Colour(fromColour);
			colours[1] = new Colour(toColour);
		}
		
		public Type		getType() {return type;}
		public Colour	getColour(int index) {return colours[index];}
		
		public Colour	getColourAt(double value)
		{
			if (type == Type.SOLID) return colours[0];
			if (type != Type.GRADIENT) return null;
			
			if (value <= from) return colours[0];
			if (value >= to) return colours[1];
			return colours[0].mix(colours[1], (float)((value - from) / (to - from)));
		}
	}
	
	public boolean			useClosestRange;
	public ArrayList<Range> ranges;
	
	public ColourRule()
	{
		useClosestRange = false;
		ranges = new ArrayList<Range>();
	}
	
	public boolean isUsingClosestRange()
	{
		return useClosestRange;
	}
	
	public void useClosestRange(boolean state)
	{
		useClosestRange = state;
	}
	
	public void addRange(Range range)
	{
		ranges.add(range);
	}
	
	public Colour getColour(double value)
	{
		Range	closestRange = null;
		
		for (Range range : ranges)
		{
			if ((value >= range.from) && (value < range.to)) 
				return range.getColourAt(value);
			else if (closestRange == null)
				closestRange = range;
			else 
			{
				double	dist = Math.min(Math.abs(closestRange.from - value), Math.abs(closestRange.to - value));
				double	curDist = Math.min(Math.abs(range.from - value), Math.abs(range.to - value));
				
				if (curDist <= dist) closestRange = range;
			}
		}
		
		if (useClosestRange && (closestRange != null))
			return closestRange.getColourAt(value);
			
		return Colour.BLACK;
	}
}
