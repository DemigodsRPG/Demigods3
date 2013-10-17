package com.censoredsoftware.demigods.engine;

import com.censoredsoftware.demigods.engine.deity.Alliance;
import com.censoredsoftware.demigods.engine.deity.Deity;
import com.censoredsoftware.demigods.engine.structure.Structure;
import com.censoredsoftware.demigods.engine.trigger.Trigger;
import com.censoredsoftware.demigods.greek.GreekMythos;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;

import java.util.Set;

public abstract class Mythos
{
	public abstract String getName();

	public abstract String getDescription();

	public abstract String getAuthor();

	public abstract Set<Alliance> getAlliances();

	public abstract Set<Deity> getDeities();

	public abstract Set<Structure> getStructures();

	// Default Greek Skill Leveling
	public boolean levelSeperateSkills()
	{
		return true;
	}

	// Default Greek Listeners
	public Set<Listener> getListeners()
	{
		return Sets.newHashSet(Collections2.transform(Sets.newHashSet(GreekMythos.GreekListener.values()), new Function<GreekMythos.GreekListener, Listener>()
		{
			@Override
			public Listener apply(GreekMythos.GreekListener listedListener)
			{
				return listedListener.getListener();
			}
		}));
	}

	// Default Greek Permissions
	public Set<Permission> getPermissions()
	{
		return Sets.newHashSet(Collections2.transform(Sets.newHashSet(GreekMythos.GreekPermission.values()), new Function<GreekMythos.GreekPermission, Permission>()
		{
			@Override
			public Permission apply(GreekMythos.GreekPermission listedPermission)
			{
				return listedPermission.getPermission();
			}
		}));
	}

	// Default Greek Triggers
	public Set<Trigger> getTriggers()
	{
		return Sets.newHashSet(Collections2.transform(Sets.newHashSet(GreekMythos.GreekTrigger.values()), new Function<GreekMythos.GreekTrigger, Trigger>()
		{
			@Override
			public Trigger apply(GreekMythos.GreekTrigger listedTrigger)
			{
				return listedTrigger.getTrigger();
			}
		}));
	}
}
