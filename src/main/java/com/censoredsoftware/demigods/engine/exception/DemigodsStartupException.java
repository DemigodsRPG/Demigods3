package com.censoredsoftware.demigods.engine.exception;

public class DemigodsStartupException extends Exception
{
	public DemigodsStartupException()
	{
		super("Demigods failed to initialize.");
	}
}
