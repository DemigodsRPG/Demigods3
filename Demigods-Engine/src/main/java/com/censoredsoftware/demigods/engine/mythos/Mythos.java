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
	public String getTitle();

	public String getTagline();

	public String getAuthor();

	public Boolean isPrimary();

	public Boolean allowSecondary();

	public String[] getIncompatible();

	public Boolean useBaseGame();

	public Collection<DivineItem> getDivineItems();

	public Collection<Alliance> getAlliances();

	public Collection<Deity> getDeities();

	public Collection<Structure> getStructures();

	public Boolean levelSeperateSkills();

	public Collection<Listener> getListeners();

	public Collection<Permission> getPermissions();

	public Collection<Trigger> getTriggers();

	public void setSecondary();

	public void lock();
}
