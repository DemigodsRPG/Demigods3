package com.censoredsoftware.demigods.engine.exception;

public class BlockDataException extends IllegalArgumentException
{
	public BlockDataException()
	{
		super("Something went wrong with calculating block data.");
	}
}
