package com.censoredsoftware.Demigods.Engine.Exceptions;

public class SpigotNotFoundException extends IllegalArgumentException
{
	public SpigotNotFoundException()
	{
		super("Spigot is not installed.");
	}
}
