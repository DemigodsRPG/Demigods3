package com.censoredsoftware.demigods;

import com.censoredsoftware.demigods.deity.Alliance;
import com.censoredsoftware.demigods.deity.Deity;
import com.censoredsoftware.demigods.structure.Structure;
import com.censoredsoftware.demigods.trigger.Trigger;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;

import java.util.Set;

public interface Mythos
{
	public String getName();

	public Set<String> getAuthors();

	public Set<Alliance> getAlliances();

	public Set<Deity> getDeities();

	public Set<Structure> getStructures();

	public Set<Listener> getListeners();

	public Set<Permission> getPermissions();

	public Set<Trigger> getTriggers();
}
