package com.censoredsoftware.Demigods.API;

import java.util.Map;
import java.util.Set;

import com.censoredsoftware.Demigods.Engine.Block.Altar;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedLocation;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedModelFactory;

public class LocationAPI
{
	/**
	 * Grab the TrackedLocation from the data with id <code>id</code>.
	 * 
	 * @param id The ID of the block.
	 * @return TrackedLocation.
	 */
	public static TrackedLocation getLocation(long id)
	{
		return TrackedLocation.load(id);
	}

	public static Set<TrackedLocation> getAllLocations()
	{
		return TrackedLocation.loadAll();
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
		if(character == null) return false;
		if(character.getWarps() == null) return false;
		for(Map.Entry<TrackedLocation, String> warp : character.getWarps().entrySet())
		{
			if(ZoneAPI.zoneAltar(warp.getKey().toLocation()) == altar) return true;
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
		return !character.getInvites().isEmpty();
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
			for(Map.Entry<TrackedLocation, String> invite : invited.getInvites().entrySet())
			{
				if(invite.getValue().equalsIgnoreCase(inviting.getName())) return invite.getKey();
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
			for(Map.Entry<TrackedLocation, String> invite : character.getInvites().entrySet())
			{
				if(invite.getValue().equalsIgnoreCase(name)) return invite.getKey();
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
	 * Sends an invite to <code>to</code> from <code>from</code>.
	 * 
	 * @param from the character whom the invite is from.
	 * @param to the character whom the invite is to.
	 */
	public static void addInvite(PlayerCharacter from, PlayerCharacter to)
	{
		to.addInvite(TrackedModelFactory.createTrackedLocation(from.getPlayer().getPlayer().getLocation()), from.getName());
	}

	/**
	 * Removes <code>invite</code> from <code>invited</code>.
	 * 
	 * @param invited the character to remove the invite from.
	 * @param invite the invite to remove.
	 */
	public static void removeInvite(PlayerCharacter invited, TrackedLocation invite)
	{
		invited.removeInvite(invite);
	}

	/**
	 * Clears all invited for <code>character</code>.
	 * 
	 * @param character the character whose invites to remove.
	 */
	public static void clearInvites(PlayerCharacter character)
	{
		character.clearInvites();
	}
}
