package com.censoredsoftware.demigods.engine.listener;

import com.censoredsoftware.demigods.engine.base.DemigodsCommand;
import com.censoredsoftware.demigods.engine.data.DPlayer;
import com.censoredsoftware.demigods.engine.util.Abilities;
import com.censoredsoftware.demigods.engine.util.Messages;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Set;

public class ChatListener implements Listener
{
	private static ImmutableSet<String> COMMANDS;

	public static void init()
	{
		Set<String> commands = Sets.newHashSet();
		for(DemigodsCommand command : DemigodsCommand.values())
			commands.addAll(command.getCommand().getCommands());
		commands.add("demigod");
		commands.add("dg");
		commands.add("c");
		commands.add("o");
		commands.add("l");
		commands.add("a");
		commands.add("n");
		COMMANDS = ImmutableSet.copyOf(commands);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
	{
		String message = event.getMessage();
		message = message.substring(1);
		String[] args = message.split("\\s+");
		Player player = event.getPlayer();

		if(Zones.inNoDemigodsZone(event.getPlayer().getLocation()))
		{
			if(COMMANDS.contains(args[0]))
			{
				player.sendMessage(ChatColor.GRAY + "Demigods is disabled in this world.");
				event.setCancelled(true);
			}
			return;
		}

		// Return if a character doesn't exist
		if(DPlayer.Util.getPlayer(event.getPlayer()).getCurrent() == null) return;

		// Process the command
		try
		{
			if(Abilities.invokeAbilityCommand(player, args[0]))
			{
				Messages.info(event.getPlayer().getName() + " used the command: /" + message);
				event.setCancelled(true);
			}
		}
		catch(Exception errored)
		{
			// Not a command
			Messages.logException(errored);
		}
	}
}
