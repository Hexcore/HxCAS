package com.hexcore.cas.ui.toolkit;

import java.util.ArrayList;
import java.util.List;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.ColourRuleSet;
import com.hexcore.cas.model.Grid;

public abstract class GridWidget extends Widget
{
	public static class Slice
	{
		public int		colourProperty;
		public int		heightProperty;
		public float	scale;
		
		public Slice(int property, float scale) 
		{
			this.colourProperty = property;
			this.heightProperty = property;
			this.scale = scale;
		}		
		
		public Slice(int colourProperty, int heightProperty, float scale) 
		{
			this.colourProperty = colourProperty;
			this.heightProperty = heightProperty;
			this.scale = scale;
		}
	}
	
	// Options
	protected ArrayList<Slice>	slices = new ArrayList<Slice>();
	
	
	protected Grid	grid;
	protected int	cellSize;
	
	protected boolean	drawWireframe = true;
	
	protected Colour		backgroundColour = Colour.BLACK;
	protected ColourRuleSet	colourRules;
	
	public GridWidget(Vector2i size, Grid grid, int cellSize)
	{
		super(size);
		this.grid = grid;
		this.cellSize = cellSize;
	}

	public GridWidget(Vector2i position, Vector2i size, Grid grid, int cellSize)
	{
		super(position, size);
		this.grid = grid;
		this.cellSize = cellSize;
	}
	
	public void addSlice(Slice slice)
	{
		slices.add(slice);
	}
	
	public void addSlice(int heightProperty, float scale)
	{
		slices.add(new Slice(heightProperty, scale));
	}	
	
	public void addSlice(int colourProperty, int heightProperty, float scale)
	{
		slices.add(new Slice(colourProperty, heightProperty, scale));
	}
	
	public void setSlice(int index, int colourProperty, int heightProperty)
	{
		slices.get(index).colourProperty = colourProperty;
		slices.get(index).heightProperty = heightProperty;
	}
	
	public void clearSlices()
	{
		slices.clear();
	}
	
	public List<Slice> getSlices()
	{
		return slices;
	}
	
	@Override
	public boolean canGetFocus() {return true;}

	public void setDrawWireframe(boolean state) {drawWireframe = state;}
	public void toggleDrawWireframe() {drawWireframe = !drawWireframe;}
	
	public void setBackgroundColour(Colour colour){backgroundColour = colour;}
	public void setGrid(Grid grid) {this.grid = grid;}
	public void setColourRuleSet(ColourRuleSet ruleSet) {colourRules = ruleSet;}
}
