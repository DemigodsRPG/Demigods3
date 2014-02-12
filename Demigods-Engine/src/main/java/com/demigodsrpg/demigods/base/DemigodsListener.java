package com.demigodsrpg.demigods.base;

import com.demigodsrpg.demigods.base.listener.*;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
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

	public static ImmutableSet<Listener> listeners()
	{
		return ImmutableSet.copyOf(Collections2.transform(Sets.newHashSet(values()), new Function<DemigodsListener, Listener>()
		{
			@Override
			public Listener apply(DemigodsListener dListener)
			{
				return dListener.getListener();
			}
		}));
	}
}
