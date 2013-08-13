package com.censoredsoftware.core.exceptions;

public class SpigotNotFoundException extends IllegalArgumentException
{
	public SpigotNotFoundException()
	{
		super("Spigot is not installed.");
	}
}
