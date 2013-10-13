package com.censoredsoftware.demigods.engine.helper;

import com.censoredsoftware.demigods.Demigods;
import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;
import java.util.Set;

public abstract class WrappedCommand implements TabExecutor
{
	public WrappedCommand(boolean tab)
	{
		for(String command : getCommands())
		{
			Demigods.PLUGIN.getCommand(command).setExecutor(this);
			if(tab) Demigods.PLUGIN.getCommand(command).setTabCompleter(this);
		}
	}

	public abstract Set<String> getCommands();

	public abstract boolean processCommand(CommandSender sender, Command command, final String[] args);

	public List<String> processTab(CommandSender sender, Command command, final String[] args)
	{
		return Lists.newArrayList();
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, final String[] args)
	{
		return processTab(sender, command, args);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		return processCommand(sender, command, args);
	}
}
