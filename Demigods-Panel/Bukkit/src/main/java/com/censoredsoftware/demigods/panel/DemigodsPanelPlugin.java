package com.censoredsoftware.demigods.panel;

import com.censoredsoftware.censoredlib.helper.CommandManager;
import com.censoredsoftware.censoredlib.trigger.Trigger;
import com.censoredsoftware.demigods.engine.mythos.*;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;

public class DemigodsPanelPlugin extends MythosPlugin
{
	private static DemigodsPanelPlugin inst;

	public static DemigodsPanelPlugin inst()
	{
		return inst;
	}

	/**
	 * The Bukkit enable method.
	 */
	@Override
	public void onEnable()
	{
		inst = this;
		// TODO Connect to panel here.
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
		return "Web Panel";
	}

	@Override
	public String getTagline()
	{
		return "Official Panel for Demigods RPG.";
	}

	@Override
	public String getAuthor()
	{
		return "_Alex & HmmmQuestionMark";
	}

	@Override
	public Boolean isPrimary()
	{
		return false;
	}

	@Override
	public Boolean allowSecondary()
	{
		return true;
	}

	@Override
	public String[] getIncompatible()
	{
		return new String[] {};
	}

	@Override
	public Boolean useBaseGame()
	{
		return true;
	}

	@Override
	public ImmutableCollection<DivineItem> getDivineItems()
	{
		return ImmutableSet.of();
	}

	@Override
	public DivineItem getDivineItem(String itemName)
	{
		return Mythos.Util.getDivineItem(this, itemName);
	}

	@Override
	public DivineItem getDivineItem(final ItemStack itemStack)
	{
		return Mythos.Util.getDivineItem(this, itemStack);
	}

	@Override
	public boolean itemHasFlag(ItemStack itemStack, DivineItem.Flag flag)
	{
		return Mythos.Util.itemHasFlag(this, itemStack, flag);
	}

	@Override
	public ImmutableCollection<Alliance> getAlliances()
	{
		return ImmutableSet.of();
	}

	@Override
	public Alliance getAlliance(final String allianceName)
	{
		return Mythos.Util.getAlliance(this, allianceName);
	}

	@Override
	public ImmutableCollection<Deity> getDeities()
	{
		return ImmutableSet.of();
	}

	@Override
	public Deity getDeity(final String deityName)
	{
		return Mythos.Util.getDeity(this, deityName);
	}

	@Override
	public ImmutableCollection<Structure> getStructures()
	{
		return ImmutableSet.of();
	}

	@Override
	public Structure getStructure(final String structureName)
	{
		return Mythos.Util.getStructure(this, structureName);
	}

	public Boolean levelSeperateSkills()
	{
		return true;
	}

	public ImmutableCollection<Listener> getListeners()
	{
		return ImmutableSet.of();
	}

	public ImmutableCollection<Permission> getPermissions()
	{
		return ImmutableSet.of();
	}

	@Override
	public ImmutableCollection<CommandManager> getCommands()
	{
		return ImmutableSet.of();
	}

	public ImmutableCollection<Trigger> getTriggers()
	{
		return ImmutableSet.of();
	}

	@Override
	public void setSecondary()
	{}

	@Override
	public void lock()
	{}
}
