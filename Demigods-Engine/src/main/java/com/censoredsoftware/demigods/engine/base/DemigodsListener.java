package com.censoredsoftware.demigods.engine.base;

import com.censoredsoftware.demigods.engine.listener.*;
import org.bukkit.event.Listener;

public enum DemigodsListener
{
	ABILITY(new AbilityListener()), BATTLE(new BattleListener()), CHAT(new ChatListener()), ENTITY(new EntityListener()), FLAG(new FlagListener()), GRIEF(new GriefListener()), MOVE(new MoveListener()), PLAYER(new PlayerListener()), TRIBUTE(new TributeListener());

	private Listener listener;

	private DemigodsListener(Listener listener)
	{
		this.listener = listener;
	}

	public Listener getListener()
	{
		return listener;
	}
}
