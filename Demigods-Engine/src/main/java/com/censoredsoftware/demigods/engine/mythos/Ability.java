package com.censoredsoftware.demigods.engine.mythos;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.material.MaterialData;

import com.censoredsoftware.demigods.engine.data.Skill;
import com.google.common.base.Predicate;

public interface Ability
{
	String getDeity();

	String getName();

	String getCommand();

	int getCost();

	int getDelay();

	int getRepeat();

	List<String> getDetails();

	Skill.Type getType();

	MaterialData getWeapon();

	boolean hasWeapon();

	Predicate<Player> getActionPredicate();

	Listener getListener();

	Runnable getRunnable();
}
