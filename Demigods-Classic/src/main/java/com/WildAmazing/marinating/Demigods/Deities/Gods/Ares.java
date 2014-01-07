package com.WildAmazing.marinating.Demigods.Deities.Gods;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.WildAmazing.marinating.Demigods.Deities.Deity;

public class Ares implements Deity
{
	private static final long serialVersionUID = -5825867521620334951L;
	private final String PLAYER;
	/*
	 * Needs to be loaded out of config
	 */
	private static final int STRIKECOST = 120;
	private static final int STRIKEDELAY = 1250; // milliseconds
	private static final int ARESULTIMATECOST = 5000;
	private static final int ARESULTIMATECOOLDOWNMAX = 180; // seconds
	private static final int ARESULTIMATECOOLDOWNMIN = 60;

	private boolean STRIKE = false;
	private Material STRIKEBIND = null;
	private long STRIKETIME;
	private long ARESULTIMATETIME;

	public Ares(String player)
	{
		PLAYER = player;
		ARESULTIMATETIME = System.currentTimeMillis();
		STRIKETIME = System.currentTimeMillis();
	}

	@Override
	public String getName()
	{
		return "Ares";
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
