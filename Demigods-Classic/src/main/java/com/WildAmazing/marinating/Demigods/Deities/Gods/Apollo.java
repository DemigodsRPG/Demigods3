package com.WildAmazing.marinating.Demigods.Deities.Gods;

import com.WildAmazing.marinating.Demigods.Deities.Deity;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class Apollo implements Deity
{
	private static final long serialVersionUID = -5219841682574911103L;

	private final String PLAYER;

	private static final int SKILLCOST = 150;
	private static final int SKILLDELAY = 3000; // milliseconds
	private static final int ULTIMATECOST = 6300;
	private static final int ULTIMATECOOLDOWNMAX = 600; // seconds
	private static final int ULTIMATECOOLDOWNMIN = 120;

	private static final String skillname = "Cure";
	private static final String ult = "Finale";

	private boolean SKILL = false;
	private Material SKILLBIND = null;
	private long SKILLTIME;
	private long ULTIMATETIME;
	private long LASTCHECK;

	public Apollo(String player)
	{
		PLAYER = player;
		SKILLTIME = System.currentTimeMillis();
		ULTIMATETIME = System.currentTimeMillis();
		LASTCHECK = System.currentTimeMillis();
	}

	@Override
	public String getName()
	{
		return "Apollo";
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
