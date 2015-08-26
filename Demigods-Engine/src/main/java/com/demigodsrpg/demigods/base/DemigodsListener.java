package com.demigodsrpg.demigods.base;

import com.demigodsrpg.demigods.base.listener.*;
import com.google.common.collect.ImmutableSet;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum DemigodsListener {
    ABILITY(new AbilityListener()), BATTLE(new BattleListener()), CHAT(new ChatListener()),
    ENTITY(new EntityListener()), FLAG(new FlagListener()), GRIEF(new GriefListener()), MOVE(new MoveListener()),
    PLAYER(new PlayerListener()), TRIBUTE(new TributeListener());

    private Listener listener;

    DemigodsListener(Listener listener) {
        this.listener = listener;
    }

    public Listener getListener() {
        return listener;
    }

    public static ImmutableSet<Listener> listeners() {
        return ImmutableSet.copyOf(Arrays.asList(values()).stream().map(DemigodsListener::getListener).
                collect(Collectors.toSet()));
    }
}
