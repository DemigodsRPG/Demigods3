package com.censoredsoftware.Modules.Data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class TimedDataModule
{
	private static List<TimedObject> timedData;

	public TimedDataModule(Plugin instance)
	{
		timedData = new ArrayList<TimedObject>();
		Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(instance, new TimedData(), 20, 20);
	}

	public void add(Object data, long expires)
	{
		timedData.add(new TimedObject(data, expires));
	}

	public void remove(Object data)
	{
		for(TimedObject timed : timedData)
		{
			if(timed.getData().equals(data)) timedData.remove(data);
		}
	}

	public boolean contains(Object data)
	{
		for(TimedObject timed : timedData)
		{
			if(timed.getData().equals(data)) return true;
		}
		return false;
	}

	public class TimedObject
	{
		private Object data;
		private long expires;

		public TimedObject(Object data, long expires)
		{
			this.data = data;
			this.expires = expires;
		}

		public long getExpires()
		{
			return expires;
		}

		public Object getData()
		{
			return data;
		}
	}

	private static class TimedData implements Runnable
	{
		@Override
		public void run()
		{
			for(TimedObject timed : timedData)
			{
				if(System.currentTimeMillis() >= timed.getExpires()) timedData.remove(timedData);
			}
		}
	}
}
