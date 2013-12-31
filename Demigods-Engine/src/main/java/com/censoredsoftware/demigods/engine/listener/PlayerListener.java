package com.censoredsoftware.demigods.engine.listener;

import com.censoredsoftware.censoredlib.helper.QuitReasonHandler;
import com.censoredsoftware.demigods.engine.DemigodsPlugin;
import com.censoredsoftware.demigods.engine.battle.Battle;
import com.censoredsoftware.demigods.engine.data.DataManager;
import com.censoredsoftware.demigods.engine.language.Text;
import com.censoredsoftware.demigods.engine.player.DCharacter;
import com.censoredsoftware.demigods.engine.player.DPlayer;
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
			player.sendMessage(Text.RUNNING_DG_VERSION.english().replace("{version}", DemigodsPlugin.plugin().getDescription().getVersion()));
			player.sendMessage(Text.DG_FOR_MORE_INFORMATION.english());
		}

		// TODO First join book
		// if(!player.hasPlayedBefore()) player.getInventory().addItem(DivineItem.WELCOME_BOOK.getItem());

		// Notifications
		if(character != null && character.getMeta().hasNotifications())
		{
			int size = character.getMeta().getNotifications().size();
			player.sendMessage(size == 1 ? Text.UNREAD_NOTIFICATION.english() : Text.UNREAD_NOTIFICATIONS.english().replace("{size}", "" + size));
			player.sendMessage(Text.FIND_ALTAR_TO_VIEW_NOTIFICATIONS.english());
		}

		// Remove temp battle data
		if(DataManager.hasKeyTemp(player.getName(), "quit_during_battle"))
		{
			DataManager.removeTemp(player.getName(), "quit_during_battle");
			player.sendMessage(Text.WELCOME_BACK_IN_BATTLE.english());
		}

		// Alert of losing battle due to leaving
		if(DataManager.hasKeyTemp(player.getName(), "quit_during_battle_final"))
		{
			DataManager.removeTemp(player.getName(), "quit_during_battle_final");
			player.sendMessage(Text.WELCOME_BACK_BATTLE_LOST.english());
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerTeleport(PlayerTeleportEvent event)
	{
		if(!Zones.inNoDemigodsZone(event.getPlayer().getLocation()) && DPlayer.Util.isPraying(event.getPlayer())) DPlayer.Util.togglePraying(event.getPlayer(), false);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		final String name = event.getPlayer().getName();
		String message = Text.DISCONNECT_QUITTING.english().replace("{name}", name);
		switch(QuitReasonHandler.latestQuit)
		{
			case GENERIC_REASON:
				message = Text.DISCONNECT_GENERIC.english().replace("{name}", name);
				break;
			case SPAM:
				message = Text.DISCONNECT_SPAM.english().replace("{name}", name);
				break;
			case END_OF_STREAM:
				message = Text.DISCONNECT_EOS.english().replace("{name}", name);
				break;
			case OVERFLOW:
				message = Text.DISCONNECT_OVERFLOW.english().replace("{name}", name);
				break;
			case QUITTING:
				message = Text.DISCONNECT_QUITTING.english().replace("{name}", name);
				break;
			case TIMEOUT:
				message = Text.DISCONNECT_TIMEOUT.english().replace("{name}", name);
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
				DataManager.saveTemp(name, "quit_during_battle", true);
				Bukkit.getScheduler().scheduleSyncDelayedTask(DemigodsPlugin.plugin(), new BukkitRunnable()
				{
					@Override
					public void run()
					{
						if(!Bukkit.getOfflinePlayer(name).isOnline() && DataManager.hasKeyTemp(name, "quit_during_battle"))
						{
							Battle battle = Battle.Util.getBattle(loggingOff);
							battle.removeParticipant(loggingOff);
							DataManager.removeTemp(name, "quit_during_battle");
							battle.sendMessage(Text.DISCONNECT_DURING_BATTLE.english().replace("{name}", loggingOff.getName()));
							DataManager.saveTemp(name, "quit_during_battle_final", true);
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
			double maxhealth = character.getMaxHealth();
			event.getPlayer().setMaxHealth(maxhealth);
			event.getPlayer().setHealth(maxhealth);
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
