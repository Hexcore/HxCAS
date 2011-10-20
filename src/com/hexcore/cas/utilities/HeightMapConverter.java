package com.hexcore.cas.utilities;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.awt.image.PixelGrabber;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import jogamp.opengl.glu.mipmap.ScaleInternal;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.HexagonGrid;
import com.hexcore.cas.model.RectangleGrid;
import com.hexcore.cas.model.TriangleGrid;

/**
 * 
 * @author Karl Zoller
 *
 */
public class HeightMapConverter
{
	private static final String TAG = "HeightMapConverter";
	public boolean makeGrid(String fileName, Grid grid, int width, int height, int propertyIndex)
	{
		int[] pixels = new int[width*height];
		BufferedImage heightMap = null;
		int imgWidth = 0;
		int imgHeight = 0;
		
		//Read file
		try
		{
			heightMap = ImageIO.read(new File(fileName));
			imgWidth = heightMap.getWidth(null);
			imgHeight = heightMap.getHeight(null);
			
			if(width != imgWidth || height != imgHeight)
			{
				Log.information("HeightMapConverter", "Scaling Image of: " + imgWidth + " x " + imgHeight + " to " + width + " x " + height);
				heightMap = (BufferedImage) heightMap.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
			}
		} 
		catch (FileNotFoundException e)
		{
			Log.warning(TAG, "Could not find file");
			return false;
		}
		catch (IOException e)
		{
			Log.error(TAG, "Error reading file");
			return false;
		}	
		
		//heightMap.get
		//PixelGrabber pg = new PixelGrabber(heightMap, 0, 0, width, height, pixels, 0, width);
		int index = 0;
		for(int r = 0; r < height; r++)
		{
			for(int c = 0; c < width; c++)
			{
				int val = heightMap.getRGB(c, r);
				System.out.println("Blue: " + (val & 0xff));
				System.out.println("Green: " + ((val >> 8) & 0xff));
				System.out.println("Red: " + ((val >> 16) & 0xff));
				System.out.println("Alpha: " + ((val >> 24) & 0xff));
			}
		}		
		
		
		return true;
	}
}
