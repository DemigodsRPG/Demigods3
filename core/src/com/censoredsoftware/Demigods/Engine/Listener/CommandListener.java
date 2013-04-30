package com.censoredsoftware.Demigods.Engine.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.censoredsoftware.Demigods.API.BattleAPI;
import com.censoredsoftware.Demigods.API.DeityAPI;
import com.censoredsoftware.Demigods.API.PlayerAPI;
import com.censoredsoftware.Demigods.API.ZoneAPI;
import com.censoredsoftware.Demigods.Engine.Demigods;

public class CommandListener implements Listener
{
	@EventHandler(priority = EventPriority.MONITOR)
	public static void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
	{
		String message = event.getMessage();
		message = message.substring(1);

		String[] args = message.split("\\s+");

		Player player = event.getPlayer();

		if(ZoneAPI.canTarget(player) && BattleAPI.isInAnyActiveBattle(PlayerAPI.getCurrentChar(player)))
		{
			if(BattleAPI.isBlockedCommand(args[0])) // TODO Battles currently don't end, so this would make everything unplayable.
			{
				// player.sendMessage(ChatColor.GRAY + "That command is blocked during a battle.");
				// event.setCancelled(true);
				// return;
			}
		}

		try
		{
			if(DeityAPI.invokeAbilityCommand(player, args[0], args.length == 2 && args[1].equalsIgnoreCase("bind")))
			{
				Demigods.message.info(event.getPlayer().getName() + " used the command: /" + message);
				event.setCancelled(true);
			}
		}
		catch(Exception e)
		{
			// Not a command
			e.printStackTrace();
		}
	}
}
