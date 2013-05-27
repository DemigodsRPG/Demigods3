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
import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.DemigodsData;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedLocation;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedModelFactory;

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
	private Integer health;
	@Attribute
	private Integer hunger;
	@Attribute
	private Float experience;
	@Attribute
	private Integer level;
	@Reference
	private TrackedLocation location;
	@Attribute
	@Indexed
	private String deity;
	@Attribute
	@Indexed
	private Boolean active;
	@Attribute
	@Indexed
	private Boolean immortal;
	@Reference
	private PlayerCharacterMeta meta;
	@Reference
	private PlayerCharacterInventory inventory;
	@CollectionMap(key = TrackedLocation.class, value = String.class)
	private Map<TrackedLocation, String> warps;
	@CollectionMap(key = TrackedLocation.class, value = String.class)
	private Map<TrackedLocation, String> invites;

	public static void save(PlayerCharacter character)
	{
		DemigodsData.jOhm.save(character);
	}

	public void delete()
	{
		DemigodsData.jOhm.delete(PlayerCharacter.class, getId());
	}

	public static PlayerCharacter load(Long id) // TODO This belongs somewhere else.
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
		Demigods.message.broadcast("Saving inventory.");
		this.inventory = PlayerCharacterFactory.createPlayerCharacterInventory(getPlayer().getPlayer().getInventory());
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

	public PlayerCharacterInventory getInventory()
	{
		if(this.inventory != null)
		{
			Demigods.message.broadcast("Getting inventory.");
			return this.inventory;
		}
		else if(Bukkit.getOfflinePlayer(this.player).isOnline())
		{
			this.inventory = PlayerCharacterFactory.createPlayerCharacterInventory(Bukkit.getOfflinePlayer(this.player).getPlayer().getInventory());
			Demigods.message.broadcast("Could not find inventory.");
			return this.inventory;
		}
		else return null;
	}

	public PlayerCharacterMeta getMeta()
	{
		if(this.meta != null) return this.meta;
		else
		{
			this.meta = PlayerCharacterFactory.createCharacterMeta();
			return this.meta;
		}
	}

	public OfflinePlayer getPlayer()
	{
		return Bukkit.getOfflinePlayer(this.player);
	}

	public String getName()
	{
		return this.name;
	}

	public Boolean isActive()
	{
		return this.active;
	}

	public Location getLocation()
	{
		return this.location.toLocation();
	}

	public Integer getHealth()
	{
		return this.health;
	}

	public Integer getLevel()
	{
		return this.level;
	}

	public ChatColor getHealthColor()
	{
		int hp = getHealth();
		int maxHP = Bukkit.getPlayer(getPlayer().getName()).getMaxHealth();
		ChatColor color = ChatColor.RESET;

		// Set favor color dynamically
		if(hp < Math.ceil(0.33 * maxHP)) color = ChatColor.RED;
		else if(hp < Math.ceil(0.66 * maxHP) && hp > Math.ceil(0.33 * maxHP)) color = ChatColor.YELLOW;
		if(hp > Math.ceil(0.66 * maxHP)) color = ChatColor.GREEN;

		return color;
	}

	public Integer getHunger()
	{
		return this.hunger;
	}

	public Float getExperience()
	{
		return this.experience;
	}

	public Boolean isDeity(String deityName)
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

	public Boolean isImmortal()
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

	public Long getId()
	{
		return id;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}
