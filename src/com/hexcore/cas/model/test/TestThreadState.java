package com.hexcore.cas.model.test;

import junit.framework.TestCase;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.ThreadState;

public class TestThreadState extends TestCase
{
	public void test1Constructor()
	{
		Cell[] c = new Cell[1];
		c[0] = new Cell(1);
		Vector2i v = new Vector2i(0, 0);
		ThreadState s = new ThreadState(c, v, 1);
		assertEquals(1, s.work.length);
		assertEquals(0, s.startingPosition.x);
		assertEquals(0, s.startingPosition.y);
		assertEquals(1, s.num);
	}

}
