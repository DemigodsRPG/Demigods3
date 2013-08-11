package com.censoredsoftware.demigods.exception;

public class DemigodsStartupException extends Exception
{
	public DemigodsStartupException()
	{
		super("Demigods failed to initialize.");
	}
}
