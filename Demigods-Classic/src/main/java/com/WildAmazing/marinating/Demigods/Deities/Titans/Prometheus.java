package com.WildAmazing.marinating.Demigods.Deities.Titans;

import com.WildAmazing.marinating.Demigods.Deities.Deity;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class Prometheus implements Deity
{
	private static final long serialVersionUID = -6437607905225500420L;
	private final String PLAYER;
	private final int FIREBALLCOST = 100;
	private final int PROMETHEUSULTIMATECOST = 5500;
	private final int PROMETHEUSULTIMATECOOLDOWNMAX = 600; // seconds
	private final int PROMETHEUSULTIMATECOOLDOWNMIN = 60;
	private final int BLAZECOST = 400;
	private final double BLAZEDELAY = 15;

	private Material FIREBALLITEM = null;
	private Material BLAZEITEM = null;
	private boolean FIREBALL = false;
	private boolean BLAZE = false;
	private long FIRESTORMTIME;
	private long BLAZETIME;
	private long FIREBALLTIME;

	public Prometheus(String name)
	{
		PLAYER = name;
		FIRESTORMTIME = System.currentTimeMillis();
		BLAZETIME = System.currentTimeMillis();
		String DISPLAYNAME = name;
		FIREBALLTIME = System.currentTimeMillis();
	}

	@Override
	public String getName()
	{
		return "Prometheus";
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
