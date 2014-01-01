package com.WildAmazing.marinating.Demigods.Deities;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.io.Serializable;

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
	 * 
	 * @return Deity name
	 */
	public String getName();

	/**
	 * Returns the name of the player this deity belongs to.
	 * 
	 * @return Player name
	 */
	public String getPlayerName();

	/**
	 * Returns the default alliance, either God or Titan.
	 * 
	 * @return
	 */
	public String getDefaultAlliance();

	/**
	 * Prints info about the deity to the target player.
	 * 
	 * @param p
	 */
	public void printInfo(Player p);

	/*
	 * Make sure to use helper methods and instanceof.
	 */
	public void onEvent(Event ee);

	/*
	 * Used for command handling
	 */
	public void onCommand(Player P, String str, String[] args, boolean bind);

	/*
	 * Used for events that execute repeatedly
	 */
	public void onTick(long timeSent);
}
