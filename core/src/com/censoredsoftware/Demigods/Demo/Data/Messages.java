package com.censoredsoftware.Demigods.Demo.Data;

public enum Messages
{
	/**
	 * They're very happy.
	 */
	HAHA("Hahahahahahahahahahhahaha!"),

	/**
	 * They think the server sucks.
	 */
	SUCK("This server sucks."),

	/**
	 * They're very mad.
	 */
	FUCK("Fuck everyone on here.  You all smell."),

	/**
	 * They're new.
	 */
	NEW("I'm new, everyone kill me!");

	private String value;

	private Messages(String value)
	{
		this.value = value;
	}

	public String getMessage()
	{
		return this.value;
	}
}
