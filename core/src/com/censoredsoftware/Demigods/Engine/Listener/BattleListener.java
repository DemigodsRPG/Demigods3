package com.censoredsoftware.Demigods.Engine.Listener;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.censoredsoftware.Demigods.API.BattleAPI;
import com.censoredsoftware.Demigods.API.CharacterAPI;
import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Event.Battle.BattleCombineEvent;
import com.censoredsoftware.Demigods.Engine.Event.Battle.BattleEndEvent;
import com.censoredsoftware.Demigods.Engine.Event.Battle.BattleParticipateEvent;
import com.censoredsoftware.Demigods.Engine.Event.Battle.BattleStartEvent;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedBattle;
import com.google.common.base.Joiner;

public class BattleListener implements Listener
{
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBattleStart(BattleStartEvent event)
	{
		PlayerCharacter attacker = event.getAttacking();
		PlayerCharacter defending = event.getDefending();
		String attackerAlliance = attacker.getTeam();
		String defendingAlliance = defending.getTeam();

		Demigods.message.broadcast(ChatColor.RED + "BETA: " + ChatColor.YELLOW + "A battle has begun between the " + ChatColor.GREEN + attackerAlliance + "s" + ChatColor.YELLOW + " and the " + ChatColor.GREEN + defendingAlliance + "s" + ChatColor.YELLOW + ".");
		Demigods.message.broadcast(ChatColor.RED + "BETA: " + ChatColor.GREEN + attacker.getName() + ChatColor.YELLOW + " took the first hit against " + ChatColor.GREEN + defending.getName() + ChatColor.YELLOW + ".");
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBattleParticipate(BattleParticipateEvent event)
	{
		// TODO
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBattleEnd(BattleEndEvent event)
	{
		int battleID = event.getID();
		TrackedBattle battle = BattleAPI.getBattle(battleID);
		Demigods.message.broadcast(ChatColor.RED + "BETA: " + ChatColor.YELLOW + "The battle started by " + ChatColor.GREEN + CharacterAPI.getChar(battle.getWhoStarted()).getName() + ChatColor.YELLOW + " has ended.");
		ArrayList<Integer> charIDs = battle.getCharIDs();
		ArrayList<String> charNames = new ArrayList<String>();
		for(int charID : charIDs)
			charNames.add(CharacterAPI.getChar(charID).getName());
		Demigods.message.broadcast(ChatColor.RED + "BETA: " + ChatColor.YELLOW + "The battle involved: " + ChatColor.AQUA + Joiner.on(", ").join(charNames) + ChatColor.YELLOW + ".");
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBattleCombine(BattleCombineEvent event)
	{
		int battleID = event.getBattleID();
		TrackedBattle combined = BattleAPI.getBattle(battleID);
		TrackedBattle first = event.getFirst();
		ArrayList<Integer> charIDs = combined.getCharIDs();
		ArrayList<String> charNames = new ArrayList<String>();
		for(int charID : charIDs)
			charNames.add(CharacterAPI.getChar(charID).getName());
		Demigods.message.broadcast(ChatColor.RED + "BETA: " + ChatColor.YELLOW + "The battle started by " + ChatColor.GREEN + CharacterAPI.getChar(first.getWhoStarted()).getName() + ChatColor.YELLOW + " has merged with another battle!");
		Demigods.message.broadcast(ChatColor.RED + "BETA: " + ChatColor.YELLOW + "The battle now involves the following: " + ChatColor.AQUA + Joiner.on(", ").join(charNames) + ChatColor.YELLOW + ".");
	}
}
