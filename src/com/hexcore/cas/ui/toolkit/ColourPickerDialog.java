package com.hexcore.cas.ui.toolkit;

import com.hexcore.cas.math.Vector2i;

public class ColourPickerDialog extends Dialog
{
	public ColourPickerDialog(Window window)
	{
		super(window, new Vector2i(400, 300));
		setContents(new ColourPicker());
	}
}
