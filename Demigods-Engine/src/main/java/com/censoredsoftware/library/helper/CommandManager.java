package com.censoredsoftware.library.helper;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;

public abstract class CommandManager implements CommandExecutor
{
	/**
	 * Registry for a plugin's commands.
	 */
	public static class Registry
	{
		private final CensoredJavaPlugin plugin;
		private final Set<String> commandsAndAliases;

		public Registry(CensoredJavaPlugin plugin)
		{
			this.plugin = plugin;
			commandsAndAliases = Sets.newHashSet();
		}

		/**
		 * Register some managers, and store the command names and aliases for future retrieval.
		 * 
		 * @param managers The manager being registered.
		 */
		public void registerNamesOnly(Collection<CommandManager> managers)
		{
			for(CommandManager manager : managers)
			{
				for(String command : manager.getCommandNames())
					commandsAndAliases.add(command);
				commandsAndAliases.addAll(manager.getAliases());
			}
		}

		/**
		 * Register a manager, and store the command names and aliases for future retrieval.
		 * 
		 * @param manager The manager being registered.
		 */
		public void registerManager(CommandManager manager)
		{
			for(String command : manager.getCommandNames())
			{
				if(plugin.getCommand(command) != null) plugin.getCommand(command).setExecutor(manager);
				commandsAndAliases.add(command);
			}

			commandsAndAliases.addAll(manager.getAliases());
		}

		/**
		 * Register some managers, and store the command names and aliases for future retrieval.
		 * 
		 * @param managers The manager being registered.
		 */
		public void registerManager(Collection<CommandManager> managers)
		{
			for(CommandManager manager : managers)
			{
				for(String command : manager.getCommandNames())
				{
					plugin.getCommand(command).setExecutor(manager);
					commandsAndAliases.add(command);
				}

				commandsAndAliases.addAll(manager.getAliases());
			}
		}

		/**
		 * Return an immutable set of the names of all commands and aliases registered.
		 * 
		 * @return Said set.
		 */
		public ImmutableSet<String> getCommandsAndAliases()
		{
			return ImmutableSet.copyOf(commandsAndAliases);
		}
	}

	private static final String OOPS = ChatColor.RED + "Oops... that isn't a known command!";
	public static final Sub WRONG_ARGUMENTS = new Sub()
	{
		@Override
		public String getName()
		{
			return "";
		}

		@Override
		public String getParentCommand()
		{
			return "";
		}

		@Override
		public int getMinArgs()
		{
			return 0;
		}

		@Override
		public boolean matches(Command command, String label, String[] args)
		{
			return true;
		}

		@Override
		public boolean execute(CommandSender sender, String label, String[] args)
		{
			sender.sendMessage(OOPS);
			return false;
		}
	};

	public abstract ImmutableSet<String> getCommandNames();

	public ImmutableSet<String> getAliases()
	{
		ImmutableSet.Builder<String> builder = ImmutableSet.builder();
		for(Command command : getCommands())
			builder.addAll(command.getAliases());
		return builder.build();
	}

	public ImmutableSet<Command> getCommands()
	{
		return ImmutableSet.copyOf(Collections2.transform(getCommandNames(), new Function<String, Command>()
		{
			@Override
			public Command apply(String name)
			{
				return Bukkit.getPluginCommand(name);
			}
		}));
	}

	public abstract ImmutableList<Sub> getSubCommands();

	public abstract boolean always(CommandSender sender, Command command, String label, String[] args);

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		// Run the pre-conditions that are always checked.
		if(!always(sender, command, label, args) || args.length < 1) return true;

		// Find the best sub-command
		Sub found = Iterables.find(getSubCommands(), new Predicate<Sub>()
		{
			@Override
			public boolean apply(@Nullable Sub sub)
			{
				return sub != null && sub.matches(command, label, args) && !sub.allowSender(sender).equals(Sub.Setting.SKIP);
			}
		}, WRONG_ARGUMENTS);

		// Process said sub-command
		if(found.allowSender(sender).equals(Sub.Setting.FAIL))
		{
			sender.sendMessage(found.getFailMessage());
			return false;
		}
		return found.execute(sender, label, args);
	}

	/**
	 * Sub-commands!
	 */
	public static abstract class Sub
	{
		private static final String DEFAULT_FAIL = ChatColor.RED + "You can't do that.";

		/**
		 * The name of this sub-command.
		 * 
		 * @return Said name.
		 */
		public abstract String getName();

		/**
		 * The usage information for this sub-command.
		 * 
		 * @return Said info.
		 */
		public String getUsage()
		{
			return "/" + getParentCommand() + " " + getName() + (getMinArgs() > 1 ? " ..." : "");
		}

		/**
		 * Get the parent command to this sub-command.
		 * 
		 * @return Said parent command.
		 */
		public abstract String getParentCommand();

		/**
		 * Get the minimum required arguments for this sub-command.
		 * 
		 * @return Said minimum.
		 */
		public abstract int getMinArgs();

		/**
		 * Return the lazy fail message.
		 * 
		 * @return Said message.
		 */
		public String getFailMessage()
		{
			return DEFAULT_FAIL;
		}

		/**
		 * Check if the sender is allowed to use this command.
		 * 
		 * @param sender The sender being checked.
		 * @return Allow setting.
		 */
		public Setting allowSender(CommandSender sender)
		{
			return Setting.ALLOW;
		}

		/**
		 * Check if the sub command matches the requirements.
		 * 
		 * If multiple commands match, the first one in the list will be executed.
		 * 
		 * @param command The command.
		 * @param label The label/alias in use.
		 * @param args The arguments of this command.
		 * @return This sub-command is a match.
		 */
		public boolean matches(Command command, String label, String[] args)
		{
			return getParentCommand().equals(command.getName()) && args.length >= getMinArgs() && getName().equalsIgnoreCase(args[0]);
		}

		/**
		 * Execute the sub-command.
		 * 
		 * @param sender The sender of this command.
		 * @param label The label/alias in use.
		 * @param args The arguments of this command.
		 * @return The end status of the command (pass/fail).
		 */
		public abstract boolean execute(CommandSender sender, String label, String[] args);

		/**
		 * Settings to return when checking if a sender is allowed.
		 */
		public enum Setting
		{
			/**
			 * Allow the sender.
			 */
			ALLOW,

			/**
			 * Skip immediately with no message, continue to look for a better match.
			 */
			SKIP,

			/**
			 * Stop looking for a better match, fail with a message to the player.
			 */
			FAIL
		}
	}
}