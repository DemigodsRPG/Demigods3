package com.censoredsoftware.demigods.base;

import com.censoredsoftware.censoredlib.helper.WrappedCommand;
import com.censoredsoftware.demigods.base.command.DevelopmentCommands;
import com.censoredsoftware.demigods.base.command.GeneralCommands;
import com.censoredsoftware.demigods.base.command.MainCommand;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

// Commands
public enum DemigodsCommand
{
	MAIN(new MainCommand()), GENERAL(new GeneralCommands()), DEVELOPMENT(new DevelopmentCommands());

	private WrappedCommand command;

	private DemigodsCommand(WrappedCommand command)
	{
		this.command = command;
	}

	public WrappedCommand getCommand()
	{
		return command;
	}

	public static ImmutableSet<WrappedCommand> commands()
	{
		return ImmutableSet.copyOf(Collections2.transform(Sets.newHashSet(values()), new Function<DemigodsCommand, WrappedCommand>()
		{
			@Override
			public WrappedCommand apply(DemigodsCommand dCommand)
			{
				return dCommand.getCommand();
			}
		}));
	}
}
