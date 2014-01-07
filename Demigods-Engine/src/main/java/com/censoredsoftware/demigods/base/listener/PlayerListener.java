package com.censoredsoftware.demigods.base.listener;

import com.censoredsoftware.censoredlib.helper.QuitReasonHandler;
import com.censoredsoftware.demigods.engine.DemigodsPlugin;
import com.censoredsoftware.demigods.engine.data.Data;
import com.censoredsoftware.demigods.engine.data.serializable.Battle;
import com.censoredsoftware.demigods.engine.data.serializable.DCharacter;
import com.censoredsoftware.demigods.engine.data.serializable.DPlayer;
import com.censoredsoftware.demigods.engine.language.English;
import com.censoredsoftware.demigods.engine.util.Configs;
import com.censoredsoftware.demigods.engine.util.Messages;
import com.censoredsoftware.demigods.engine.util.Zones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListener implements Listener
{
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event)
	{
		Player player = event.getPlayer();
		try
		{
			DPlayer.Util.getPlayer(player);
		}
		catch(Exception whoops)
		{
			player.kickPlayer("Error finding your Mojang Id, please try again.");
			Messages.warning(player.getName() + " could not join the game due to a Mojang Id problem.");
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		if(Zones.inNoDemigodsZone(event.getPlayer().getLocation())) return;

		// Define variables
		Player player = event.getPlayer();
		DPlayer wrapper = DPlayer.Util.getPlayer(player);
		DCharacter character = wrapper.getCurrent();

		// Set their last login-time
		Long now = System.currentTimeMillis();
		wrapper.setLastLoginTime(now);

		// Check for changed usernames
		if(!player.getName().equals(wrapper.getPlayerName())) wrapper.setPlayerName(player.getName());

		// Set display name
		if(character != null && wrapper.canUseCurrent())
		{
			String name = character.getName();
			ChatColor color = character.getDeity().getColor();
			player.setDisplayName(color + name);
			player.setPlayerListName(color + name);
			if(character.isAlive())
			{
				event.getPlayer().setMaxHealth(character.getMaxHealth());
				event.getPlayer().setHealth(character.getHealth() >= character.getMaxHealth() ? character.getMaxHealth() : character.getHealth());
			}
		}

		// Demigods welcome message
		if(Configs.getSettingBoolean("misc.welcome_message"))
		{
			player.sendMessage(English.RUNNING_DG_VERSION.getLine().replace("{version}", DemigodsPlugin.plugin().getDescription().getVersion()));
			player.sendMessage(English.DG_FOR_MORE_INFORMATION.getLine());
		}

		// TODO First join book
		// if(!player.hasPlayedBefore()) player.getInventory().addItem(DivineItem.WELCOME_BOOK.getItem());

		// Notifications
		if(character != null && character.getMeta().hasNotifications())
		{
			int size = character.getMeta().getNotifications().size();
			player.sendMessage(size == 1 ? English.UNREAD_NOTIFICATION.getLine() : English.UNREAD_NOTIFICATIONS.getLine().replace("{size}", "" + size));
			player.sendMessage(English.FIND_ALTAR_TO_VIEW_NOTIFICATIONS.getLine());
		}

		// Remove temp battle data
		if(Data.hasKeyTemp(player.getName(), "quit_during_battle"))
		{
			Data.removeTemp(player.getName(), "quit_during_battle");
			player.sendMessage(English.WELCOME_BACK_IN_BATTLE.getLine());
		}

		// Alert of losing battle due to leaving
		if(Data.hasKeyTemp(player.getName(), "quit_during_battle_final"))
		{
			Data.removeTemp(player.getName(), "quit_during_battle_final");
			player.sendMessage(English.WELCOME_BACK_BATTLE_LOST.getLine());
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerTeleport(PlayerTeleportEvent event)
	{
		if(!Zones.inNoDemigodsZone(event.getPlayer().getLocation()) && DPlayer.Util.isPraying(event.getPlayer())) DPlayer.Util.togglePraying(event.getPlayer(), false);
	}

	// TODO @EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		final String name = event.getPlayer().getName();
		String message = English.DISCONNECT_QUITTING.getLine().replace("{name}", name);
		switch(QuitReasonHandler.latestQuit)
		{
			case GENERIC_REASON:
				message = English.DISCONNECT_GENERIC.getLine().replace("{name}", name);
				break;
			case SPAM:
				message = English.DISCONNECT_SPAM.getLine().replace("{name}", name);
				break;
			case END_OF_STREAM:
				message = English.DISCONNECT_EOS.getLine().replace("{name}", name);
				break;
			case OVERFLOW:
				message = English.DISCONNECT_OVERFLOW.getLine().replace("{name}", name);
				break;
			case QUITTING:
				message = English.DISCONNECT_QUITTING.getLine().replace("{name}", name);
				break;
			case TIMEOUT:
				message = English.DISCONNECT_TIMEOUT.getLine().replace("{name}", name);
				break;
		}
		event.setQuitMessage(message);
		if(Zones.inNoDemigodsZone(event.getPlayer().getLocation())) return;
		final DCharacter loggingOff = DPlayer.Util.getPlayer(event.getPlayer()).getCurrent();
		if(loggingOff != null)
		{
			loggingOff.setLocation(event.getPlayer().getLocation());
			if(Battle.Util.isInBattle(loggingOff))
			{
				Battle battle = Battle.Util.getBattle(loggingOff);
				battle.addDeath(loggingOff);
				Data.saveTemp(name, "quit_during_battle", true);
				Bukkit.getScheduler().scheduleSyncDelayedTask(DemigodsPlugin.plugin(), new BukkitRunnable()
				{
					@Override
					public void run()
					{
						if(!Bukkit.getOfflinePlayer(name).isOnline() && Data.hasKeyTemp(name, "quit_during_battle"))
						{
							Battle battle = Battle.Util.getBattle(loggingOff);
							battle.removeParticipant(loggingOff);
							Data.removeTemp(name, "quit_during_battle");
							battle.sendMessage(English.DISCONNECT_DURING_BATTLE.getLine().replace("{name}", loggingOff.getName()));
							Data.saveTemp(name, "quit_during_battle_final", true);
						}
					}
				}, 200);
			}
		}

		// Set their last logout-time
		Long now = System.currentTimeMillis();
		DPlayer.Util.getPlayer(event.getPlayer()).setLastLogoutTime(now);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		if(Zones.inNoDemigodsZone(event.getPlayer().getLocation())) return;
		DCharacter character = DPlayer.Util.getPlayer(event.getPlayer()).getCurrent();
		if(character != null)
		{
			character.setAlive(true);
			double maxHealth = character.getMaxHealth();
			event.getPlayer().setMaxHealth(maxHealth);
			event.getPlayer().setHealth(maxHealth);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		if(Zones.inNoDemigodsZone(event.getEntity().getLocation())) return;
		DCharacter character = DPlayer.Util.getPlayer(event.getEntity()).getCurrent();
		if(character != null) character.setAlive(false);
	}
}
