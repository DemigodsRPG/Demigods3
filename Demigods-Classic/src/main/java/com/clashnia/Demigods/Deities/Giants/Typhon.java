package com.clashnia.Demigods.Deities.Giants;

import com.WildAmazing.marinating.Demigods.Deities.Deity;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class Typhon implements Deity
{
	private static final long serialVersionUID = -7376781567872708495L;

	private final String PLAYER;

	private static final int SKILLCOST = 120;
	private static final int SKILLDELAY = 1250; // milliseconds
	private static final int EXPLOSIONSIZE = 3;
	private boolean SKILL = false;

	private final Material SKILLBIND = null;
	private long SKILLTIME;
	private long LASTCHECK;

	public Typhon(String name)
	{
		PLAYER = name;
		SKILLTIME = System.currentTimeMillis();
		LASTCHECK = System.currentTimeMillis();
	}

	@Override
	public String getName()
	{
		return "Typhon";
	}

	@Override
	public String getPlayerName()
	{
		return PLAYER;
	}

	@Override
	public String getDefaultAlliance()
	{
		return "";
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