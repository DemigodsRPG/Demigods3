package com.censoredsoftware.demigods.engine.data.util;

import com.censoredsoftware.censoredlib.data.ServerData;
import com.censoredsoftware.demigods.engine.data.DataManager;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;

import java.util.Set;
import java.util.UUID;

public class ServerDatas
{
	public static ServerData get(UUID id)
	{
		return DataManager.serverData.get(id);
	}

	public static Set<ServerData> getAll()
	{
		return Sets.newHashSet(DataManager.serverData.values());
	}

	public static ServerData find(String key, String subKey)
	{
		if(findByKey(key) == null) return null;

		for(ServerData data : findByKey(key))
			if(data.getSubKey().equals(subKey)) return data;

		return null;
	}

	public static Set<ServerData> findByKey(final String key)
	{
		return Sets.newHashSet(Collections2.filter(getAll(), new Predicate<ServerData>()
		{
			@Override
			public boolean apply(ServerData serverData)
			{
				return serverData.getKey().equals(key);
			}
		}));
	}

	public static void delete(ServerData data)
	{
		DataManager.serverData.remove(data.getId());
	}

	public static void remove(String key, String subKey)
	{
		if(find(key, subKey) != null) delete(find(key, subKey));
	}
}
