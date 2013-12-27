package com.censoredsoftware.demigods.engine.mythos;

import com.censoredsoftware.censoredlib.trigger.Trigger;
import com.censoredsoftware.demigods.engine.deity.Alliance;
import com.censoredsoftware.demigods.engine.deity.Deity;
import com.censoredsoftware.demigods.engine.item.DivineItem;
import com.censoredsoftware.demigods.engine.structure.Structure;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;

import java.util.Collection;

public interface Mythos
{
	public abstract String getTitle();

	public abstract String getTagline();

	public abstract String getAuthor();

	public abstract boolean isPrimary();

	public abstract boolean allowSecondary();

	public abstract String[] getIncompatible();

	public abstract boolean useBaseGame();

	public abstract Collection<DivineItem> getDivineItems();

	public abstract Collection<Alliance> getAlliances();

	public abstract Collection<Deity> getDeities();

	public abstract Collection<Structure> getStructures();

	public boolean levelSeperateSkills();

	public Collection<Listener> getListeners();

	public Collection<Permission> getPermissions();

	public Collection<Trigger> getTriggers();

	public void setSecondary();

	public void lock();
}
