package com.censoredsoftware.Demigods.Engine.Tracked;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.Engine.DemigodsData;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;

@Model
public class TrackedBattle
{
	@Id
	private long id;
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

	public TrackedBattle(PlayerCharacter attacking, PlayerCharacter defending, final Long startTime)
	{
		// Define variables
		Player started = (Player) attacking.getOwner();
		Location startedLocation = started.getLocation();

		this.whoStarted = attacking;
		this.startLocation = new TrackedLocation(startedLocation);
		this.startTime = startTime;

		addCharacter(attacking);
		addCharacter(defending);

		this.active = true;

		save();
	}

	public void save()
	{
		DemigodsData.jOhm.save(this);
	}

	public static TrackedBattle load(long id) // TODO This belongs somewhere else.
	{
		return DemigodsData.jOhm.get(TrackedBattle.class, id);
	}

	public static Set<TrackedBattle> loadAll()
	{
		return DemigodsData.jOhm.getAll(TrackedBattle.class);
	}

	public long getId()
	{
		return this.id;
	}

	public void addCharacter(PlayerCharacter character)
	{
		this.involvedCharacters.add(character);
		if(character.getOwner().isOnline()) addLocation(character.getOwner().getPlayer().getLocation());
		save();
	}

	public void removeCharacter(PlayerCharacter character)
	{
		if(this.involvedCharacters.contains(character)) this.involvedCharacters.remove(character);
		save();
	}

	public Set<TrackedLocation> getLocations()
	{
		return this.involvedLocations;
	}

	public void addLocation(Location location)
	{
		if(!this.involvedLocations.contains(new TrackedLocation(location))) this.involvedLocations.add(new TrackedLocation(location));
		save();
	}

	public void removeLocation(Location location)
	{
		if(this.involvedLocations.contains(new TrackedLocation(location))) this.involvedLocations.remove(new TrackedLocation(location));
		save();
	}

	public PlayerCharacter getWhoStarted()
	{
		return this.whoStarted;
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

	public synchronized void setActive(boolean active)
	{
		this.active = active;
	}
}
