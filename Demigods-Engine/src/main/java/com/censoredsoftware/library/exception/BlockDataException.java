package com.censoredsoftware.library.exception;

public class BlockDataException extends IllegalArgumentException
{
	public BlockDataException()
	{
		super("Odds must be between 1 and 100.");
	}
}
