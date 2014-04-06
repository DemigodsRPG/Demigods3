package com.demigodsrpg.demigods.exclusive;

import com.censoredsoftware.library.helper.CommandManager;
import com.demigodsrpg.demigods.engine.DemigodsPlugin;
import com.demigodsrpg.demigods.engine.mythos.Mythos;
import com.demigodsrpg.demigods.exclusive.command.WorldCommands;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.scheduler.BukkitRunnable;

public class Exclusive
{
	private static final Exclusive INST = new Exclusive();
	private static final CommandManager.Registry COMMAND_REGISTRY = new CommandManager.Registry(ExclusiveMythos.inst());

	private static int worldTaskId = -1;

	private Exclusive()
	{
	}

	private static Exclusive inst()
	{
		return INST;
	}

	public static boolean init()
	{
		// Register Mythos
		Bukkit.getServer().getServicesManager().register(Mythos.class, ExclusiveMythos.inst(), ExclusiveMythos.inst(), ServicePriority.Highest); // not really sure how Bukkit handles these, presuming the same way as EventPriority

		// Register commands
		COMMAND_REGISTRY.registerManager(ExclusiveMythos.inst().getCommands());

		// Load worlds
		worldTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(ExclusiveMythos.inst(), new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if(DemigodsPlugin.getReady())
				{
					WorldCommands.Util.loadHandledWorlds();
					Bukkit.getScheduler().cancelTask(worldTaskId);
				}
			}
		}, 1, 1);

		return true;
	}
}