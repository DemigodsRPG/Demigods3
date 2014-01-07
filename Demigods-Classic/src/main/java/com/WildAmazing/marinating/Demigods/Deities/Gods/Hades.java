package com.WildAmazing.marinating.Demigods.Deities.Gods;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.WildAmazing.marinating.Demigods.Deities.Deity;

public class Hades implements Deity
{

	/* General */
	private static final long serialVersionUID = 3647481847975286534L;
	private static final int CHAINCOST = 250;
	private static final int CHAINDELAY = 1500;
	private static final int ENTOMBCOST = 470;
	private static final int ENTOMBDELAY = 2000;
	private static final int ULTIMATECOST = 4000;
	private static final int ULTIMATECOOLDOWNMAX = 600;
	private static final int ULTIMATECOOLDOWNMIN = 320;

	/* Specific to player */
	private final String PLAYER;
	private boolean CHAIN = false;
	private boolean ENTOMB = false;
	private long CHAINTIME, ENTOMBTIME, ULTIMATETIME;
	private Material CHAINBIND = null;
	private Material ENTOMBBIND = null;

	public Hades(String name)
	{
		PLAYER = name;
		CHAINTIME = System.currentTimeMillis();
		ENTOMBTIME = System.currentTimeMillis();
		ULTIMATETIME = System.currentTimeMillis();
	}

	@Override
	public String getName()
	{
		return "Hades";
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
