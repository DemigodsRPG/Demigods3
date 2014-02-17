package com.demigodsrpg.demigods.engine.data;

import com.google.common.base.Supplier;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TempDataManager
{
	// -- TEMP DATA -- //

	// Temp Data
	static final Table<String, String, Object> TEMP = Tables.newCustomTable(new ConcurrentHashMap<String, Map<String, Object>>(), new Supplier<ConcurrentHashMap<String, Object>>()
	{
		@Override
		public ConcurrentHashMap<String, Object> get()
		{
			return new ConcurrentHashMap<>();
		}
	});

	public static boolean hasKeyTemp(String row, String column)
	{
		return TEMP.contains(row, column);
	}

	public static Object getValueTemp(String row, String column)
	{
		if(hasKeyTemp(row, column)) return TEMP.get(row, column);
		else return null;
	}

	public static void saveTemp(String row, String column, Object value)
	{
		TEMP.put(row, column, value);
	}

	public static void removeTemp(String row, String column)
	{
		if(hasKeyTemp(row, column)) TEMP.remove(row, column);
	}

	public static void clear()
	{
		TEMP.clear();
	}
}
