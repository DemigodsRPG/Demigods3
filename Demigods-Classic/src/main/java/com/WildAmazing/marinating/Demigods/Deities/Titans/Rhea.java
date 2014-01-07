package com.WildAmazing.marinating.Demigods.Deities.Titans;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.WildAmazing.marinating.Demigods.Deities.Deity;
import com.WildAmazing.marinating.Demigods.WriteLocation;

public class Rhea implements Deity
{
	/* Generalized things */
	private static final long serialVersionUID = 4917938727569988533L;
	private final int POISONCOST = 50;
	private final int PLANTCOST = 100;
	private final int RHEAULTIMATECOST = 5500;
	private final int RHEAULTIMATECOOLDOWNMAX = 500;
	private final int RHEAULTIMATECOOLDOWNMIN = 120;

	/* Specific to owner */
	private final String PLAYER;
	private final ArrayList<WriteLocation> TREES;
	private boolean PLANT = false;
	private boolean POISON = false;
	private long PLANTTIME, POISONTIME, RHEAULTIMATETIME;
	private Material PLANTBIND = null;
	private Material DETONATEBIND = null;
	private Material POISONBIND = null;

	public Rhea(String name)
	{
		PLAYER = name;
		TREES = new ArrayList<WriteLocation>();
		PLANTTIME = System.currentTimeMillis();
		POISONTIME = System.currentTimeMillis();
		RHEAULTIMATETIME = System.currentTimeMillis();
	}

	@Override
	public String getName()
	{
		return "Rhea";
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
