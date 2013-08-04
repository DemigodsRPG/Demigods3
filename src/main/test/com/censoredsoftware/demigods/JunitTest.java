package com.censoredsoftware.demigods;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class JunitTest
{
	@Test
	public void testEquals()
	{
		assertEquals(50, 5 * 10);
	}

	@Test
	public void testNotEquals()
	{
		assertNotEquals(10, 5 * 10);
	}
}
