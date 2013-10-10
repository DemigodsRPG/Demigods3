package com.censoredsoftware.demigods.helper;

import com.censoredsoftware.demigods.Demigods;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public abstract class CommandWrapper implements TabExecutor
{
	private WrappedCommandListItem[] wrappedCommandList;

	public CommandWrapper(boolean tab, WrappedCommandListItem[] wrappedCommandList)
	{
		this.wrappedCommandList = wrappedCommandList;
		for(String commandName : getCommandNames())
		{
			Demigods.PLUGIN.getCommand(commandName).setExecutor(this);
			if(tab) Demigods.PLUGIN.getCommand(commandName).setTabCompleter(this);
		}
	}

	public Set<WrappedCommand> getCommands()
	{
		return Sets.newHashSet(Collections2.transform(Sets.newHashSet(wrappedCommandList), new Function<WrappedCommandListItem, WrappedCommand>()
		{
			@Override
			public WrappedCommand apply(WrappedCommandListItem wrappedCommandListItem)
			{
				return wrappedCommandListItem.getCommand();
			}
		}));
	}

	public Collection<String> getCommandNames()
	{
		return Collections2.transform(getCommands(), new Function<WrappedCommand, String>()
		{
			@Override
			public String apply(WrappedCommand found)
			{
				return found.getName();
			}
		});
	}

	public List<String> processTab(CommandSender sender, Command command, String[] args)
	{
		return Lists.newArrayList();
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
	{
		return processTab(sender, command, args);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		try
		{
			final String commandName = command.getName();
			return Iterables.find(getCommands(), new Predicate<WrappedCommand>()
			{
				@Override
				public boolean apply(WrappedCommand found)
				{
					return found.getName().equalsIgnoreCase(commandName);
				}
			}).processCommand(sender, args);
		}
		catch(NoSuchElementException ignored)
		{}
		return false;
	}

	public static abstract class WrappedCommand
	{
		public abstract String getName();

		public abstract Set<WrappedSubCommand> getSubCommands();

		/**
		 * If this returns false, cancel the command.
		 */
		public boolean shouldContinue(CommandSender sender, String[] args)
		{
			return true;
		}

		public abstract boolean processMain(CommandSender sender, String[] args);

		public boolean processCommand(CommandSender sender, final String[] args)
		{
			if(!shouldContinue(sender, args)) return true;
			if(args.length < 1 || getSubCommands() == null || getSubCommands().isEmpty() || args.length > 0 && !Iterables.any(getSubCommands(), new Predicate<WrappedSubCommand>()
			{
				@Override
				public boolean apply(WrappedSubCommand found)
				{
					return found.getName().equalsIgnoreCase(args[0]);
				}
			})) return processMain(sender, args);
			try
			{
				final String subCommandName = args[0];
				Iterables.find(getSubCommands(), new Predicate<WrappedSubCommand>()
				{
					@Override
					public boolean apply(WrappedSubCommand found)
					{
						return found.getName().equalsIgnoreCase(subCommandName);
					}
				}).processSubCommand(sender, args);
				return true;
			}
			catch(NoSuchElementException ignored)
			{}
			return false;
		}
	}

	public static interface WrappedSubCommand
	{
		public String getName();

		public boolean processSubCommand(CommandSender sender, String[] args);
	}

	public static interface WrappedCommandListItem
	{
		public WrappedCommand getCommand();
	}
}
