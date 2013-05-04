package com.censoredsoftware.Demigods.Demo.Data.Quest;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.censoredsoftware.Demigods.API.PlayerAPI;
import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.Quest.Quest;
import com.censoredsoftware.Demigods.Engine.Quest.Task;
import com.censoredsoftware.Demigods.Engine.Quest.TaskInfo;

public class Tutorial extends Quest
{
	private static String name = "Tutorial", permission = "demigods.tutorial";
	private static List<String> about = new ArrayList<String>()
	{
		{
			add("About."); // TODO This.
		}
	}, accepted = new ArrayList<String>()
	{
		{
			add("Accepted.");
		}
	}, complete = new ArrayList<String>()
	{
		{
			add("Complete.");
		}
	}, failed = new ArrayList<String>()
	{
		{
			add("Failed.");
		}
	};
	private static Type type = Type.TUTORIAL;
	private static List<Task> tasks = new ArrayList<Task>()
	{
		{
			add(new TutorialTask(name, permission, about, accepted, complete, failed, type));
		}
	};

	public Tutorial()
	{
		super(name, permission, about, accepted, complete, failed, type, tasks);
	}
}

class TutorialTask extends Task
{
	private static String name = "Welcome to Demigods!";
	private static int order = 0;
	private static double reward = 50.0, penalty = 0;
	private static Listener listener = new Listener()
	{
		@EventHandler(priority = EventPriority.MONITOR)
		private void onPlayerJoin(PlayerJoinEvent event)
		{
			Player player = event.getPlayer();
			if(player.hasPlayedBefore()) return;

			PlayerCharacter character = PlayerAPI.getCurrentChar(player);
			if(character == null || character.getTasks().isFinishedTask(name)) return;

			Demigods.message.tagged(player, name);
			Demigods.message.tagged(player, "Use " + ChatColor.YELLOW + "/dg" + ChatColor.WHITE + " for more information.");

			character.getTasks().finishTask(name, true);
		}
	};

	public TutorialTask(String quest, String permission, List<String> about, List<String> accepted, List<String> complete, List<String> failed, Quest.Type type)
	{
		super(new TaskInfo(name, quest, permission, order, reward, penalty, about, accepted, complete, failed, type, TaskInfo.Subtype.QUEST), listener);
	}
}
