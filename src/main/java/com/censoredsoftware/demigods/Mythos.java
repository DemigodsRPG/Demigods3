package com.censoredsoftware.demigods;

import com.censoredsoftware.demigods.deity.Alliance;
import com.censoredsoftware.demigods.deity.Deity;
import com.censoredsoftware.demigods.greek.GreekMythos;
import com.censoredsoftware.demigods.structure.Structure;
import com.censoredsoftware.demigods.trigger.Trigger;
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

	// Default Greek Listeners
	public Set<Listener> getListeners()
	{
		return (Set<Listener>) Collections2.transform(Sets.newHashSet(GreekMythos.GreekListener.values()), new Function<GreekMythos.GreekListener, Listener>()
		{
			@Override
			public Listener apply(GreekMythos.GreekListener listedListener)
			{
				return listedListener.getListener();
			}
		});
	}

	// Default Greek Permissions
	public Set<Permission> getPermissions()
	{
		return (Set<Permission>) Collections2.transform(Sets.newHashSet(GreekMythos.GreekPermission.values()), new Function<GreekMythos.GreekPermission, Permission>()
		{
			@Override
			public Permission apply(GreekMythos.GreekPermission listedPermission)
			{
				return listedPermission.getPermission();
			}
		});
	}

	// Default Greek Triggers
	public Set<Trigger> getTriggers()
	{
		return (Set<Trigger>) Collections2.transform(Sets.newHashSet(GreekMythos.GreekTrigger.values()), new Function<GreekMythos.GreekTrigger, Trigger>()
		{
			@Override
			public Trigger apply(GreekMythos.GreekTrigger listedTrigger)
			{
				return listedTrigger.getTrigger();
			}
		});
	}
}
