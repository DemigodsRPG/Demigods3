package com.censoredsoftware.demigods.engine.base;

import com.censoredsoftware.censoredlib.helper.WrappedCommand;
import com.censoredsoftware.demigods.engine.command.DevelopmentCommands;
import com.censoredsoftware.demigods.engine.command.GeneralCommands;
import com.censoredsoftware.demigods.engine.command.MainCommand;

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
}