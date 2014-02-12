package com.demigodsrpg.demigods.base;

import com.censoredsoftware.censoredlib.helper.CommandManager;
import com.demigodsrpg.demigods.base.command.DevelopmentCommands;
import com.demigodsrpg.demigods.base.command.GeneralCommands;
import com.demigodsrpg.demigods.base.command.MainCommand;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

// Commands
public enum DemigodsCommand
{
	MAIN(new MainCommand()), GENERAL(new GeneralCommands()), DEVELOPMENT(new DevelopmentCommands());

	private CommandManager command;

	private DemigodsCommand(CommandManager command)
	{
		this.command = command;
	}

	public CommandManager getCommandManager()
	{
		return command;
	}

	public static ImmutableSet<CommandManager> commands()
	{
		return ImmutableSet.copyOf(Collections2.transform(Sets.newHashSet(values()), new Function<DemigodsCommand, CommandManager>()
		{
			@Override
			public CommandManager apply(DemigodsCommand dCommand)
			{
				return dCommand.getCommandManager();
			}
		}));
	}
}
