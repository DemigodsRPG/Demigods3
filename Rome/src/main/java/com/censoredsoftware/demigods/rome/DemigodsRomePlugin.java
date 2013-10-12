package com.censoredsoftware.demigods.rome;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.Mythos;
import com.censoredsoftware.demigods.data.ThreadManager;
import com.censoredsoftware.demigods.deity.Alliance;
import com.censoredsoftware.demigods.deity.Deity;
import com.censoredsoftware.demigods.rome.deity.RomanAlliance;
import com.censoredsoftware.demigods.rome.deity.RomanDeity;
import com.censoredsoftware.demigods.structure.ListedStructure;
import com.censoredsoftware.demigods.structure.Structure;
import com.censoredsoftware.demigods.trigger.Trigger;
import com.censoredsoftware.demigods.util.Messages;
import com.google.common.collect.Sets;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public class DemigodsRomePlugin extends JavaPlugin
{
	/**
	 * The Bukkit enable method.
	 */
	@Override
	public void onEnable()
	{
		getServer().getServicesManager().register(Mythos.class, new Mythos()
		{
			@Override
			public String getName()
			{
				return "Roman";
			}

			@Override
			public String getDescription()
			{
				return "Roman mythology.";
			}

			@Override
			public String getAuthor()
			{
				return "HmmmQuestionMark";
			}

			@Override
			public Set<Alliance> getAlliances()
			{
				return Sets.newHashSet((Alliance[]) RomanAlliance.values());
			}

			@Override
			public Set<Deity> getDeities()
			{
				return Sets.newHashSet((Deity[]) RomanDeity.values());
			}

			@Override
			public Set<Structure> getStructures()
			{
				return Sets.newHashSet((Structure[]) ListedStructure.values());
			}

			@Override
			public Set<Listener> getListeners()
			{
				return Sets.newHashSet();
			}

			@Override
			public Set<Permission> getPermissions()
			{
				return Sets.newHashSet();
			}

			@Override
			public Set<Trigger> getTriggers()
			{
				return Sets.newHashSet(ThreadManager.ListedTrigger.getTriggers());
			}
		}, this, ServicePriority.Highest);

		Messages.info("Successfully enabled.");
	}

	/**
	 * The Bukkit disable method.
	 */
	@Override
	public void onDisable()
	{
		getServer().getPluginManager().disablePlugin(Demigods.PLUGIN);
		Messages.info("Successfully disabled.");
	}
}
