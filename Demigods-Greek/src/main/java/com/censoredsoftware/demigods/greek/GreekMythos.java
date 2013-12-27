package com.censoredsoftware.demigods.greek;

import com.censoredsoftware.censoredlib.trigger.Trigger;
import com.censoredsoftware.demigods.engine.deity.Alliance;
import com.censoredsoftware.demigods.engine.deity.Deity;
import com.censoredsoftware.demigods.engine.item.DivineItem;
import com.censoredsoftware.demigods.engine.mythos.Mythos;
import com.censoredsoftware.demigods.engine.mythos.MythosPlugin;
import com.censoredsoftware.demigods.engine.structure.Structure;
import com.censoredsoftware.demigods.greek.deity.GreekAlliance;
import com.censoredsoftware.demigods.greek.deity.GreekDeity;
import com.censoredsoftware.demigods.greek.item.GreekItem;
import com.censoredsoftware.demigods.greek.structure.GreekStructure;
import com.censoredsoftware.demigods.greek.trigger.DivinityUnbalanced;
import com.censoredsoftware.demigods.greek.trigger.NewPlayerNeedsHelp;
import com.censoredsoftware.demigods.greek.trigger.ProcessAltars;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.ServicePriority;

import java.util.HashSet;

public class GreekMythos extends MythosPlugin
{
	private static boolean PRIMARY = true;
	private static boolean lock = false;

	/**
	 * The Bukkit enable method.
	 */
	@Override
	public void onEnable()
	{
		getServer().getServicesManager().register(Mythos.class, this, this, ServicePriority.Highest); // not really sure how Bukkit handles these, presuming the same way as EventPriority
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
		return "Greek";
	}

	@Override
	public String getTagline()
	{
		return "Greek mythology, as described by Hesiod, Homer, and other Greek bards.";
	}

	@Override
	public String getAuthor()
	{
		return "_Alex & HmmmQuestionMark";
	}

	@Override
	public Boolean isPrimary()
	{
		return PRIMARY;
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
		return ImmutableSet.copyOf((DivineItem[]) GreekItem.values());
	}

	@Override
	public ImmutableCollection<Alliance> getAlliances()
	{
		return ImmutableSet.copyOf((Alliance[]) GreekAlliance.values());
	}

	@Override
	public ImmutableCollection<Deity> getDeities()
	{
		return ImmutableSet.copyOf(new HashSet<Deity>()
		{
			{
				add(GreekDeity.ZEUS);
				add(GreekDeity.POSEIDON);

				add(GreekDeity.PERSES);
				add(GreekDeity.OCEANUS);

				add(GreekDeity.CLOTHO);
				add(GreekDeity.LACHESIS);
				add(GreekDeity.ATROPOS);
			}
		});
	}

	@Override
	public ImmutableCollection<Structure> getStructures()
	{
		return ImmutableSet.copyOf(new HashSet<Structure>()
		{
			{
				add(GreekStructure.ALTAR);
				add(GreekStructure.OBELISK);
				add(GreekStructure.SHRINE);
			}
		});
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

	public ImmutableCollection<Trigger> getTriggers()
	{
		if(!isPrimary()) return ImmutableSet.of();
		return ImmutableSet.copyOf(Collections2.transform(Sets.newHashSet(GreekMythos.GreekTrigger.values()), new Function<GreekMythos.GreekTrigger, Trigger>()
		{
			@Override
			public Trigger apply(GreekMythos.GreekTrigger listedTrigger)
			{
				return listedTrigger.getTrigger();
			}
		}));
	}

	@Override
	public void setSecondary()
	{
		if(lock) return;
		PRIMARY = false;
	}

	@Override
	public void lock()
	{
		lock = true;
	}

	// Triggers
	public enum GreekTrigger
	{
		/**
		 * Balance related.
		 */
		DIVINITY_UNBALANCED(new DivinityUnbalanced()), NEW_PLAYER_NEEDS_HELP(new NewPlayerNeedsHelp()), PROCESS_ALTARS(new ProcessAltars());

		private Trigger trigger;

		private GreekTrigger(Trigger trigger)
		{
			this.trigger = trigger;
		}

		public Trigger getTrigger()
		{
			return trigger;
		}
	}
}
