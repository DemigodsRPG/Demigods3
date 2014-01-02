package com.censoredsoftware.demigods.engine.data;

import com.censoredsoftware.censoredlib.data.TimedData;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;

import java.util.Set;
import java.util.UUID;

public class TimedDataManager
{
	public static TimedData get(UUID id)
	{
		return Data.TIMED_DATA.get(id);
	}

	public static Set<TimedData> getAll()
	{
		return Sets.newHashSet(Data.TIMED_DATA.values());
	}

	public static TimedData find(String key, String subKey)
	{
		if(findByKey(key) == null) return null;

		for(TimedData data : findByKey(key))
			if(data.getSubKey().equals(subKey)) return data;

		return null;
	}

	public static Set<TimedData> findByKey(final String key)
	{
		return Sets.newHashSet(Collections2.filter(getAll(), new Predicate<TimedData>()
		{
			@Override
			public boolean apply(TimedData serverData)
			{
				return serverData.getKey().equals(key);
			}
		}));
	}

	public static void delete(TimedData data)
	{
		Data.TIMED_DATA.remove(data.getId());
	}

	public static void remove(String key, String subKey)
	{
		if(find(key, subKey) != null) delete(find(key, subKey));
	}

	/**
	 * Updates all timed data.
	 */
	public static void updateTimedData()
	{
		for(TimedData data : Collections2.filter(getAll(), new Predicate<TimedData>()
		{
			@Override
			public boolean apply(TimedData data)
			{
				return data.getExpiration() <= System.currentTimeMillis();
			}
		}))
			delete(data);
	}
}
