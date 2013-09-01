package com.censoredsoftware.demigods.language;

public enum Symbol
{
	BLACK_SQUARE("■"), RIGHTWARD_ARROW("➡"), HEAVY_HEART("❤"), DASH("―"), CHECK_MARK("✓"), WRONG_MARK("✗"), CAUTION("⚠"), LIGHTNING("⚡"), MAIL("✉"), WRITING_HAND("✍");

	private String symbol;

	private Symbol(String symbol)
	{
		this.symbol = symbol;
	}

	@Override
	public String toString()
	{
		return symbol;
	}
}
