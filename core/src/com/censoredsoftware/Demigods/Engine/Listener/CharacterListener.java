package com.censoredsoftware.Demigods.Engine.Listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Event.Character.CharacterBetrayCharacterEvent;
import com.censoredsoftware.Demigods.Engine.Event.Character.CharacterKillCharacterEvent;
import com.censoredsoftware.Demigods.Engine.Event.Character.CharacterKillstreakEvent;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;

public class CharacterListener implements Listener
{
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
