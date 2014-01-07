package com.WildAmazing.marinating.Demigods.Deities.Titans;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.WildAmazing.marinating.Demigods.Deities.Deity;

public class Oceanus implements Deity
{
	private static final long serialVersionUID = -2472769863144336856L;
	private final String PLAYER;

	private static final String skillname = "Squid";
	private static final int SKILLCOST = 225;
	private static final int SKILLDELAY = 2000; // milliseconds
	private static final int ULTIMATECOST = 2000;
	private static final int ULTIMATECOOLDOWNMAX = 700; // seconds
	private static final int ULTIMATECOOLDOWNMIN = 400;

	private boolean SKILL = false;
	private Material SKILLBIND = null;
	private long SKILLTIME;
	private long ULTIMATETIME;
	private long LASTCHECK;

	public Oceanus(String player)
	{
		PLAYER = player;
		SKILLTIME = System.currentTimeMillis();
		ULTIMATETIME = System.currentTimeMillis();
		LASTCHECK = System.currentTimeMillis();
	}

	@Override
	public String getName()
	{
		return "Oceanus";
	}

	@Override
	public String getPlayerName()
	{
		return PLAYER;
	}

	@Override
	public String getDefaultAlliance()
	{
		return "Titan";
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
