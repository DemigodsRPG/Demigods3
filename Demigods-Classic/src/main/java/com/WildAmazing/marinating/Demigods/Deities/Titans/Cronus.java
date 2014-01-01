package com.WildAmazing.marinating.Demigods.Deities.Titans;

import com.WildAmazing.marinating.Demigods.Deities.Deity;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class Cronus implements Deity
{

	private static final long serialVersionUID = -6160291350540472542L;

	// global vars
	private static final int CLEAVECOST = 100;
	private static final int SLOWCOST = 180;
	private static final int CRONUSULTIMATECOST = 5000;
	private static final int CRONUSULTIMATECOOLDOWNMAX = 500;
	private static final int CRONUSULTIMATECOOLDOWNMIN = 120;

	// per player
	private final String PLAYER;
	private boolean CLEAVE = false;
	private Material CLEAVEITEM = null;
	private boolean SLOW = false;
	private Material SLOWITEM = null;
	private long CRONUSULTIMATETIME;
	private long CLEAVETIME;

	public Cronus(String player)
	{
		PLAYER = player;
		CRONUSULTIMATETIME = System.currentTimeMillis();
		CLEAVETIME = System.currentTimeMillis();
	}

	@Override
	public String getName()
	{
		return "Cronus";
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
