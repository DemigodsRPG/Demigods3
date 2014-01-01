package com.WildAmazing.marinating.Demigods.Deities.Gods;

import com.WildAmazing.marinating.Demigods.Deities.Deity;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class Zeus implements Deity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2242753324910371936L;

	private final String PLAYER;
	private static final int SHOVECOST = 170;
	private static final int SHOVEDELAY = 1500; // milliseconds
	private static final int LIGHTNINGCOST = 140;
	private static final int LIGHTNINGDELAY = 1000; // milliseconds
	private static final int ZEUSULTIMATECOST = 3700;
	private static final int ZEUSULTIMATECOOLDOWNMAX = 600; // seconds
	private static final int ZEUSULTIMATECOOLDOWNMIN = 60;

	private long ZEUSSHOVETIME;
	private long ZEUSLIGHTNINGTIME;
	private boolean SHOVE = false;
	private boolean LIGHTNING = false;
	private Material SHOVEBIND = null;
	private Material LIGHTNINGBIND = null;

	public Zeus(String player)
	{
		PLAYER = player;
		ZEUSSHOVETIME = System.currentTimeMillis();
		ZEUSLIGHTNINGTIME = System.currentTimeMillis();
	}

	@Override
	public String getName()
	{
		return "Zeus";
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
