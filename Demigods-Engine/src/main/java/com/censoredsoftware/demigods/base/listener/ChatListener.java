package com.censoredsoftware.demigods.base.listener;

import com.censoredsoftware.censoredlib.helper.CommandManager;
import com.censoredsoftware.demigods.base.DemigodsCommand;
import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.DemigodsPlugin;
import com.censoredsoftware.demigods.engine.data.serializable.DPlayer;
import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.censoredsoftware.demigods.engine.util.Messages;
import com.censoredsoftware.demigods.engine.util.Zones;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class ChatListener implements Listener
{
	private static final CommandManager.Registry COMMAND_REGISTRY = new CommandManager.Registry(DemigodsPlugin.plugin());

	public static void init()
	{
		COMMAND_REGISTRY.registerManager(DemigodsCommand.commands());
		COMMAND_REGISTRY.registerNamesOnly(Demigods.mythos().getCommands());
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
			if(COMMAND_REGISTRY.getCommandsAndAliases().contains(args[0]))
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
			if(Ability.Util.bindAbility(player, args[0]))
			{
				Messages.info(event.getPlayer().getName() + " used the command: /" + message);
				event.setCancelled(true);
			}
		}
		catch(Exception errored)
		{
			// Not a command
			errored.printStackTrace();;
		}
	}
}
