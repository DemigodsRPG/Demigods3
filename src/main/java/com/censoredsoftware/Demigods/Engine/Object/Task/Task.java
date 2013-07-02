package com.censoredsoftware.Demigods.Engine.Object.Task;

import org.bukkit.event.Listener;

public class Task
{
	private TaskInfo info;
	private Listener listener;

	public Task(TaskInfo info, Listener listener)
	{
		this.info = info;
		this.listener = listener;
	}

	public TaskInfo getInfo()
	{
		return info;
	}

	public Listener getListener()
	{
		return listener;
	}
}
