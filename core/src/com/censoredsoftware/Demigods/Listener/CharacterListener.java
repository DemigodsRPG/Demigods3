package com.censoredsoftware.Demigods.Listener;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.censoredsoftware.Demigods.API.DeityAPI;
import com.censoredsoftware.Demigods.API.DemigodAPI;
import com.censoredsoftware.Demigods.API.PlayerAPI;
import com.censoredsoftware.Demigods.Demigod.Demigod;
import com.censoredsoftware.Demigods.Demigods;
import com.censoredsoftware.Demigods.DemigodsData;
import com.censoredsoftware.Demigods.Event.Demigod.DemigodBetrayDemigodEvent;
import com.censoredsoftware.Demigods.Event.Demigod.DemigodCreateEvent;
import com.censoredsoftware.Demigods.Event.Demigod.DemigodKillDemigodEvent;
import com.censoredsoftware.Demigods.Event.Demigod.DemigodKillstreakEvent;
import com.censoredsoftware.Modules.Persistence.Event.LoadStubYAMLEvent;
import com.censoredsoftware.Modules.PlayerCharacter.PlayerCharacter;

public class CharacterListener implements Listener
{
	@EventHandler(priority = EventPriority.LOWEST)
	public void onCharacterLoad(LoadStubYAMLEvent event)
	{
		Demigods.message.broadcast("TEST");
		if(!event.getPluginName().equals(Demigods.demigods.getName())) return;

		if(event.getPath().equals("core") && event.getDataName().equals("character_data"))
		{
			int charID = event.getID();
			new PlayerCharacter(event.getData());
			Demigods.message.broadcast("Loaded: " + DemigodAPI.getChar(charID).getName());
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onCharacterCreation(DemigodCreateEvent event)
	{
		if(event.isCancelled()) return;

		OfflinePlayer player = event.getOwner();
		String chosenName = event.getName();
		String chosenDeity = event.getDeity();

		Demigod character = DemigodAPI.create(player, chosenName, chosenDeity);

		// Remove temporary data
		DemigodsData.tempPlayerData.removeData(player, "temp_createchar");

		if(player.isOnline())
		{
			Player online = player.getPlayer();
			online.setDisplayName(DeityAPI.getDeityColor(chosenDeity) + chosenName + ChatColor.WHITE);
			online.setPlayerListName(DeityAPI.getDeityColor(chosenDeity) + chosenName + ChatColor.WHITE);

			online.sendMessage(ChatColor.GREEN + "You have been accepted into the lineage of " + chosenDeity + "!");
			online.getWorld().strikeLightningEffect(online.getLocation());

			for(int i = 0; i < 20; i++)
				online.getWorld().spawn(online.getLocation(), ExperienceOrb.class);

			// Switch current character
			PlayerAPI.changeCurrentChar(player, character.getID());
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onCharacterKillstreak(DemigodKillstreakEvent event)
	{
		Demigod character = event.getCharacter();
		int killstreak = event.getKills();

		Demigods.message.broadcast(ChatColor.YELLOW + character.getName() + ChatColor.GRAY + " is on a killstreak of " + ChatColor.RED + killstreak + ChatColor.GRAY + " kills.");
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerKillPlayer(DemigodKillDemigodEvent event)
	{
		Demigod attacker = event.getCharacter();
		Demigod killed = event.getKilled();
		String attackerAlliance = "Mortal";
		if(attacker != null) attackerAlliance = attacker.getTeam();
		String killedAlliance = "Mortal";
		if(killed != null) killedAlliance = killed.getTeam();

		if(killed == null && attacker == null) Demigods.message.broadcast(ChatColor.YELLOW + "A mortal" + ChatColor.GRAY + " was slain by " + ChatColor.YELLOW + "another mortal" + ChatColor.GRAY + ".");
		else if(killed == null && attacker != null) Demigods.message.broadcast(ChatColor.YELLOW + "A mortal" + ChatColor.GRAY + " was slain by " + ChatColor.YELLOW + attacker.getName() + ChatColor.GRAY + " of the " + attackerAlliance + " alliance.");
		else if(killed != null && attacker == null) Demigods.message.broadcast(ChatColor.YELLOW + killed.getName() + ChatColor.GRAY + " of the " + killedAlliance + " alliance was slain by " + ChatColor.YELLOW + "a mortal" + ChatColor.GRAY + ".");
		else if(killed != null && attacker != null) Demigods.message.broadcast(ChatColor.YELLOW + killed.getName() + ChatColor.GRAY + " of the " + killedAlliance + " alliance was slain by " + ChatColor.YELLOW + attacker.getName() + ChatColor.GRAY + " of the " + attackerAlliance + " alliance.");
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerBetrayPlayer(DemigodBetrayDemigodEvent event)
	{
		Demigod attacker = event.getCharacter();
		Demigod killed = event.getKilled();
		String alliance = event.getAlliance();

		if(alliance != "Mortal") Demigods.message.broadcast(ChatColor.YELLOW + killed.getName() + ChatColor.GRAY + " was betrayed by " + ChatColor.YELLOW + attacker.getName() + ChatColor.GRAY + " of the " + alliance + " alliance.");
		else Demigods.message.broadcast(ChatColor.GRAY + "A few worthless mortals killed each other.");
	}
}
