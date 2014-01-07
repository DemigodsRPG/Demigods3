package com.WildAmazing.marinating.Demigods.Deities.Gods;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.WildAmazing.marinating.Demigods.Deities.Deity;

public class Athena implements Deity
{
	private static final long serialVersionUID = -9039521341663053625L;
	private final String PLAYER;

	private static final int SKILLCOST = 100;
	private static final int SKILLDELAY = 3600; // milliseconds
	private static final int ULTIMATECOST = 4000;
	private static final int ULTIMATECOOLDOWNMAX = 500; // seconds
	private static final int ULTIMATECOOLDOWNMIN = 300;

	private boolean SKILL = false;
	private Material SKILLBIND = null;
	private long SKILLTIME;
	private long ULTIMATETIME;

	public Athena(String player)
	{
		PLAYER = player;
	}

	@Override
	public String getName()
	{
		return "Athena";
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
