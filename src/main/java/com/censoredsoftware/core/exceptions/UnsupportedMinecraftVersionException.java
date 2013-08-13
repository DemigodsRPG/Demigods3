package com.censoredsoftware.core.exceptions;

public class UnsupportedMinecraftVersionException extends IllegalArgumentException
{
	public UnsupportedMinecraftVersionException()
	{
		super("This version of Minecraft is not currently supported");
	}
}
