package com.censoredsoftware.demigods.language;

public enum Symbol
{
	// Misc
	BLACK_SQUARE("⬛"), HEAVY_HEART("❤"), DASH("―"), CAUTION("⚠"), MAIL("✉"), DEATH("☠"), WATCH("⌚"), HOURGLASS("⌛"), POINT_OF_INTEREST("⌘"),

	// Misc cont.
	SUN("☀"), CLOUD("☁"), UNCHECKED_BOX("☐"), CHECKED_BOX("☑"), X_BOX("☒"), COFFEE("☕"), NO_ENTRY("⛔"), WHITE_FLAG("⚐"), BLACK_FLAG("⚑"),

	// Math
	ONE_THIRD("⅓"), TWO_THIRDS("⅔"), ONE_FIFTH("⅕"), TWO_FIFTHS("⅖"), THREE_FIFTHS("⅗"), FOUR_FIFTHS("⅘"),

	// Roman Numerals
	RN_ONE("Ⅰ"), RN_FIVE("Ⅴ"), RN_TEN("Ⅹ"), RN_FIFTY("Ⅼ"), RN_HUNDRED("Ⅽ"), RN_FIVE_HUNDRED("Ⅾ"), RN_THOUSAND("Ⅿ"),

	// Greek
	L_ALPHA("α"), L_BETA("β"), U_GAMMA("Γ"), L_GAMMA("γ"), U_DELTA("Δ"), L_DELTA("δ"), L_ELIPSON("ε"), THETA("Θ"), U_PHI("Φ"), L_PHI("φ"), U_OMEGA("Ω"), L_OMEGA("ω"),

	// COPYRIGHT
	COPYRIGHT("©"), TRADEMARK("™"), SERVICEMARK("℠"),

	// Arrows
	RIGHTWARD_ARROW("➡"), COUNTERCLOCKWISE_SEMICIRLCE_ARROW("↶"), CLOCKWISE_SEMICIRCLE_ARROW("↷"), COUNTERCLOCKWISE_OPEN_CIRCLE_ARROW("↺"), CLOCKWISE_OPEN_CIRCLE_ARROW("↻");

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
