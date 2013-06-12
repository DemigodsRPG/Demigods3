package com.censoredsoftware.Demigods.Engine.Runnable;

import com.censoredsoftware.Demigods.Engine.Miscellaneous.TimedData;

public class TimedDataRunnable implements Runnable
{
	@Override
	public void run()
	{
		for(TimedData data : TimedData.getAll())
		{
			if(data.getExpiration() <= System.currentTimeMillis()) data.delete();
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}
