package com.censoredsoftware.Demigods.Engine.Object.General;

import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.Engine.Object.PlayerCharacter.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.Object.Structure.Altar;
import com.censoredsoftware.Demigods.Engine.Utility.ZoneUtility;

@Model
public class DemigodsLocation
{
	@Id
	private Long id;
	@Attribute
	@Indexed
	String world;
	@Attribute
	Double X;
	@Attribute
	Double Y;
	@Attribute
	Double Z;
	@Attribute
	Float pitch;
	@Attribute
	Float yaw;

	void setWorld(String world)
	{
		this.world = world;
	}

	void setX(Double X)
	{
		this.X = X;
	}

	void setY(Double Y)
	{
		this.Y = Y;
	}

	void setZ(Double Z)
	{
		this.Z = Z;
	}

	void setYaw(Float yaw)
	{
		this.yaw = yaw;
	}

	void setPitch(Float pitch)
	{
		this.pitch = pitch;
	}

	public static void save(DemigodsLocation location)
	{
		JOhm.save(location);
	}

	public static DemigodsLocation load(long id) // TODO This belongs somewhere else.
	{
		return JOhm.get(DemigodsLocation.class, id);
	}

	public static Set<DemigodsLocation> loadAll()
	{
		return JOhm.getAll(DemigodsLocation.class);
	}

	public static DemigodsLocation getTracked(Location location) // TODO: Determine if this should be the default for getting TrackedLocations, or if it is too intensive on the DB for constant use.
	{
		for(DemigodsLocation tracked : loadAll())
		{
			if(location.equals(tracked)) return tracked;
		}
		return GeneralModelFactory.createDemigodsLocation(location);
	}

	public Location toLocation() throws NullPointerException
	{
		return new Location(Bukkit.getServer().getWorld(this.world), this.X, this.Y, this.Z, this.yaw, this.pitch);
	}

	public Long getId()
	{
		return this.id;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}

	/**
	 * Grab the DemigodsLocation from the data with id <code>id</code>.
	 * 
	 * @param id The ID of the block.
	 * @return DemigodsLocation.
	 */
	public static DemigodsLocation getLocation(long id)
	{
		return DemigodsLocation.load(id);
	}

	public static Set<DemigodsLocation> getAllLocations()
	{
		return DemigodsLocation.loadAll();
	}

	/**
	 * Checks to see if <code>character</code> has a warp for <code>altar</code>.
	 * 
	 * @param altar the altar to be checked.
	 * @param character the character to be checked.
	 * @return true/false depending on if the <code>character</code> has the warp.
	 */
	public static boolean hasWarp(Altar altar, PlayerCharacter character) // TODO Move all warps and invites to PlayerCharacterMeta.
	{
		if(character == null || character.getWarps() == null) return false;
		for(Map.Entry<DemigodsLocation, String> warp : character.getWarps().entrySet())
		{
			if(ZoneUtility.zoneAltar(warp.getKey().toLocation()) == altar) return true;
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
	 * returns a <code>DemigodsLocation</code>.
	 * 
	 * @param inviting the character who sent the invite.
	 * @param invited the character who received the invite.
	 * @return the <code>DemigodsLocation</code> of the invite.
	 */
	public static DemigodsLocation getInvite(PlayerCharacter inviting, PlayerCharacter invited)
	{
		if(hasInvites(invited))
		{
			for(Map.Entry<DemigodsLocation, String> invite : invited.getInvites().entrySet())
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
	 * @return the <code>DemigodsLocation</code> of the invite.
	 */
	public static DemigodsLocation getInvite(PlayerCharacter character, String name)
	{
		if(hasInvites(character))
		{
			for(Map.Entry<DemigodsLocation, String> invite : character.getInvites().entrySet())
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
		return getInvite(inviting, invited) != null;
	}

	/**
	 * Sends an invite to <code>to</code> from <code>from</code>.
	 * 
	 * @param from the character whom the invite is from.
	 * @param to the character whom the invite is to.
	 */
	public static void addInvite(PlayerCharacter from, PlayerCharacter to)
	{
		to.addInvite(GeneralModelFactory.createDemigodsLocation(from.getOfflinePlayer().getPlayer().getLocation()), from.getName());
	}

	/**
	 * Removes <code>invite</code> from <code>invited</code>.
	 * 
	 * @param invited the character to remove the invite from.
	 * @param invite the invite to remove.
	 */
	public static void removeInvite(PlayerCharacter invited, DemigodsLocation invite)
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
