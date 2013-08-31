package com.censoredsoftware.demigods.helper;

import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Set;

public abstract class ListedCommand implements TabExecutor
{
	public abstract Set<String> getCommands();

	public abstract boolean processCommand(CommandSender sender, Command command, final String[] args);

	public List<String> processTab(CommandSender sender, Command command, final String[] args)
	{
		return Lists.newArrayList();
	}

	public void register(JavaPlugin plugin, boolean tab)
	{
		for(String command : getCommands())
		{
			plugin.getCommand(command).setExecutor(this);
			if(tab) plugin.getCommand(command).setTabCompleter(this);
		}
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
