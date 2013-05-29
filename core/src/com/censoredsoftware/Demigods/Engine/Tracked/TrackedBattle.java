package com.censoredsoftware.Demigods.Engine.Tracked;

import java.util.Set;

import org.bukkit.Location;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.Engine.DemigodsData;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;

@Model
public class TrackedBattle
{
	@Id
	private Long id;
	@Reference
	@Indexed
	private PlayerCharacter whoStarted;
	@Reference
	@Indexed
	private TrackedLocation startLocation;
	@CollectionSet(of = PlayerCharacter.class)
	@Indexed
	private Set<PlayerCharacter> involvedCharacters;
	@CollectionSet(of = TrackedLocation.class)
	@Indexed
	private Set<TrackedLocation> involvedLocations;
	@Attribute
	private Long startTime;
	@Attribute
	private Long endTime;
	@Attribute
	private boolean active;

	void setWhoStarted(PlayerCharacter character)
	{
		this.whoStarted = character;
	}

	void setStartLocation(TrackedLocation location)
	{
		this.startLocation = location;
	}

	void setInvolvedCharacters(Set<PlayerCharacter> characters)
	{
		this.involvedCharacters = characters;
	}

	void setInvolvedLocations(Set<TrackedLocation> locations)
	{
		this.involvedLocations = locations;
	}

	void setStartTime(long time)
	{
		this.startTime = time;
	}

	public void setEndTime(long time)
	{
		this.endTime = time;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}

	public static void save(TrackedBattle battle)
	{
		DemigodsData.jOhm.save(battle);
	}

	public static TrackedBattle load(long id) // TODO This belongs somewhere else.
	{
		return DemigodsData.jOhm.get(TrackedBattle.class, id);
	}

	public static Set<TrackedBattle> loadAll()
	{
		return DemigodsData.jOhm.getAll(TrackedBattle.class);
	}

	public Long getId()
	{
		return this.id;
	}

	public void addCharacter(PlayerCharacter character)
	{
		this.involvedCharacters.add(character);
		if(character.getOfflinePlayer().isOnline()) addLocation(character.getOfflinePlayer().getPlayer().getLocation());
		save(this);
	}

	public void removeCharacter(PlayerCharacter character)
	{
		if(this.involvedCharacters.contains(character)) this.involvedCharacters.remove(character);
		save(this);
	}

	public Set<TrackedLocation> getLocations()
	{
		return this.involvedLocations;
	}

	public void addLocation(Location location)
	{
		if(!this.involvedLocations.contains(TrackedLocation.getTracked(location))) this.involvedLocations.add(TrackedLocation.getTracked(location));
		save(this);
	}

	public void removeLocation(Location location)
	{
		if(this.involvedLocations.contains(TrackedLocation.getTracked(location))) this.involvedLocations.remove(TrackedLocation.getTracked(location));
		save(this);
	}

	public PlayerCharacter getWhoStarted()
	{
		return this.whoStarted;
	}

	public Set<PlayerCharacter> getInvolvedCharacters()
	{
		return this.involvedCharacters;
	}

	public Long getStartTime()
	{
		return this.startTime;
	}

	public Long getEndTime()
	{
		return this.endTime;
	}

	public boolean isActive()
	{
		return this.active;
	}
}
