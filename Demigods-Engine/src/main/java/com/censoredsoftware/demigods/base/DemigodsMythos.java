package com.censoredsoftware.demigods.base;

import com.censoredsoftware.censoredlib.helper.WrappedCommand;
import com.censoredsoftware.censoredlib.trigger.Trigger;
import com.censoredsoftware.demigods.engine.mythos.*;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;

public class DemigodsMythos implements Mythos
{
	@Override
	public String getTitle()
	{
		return "Demigods";
	}

	@Override
	public String getTagline()
	{
		return "The base game for Demigods.";
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
		return ImmutableSet.of(); // TODO (Documentation books, admin wands, etc).
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
		// There are no default Alliances.
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
		// There are no default Deities.
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
		return DemigodsStructure.structures();
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
		return DemigodsListener.listeners();
	}

	public ImmutableCollection<Permission> getPermissions()
	{
		return DemigodsPermission.permissions();
	}

	@Override
	public ImmutableCollection<WrappedCommand> getCommands()
	{
		return DemigodsCommand.commands();
	}

	public ImmutableCollection<Trigger> getTriggers()
	{
		return ImmutableSet.of(); // TODO
	}

	@Override
	public void setSecondary()
	{}

	@Override
	public void lock()
	{}
}