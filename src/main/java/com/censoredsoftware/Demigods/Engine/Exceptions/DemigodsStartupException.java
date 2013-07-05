package com.censoredsoftware.Demigods.Engine.Exceptions;

public class DemigodsStartupException extends Exception
{
	public DemigodsStartupException()
	{
		super("Demigods failed to initialize.");
	}
}
