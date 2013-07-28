package com.censoredsoftware.Demigods.Engine.Command;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.censoredsoftware.Demigods.Engine.Element.Ability;
import com.censoredsoftware.Demigods.Engine.Player.DPlayer;
import com.censoredsoftware.Demigods.Engine.Utility.MessageUtility;

public class CommandListener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public static void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
	{
		// Return if a character doesn't exist
		if(DPlayer.Util.getPlayer(event.getPlayer()).getCurrent() == null) return;

		// Define variables
		String message = event.getMessage();
		message = message.substring(1);
		String[] args = message.split("\\s+");
		Player player = event.getPlayer();

		// Process the command
		try
		{
			if(Ability.Util.invokeAbilityCommand(player, args[0]))
			{
				MessageUtility.info(event.getPlayer().getName() + " used the command: /" + message);
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
