package com.censoredsoftware.demigods.engine.mythos;

import com.censoredsoftware.demigods.engine.data.Skill;
import com.google.common.base.Predicate;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.material.MaterialData;

import java.util.List;

public interface Ability
{
	public String getDeity();

	public String getName();

	public String getCommand();

	public int getCost();

	public int getDelay();

	public int getRepeat();

	public List<String> getDetails();

	public Skill.Type getType();

	public MaterialData getWeapon();

	public boolean hasWeapon();

	public Predicate<Player> getActionPredicate();

	public Listener getListener();

	public Runnable getRunnable();
}