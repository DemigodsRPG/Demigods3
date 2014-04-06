package com.censoredsoftware.library.helper;

import com.google.common.base.Joiner;
import demigods.shaded.com.mojang.api.profiles.HttpProfileRepository;
import demigods.shaded.com.mojang.api.profiles.Profile;
import demigods.shaded.com.mojang.api.profiles.ProfileCriteria;
import org.bukkit.OfflinePlayer;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MojangIdProvider
{
	private static final ConcurrentMap<String, String> CACHE;
	private static final String AGENT;
	private static final HttpProfileRepository repository;

	static
	{
		AGENT = "minecraft";
		CACHE = new ConcurrentHashMap<>();
		repository = new HttpProfileRepository();
	}

	private MojangIdProvider()
	{
	}

	public static UUID toUUID(String mojangId)
	{
		// Check that it is long enough.
		if(mojangId.length() != 32) return null;

		// Grab the components from the UUID.
		String component1 = mojangId.substring(0, 8),
				component2 = mojangId.substring(8, 12),
				component3 = mojangId.substring(12, 16),
				component4 = mojangId.substring(16, 20),
				component5 = mojangId.substring(20);

		// Create the new String.
		String fullId = Joiner.on('-').join(component1, component2, component3, component4, component5);

		// Transform to UUID
		return UUID.fromString(fullId);
	}

	/**
	 * Get the UUID of a player name.
	 *
	 * @param offlinePlayer The player that we are checking the UUID of.
	 * @return The Mojang UUID.
	 */
	public static String getId(OfflinePlayer offlinePlayer)
	{
		// Get the player name.
		String playerName = offlinePlayer.getName();

		// Check if we already know this id, of if the name actually belongs to a player.
		if(CACHE.containsKey(playerName)) return CACHE.get(playerName);

		// Find the id.
		String id = find(playerName);

		// Check for null.
		if(id == null) return null;

		// Put the player in the known ids, and return the found id.
		CACHE.put(playerName, id);
		return id;
	}

	private static String find(String playerName)
	{
		String playerId = null;

		Profile[] profiles = repository.findProfilesByCriteria(new ProfileCriteria(playerName, AGENT));
		if(profiles.length == 1) playerId = profiles[0].getId();

		return playerId;
	}
}
