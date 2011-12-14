package com.hexcore.cas.ui.toolkit;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;

public class ImageWidget extends Widget
{
	private Image	image;
	
	public ImageWidget()
	{
		this(new Vector2i(), new Image());
	}	
	
	public ImageWidget(Image image)
	{
		this(new Vector2i(), image);
	}

	public ImageWidget(Vector2i position, Image image)
	{
		super(position, new Vector2i());
		setImage(image);
	}
		
	public ImageWidget(String imageFilename)
	{
		this(new Vector2i(), imageFilename);
	}
	
	public ImageWidget(Vector2i position, String imageFilename)
	{
		this(position, new Image(imageFilename));
	}	
	
	public void setImage(Image image)
	{
		this.image = image;
		setSize(image.getSize().add(padding).add(padding));
	}
	
	@Override
	public void relayout()
	{
		if (image != null) 
			setSize(image.getSize().add(padding).add(padding));		
	}
	
	@Override
	public void render(GL gl, Vector2i position)
	{
		Vector2i pos = this.position.add(position);
		Graphics.renderRectangle(gl, pos.add(padding), image);
		
		if (window.isDebugLayout()) 
		{
			Graphics.renderBorder(gl, pos.add(padding), size.subtract(padding).subtract(padding), new Colour(0.0f, 0.5f, 0.5f));
			Graphics.renderBorder(gl, pos, size, new Colour(0.0f, 0.5f, 1.0f));
		}
	}
}
