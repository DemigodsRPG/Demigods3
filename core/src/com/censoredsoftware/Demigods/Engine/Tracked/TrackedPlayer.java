package com.censoredsoftware.Demigods.Engine.Tracked;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.Engine.DemigodsData;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;

@Model
public class TrackedPlayer
{
	@Id
	private long id;
	@Attribute
	@Indexed
	private String player;
	@Attribute
	private long lastLoginTime;
	@Reference
	@Indexed
	private PlayerCharacter current;
	@Reference
	@Indexed
	private PlayerCharacter previous;

	void setPlayer(String player)
	{
		this.player = player;
	}

	public static void save(TrackedPlayer meta)
	{
		DemigodsData.jOhm.save(meta);
	}

	public static TrackedPlayer load(long id) // TODO This belongs somewhere else.
	{
		return DemigodsData.jOhm.get(TrackedPlayer.class, id);
	}

	public static Set<TrackedPlayer> loadAll()
	{
		try
		{
			return DemigodsData.jOhm.getAll(TrackedPlayer.class);
		}
		catch(Exception ignored)
		{}
		return new HashSet<TrackedPlayer>();
	}

	public static TrackedPlayer getMeta(OfflinePlayer player)
	{
		for(TrackedPlayer tracked : loadAll())
		{
			if(player.equals(tracked.getPlayer())) return tracked;
		}
		return TrackedModelFactory.createTrackedPlayer(player);
	}

	public OfflinePlayer getPlayer()
	{
		return Bukkit.getOfflinePlayer(this.player);
	}

	public void setLastLoginTime(long time)
	{
		this.lastLoginTime = time;
		TrackedPlayer.save(this);
	}

	public long getLastLoginTime()
	{
		return this.lastLoginTime;
	}

	public void setCurrent(PlayerCharacter character)
	{
		this.previous = this.current;
		this.current = character;
		TrackedPlayer.save(this);
	}

	public PlayerCharacter getCurrent()
	{
		return this.current;
	}

	public PlayerCharacter getPrevious()
	{
		return this.previous;
	}
}
