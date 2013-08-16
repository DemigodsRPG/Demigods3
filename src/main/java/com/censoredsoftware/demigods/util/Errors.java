package com.censoredsoftware.demigods.util;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.errornoise.ErrorNoise;

public class Errors
{
	public static void triggerError(String... message)
	{
		ErrorNoise.API.triggerError(Demigods.plugin, message);
	}
}
