package com.censoredsoftware.demigods.engine.util;

import com.google.common.collect.Maps;
import org.apache.commons.lang.time.StopWatch;

import java.util.Map;

public class StopWatches
{
    private static Map<Long, String> sWs = Maps.newHashMap();

    public static StopWatch newStopWatch(String n)
    {
        StopWatch sW = new StopWatch();
        sW.start();
        sWs.put(sW.getStartTime(), n);
        return sW;
    }

    public static void reportTime(StopWatch sW)
    {
        Messages.broadcast(sWs.get(sW.getStartTime()) + ": " + sW.toString());
    }

    public static void endStopWatch(StopWatch sW)
    {
        reportTime(sW);
        sWs.remove(sW.getStartTime());
        sW.stop();
    }
}
