package com.censoredsoftware.Demigods.Engine.Quest;

import java.util.List;

public class Quest
{
	private String name, permission;
	private List<String> about, accepted, complete, failed;
	private Type type;
	private List<Task> tasks;

	public Quest(String name, String permission, List<String> about, List<String> accepted, List<String> complete, List<String> failed, Type type, List<Task> tasks)
	{
		this.name = name;
		this.permission = permission;
		this.about = about;
		this.accepted = accepted;
		this.complete = complete;
		this.failed = failed;
		this.type = type;
		this.tasks = tasks;
	}

	public enum Type
	{
		PASSIVE, DEMO, TUTORIAL;
	}

	public String getName()
	{
		return name;
	}

	public String getPermission()
	{
		return permission;
	}

	public List<String> getAbout()
	{
		return about;
	}

	public List<String> getAccepted()
	{
		return accepted;
	}

	public List<String> getComplete()
	{
		return complete;
	}

	public List<String> getFailed()
	{
		return failed;
	}

	public List<Task> getTasks()
	{
		return tasks;
	}

	public Quest.Type getType()
	{
		return type;
	}
}
