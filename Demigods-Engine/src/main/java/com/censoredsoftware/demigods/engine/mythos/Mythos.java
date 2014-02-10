package com.censoredsoftware.demigods.engine.mythos;

import com.censoredsoftware.censoredlib.helper.CommandManager;
import com.censoredsoftware.censoredlib.trigger.Trigger;
import com.censoredsoftware.censoredlib.util.Items;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.Iterables;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;

import java.util.NoSuchElementException;

public interface Mythos
{
	String getTitle();

	String getTagline();

	String getAuthor();

	Boolean isPrimary();

	Boolean allowSecondary();

	String[] getIncompatible();

	Boolean useBaseGame();

	ImmutableCollection<DivineItem> getDivineItems();

	DivineItem getDivineItem(String itemName);

	DivineItem getDivineItem(ItemStack itemStack);

	boolean itemHasFlag(ItemStack itemStack, DivineItem.Flag flag);

	ImmutableCollection<Alliance> getAlliances();

	Alliance getAlliance(String allianceName);

	ImmutableCollection<Deity> getDeities();

	Deity getDeity(String deityName);

	ImmutableCollection<StructureType> getStructures();

	StructureType getStructure(String structureName);

	Boolean levelSeperateSkills();

	ImmutableCollection<Listener> getListeners();

	ImmutableCollection<Permission> getPermissions();

	ImmutableCollection<CommandManager> getCommands();

	ImmutableCollection<Trigger> getTriggers();

	void setSecondary();

	void lock();

	public static class Util
	{
		public static DivineItem getDivineItem(Mythos mythos, final String itemName)
		{
			try
			{
				return Iterables.find(mythos.getDivineItems(), new Predicate<DivineItem>()
				{
					@Override
					public boolean apply(DivineItem foundItem)
					{
						return foundItem.getName().equals(itemName);
					}
				});
			}
			catch(NoSuchElementException ignored)
			{
				// ignored
			}
			return null;
		}

		public static DivineItem getDivineItem(Mythos mythos, final ItemStack itemStack)
		{
			try
			{
				return Iterables.find(mythos.getDivineItems(), new Predicate<DivineItem>()
				{
					@Override
					public boolean apply(DivineItem foundItem)
					{
						return Items.areEqualIgnoreEnchantments(foundItem.getItem(), itemStack);
					}
				});
			}
			catch(NoSuchElementException ignored)
			{
				// ignored
			}
			return null;
		}

		public static boolean itemHasFlag(Mythos mythos, ItemStack itemStack, DivineItem.Flag flag)
		{
			DivineItem divineItem = getDivineItem(mythos, itemStack);
			return divineItem != null && divineItem.getFlags().contains(flag);
		}

		public static Alliance getAlliance(Mythos mythos, final String allianceName)
		{
			try
			{
				return Iterables.find(mythos.getAlliances(), new Predicate<Alliance>()
				{
					@Override
					public boolean apply(Alliance alliance)
					{
						return alliance.getName().equals(allianceName);
					}
				});
			}
			catch(NoSuchElementException ignored)
			{
				// ignored
			}
			return null;
		}

		public static Deity getDeity(Mythos mythos, final String deityName)
		{
			try
			{
				return Iterables.find(mythos.getDeities(), new Predicate<Deity>()
				{
					@Override
					public boolean apply(Deity deity)
					{
						return deity.getName().equals(deityName);
					}
				});
			}
			catch(NoSuchElementException ignored)
			{
				// ignored
			}
			return null;
		}

		public static StructureType getStructure(Mythos mythos, final String structureName)
		{
			try
			{
				return Iterables.find(mythos.getStructures(), new Predicate<StructureType>()
				{
					@Override
					public boolean apply(StructureType structureType)
					{
						return structureName.equals(structureType.getName());
					}
				});
			}
			catch(NoSuchElementException ignored)
			{
				// ignored
			}
			return null;
		}
	}
}
