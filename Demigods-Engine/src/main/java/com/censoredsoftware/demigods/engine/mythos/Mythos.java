package com.censoredsoftware.demigods.engine.mythos;

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
	public String getTitle();

	public String getTagline();

	public String getAuthor();

	public Boolean isPrimary();

	public Boolean allowSecondary();

	public String[] getIncompatible();

	public Boolean useBaseGame();

	public ImmutableCollection<DivineItem> getDivineItems();

	public DivineItem getDivineItem(String itemName);

	public DivineItem getDivineItem(ItemStack itemStack);

	public boolean itemHasFlag(ItemStack itemStack, DivineItem.Flag flag);

	public ImmutableCollection<Alliance> getAlliances();

	public Alliance getAlliance(String allianceName);

	public ImmutableCollection<Deity> getDeities();

	public Deity getDeity(String deityName);

	public ImmutableCollection<Structure> getStructures();

	public Structure getStructure(String structureName);

	public Boolean levelSeperateSkills();

	public ImmutableCollection<Listener> getListeners();

	public ImmutableCollection<Permission> getPermissions();

	public ImmutableCollection<Trigger> getTriggers();

	public void setSecondary();

	public void lock();

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

		public static Structure getStructure(Mythos mythos, final String structureName)
		{
			try
			{
				return Iterables.find(mythos.getStructures(), new Predicate<Structure>()
				{
					@Override
					public boolean apply(Structure structure)
					{
						return structure.getName().equals(structureName);
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
