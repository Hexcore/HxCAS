package com.hexcore.cas.ui;

import java.util.ArrayList;
import java.util.List;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.ColourRuleSet;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.HexagonGrid;
import com.hexcore.cas.model.RectangleGrid;
import com.hexcore.cas.model.TriangleGrid;
import com.hexcore.cas.ui.toolkit.Button;
import com.hexcore.cas.ui.toolkit.Container;
import com.hexcore.cas.ui.toolkit.DropDownBox;
import com.hexcore.cas.ui.toolkit.Grid2DWidget;
import com.hexcore.cas.ui.toolkit.Grid3DWidget;
import com.hexcore.cas.ui.toolkit.GridWidget;
import com.hexcore.cas.ui.toolkit.GridWidget.Slice;
import com.hexcore.cas.ui.toolkit.HexagonGrid3DWidget;
import com.hexcore.cas.ui.toolkit.HexagonGridWidget;
import com.hexcore.cas.ui.toolkit.LinearLayout;
import com.hexcore.cas.ui.toolkit.RectangleGrid3DWidget;
import com.hexcore.cas.ui.toolkit.RectangleGridWidget;
import com.hexcore.cas.ui.toolkit.TriangleGrid3DWidget;
import com.hexcore.cas.ui.toolkit.TriangleGridWidget;
import com.hexcore.cas.ui.toolkit.Widget;
import com.hexcore.cas.ui.toolkit.Window;
import com.hexcore.cas.utilities.Log;

public class Viewport
{
	enum Type {TWO_D, THREE_D};
	
	public Grid			grid;
	public Container	container;
	public GridWidget	gridWidget;
	public Type 		type;
	
	public ColourRuleSet	colourRuleSet;
	
	public List<DropDownBox>	propertyDropDownBoxes;
	
	public Viewport(Container container, Type type, ColourRuleSet colourRuleSet)
	{
		this.container = container;
		this.type = type;
		this.colourRuleSet = colourRuleSet;
		this.propertyDropDownBoxes = new ArrayList<DropDownBox>();
	}

	public void switchDimension(Grid grid, Window window)
	{			
		Log.debug("Viewport", "Switch Dimension");
		
		if (this.type == Viewport.Type.THREE_D)
			type = Viewport.Type.TWO_D;
		else			
			type = Viewport.Type.THREE_D;
		
		recreate(grid, window, colourRuleSet);
	}
	
	public void recreate(Grid grid, Window window, ColourRuleSet colourRules)
	{
		this.grid = grid;
		this.colourRuleSet = colourRules;
		
		Grid3DWidget temp3DWidget = null;
		Grid2DWidget temp2DWidget = null;
		
    	switch (grid.getType())
		{
			case RECTANGLE:
				if (type == Viewport.Type.THREE_D)
				{
					temp3DWidget = new RectangleGrid3DWidget(new Vector2i(10, 10), (RectangleGrid)grid, 10);
											
					if (gridWidget != null)
						if (gridWidget.hasFocus()) 
							window.requestFocus(temp3DWidget);
					gridWidget = temp3DWidget;
				}
				else
				{
					temp2DWidget = new RectangleGridWidget(new Vector2i(10, 10), (RectangleGrid)grid, 10);
					
					if (gridWidget != null)
						if (gridWidget.hasFocus()) 
							window.requestFocus(temp2DWidget);
					gridWidget = temp2DWidget;
				}	
				break;
			case HEXAGON:
				if (type == Viewport.Type.THREE_D)
				{
					temp3DWidget = new HexagonGrid3DWidget(new Vector2i(10, 10), (HexagonGrid)grid, 10);
					
					if (gridWidget != null)
						if (gridWidget.hasFocus()) 
							window.requestFocus(temp3DWidget);
					gridWidget = temp3DWidget;
				}
				else
				{
					temp2DWidget = new HexagonGridWidget(new Vector2i(10, 10), (HexagonGrid)grid, 10);

					if (gridWidget != null)
						if (gridWidget.hasFocus()) 
							window.requestFocus(temp2DWidget);
					gridWidget = temp2DWidget;
				}	
				break;
			case TRIANGLE:
				if (type == Viewport.Type.THREE_D)
				{
					temp3DWidget = new TriangleGrid3DWidget(new Vector2i(10, 10), (TriangleGrid)grid, 10);
					
					if (gridWidget != null)
						if (gridWidget.hasFocus()) 
							window.requestFocus(temp3DWidget);
					gridWidget = temp3DWidget;
				}
				else
				{
					temp2DWidget = new TriangleGridWidget(new Vector2i(10, 10), (TriangleGrid)grid, 10);

					if (gridWidget != null)
						if (gridWidget.hasFocus()) 
							window.requestFocus(temp2DWidget);
					gridWidget = temp2DWidget;
				}	
				break;
			
		}
    	
		for (int index = 1; index < 2; index++)
			gridWidget.addSlice(index, 10.0f);
    	
    	gridWidget.setColourRuleSet(colourRuleSet);
    	gridWidget.setFlag(Widget.FILL);
    	container.setContents(gridWidget);
	}
	
	void updateControlPanel(LinearLayout controlPanel, Button addSliceButton)
	{
		controlPanel.clear();
		propertyDropDownBoxes.clear();
		
		if (grid != null)
		{							
			for (Slice slice : gridWidget.getSlices())
			{
				DropDownBox colourProperty = new DropDownBox(new Vector2i(100, 20));
				
				for (int i = 0; i < grid.getNumProperties(); i++)
					colourProperty.addItem("Property " + i);
				
				colourProperty.setSelected(slice.colourProperty);
				controlPanel.add(colourProperty);
				propertyDropDownBoxes.add(colourProperty);
			}
		
			controlPanel.add(addSliceButton);
		}
	}
}
