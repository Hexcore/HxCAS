package com.hexcore.cas.ui.toolkit.widgets;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.ui.toolkit.Colour;
import com.hexcore.cas.ui.toolkit.Window;

public class ColourPickerDialog extends Dialog
{
	private ColourPicker colourPicker;
	private Button applyButton;
	
	public ColourPickerDialog(Window window)
	{
		super(window, new Vector2i(400, 300));
		
		LinearLayout horizontalLayout = new LinearLayout(LinearLayout.Direction.HORIZONTAL);
		horizontalLayout.setMargin(new Vector2i(0, 0));
		horizontalLayout.setFlag(Widget.WRAP);
		setContents(horizontalLayout);
		
		colourPicker = new ColourPicker(window);
		horizontalLayout.add(colourPicker);
		
		applyButton = new Button(new Vector2i(120, 40), "Apply");
		applyButton.setFlag(Widget.CENTER_HORIZONTAL);
		horizontalLayout.add(applyButton);
	}
	
	public boolean isApplyButton(Widget widget)
	{
		return widget == applyButton;
	}
	
	public Colour getColour()
	{
		return colourPicker.getColour();
	}
}
