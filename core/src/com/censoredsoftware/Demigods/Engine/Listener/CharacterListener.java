package com.censoredsoftware.Demigods.Engine.Listener;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.censoredsoftware.Demigods.API.CharacterAPI;
import com.censoredsoftware.Demigods.API.DeityAPI;
import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.DemigodsData;
import com.censoredsoftware.Demigods.Engine.Event.Character.CharacterBetrayCharacterEvent;
import com.censoredsoftware.Demigods.Engine.Event.Character.CharacterCreateEvent;
import com.censoredsoftware.Demigods.Engine.Event.Character.CharacterKillCharacterEvent;
import com.censoredsoftware.Demigods.Engine.Event.Character.CharacterKillstreakEvent;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedPlayer;

public class CharacterListener implements Listener
{
	@EventHandler(priority = EventPriority.LOWEST)
	public void onCharacterCreation(CharacterCreateEvent event)
	{
		if(event.isCancelled()) return;

		OfflinePlayer player = event.getOwner();
		String chosenName = event.getName();
		String chosenDeity = event.getDeity();

		PlayerCharacter character = CharacterAPI.create(player, chosenName, chosenDeity);

		// Remove temporary data
		DemigodsData.removeTemp(player.getName(), "temp_createchar");

		if(player.isOnline())
		{
			Player online = player.getPlayer();
			online.setDisplayName(DeityAPI.getDeity(chosenDeity).getInfo().getColor() + chosenName + ChatColor.WHITE);
			online.setPlayerListName(DeityAPI.getDeity(chosenDeity).getInfo().getColor() + chosenName + ChatColor.WHITE);

			online.sendMessage(ChatColor.GREEN + "You have been accepted into the lineage of " + chosenDeity + "!");
			online.getWorld().strikeLightningEffect(online.getLocation());

			for(int i = 0; i < 20; i++)
				online.getWorld().spawn(online.getLocation(), ExperienceOrb.class);

			// Switch current character
			TrackedPlayer.getTracked(player).setCurrent(character);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onCharacterKillstreak(CharacterKillstreakEvent event)
	{
		PlayerCharacter character = event.getCharacter();
		int killstreak = event.getKills();

		Demigods.message.broadcast(ChatColor.YELLOW + character.getName() + ChatColor.GRAY + " is on a killstreak of " + ChatColor.RED + killstreak + ChatColor.GRAY + " kills.");
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerKillPlayer(CharacterKillCharacterEvent event)
	{
		PlayerCharacter attacker = event.getCharacter();
		PlayerCharacter killed = event.getKilled();
		String attackerAlliance = "Mortal";
		if(attacker != null) attackerAlliance = attacker.getAlliance();
		String killedAlliance = "Mortal";
		if(killed != null) killedAlliance = killed.getAlliance();

		if(killed == null && attacker == null) Demigods.message.broadcast(ChatColor.YELLOW + "A mortal" + ChatColor.GRAY + " was slain by " + ChatColor.YELLOW + "another mortal" + ChatColor.GRAY + ".");
		else if(killed == null && attacker != null) Demigods.message.broadcast(ChatColor.YELLOW + "A mortal" + ChatColor.GRAY + " was slain by " + ChatColor.YELLOW + attacker.getName() + ChatColor.GRAY + " of the " + attackerAlliance + " alliance.");
		else if(killed != null && attacker == null) Demigods.message.broadcast(ChatColor.YELLOW + killed.getName() + ChatColor.GRAY + " of the " + killedAlliance + " alliance was slain by " + ChatColor.YELLOW + "a mortal" + ChatColor.GRAY + ".");
		else if(killed != null && attacker != null) Demigods.message.broadcast(ChatColor.YELLOW + killed.getName() + ChatColor.GRAY + " of the " + killedAlliance + " alliance was slain by " + ChatColor.YELLOW + attacker.getName() + ChatColor.GRAY + " of the " + attackerAlliance + " alliance.");
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerBetrayPlayer(CharacterBetrayCharacterEvent event)
	{
		PlayerCharacter attacker = event.getCharacter();
		PlayerCharacter killed = event.getKilled();
		String alliance = event.getAlliance();

		if(alliance != "Mortal") Demigods.message.broadcast(ChatColor.YELLOW + killed.getName() + ChatColor.GRAY + " was betrayed by " + ChatColor.YELLOW + attacker.getName() + ChatColor.GRAY + " of the " + alliance + " alliance.");
		else Demigods.message.broadcast(ChatColor.GRAY + "A few worthless mortals killed each other.");
	}
}
