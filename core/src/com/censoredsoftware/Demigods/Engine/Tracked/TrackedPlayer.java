package com.censoredsoftware.Demigods.Engine.Tracked;

import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import redis.clients.johm.Attribute;
import redis.clients.johm.Id;
import redis.clients.johm.Indexed;
import redis.clients.johm.Model;

import com.censoredsoftware.Demigods.API.CharacterAPI;
import com.censoredsoftware.Demigods.Engine.DemigodsData;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;
import com.google.common.collect.Sets;

@Model
public class TrackedPlayer
{
	@Id
	private Long id;
	@Attribute
	@Indexed
	private String player;
	@Attribute
	private long lastLoginTime;
	@Attribute
	private long current;
	@Attribute
	private long previous;

	void setPlayer(String player)
	{
		this.player = player;
		TrackedPlayer.save(this);
	}

	public static void save(TrackedPlayer trackedPlayer)
	{
		DemigodsData.jOhm.save(trackedPlayer);
	}

	public static TrackedPlayer load(Long id) // TODO This belongs somewhere else.
	{
		return DemigodsData.jOhm.get(TrackedPlayer.class, id);
	}

	public static Set<TrackedPlayer> loadAll()
	{
		try
		{
			return DemigodsData.jOhm.getAll(TrackedPlayer.class);
		}
		catch(Exception e)
		{
			return Sets.newHashSet();
		}
	}

	public static TrackedPlayer getTracked(OfflinePlayer player)
	{
		try
		{
			List<TrackedPlayer> tracking = DemigodsData.jOhm.find(TrackedPlayer.class, "player", player.getName());
			return tracking.get(0);
		}
		catch(Exception ignored)
		{}
		return TrackedModelFactory.createTrackedPlayer(player);
	}

	public OfflinePlayer getPlayer()
	{
		return Bukkit.getOfflinePlayer(this.player);
	}

	public void setLastLoginTime(Long time)
	{
		this.lastLoginTime = time;
		TrackedPlayer.save(this);
	}

	public Long getLastLoginTime()
	{
		return this.lastLoginTime;
	}

	public void setCurrent(PlayerCharacter character)
	{
		// Update previous character
		if(this.previous != 0)
		{
			PlayerCharacter previousChar = CharacterAPI.getChar(this.previous);
			previousChar.setActive(false);
			PlayerCharacter.save(previousChar);
			this.previous = this.current;
		}

		// Update current character
		character.setActive(true);
		PlayerCharacter.save(character);
		this.current = character.getId();
		TrackedPlayer.save(this);
	}

	public PlayerCharacter getCurrent()
	{
		return CharacterAPI.getChar(this.current);
	}

	public PlayerCharacter getPrevious()
	{
		return CharacterAPI.getChar(this.previous);
	}

	public List<PlayerCharacter> getCharacters()
	{
		return DemigodsData.jOhm.find(PlayerCharacter.class, "player", this.player);
	}
}
