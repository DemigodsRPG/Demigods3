package com.censoredsoftware.demigods;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
