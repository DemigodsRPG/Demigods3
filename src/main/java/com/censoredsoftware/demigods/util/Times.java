package com.censoredsoftware.demigods.util;

import java.text.DecimalFormat;

public class Times
{
	public static double getSeconds(long time)
	{
		return (double) Math.abs((time - System.currentTimeMillis()) / 1000);
	}

	public static double getMinutes(long time)
	{
		return (double) Math.abs((time - System.currentTimeMillis()) / 60000);
	}

	public static double getHours(long time)
	{
		return (double) Math.abs((time - System.currentTimeMillis()) / 3600000);
	}

	public static String getTimeTagged(long time, boolean round)
	{
		DecimalFormat format = round ? new DecimalFormat("#") : new DecimalFormat("#.##");
		if(getHours(time) >= 1) return format.format(getHours(time)) + "h";
		else if(Double.valueOf(format.format(getMinutes(time))) >= 1) return format.format(getMinutes(time)) + "m";
		else return format.format(getSeconds(time)) + "s";
	}
}
