package com.censoredsoftware.demigods.engine.event;

import com.censoredsoftware.demigods.engine.battle.Battle;
import com.censoredsoftware.demigods.engine.battle.Participant;
import com.censoredsoftware.demigods.engine.data.Data;
import com.censoredsoftware.demigods.engine.entity.player.DemigodsCharacter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class BattleDeathEvent extends Event implements Cancellable
{
	private static final HandlerList HANDLERS = new HandlerList();
	private boolean cancel = false;
	private UUID battle, character, killer;

	public BattleDeathEvent(Battle battle, Participant character)
	{
		this.battle = battle.getId();
		this.character = character.getRelatedCharacter().getId();
	}

	public BattleDeathEvent(Battle battle, Participant character, Participant killer)
	{
		this.battle = battle.getId();
		this.character = character.getRelatedCharacter().getId();
		this.killer = killer.getRelatedCharacter().getId();
	}

	public boolean isCancelled()
	{
		return cancel;
	}

	public void setCancelled(boolean cancel)
	{
		this.cancel = cancel;
	}

	public Battle getBattle()
	{
		if(Data.BATTLE.containsKey(battle)) return Battle.Util.get(battle);
		return null;
	}

	public DemigodsCharacter getCharacter()
	{
		if(Data.CHARACTER.containsKey(character)) return DemigodsCharacter.Util.load(character);
		return null;
	}

	public boolean hasKiller()
	{
		return getKiller() != null;
	}

	public DemigodsCharacter getKiller()
	{
		if(killer != null && Data.CHARACTER.containsKey(killer)) return DemigodsCharacter.Util.load(killer);
		return null;
	}

	@Override
	public HandlerList getHandlers()
	{
		return HANDLERS;
	}

	public static HandlerList getHandlerList()
	{
		return HANDLERS;
	}
}
