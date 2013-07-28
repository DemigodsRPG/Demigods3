package com.censoredsoftware.demigods.engine.util;

import com.censoredsoftware.demigods.engine.Demigods;
import org.apache.commons.lang.time.StopWatch;

public class StopWatches
{
	public static StopWatch start()
	{
		StopWatch sW = new StopWatch();
		sW.start();
		return sW;
	}

	public static void end(StopWatch sW, String n)
	{
        Demigods.message.broadcast(n + ": " + sW.toString());
		sW.stop();
	}
}
