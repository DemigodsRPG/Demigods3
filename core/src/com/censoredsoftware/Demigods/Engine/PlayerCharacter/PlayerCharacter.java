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
	private String alliance;
	@Attribute
	private int favor;
	@Attribute
	private int maxFavor;
	@Attribute
	private int devotion;
	@Attribute
	private int ascensions;
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

	public PlayerCharacter(OfflinePlayer player, String charName, boolean active, Deity deity, int favor, int maxFavor, int devotion, int ascensions, int offense, int defense, int stealth, int support, int passive, boolean immortal)
	{
		// Vanilla Data
		this.player = player.getName();
		this.health = 20;
		this.hunger = 20;
		this.experience = 0;
		this.level = 0;
		if(player.isOnline()) this.location = TrackedModelFactory.createTrackedLocation(player.getPlayer().getLocation()); // TODO
		if(player.isOnline()) this.inventory = TrackedModelFactory.createTrackedPlayerInventory(player.getPlayer().getInventory()); // TODO

		// Demigods Data
		this.name = charName;
		this.active = active;
		this.deity = deity.getInfo().getName();
		this.alliance = deity.getInfo().getAlliance();
		this.favor = favor;
		this.maxFavor = maxFavor;
		this.devotion = devotion;
		this.ascensions = ascensions;

		this.immortal = immortal;

		// Meta Data
		this.meta = new PlayerCharacterMeta(true);
		this.meta.setLevel("OFFENSE", offense);
		this.meta.setLevel("DEFENSE", defense);
		this.meta.setLevel("STEALTH", stealth);
		this.meta.setLevel("SUPPORT", support);
		this.meta.setLevel("PASSIVE", passive);

		// Location Data
		this.warps = new HashMap<TrackedLocation, String>();
		this.invites = new HashMap<TrackedLocation, String>();

		save();
	}

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

	public void setHealth(int amount)
	{
		this.health = amount;
	}

	public void saveInventory()
	{
		this.inventory = TrackedModelFactory.createTrackedPlayerInventory(getOwner().getPlayer().getInventory());
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

	public void setHunger(int amount)
	{
		this.hunger = amount;
	}

	public void setExperience(float amount)
	{
		this.experience = amount;
	}

	public void setLevel(int amount)
	{
		this.level = amount;
	}

	public void setLocation(Location location)
	{
		this.location = TrackedModelFactory.createTrackedLocation(location);
	}

	public void setActive(boolean option)
	{
		this.active = option;
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

	public void setImmortal(boolean option)
	{
		this.immortal = option;
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

	public int getFavor()
	{
		return this.favor;
	}

	public int getMaxFavor()
	{
		return this.maxFavor;
	}

	public int getDevotion()
	{
		return this.maxFavor;
	}

	public int getAscensions()
	{
		return this.ascensions;
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
