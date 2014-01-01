package com.censoredsoftware.demigods.legacy.listener;

import com.censoredsoftware.demigods.engine.listener.*;
import org.bukkit.event.Listener;

public enum LegacyListener
{
	CHAT(new ChatListener()), ENTITY(new EntityListener()), FLAG(new FlagListener()), LEGACY_BATTLE(new LegacyBattleListener()), LEGACY_LEVEL(new LegacyLevelListener()), PLAYER(new PlayerListener()), TRIBUTE(new TributeListener());

	private Listener listener;

	private LegacyListener(Listener listener)
	{
		this.listener = listener;
	}

	public Listener getListener()
	{
		return listener;
	}
}
