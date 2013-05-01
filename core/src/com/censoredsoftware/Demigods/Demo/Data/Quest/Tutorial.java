package com.censoredsoftware.Demigods.Demo.Data.Quest;

import java.util.List;

import com.censoredsoftware.Demigods.Engine.Quest.Quest;
import com.censoredsoftware.Demigods.Engine.Quest.Task;

public class Tutorial extends Quest
{
	private static String name = "Tutorial", permission = "demigods.tutorial";
	private static List<String> about, accepted, complete, failed;
	private static Type type = Type.TUTORIAL;
	private static List<Task> tasks;

	public Tutorial()
	{
		super(name, permission, about, accepted, complete, failed, type, tasks);
	}
}
