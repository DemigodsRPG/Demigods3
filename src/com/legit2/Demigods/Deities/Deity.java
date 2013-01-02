package com.legit2.Demigods.Deities;

import java.io.Serializable;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/*
 * Each Deity is attached to a player.
 * The Deity's abilities are activated by listeners, and
 * helper methods should only be used within the Deity.
 * Universal functions (finding blocks) will be called
 * statically.
 */

public interface Deity extends Serializable
{
	/**
	 * Returns the name of this deity. Shouldn't be needed often.
	 * @return Deity name
	 */
	public String getName();
	
	/**
	 * Returns the claim item for the deity.
	 * @return Claim item
	 */
	public ArrayList<Material> getClaimItems();
	
	/**
	 * Returns the default alliance, either God or Titan.
	 * @return
	 */
	public String getAlliance();
	
	/**
	 * Prints info about the deity to the target player.
	 * @param p
	 */
	public void printInfo(Player p);
	
	/*
	 * Make sure to use helper methods and instanceof.
	 */
	public void onEvent(Event e);
	
	/*
	 * Used for events that execute repeatedly
	 */
	public void onTick(long timeSent);
}

