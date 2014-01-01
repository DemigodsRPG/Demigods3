package com.WildAmazing.marinating.Demigods.Deities.Gods;

import com.WildAmazing.marinating.Demigods.Deities.Deity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public class Hephaestus implements Deity, Listener
{
	private static final long serialVersionUID = -2472769863144336856L;
	private final String PLAYER;

	private static final int SKILLCOST = 200;
	private static final int ULTIMATECOST = 9000;
	private static final int ULTIMATECOOLDOWNMAX = 1800; // seconds
	private static final int ULTIMATECOOLDOWNMIN = 900;

	private static final String skillname = "Reforge";
	private static final String ult = "Shatter";

	private long ULTIMATETIME;

	public Hephaestus(String player)
	{
		PLAYER = player;
		ULTIMATETIME = System.currentTimeMillis();
	}

	@Override
	public String getName()
	{
		return "Hephaestus";
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
