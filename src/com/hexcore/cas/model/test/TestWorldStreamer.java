package com.hexcore.cas.model.test;

import java.io.IOException;
import java.util.zip.ZipException;

import com.hexcore.cas.model.World;
import com.hexcore.cas.model.WorldStreamer;

import junit.framework.TestCase;

public class TestWorldStreamer extends TestCase
{
	private World world = new World();
	
	public void testStartAndStop()
		throws ZipException, IOException
	{
		world.setFileName("Test Data/world/world.caw");
		
		WorldStreamer streamer = new WorldStreamer();
		streamer.start(world);
		assertTrue(streamer.isZipWorldOpen());
		streamer.stop();
		assertFalse(streamer.isZipWorldOpen());
	}
}
