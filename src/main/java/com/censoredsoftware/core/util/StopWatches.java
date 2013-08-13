package com.censoredsoftware.core.util;

import org.apache.commons.lang.time.StopWatch;

public class StopWatches
{
	public static StopWatch start()
	{
		StopWatch sW = new StopWatch();
		sW.start();
		return sW;
	}

	public static StopWatch end(StopWatch sW)
	{
		sW.stop();
		return sW;
	}
}
