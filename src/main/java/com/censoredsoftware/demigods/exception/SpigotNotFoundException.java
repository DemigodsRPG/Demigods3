package com.censoredsoftware.demigods.exception;

public class SpigotNotFoundException extends IllegalArgumentException
{
	public SpigotNotFoundException()
	{
		super("Spigot is not installed.");
	}
}
