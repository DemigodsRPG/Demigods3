package com.censoredsoftware.Demigods.API;

import java.util.ArrayList;
import java.util.List;

import com.censoredsoftware.Demigods.Block.Altar;
import com.censoredsoftware.Demigods.DemigodsData;
import com.censoredsoftware.Demigods.PlayerCharacter.PlayerCharacter;
import com.censoredsoftware.Demigods.Tracked.TrackedLocation;

public class LocationAPI
{
	/**
	 * Grab the TrackedLocation from the data with id <code>id</code>.
	 * 
	 * @param id The ID of the block.
	 * @return TrackedLocation.
	 */
	public static TrackedLocation getLocation(int id)
	{
		return (TrackedLocation) DemigodsData.locationData.getDataObject(id);
	}

	public static List<TrackedLocation> getAllLocations()
	{
		List<TrackedLocation> locations = new ArrayList<TrackedLocation>();
		for(int charID : DemigodsData.locationData.listKeys())
		{
			TrackedLocation location = (TrackedLocation) DemigodsData.locationData.getDataObject(charID);
			locations.add(location);
		}
		return locations;
	}

	/**
	 * Returns an ArrayList of all warps for <code>character</code>.
	 * 
	 * @param character the character whose warps to gather
	 * @return an ArrayList of the <code>character</code> warps.
	 */
	public static ArrayList<TrackedLocation> getWarps(PlayerCharacter character)
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
	public static boolean hasWarp(Altar altar, PlayerCharacter character)
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
	public static boolean hasInvites(PlayerCharacter character)
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
	public static TrackedLocation getInvite(PlayerCharacter inviting, PlayerCharacter invited)
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
	public static TrackedLocation getInvite(PlayerCharacter character, String name)
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
	public static boolean alreadyInvited(PlayerCharacter inviting, PlayerCharacter invited)
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
	public static ArrayList<TrackedLocation> getInvites(PlayerCharacter character)
	{
		return (ArrayList<TrackedLocation>) DemigodsData.inviteData.getDataObject(character.getID());
	}

	/**
	 * Sends an invite to <code>to</code> from <code>from</code>.
	 * 
	 * @param from the character whom the invite is from.
	 * @param to the character whom the invite is to.
	 */
	public static void addInvite(PlayerCharacter from, PlayerCharacter to)
	{
		ArrayList<TrackedLocation> invites;
		if(hasInvites(to)) invites = getInvites(to);
		else invites = new ArrayList<TrackedLocation>();
		invites.add(new TrackedLocation(DemigodsData.generateInt(5), from.getOwner().getPlayer().getLocation(), from.getName()));
		DemigodsData.inviteData.saveData(to.getID(), invites);
	}

	/**
	 * Removes <code>invite</code> from <code>invited</code>.
	 * 
	 * @param invited the character to remove the invite from.
	 * @param invite the invite to remove.
	 */
	public static void removeInvite(PlayerCharacter invited, TrackedLocation invite)
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
	public static void clearInvites(PlayerCharacter character)
	{
		DemigodsData.inviteData.removeData(character.getID());
	}
}
