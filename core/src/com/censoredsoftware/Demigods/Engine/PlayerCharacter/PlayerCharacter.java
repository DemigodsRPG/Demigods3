package com.censoredsoftware.Demigods.Engine.PlayerCharacter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.API.DeityAPI;
import com.censoredsoftware.Demigods.Engine.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.DemigodsData;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedLocation;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedModelFactory;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedPlayerInventory;

@Model
public class PlayerCharacter
{
	@Id
	private Long id;
	@Attribute
	@Indexed
	private String name;
	@Attribute
	@Indexed
	private String player;
	@Attribute
	private int health;
	@Attribute
	private int hunger;
	@Attribute
	private float experience;
	@Attribute
	private int level;
	@Reference
	private TrackedLocation location;
	@Reference
	private TrackedPlayerInventory inventory;
	@Attribute
	@Indexed
	private String deity;
	@Attribute
	@Indexed
	private boolean active;
	@Attribute
	@Indexed
	private boolean immortal;
	@Reference
	private PlayerCharacterMeta meta;
	@CollectionMap(key = TrackedLocation.class, value = Integer.class)
	private Map<TrackedLocation, String> warps;
	@CollectionMap(key = TrackedLocation.class, value = Integer.class)
	private Map<TrackedLocation, String> invites;

	public void save()
	{
		DemigodsData.jOhm.save(this);
	}

	public void delete()
	{
		DemigodsData.jOhm.delete(PlayerCharacter.class, getId());
	}

	public static PlayerCharacter load(long id) // TODO This belongs somewhere else.
	{
		return DemigodsData.jOhm.get(PlayerCharacter.class, id);
	}

	public static Set<PlayerCharacter> loadAll()
	{
		return DemigodsData.jOhm.getAll(PlayerCharacter.class);
	}

	public static PlayerCharacter getCharacterByName(String name)
	{
		for(PlayerCharacter loaded : loadAll())
		{
			if(loaded.getName().equalsIgnoreCase(name)) return loaded;
		}
		return null;
	}

	void setName(String name)
	{
		this.name = name;
	}

	void setDeity(Deity deity)
	{
		this.deity = deity.getInfo().getName();
	}

	void setPlayer(OfflinePlayer player)
	{
		this.player = player.getName();
	}

	public void setImmortal(boolean option)
	{
		this.immortal = option;
	}

	public void setActive(boolean option)
	{
		this.active = option;
	}

	public void saveInventory()
	{
		this.inventory = TrackedModelFactory.createTrackedPlayerInventory(getOwner().getPlayer().getInventory());
	}

	public void setHealth(int health)
	{
		this.health = health;
	}

	public void setHunger(int hunger)
	{
		this.hunger = hunger;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	public void setExperience(float exp)
	{
		this.experience = exp;
	}

	public void setLocation(Location location)
	{
		this.location = TrackedModelFactory.createTrackedLocation(location);
	}

	public TrackedPlayerInventory getInventory()
	{
		if(this.inventory != null) return this.inventory;
		else if(Bukkit.getOfflinePlayer(this.player).isOnline())
		{
			this.inventory = TrackedModelFactory.createTrackedPlayerInventory(Bukkit.getOfflinePlayer(this.player).getPlayer().getInventory());
			return this.inventory;
		}
		else return null;
	}

	public PlayerCharacterMeta getMeta()
	{
		if(this.meta != null) return this.meta;
		else
		{
			this.meta = new PlayerCharacterMeta(true);
			return this.meta;
		}
	}

	public OfflinePlayer getOwner()
	{
		return Bukkit.getOfflinePlayer(this.player);
	}

	public String getName()
	{
		return this.name;
	}

	public boolean isActive()
	{
		return this.active;
	}

	public TrackedLocation getLocation()
	{
		return this.location;
	}

	public int getHealth()
	{
		return this.health;
	}

	public int getLevel()
	{
		return this.level;
	}

	public ChatColor getHealthColor()
	{
		int hp = getHealth();
		int maxHP = Bukkit.getPlayer(getOwner().getName()).getMaxHealth();
		ChatColor color = ChatColor.RESET;

		// Set favor color dynamically
		if(hp < Math.ceil(0.33 * maxHP)) color = ChatColor.RED;
		else if(hp < Math.ceil(0.66 * maxHP) && hp > Math.ceil(0.33 * maxHP)) color = ChatColor.YELLOW;
		if(hp > Math.ceil(0.66 * maxHP)) color = ChatColor.GREEN;

		return color;
	}

	public int getHunger()
	{
		return this.hunger;
	}

	public float getExperience()
	{
		return this.experience;
	}

	public boolean isDeity(String deityName)
	{
		return getDeity().getInfo().getName().equalsIgnoreCase(deityName);
	}

	public Deity getDeity()
	{
		return DeityAPI.getDeity(this.deity);
	}

	public String getAlliance()
	{
		return getDeity().getInfo().getAlliance();
	}

	public boolean isImmortal()
	{
		return this.immortal;
	}

	public void addWarp(TrackedLocation location, String name)
	{
		this.warps.put(location, name);
	}

	public void removeWarp(TrackedLocation location)
	{
		this.warps.remove(location);
	}

	public Map<TrackedLocation, String> getWarps()
	{
		return this.warps;
	}

	public void addInvite(TrackedLocation location, String name)
	{
		this.invites.put(location, name);
	}

	public void removeInvite(TrackedLocation location)
	{
		this.invites.remove(location);
	}

	public void clearInvites()
	{
		this.invites = new HashMap<TrackedLocation, String>();
	}

	public Map<TrackedLocation, String> getInvites()
	{
		return this.invites;
	}

	public long getId()
	{
		return id;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}
