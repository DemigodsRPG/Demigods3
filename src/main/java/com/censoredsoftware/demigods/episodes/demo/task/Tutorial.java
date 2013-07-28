package com.censoredsoftware.demigods.episodes.demo.task;

import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.element.Task;
import com.censoredsoftware.demigods.engine.player.DCharacter;
import com.censoredsoftware.demigods.engine.player.DPlayer;
import com.censoredsoftware.demigods.episodes.demo.item.Book;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

public class Tutorial extends Task.List
{
	private static final String name = "Tutorial";
	private static final String permission = "demigods.tutorial";
	private static final java.util.List about = new ArrayList<String>(1)
	{
		{
			add("About."); // TODO This.
		}
	};
	private static final java.util.List accepted = new ArrayList<String>(1)
	{
		{
			add("Accepted.");
		}
	};
	private static final java.util.List complete = new ArrayList<String>(1)
	{
		{
			add("Complete.");
		}
	};
	private static final java.util.List failed = new ArrayList<String>(1)
	{
		{
			add("Failed.");
		}
	};
	private static final Type type = Type.TUTORIAL;
	private static final java.util.List tasks = new ArrayList<Task>()
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
	private static final String name = "Welcome to demigods!";
	private static final int order = 0;
	private static final double reward = 50.0;
	private static final double penalty = 0;
	private static final Listener listener = new Listener()
	{
		@EventHandler(priority = EventPriority.MONITOR)
		private void onPlayerJoin(PlayerJoinEvent event)
		{
			Player player = event.getPlayer();

			DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();
			if(character == null || character.getMeta().isFinishedTask(name)) return;

            Demigods.message.tagged(player, "Welcome to demigods, " + character.getDeity().getInfo().getColor() + character.getName() + ChatColor.RESET + "!");

			player.getInventory().setItem(player.getInventory().firstEmpty(), Book.FIRST_JOIN.getBook());

			character.getMeta().finishTask(name, true);
		}
	};

	public TutorialTask(String quest, String permission, java.util.List about, java.util.List accepted, java.util.List complete, java.util.List failed, List.Type type)
	{
		super(new Info(name, quest, permission, order, reward, penalty, about, accepted, complete, failed, type, Info.Subtype.QUEST), listener);
	}
}
