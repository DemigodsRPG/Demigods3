package com.censoredsoftware.demigods.engine.misc.exception;

public class DemigodsStartupException extends Exception
{
	public DemigodsStartupException()
	{
		super("demigods failed to initialize.");
	}
}
