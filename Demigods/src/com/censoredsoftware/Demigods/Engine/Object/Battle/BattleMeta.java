package com.censoredsoftware.Demigods.Engine.Object.Battle;

import java.util.Set;

import org.bukkit.Location;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.Engine.Object.DemigodsLocation;
import com.censoredsoftware.Demigods.Engine.Object.DemigodsModelFactory;
import com.censoredsoftware.Demigods.Engine.Object.PlayerCharacter.PlayerCharacter;
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
	@CollectionSet(of = DemigodsLocation.class)
	private Set<DemigodsLocation> locations;
	@Reference
	@Indexed
	private PlayerCharacter startedBy;
	@CollectionSet(of = Long.class)
	private Set<PlayerCharacter> participants;
	@CollectionSet(of = String.class)
	private Set<String> alliances;

	public static BattleMeta create(PlayerCharacter character)
	{
		BattleMeta meta = new BattleMeta();
		meta.initialize();
		meta.setStarter(character);
		BattleMeta.save(meta);
		return meta;
	}

	void setStarter(PlayerCharacter character)
	{
		this.startedBy = character;
		addParticipant(character);
	}

	void initialize()
	{
		this.kills = 0;
		this.deaths = 0;
	}

	public void addParticipant(PlayerCharacter character)
	{
		if(this.participants == null) this.participants = Sets.newHashSet();
		this.participants.add(character);
	}

	public void addLocation(Location location)
	{
		if(this.locations == null) this.locations = Sets.newHashSet();
		this.locations.add(DemigodsModelFactory.createDemigodsLocation(location));
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
		if(this.participants == null) this.participants = Sets.newHashSet();
		return this.participants;
	}

	public Set<Location> getLocations()
	{
		Set<Location> locations = Sets.newHashSet();

		if(this.locations != null)
		{
			for(DemigodsLocation location : this.locations)
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
