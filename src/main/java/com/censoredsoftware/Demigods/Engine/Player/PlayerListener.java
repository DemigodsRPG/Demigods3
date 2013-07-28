package com.censoredsoftware.demigods.engine.player;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.utility.ConfigUtility;

public class PlayerListener implements Listener
{
	private static QuitReasonHandler quitReasonFilter = new QuitReasonHandler();

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		// Define Variables
		Player player = event.getPlayer();
		DPlayer wrapper = DPlayer.Util.getPlayer(player);
		DCharacter character = wrapper.getCurrent();

		// Set their lastlogintime
		Long now = System.currentTimeMillis();
		wrapper.setLastLoginTime(now);

		// Set Displayname
		if(character != null && wrapper.canUseCurrent())
		{
			String name = character.getName();
			ChatColor color = character.getDeity().getInfo().getColor();
			player.setDisplayName(color + name + ChatColor.RESET);
			player.setPlayerListName(color + name + ChatColor.RESET);
			event.getPlayer().setMaxHealth(character.getMaxHealth());
			event.getPlayer().setHealth(character.getHealth());
		}

		if(ConfigUtility.getSettingBoolean("misc.welcome_message"))
		{
			player.sendMessage(ChatColor.GRAY + "This server is running demigods version: " + ChatColor.YELLOW + Demigods.plugin.getDescription().getVersion());
			player.sendMessage(ChatColor.GRAY + "Type " + ChatColor.GREEN + "/dg" + ChatColor.GRAY + " for more information.");
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerTeleport(PlayerTeleportEvent event)
	{
		// Define variables
		Player player = event.getPlayer();

		if(DPlayer.Util.isPraying(player)) DPlayer.Util.togglePraying(player, false);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		String name = event.getPlayer().getName();
		String message = ChatColor.YELLOW + name + " has left the game.";
		switch(quitReasonFilter.getLatestQuitReason())
		{
			case GENERIC_REASON:
				message = ChatColor.YELLOW + name + " has either quit or crashed.";
				break;
			case SPAM:
				message = ChatColor.YELLOW + name + " has disconnected due to spamming.";
				break;
			case END_OF_STREAM:
				message = ChatColor.YELLOW + name + " has lost connection.";
				break;
			case OVERFLOW:
				message = ChatColor.YELLOW + name + " has disconnected due to overload.";
				break;
			case QUITTING:
				message = ChatColor.YELLOW + name + " has quit.";
				break;
			case TIMEOUT:
				message = ChatColor.YELLOW + name + " has disconnected due to timeout.";
				break;
		}
		event.setQuitMessage(message);
		DCharacter loggingOff = DPlayer.Util.getPlayer(event.getPlayer()).getCurrent();
		if(loggingOff != null) loggingOff.setLocation(event.getPlayer().getLocation());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerRespawn(PlayerRespawnEvent event) // TODO Is this working?
	{
		DPlayer wrapper = DPlayer.Util.getPlayer(event.getPlayer());
		if(wrapper.getCurrent() != null)
		{
			double maxhealth = wrapper.getCurrent().getMaxHealth();
			event.getPlayer().setMaxHealth(maxhealth);
			event.getPlayer().setHealth(maxhealth);
		}
	}
}
