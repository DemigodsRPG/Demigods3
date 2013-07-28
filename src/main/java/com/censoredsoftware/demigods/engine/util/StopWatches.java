package com.censoredsoftware.demigods.engine.util;

import org.apache.commons.lang.time.StopWatch;

public class StopWatches
{
    public static StopWatch newStopWatch()
    {
        StopWatch sW = new StopWatch();
        sW.start();
        return sW;
    }

    public static void endStopWatch(StopWatch sW, String n)
    {
        Messages.broadcast(n + ": " + sW.toString());
        sW.stop();
    }
}
