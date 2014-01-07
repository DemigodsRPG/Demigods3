package com.WildAmazing.marinating.Demigods.Deities.Titans;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.WildAmazing.marinating.Demigods.Deities.Deity;

public class Atlas implements Deity
{

	/* General */
	private static final long serialVersionUID = 1898032566168889851L;
	private final int SKILLCOST = 95;
	private final int ULTIMATECOST = 6000;
	private final int ULTIMATECOOLDOWNMAX = 400;
	private final int ULTIMATECOOLDOWNMIN = 300;

	/* Specific to player */
	private final String PLAYER;
	private boolean SKILL = false;
	private long ULTIMATETIME;

	public Atlas(String name)
	{
		PLAYER = name;
		ULTIMATETIME = System.currentTimeMillis();
	}

	@Override
	public String getName()
	{
		return "Atlas";
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
