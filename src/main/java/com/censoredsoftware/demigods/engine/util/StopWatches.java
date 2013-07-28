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

    public static void reportTime(StopWatch sW)
    {
        Messages.broadcast(sW.toString());
        sW.reset();
    }

    public static void endStopWatch(StopWatch sW)
    {
        Messages.broadcast(sW.toString());
        sW.stop();
    }
}
