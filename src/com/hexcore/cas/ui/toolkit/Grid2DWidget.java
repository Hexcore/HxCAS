package com.hexcore.cas.ui.toolkit;

import java.awt.event.KeyEvent;

import com.hexcore.cas.math.Vector2f;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Grid;

public class Grid2DWidget extends GridWidget
{		
	protected float		zoom = 1.0f;
	protected Vector2f 	scroll = new Vector2f(0, 0);
	
	protected Colour	cellBorderColour = Colour.WHITE;
	protected Colour	cellSelectedBorderColour = Colour.RED;
	
	protected boolean	drawSelected = true;
	protected Vector2i	selectedCell = new Vector2i(0, 0);
	
	public Grid2DWidget(Vector2i size, Grid grid, int cellSize)
	{
		super(size, grid, cellSize);
	}

	public Grid2DWidget(Vector2i position, Vector2i size, Grid grid, int cellSize)
	{
		super(position, size, grid, cellSize);
	}	
		
	public float getZoom() {return zoom;}
	public void setZoom(float zoom) {this.zoom = zoom;}
	public void changeZoom(float amount) {this.zoom += zoom;}
	
	public void setDrawSelected(boolean state) {drawSelected = state;}
	public boolean isSelectedDrawn() {return drawSelected;}	
	public Vector2i getSelectedCell() {return selectedCell;}
	
	public void setCellBorderColour(Colour colour) {cellBorderColour = colour;}
	public void setSelectedCellBorderColour(Colour colour) {cellSelectedBorderColour = colour;}
	
	@Override
	public void update(Vector2i position, float delta)
	{		
		if (focused)
		{
			float speed = 0.25f / zoom;
			
			if (window.getKeyState(KeyEvent.VK_UP))
				scroll.y += speed;
			if (window.getKeyState(KeyEvent.VK_DOWN))
				scroll.y -= speed;
			if (window.getKeyState(KeyEvent.VK_LEFT))
				scroll.x += speed;
			if (window.getKeyState(KeyEvent.VK_RIGHT))
				scroll.x -= speed;
		}
	}
	
	@Override
	public boolean handleEvent(Event event, Vector2i position)
	{
		boolean handled = super.handleEvent(event, position);
		
		if (event.type == Event.Type.LOST_FOCUS)
		{
			active = false;
		}
		else if (event.type == Event.Type.MOUSE_SCROLL)
		{
			zoom *= (1.0f - event.amount * 0.002f);
			if (zoom < 0.01f) zoom = 0.01f;
			handled = true;
		}
		
		return handled;
	}
}
