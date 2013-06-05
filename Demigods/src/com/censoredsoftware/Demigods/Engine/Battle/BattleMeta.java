package com.censoredsoftware.Demigods.Engine.Battle;

import java.util.Set;

import org.bukkit.Location;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedLocation;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedModelFactory;
import com.google.common.collect.Sets;

@Model
public class BattleMeta
{
	@Id
	private Long id;
	@Attribute
	private Integer kills;
	@Attribute
	private Integer deaths;
	@CollectionSet(of = TrackedLocation.class)
	private Set<TrackedLocation> locations;
	@Reference
	@Indexed
	private PlayerCharacter startedBy;
	@CollectionSet(of = Long.class)
	private Set<PlayerCharacter> participants;
	@CollectionSet(of = String.class)
	private Set<String> alliances;

	void setStarter(PlayerCharacter character)
	{
		this.startedBy = character;
	}

	public void addParticipant(PlayerCharacter character)
	{
		if(this.participants == null) this.participants = Sets.newHashSet();
		this.participants.add(character);
	}

	public void addLocation(Location location)
	{
		if(this.locations == null) this.locations = Sets.newHashSet();
		this.locations.add(TrackedModelFactory.createTrackedLocation(location));
	}

	public void addKills(int kills)
	{
		this.kills += kills;
	}

	public void addDeaths(int deaths)
	{
		this.deaths += deaths;
	}

	public PlayerCharacter getStarter()
	{
		return this.startedBy;
	}

	public Set<PlayerCharacter> getParticipants()
	{
		return this.participants;
	}

	public Set<Location> getLocations()
	{
		Set<Location> locations = Sets.newHashSet();

		if(this.locations != null)
		{
			for(TrackedLocation location : this.locations)
			{
				locations.add(location.toLocation());
			}
		}

		return locations;
	}

	public long getId()
	{
		return this.id;
	}

	public static BattleMeta load(Long id)
	{
		return JOhm.get(BattleMeta.class, id);
	}

	public static Set<BattleMeta> loadAll()
	{
		return JOhm.getAll(BattleMeta.class);
	}

	public static void save(BattleMeta meta)
	{
		JOhm.save(meta);
	}

	public void delete()
	{
		JOhm.delete(BattleMeta.class, getId());
	}
}
