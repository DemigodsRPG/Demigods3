package com.censoredsoftware.demigods.exclusive;

import com.censoredsoftware.censoredlib.helper.CommandManager;
import com.censoredsoftware.demigods.engine.mythos.Mythos;
import com.censoredsoftware.demigods.exclusive.command.WorldCommands;
import com.censoredsoftware.demigods.exclusive.data.ExData;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public class Exclusive
{
	private static final Exclusive INST = new Exclusive();
	private static final CommandManager.Registry COMMAND_REGISTRY = new CommandManager.Registry(ExclusiveMythos.inst());

	private Exclusive()
	{}

	private static Exclusive inst()
	{
		return INST;
	}

	public static boolean init()
	{
		// Register Mythos
		Bukkit.getServer().getServicesManager().register(Mythos.class, ExclusiveMythos.inst(), ExclusiveMythos.inst(), ServicePriority.Highest); // not really sure how Bukkit handles these, presuming the same way as EventPriority

		// Load data
		ExData.init();

		// Register commands
		COMMAND_REGISTRY.registerManager(ExclusiveMythos.inst().getCommands());

		// Load worlds
		WorldCommands.Util.loadHandledWorlds(); // FIXME Doesn't work yet.

		return true;
	}
}
