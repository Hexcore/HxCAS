package com.hexcore.cas.ui.toolkit;

import com.hexcore.cas.math.Vector2i;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.media.opengl.GLException;
import javax.media.opengl.Threading;

import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.opengl.util.texture.Texture;

public class Image
{
	private boolean		valid = false;
	private  String 	filename;
	private Texture 	texture;
	
	public Image()
	{
		this.filename = "";
		this.texture = null;
	}
	
	public Image(String filename)
	{
		this.filename = filename;
		
		LoadImageRunnable loadImage = new LoadImageRunnable();
		
		if (Threading.isOpenGLThread() || Threading.isSingleThreaded())
			loadImage.run();
		else
			Threading.invokeOnOpenGLThread(loadImage);
	}
	
	public String	getFilename() {return filename;}
	public boolean	isValid() {return valid;}	
	public int		getWidth() {return valid ? texture.getImageWidth() : 0;}
	public int		getHeight() {return valid ? texture.getImageHeight() : 0;}
	public Vector2i	getSize() {return new Vector2i(getWidth(), getHeight());}
	
	public void bind()
	{
		if (!valid) return;
		texture.enable();
		texture.bind();
	}
	
	public void unbind()
	{
		if (!valid) return;
		texture.disable();
	}
	
	class LoadImageRunnable implements Runnable
	{		
		@Override
		public void run()
		{
			try
			{
				texture = TextureIO.newTexture(new File(filename), true);
				valid = true;
			}
			catch (GLException e)
			{
				e.printStackTrace();
			}
			catch (FileNotFoundException e)
			{
				System.out.println("Error: Could not find file : " + filename);
			}
			catch (IOException e)
			{
				System.out.println("Error: Could not open file : " + filename);
				e.printStackTrace();
			}
		}
	};
}
