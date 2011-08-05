package com.hexcore.cas.math.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.hexcore.cas.math.Vector2f;
import com.hexcore.cas.math.Vector3f;

public class TestVector3f
{
	static final float epsilon = 0.0001f;
	
	@Test
	public void testVector3f()
	{
		Vector3f point = new Vector3f();
		assertEquals(0.0f, point.x, epsilon);
		assertEquals(0.0f, point.y, epsilon);
	}

	@Test
	public void testVector3fIntIntInt()
	{
		Vector3f point = new Vector3f(7.7f, 9.9f, 13.13f);
		assertEquals(7.7f, point.x, epsilon);
		assertEquals(9.9f, point.y, epsilon);
		assertEquals(13.13f, point.z, epsilon);
	}

	@Test
	public void testVector3fVector3f()
	{
		Vector3f point = new Vector3f(3.3f, 4.4f, 5.5f);
		Vector3f copy = new Vector3f(point);
		assertEquals(3.3f, copy.x, epsilon);
		assertEquals(4.4f, copy.y, epsilon);
		assertEquals(5.5f, copy.z, epsilon);
	}

	@Test
	public void testGet()
	{
		Vector3f point = new Vector3f(7.7f, 9.9f, 13.13f);
		assertEquals(7.7f, point.get(0), epsilon);
		assertEquals(9.9f, point.get(1), epsilon);
		assertEquals(13.13f, point.get(2), epsilon);
	}
}
