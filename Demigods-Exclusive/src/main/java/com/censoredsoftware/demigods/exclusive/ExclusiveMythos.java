package com.censoredsoftware.demigods.exclusive;

import com.censoredsoftware.censoredlib.trigger.Trigger;
import com.censoredsoftware.demigods.engine.deity.Alliance;
import com.censoredsoftware.demigods.engine.deity.Deity;
import com.censoredsoftware.demigods.engine.item.DivineItem;
import com.censoredsoftware.demigods.engine.mythos.MythosPlugin;
import com.censoredsoftware.demigods.engine.structure.Structure;
import com.google.common.collect.ImmutableSet;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;

import java.util.Collection;

public class ExclusiveMythos extends MythosPlugin
{
	/**
	 * The Bukkit enable method.
	 */
	@Override
	public void onEnable()
	{
		Exclusive.init();
	}

	/**
	 * The Bukkit disable method.
	 */
	@Override
	public void onDisable()
	{}

	@Override
	public String getTitle()
	{
		return "Exclusive";
	}

	@Override
	public String getTagline()
	{
		return "Exclusive enhancements for the official Demigods RPG server.";
	}

	@Override
	public String getAuthor()
	{
		return "_Alex & HmmmQuestionMark";
	}

	@Override
	public boolean isPrimary()
	{
		return false;
	}

	@Override
	public boolean allowSecondary()
	{
		return true;
	}

	@Override
	public String[] getIncompatible()
	{
		return new String[] {};
	}

	@Override
	public boolean useBaseGame()
	{
		return true;
	}

	@Override
	public Collection<DivineItem> getDivineItems()
	{
		return ImmutableSet.of();
	}

	@Override
	public Collection<Alliance> getAlliances()
	{
		return ImmutableSet.of();
	}

	@Override
	public Collection<Deity> getDeities()
	{
		return ImmutableSet.of();
	}

	@Override
	public Collection<Structure> getStructures()
	{
		return ImmutableSet.of();
	}

	public boolean levelSeperateSkills()
	{
		return true;
	}

	public Collection<Listener> getListeners()
	{
		return Exclusive.inst().LISTENERS;
	}

	public Collection<Permission> getPermissions()
	{
		return ImmutableSet.of();
	}

	public Collection<Trigger> getTriggers()
	{
		return ImmutableSet.of();
	}

	@Override
	public void setSecondary()
	{}
}
