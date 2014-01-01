package com.WildAmazing.marinating.Demigods.Deities.Gods;

import com.WildAmazing.marinating.Demigods.Deities.Deity;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class Poseidon implements Deity
{
	/* General */
	private static final long serialVersionUID = 2319323778421842381L;
	private final int REELCOST = 120;
	private final int drownCOST = 240;

	/* Specific to player */
	private final String PLAYER;
	private boolean REEL = false;
	private boolean drown = false;
	private long REELTIME, drownTIME, LASTCHECK;
	private Material drownBIND = null;

	public Poseidon(String name)
	{
		PLAYER = name;
		REELTIME = System.currentTimeMillis();
		drownTIME = System.currentTimeMillis();
		LASTCHECK = System.currentTimeMillis();
	}

	@Override
	public String getName()
	{
		return "Poseidon";
	}

	@Override
	public String getPlayerName()
	{
		return PLAYER;
	}

	@Override
	public String getDefaultAlliance()
	{
		return "God";
	}

	@Override
	public void printInfo(Player p)
	{}

	@Override
	public void onEvent(Event ee)
	{}

	@Override
	public void onCommand(Player P, String str, String[] args, boolean bind)
	{}

	@Override
	public void onTick(long timeSent)
	{}
}
