package com.censoredsoftware.Demigods.API;

import java.util.ArrayList;

import com.censoredsoftware.Demigods.Block.Altar;
import com.censoredsoftware.Demigods.Demigod.Demigod;
import com.censoredsoftware.Demigods.DemigodsData;
import com.censoredsoftware.Demigods.Tracked.TrackedLocation;

public class WarpAPI
{
	/**
	 * Returns an ArrayList of all warps for <code>character</code>.
	 * 
	 * @param character the character whose warps to gather
	 * @return an ArrayList of the <code>character</code> warps.
	 */
	public static ArrayList<TrackedLocation> getWarps(Demigod character)
	{
		if(character == null || !DemigodsData.warpData.containsKey(character.getID())) return null;
		return (ArrayList<TrackedLocation>) DemigodsData.warpData.getDataObject(character.getID());
	}

	/**
	 * Checks to see if <code>character</code> has a warp for <code>altar</code>.
	 * 
	 * @param altar the altar to be checked.
	 * @param character the character to be checked.
	 * @return true/false depending on if the <code>character</code> has the warp.
	 */
	public static boolean hasWarp(Altar altar, Demigod character)
	{
		if(getWarps(character) == null) return false;
		for(TrackedLocation warp : getWarps(character))
		{
			if(ZoneAPI.zoneAltar(warp.toLocation()) == altar) return true;
		}
		return false;
	}

	/**
	 * Returns true if <code>character</code> has invites in their que.
	 * 
	 * @param character the character to check.
	 * @return true/false depending on the presence of invites.
	 */
	public static boolean hasInvites(Demigod character)
	{
		return getInvites(character) != null && !getInvites(character).isEmpty();
	}

	/**
	 * Gets the invite from <code>inviting</code> to <code>invited</code> and
	 * returns a <code>TrackedLocation</code>.
	 * 
	 * @param inviting the character who sent the invite.
	 * @param invited the character who received the invite.
	 * @return the <code>TrackedLocation</code> of the invite.
	 */
	public static TrackedLocation getInvite(Demigod inviting, Demigod invited)
	{
		if(hasInvites(invited))
		{
			for(TrackedLocation invite : getInvites(invited))
			{
				if(invite.getName().equalsIgnoreCase(inviting.getName())) return invite;
			}
		}
		return null;
	}

	/**
	 * Returns the invite for the <code>character</code> with the name <code>name</code>.
	 * 
	 * @param character the character to check.
	 * @param name the name of the invite to get.
	 * @return the <code>TrackedLocation</code> of the invite.
	 */
	public static TrackedLocation getInvite(Demigod character, String name)
	{
		if(hasInvites(character))
		{
			for(TrackedLocation invite : getInvites(character))
			{
				if(invite.getName().equalsIgnoreCase(name)) return invite;
			}
		}
		return null;
	}

	/**
	 * Returns true if <code>invited</code> has already been given an invite from <code>inviting</code>.
	 * 
	 * @param inviting the character the invite is from.
	 * @param invited the character the invite is to.
	 * @return boolean for if the character has been invited or not.
	 */
	public static boolean alreadyInvited(Demigod inviting, Demigod invited)
	{
		if(getInvite(inviting, invited) != null) return true;
		return false;
	}

	/**
	 * Returns an ArrayList of all invite locations for <code>character</code>.
	 * 
	 * @param character the character whose invites to grab.
	 * @return an ArrayList of invite locations.
	 */
	public static ArrayList<TrackedLocation> getInvites(Demigod character)
	{
		return (ArrayList<TrackedLocation>) DemigodsData.inviteData.getDataObject(character.getID());
	}

	/**
	 * Sends an invite to <code>to</code> from <code>from</code>.
	 * 
	 * @param from the character whom the invite is from.
	 * @param to the character whom the invite is to.
	 */
	public static void addInvite(Demigod from, Demigod to)
	{
		ArrayList<TrackedLocation> invites;
		if(hasInvites(to)) invites = getInvites(to);
		else invites = new ArrayList<TrackedLocation>();
		invites.add(new TrackedLocation(from.getOwner().getPlayer().getLocation(), from.getName()));
		DemigodsData.inviteData.saveData(to.getID(), invites);
	}

	/**
	 * Removes <code>invite</code> from <code>invited</code>.
	 * 
	 * @param invited the character to remove the invite from.
	 * @param invite the invite to remove.
	 */
	public static void removeInvite(Demigod invited, TrackedLocation invite)
	{
		ArrayList<TrackedLocation> invites;
		if(hasInvites(invited)) invites = getInvites(invited);
		else return;
		invites.remove(invite);
		DemigodsData.inviteData.saveData(invited.getID(), invites);
	}

	/**
	 * Clears all invited for <code>character</code>.
	 * 
	 * @param character the character whose invites to remove.
	 */
	public static void clearInvites(Demigod character)
	{
		DemigodsData.inviteData.removeData(character.getID());
	}
}
