package com.censoredsoftware.Demigods.Engine.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.Ability.Ability;

public class CommandListener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public static void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
	{
		String message = event.getMessage();
		message = message.substring(1);
		String[] args = message.split("\\s+");
		Player player = event.getPlayer();

		try
		{
			if(Ability.invokeAbilityCommand(player, args[0], args.length == 2 && args[1].equalsIgnoreCase("bind")))
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
