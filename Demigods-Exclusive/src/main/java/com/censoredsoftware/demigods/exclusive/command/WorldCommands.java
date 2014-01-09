package com.censoredsoftware.demigods.exclusive.command;

import com.censoredsoftware.censoredlib.helper.CommandManager;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class WorldCommands extends CommandManager
{
	public static final WorldCommands INST = new WorldCommands();

	public static WorldCommands inst()
	{
		return INST;
	}

	private WorldCommands()
	{}

	@Override
	public ImmutableSet<String> getCommandNames()
	{
		return ImmutableSet.of();
	}

	@Override
	public ImmutableList<Sub> getSubCommands()
	{
		return ImmutableList.of();
	}

	@Override
	public boolean always(CommandSender sender, Command command, String label, String[] args)
	{
		return false;
	}
}
