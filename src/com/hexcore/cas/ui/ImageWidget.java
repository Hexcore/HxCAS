package com.hexcore.cas.ui;

import javax.media.opengl.GL;

import com.hexcore.cas.math.Vector2i;

public class ImageWidget extends Widget
{
	private Image	image;
	
	public ImageWidget(Image image)
	{
		this(new Vector2i(), image);
	}

	public ImageWidget(Vector2i position, Image image)
	{
		super(position, image.getSize());
		this.image = image;
	}
		
	public ImageWidget(String imageFilename)
	{
		this(new Vector2i(), imageFilename);
	}
	
	public ImageWidget(Vector2i position, String imageFilename)
	{
		this(position, new Image(imageFilename));
	}	
	
	@Override
	public void render(GL gl, Vector2i position)
	{
		Vector2i pos = this.position.add(position);
		Graphics.renderRectangle(gl, pos, image);
	}
}
