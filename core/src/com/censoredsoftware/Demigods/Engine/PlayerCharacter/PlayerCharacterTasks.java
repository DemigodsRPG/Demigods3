package com.censoredsoftware.Demigods.Engine.PlayerCharacter;

import java.util.HashMap;
import java.util.Map;

import com.censoredsoftware.Demigods.Engine.DemigodsData;
import com.censoredsoftware.Modules.Data.DataStubModule;

public class PlayerCharacterTasks implements DataStubModule
{
	private Map<String, Object> taskData;

	public PlayerCharacterTasks(Map map)
	{
		setMap(map);
		save(this);
	}

	public PlayerCharacterTasks(int id)
	{
		taskData = new HashMap();
		saveData("ID", id);
	}

	static void save(PlayerCharacterTasks tasks)
	{
		DemigodsData.characterTaskData.saveData(tasks.getID(), tasks);
	}

	public boolean containsKey(String key)
	{
		return taskData.get(key) != null && taskData.containsKey(key);
	}

	public Object getData(String key)
	{
		return taskData.get(key);
	}

	public void saveData(String key, Object data)
	{
		taskData.put(key, data);
	}

	public void removeData(String key)
	{
		if(!containsKey(key)) return;
		taskData.remove(key);
	}

	@Override
	public int getID()
	{
		return Integer.parseInt(getData("ID").toString());
	}

	@Override
	public Map getMap()
	{
		return taskData;
	}

	@Override
	public void setMap(Map map)
	{
		taskData = map;
	}

	public boolean isFinishedTask(String taskName)
	{
		return containsKey(taskName) && Boolean.parseBoolean(getData(taskName).toString());
	}

	public void finishTask(String taskName, boolean option)
	{
		saveData(taskName, option);
	}
}
