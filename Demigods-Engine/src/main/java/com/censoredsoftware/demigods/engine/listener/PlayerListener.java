package com.censoredsoftware.demigods.engine.listener;

import com.censoredsoftware.censoredlib.helper.QuitReasonHandler;
import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.DemigodsPlugin;
import com.censoredsoftware.demigods.engine.battle.Battle;
import com.censoredsoftware.demigods.engine.data.DataManager;
import com.censoredsoftware.demigods.engine.language.Translation;
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
			player.sendMessage(Demigods.LANGUAGE.getText(Translation.Text.RUNNING_DG_VERSION).replace("{version}", DemigodsPlugin.inst().getDescription().getVersion()));
			player.sendMessage(Demigods.LANGUAGE.getText(Translation.Text.DG_FOR_MORE_INFORMATION));
		}

		// TODO First join book
		// if(!player.hasPlayedBefore()) player.getInventory().addItem(DivineItem.WELCOME_BOOK.getItem());

		// Notifications
		if(character != null && character.getMeta().hasNotifications())
		{
			int size = character.getMeta().getNotifications().size();
			player.sendMessage(size == 1 ? Demigods.LANGUAGE.getText(Translation.Text.UNREAD_NOTIFICATION) : Demigods.LANGUAGE.getText(Translation.Text.UNREAD_NOTIFICATIONS).replace("{size}", "" + size));
			player.sendMessage(Demigods.LANGUAGE.getText(Translation.Text.FIND_ALTAR_TO_VIEW_NOTIFICATIONS));
		}

		// Remove temp battle data
		if(DataManager.hasKeyTemp(player.getName(), "quit_during_battle"))
		{
			DataManager.removeTemp(player.getName(), "quit_during_battle");
			player.sendMessage(Demigods.LANGUAGE.getText(Translation.Text.WELCOME_BACK_IN_BATTLE));
		}

		// Alert of losing battle due to leaving
		if(DataManager.hasKeyTemp(player.getName(), "quit_during_battle_final"))
		{
			DataManager.removeTemp(player.getName(), "quit_during_battle_final");
			player.sendMessage(Demigods.LANGUAGE.getText(Translation.Text.WELCOME_BACK_BATTLE_LOST));
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
		String message = Demigods.LANGUAGE.getText(Translation.Text.DISCONNECT_QUITTING).replace("{name}", name);
		switch(QuitReasonHandler.latestQuit)
		{
			case GENERIC_REASON:
				message = Demigods.LANGUAGE.getText(Translation.Text.DISCONNECT_GENERIC).replace("{name}", name);
				break;
			case SPAM:
				message = Demigods.LANGUAGE.getText(Translation.Text.DISCONNECT_SPAM).replace("{name}", name);
				break;
			case END_OF_STREAM:
				message = Demigods.LANGUAGE.getText(Translation.Text.DISCONNECT_EOS).replace("{name}", name);
				break;
			case OVERFLOW:
				message = Demigods.LANGUAGE.getText(Translation.Text.DISCONNECT_OVERFLOW).replace("{name}", name);
				break;
			case QUITTING:
				message = Demigods.LANGUAGE.getText(Translation.Text.DISCONNECT_QUITTING).replace("{name}", name);
				break;
			case TIMEOUT:
				message = Demigods.LANGUAGE.getText(Translation.Text.DISCONNECT_TIMEOUT).replace("{name}", name);
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
				Bukkit.getScheduler().scheduleSyncDelayedTask(DemigodsPlugin.inst(), new BukkitRunnable()
				{
					@Override
					public void run()
					{
						if(!Bukkit.getOfflinePlayer(name).isOnline() && DataManager.hasKeyTemp(name, "quit_during_battle"))
						{
							Battle battle = Battle.Util.getBattle(loggingOff);
							battle.removeParticipant(loggingOff);
							DataManager.removeTemp(name, "quit_during_battle");
							battle.sendMessage(Demigods.LANGUAGE.getText(Translation.Text.DISCONNECT_DURING_BATTLE).replace("{name}", loggingOff.getName()));
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
