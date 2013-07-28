package com.censoredsoftware.why.engine.exception;

public class SpigotNotFoundException extends IllegalArgumentException
{
	public SpigotNotFoundException()
	{
		super("Spigot is not installed.");
	}
}
