package com.censoredsoftware.demigods.engine.ability;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import com.censoredsoftware.demigods.engine.player.Skill;

public interface Ability
{
	public String getDeity();

	public String getName();

	public String getCommand();

	public String getPermission();

	public int getCost();

	public int getDelay();

	public int getRepeat();

	public List<String> getDetails();

	public Skill.Type getType();

	public Material getWeapon();

	public boolean hasWeapon();

	public Listener getListener();

	public BukkitRunnable getRunnable();
}
