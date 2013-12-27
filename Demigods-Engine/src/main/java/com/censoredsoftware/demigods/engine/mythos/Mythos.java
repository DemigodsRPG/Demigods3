package com.censoredsoftware.demigods.engine.mythos;

import com.censoredsoftware.censoredlib.trigger.Trigger;
import com.censoredsoftware.demigods.engine.deity.Alliance;
import com.censoredsoftware.demigods.engine.deity.Deity;
import com.censoredsoftware.demigods.engine.item.DivineItem;
import com.censoredsoftware.demigods.engine.structure.Structure;
import com.google.common.collect.ImmutableCollection;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;

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

	public ImmutableCollection<Alliance> getAlliances();

	public ImmutableCollection<Deity> getDeities();

	public ImmutableCollection<Structure> getStructures();

	public Boolean levelSeperateSkills();

	public ImmutableCollection<Listener> getListeners();

	public ImmutableCollection<Permission> getPermissions();

	public ImmutableCollection<Trigger> getTriggers();

	public void setSecondary();

	public void lock();
}
