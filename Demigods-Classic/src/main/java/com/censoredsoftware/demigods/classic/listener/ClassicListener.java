package com.censoredsoftware.demigods.classic.listener;

import com.censoredsoftware.demigods.base.listener.*;
import org.bukkit.event.Listener;

public enum ClassicListener
{
	CHAT(new ChatListener()), ENTITY(new EntityListener()), FLAG(new FlagListener()), LEGACY_BATTLE(new ClassicBattleListener()), LEGACY_LEVEL(new ClassicLevelListener()), PLAYER(new PlayerListener()), TRIBUTE(new TributeListener());

	private Listener listener;

	private ClassicListener(Listener listener)
	{
		this.listener = listener;
	}

	public Listener getListener()
	{
		return listener;
	}
}