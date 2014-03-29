package com.demigodsrpg.demigods.engine.entity.player;

import com.censoredsoftware.library.language.Symbol;
import com.demigodsrpg.demigods.engine.Demigods;
import com.demigodsrpg.demigods.engine.DemigodsPlugin;
import com.demigodsrpg.demigods.engine.battle.Participant;
import com.demigodsrpg.demigods.engine.data.DataAccess;
import com.demigodsrpg.demigods.engine.data.IdType;
import com.demigodsrpg.demigods.engine.data.Register;
import com.demigodsrpg.demigods.engine.data.TimedServerData;
import com.demigodsrpg.demigods.engine.deity.Ability;
import com.demigodsrpg.demigods.engine.deity.Alliance;
import com.demigodsrpg.demigods.engine.deity.Deity;
import com.demigodsrpg.demigods.engine.entity.DemigodsTameable;
import com.demigodsrpg.demigods.engine.entity.player.attribute.Death;
import com.demigodsrpg.demigods.engine.entity.player.attribute.DemigodsCharacterMeta;
import com.demigodsrpg.demigods.engine.entity.player.attribute.DemigodsPotionEffect;
import com.demigodsrpg.demigods.engine.entity.player.attribute.Skill;
import com.demigodsrpg.demigods.engine.event.DemigodsChatEvent;
import com.demigodsrpg.demigods.engine.inventory.DemigodsEnderInventory;
import com.demigodsrpg.demigods.engine.inventory.DemigodsPlayerInventory;
import com.demigodsrpg.demigods.engine.language.English;
import com.demigodsrpg.demigods.engine.location.DemigodsLocation;
import com.demigodsrpg.demigods.engine.structure.DemigodsStructure;
import com.demigodsrpg.demigods.engine.structure.DemigodsStructureType;
import com.demigodsrpg.demigods.engine.util.Configs;
import com.demigodsrpg.demigods.engine.util.Messages;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class DemigodsCharacter extends DataAccess<UUID, DemigodsCharacter> implements Participant
{
	private UUID id;
	private String name;
	private String mojangAccount;
	private boolean alive;
	private double health;
	private Integer hunger;
	private Float experience;
	private Integer level;
	private Integer killCount;
	private UUID location;
	private UUID bedSpawn;
	private GameMode gameMode;
	private String deity;
	private Set<String> minorDeities;
	private boolean active;
	private boolean usable;
	private UUID meta;
	private UUID inventory, enderInventory;
	private Set<String> potionEffects;
	private Set<String> deaths;

	private DemigodsCharacter(Object ignored)
	{
	}

	public DemigodsCharacter()
	{
		deaths = Sets.newHashSet();
		potionEffects = Sets.newHashSet();
		minorDeities = Sets.newHashSet();
	}

	@Register(idType = IdType.UUID)
	public DemigodsCharacter(UUID id, ConfigurationSection conf)
	{
		this.id = id;
		name = conf.getString("name");
		mojangAccount = conf.getString("mojangAccount");
		if(conf.isBoolean("alive")) alive = conf.getBoolean("alive");
		health = conf.getDouble("health");
		hunger = conf.getInt("hunger");
		experience = Float.valueOf(conf.getString("experience"));
		level = conf.getInt("level");
		killCount = conf.getInt("killCount");
		if(conf.isString("location"))
		{
			location = UUID.fromString(conf.getString("location"));
			try
			{
				DemigodsLocation.get(location);
			}
			catch(Exception errored)
			{
				location = null;
			}
		}
		if(conf.getString("bedSpawn") != null)
		{
			bedSpawn = UUID.fromString(conf.getString("bedSpawn"));
			try
			{
				DemigodsLocation.get(bedSpawn);
			}
			catch(Exception errored)
			{
				bedSpawn = null;
			}
		}
		if(conf.getString("gameMode") != null) gameMode = GameMode.SURVIVAL;
		deity = conf.getString("deity");
		active = conf.getBoolean("active");
		usable = conf.getBoolean("usable");
		meta = UUID.fromString(conf.getString("meta"));
		if(conf.isList("minorDeities")) minorDeities = Sets.newHashSet(conf.getStringList("minorDeities"));
		if(conf.isString("inventory")) inventory = UUID.fromString(conf.getString("inventory"));
		if(conf.isString("enderInventory")) enderInventory = UUID.fromString(conf.getString("enderInventory"));
		if(conf.isList("deaths")) deaths = Sets.newHashSet(conf.getStringList("deaths"));
		if(conf.isList("potionEffects")) potionEffects = Sets.newHashSet(conf.getStringList("potionEffects"));
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = Maps.newHashMap();
		try
		{
			map.put("name", name);
			map.put("mojangAccount", mojangAccount);
			map.put("alive", alive);
			map.put("health", health);
			map.put("hunger", hunger);
			map.put("experience", experience);
			map.put("level", level);
			map.put("killCount", killCount);
			if(location != null) map.put("location", location.toString());
			if(bedSpawn != null) map.put("bedSpawn", bedSpawn.toString());
			if(gameMode != null) map.put("gameMode", gameMode.name());
			map.put("deity", deity);
			if(minorDeities != null) map.put("minorDeities", Lists.newArrayList(minorDeities));
			map.put("active", active);
			map.put("usable", usable);
			map.put("meta", meta.toString());
			if(inventory != null) map.put("inventory", inventory.toString());
			if(enderInventory != null) map.put("enderInventory", enderInventory.toString());
			if(deaths != null) map.put("deaths", Lists.newArrayList(deaths));
			if(potionEffects != null) map.put("potionEffects", Lists.newArrayList(potionEffects));
		}
		catch(Exception ignored)
		{
		}
		return map;
	}

	void generateId()
	{
		id = UUID.randomUUID();
	}

	void setName(String name)
	{
		this.name = name;
	}

	void setDeity(Deity deity)
	{
		this.deity = deity.getName();
	}

	public void setMinorDeities(Set<String> set)
	{
		this.minorDeities = set;
	}

	public void addMinorDeity(Deity deity)
	{
		this.minorDeities.add(deity.getName());
	}

	public void removeMinorDeity(Deity deity)
	{
		this.minorDeities.remove(deity.getName());
	}

	void setMojangAccount(DemigodsPlayer player)
	{
		this.mojangAccount = player.getMojangAccount();
	}

	public void setActive(boolean option)
	{
		this.active = option;
		save();
	}

	public void saveInventory()
	{
		this.inventory = DemigodsPlayerInventory.create(this).getId();
		this.enderInventory = DemigodsEnderInventory.create(this).getId();
		save();
	}

	public void setAlive(boolean alive)
	{
		this.alive = alive;
		save();
	}

	public void setHealth(double health)
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
		this.location = DemigodsLocation.of(location).getId();
	}

	public void setBedSpawn(Location location)
	{
		this.bedSpawn = DemigodsLocation.of(location).getId();
	}

	public void setGameMode(GameMode gameMode)
	{
		this.gameMode = gameMode;
	}

	public void setMeta(DemigodsCharacterMeta meta)
	{
		this.meta = meta.getId();
	}

	public void setUsable(boolean usable)
	{
		this.usable = usable;
	}

	public void setPotionEffects(Collection<PotionEffect> potions)
	{
		if(potions != null)
		{
			if(potionEffects == null) potionEffects = Sets.newHashSet();

			for(PotionEffect potion : potions)
				potionEffects.add(DemigodsPotionEffect.of(potion).getId().toString());
		}
	}

	private Set<PotionEffect> getPotionEffects()
	{
		if(potionEffects == null) potionEffects = Sets.newHashSet();

		Set<PotionEffect> set = new HashSet<>();
		for(String stringId : potionEffects)
		{
			try
			{
				PotionEffect potion = DemigodsPotionEffect.get(UUID.fromString(stringId)).getBukkitPotionEffect();
				if(potion != null)
				{
					DemigodsPotionEffect.get(UUID.fromString(stringId)).remove();
					set.add(potion);
				}
			}
			catch(Exception ignored)
			{
			}
		}

		potionEffects.clear(); // METHOD MUST BE PRIVATE IF WE DO THIS HERE
		return set;
	}

	public Collection<DemigodsPotionEffect> getRawPotionEffects()
	{
		if(potionEffects == null) potionEffects = Sets.newHashSet();
		return Collections2.transform(potionEffects, new Function<String, DemigodsPotionEffect>()
		{
			@Override
			public DemigodsPotionEffect apply(String s)
			{
				try
				{
					return DemigodsPotionEffect.get(UUID.fromString(s));
				}
				catch(Exception ignored)
				{
				}
				return null;
			}
		});
	}

	public DemigodsPlayerInventory getInventory()
	{
		if(DemigodsPlayerInventory.get(inventory) == null) inventory = DemigodsPlayerInventory.createEmpty().getId();
		return DemigodsPlayerInventory.get(inventory);
	}

	public DemigodsEnderInventory getDemigodsEnderInventory()
	{
		if(DemigodsEnderInventory.get(enderInventory) == null) enderInventory = DemigodsEnderInventory.create(this).getId();
		return DemigodsEnderInventory.get(enderInventory);
	}

	public DemigodsCharacterMeta getMeta()
	{
		return DemigodsCharacterMeta.get(meta);
	}

	public OfflinePlayer getBukkitOfflinePlayer()
	{
		return Bukkit.getOfflinePlayer(getPlayerName());
	}

	public Player getBukkitPlayer()
	{
		return getBukkitOfflinePlayer().getPlayer();
	}

	public String getName()
	{
		return name;
	}

	public boolean isActive()
	{
		return active;
	}

	public Location getLocation()
	{
		if(location == null) return null;
		return DemigodsLocation.get(location).getBukkitLocation();
	}

	public Location getBedSpawn()
	{
		if(bedSpawn == null) return null;
		return DemigodsLocation.get(bedSpawn).getBukkitLocation();
	}

	public GameMode getGameMode()
	{
		return gameMode;
	}

	public Location getCurrentLocation()
	{
		if(getBukkitOfflinePlayer().isOnline()) return getBukkitOfflinePlayer().getPlayer().getLocation();
		return getLocation();
	}

	@Override
	public DemigodsCharacter getRelatedCharacter()
	{
		return this;
	}

	@Override
	public LivingEntity getEntity()
	{
		return getBukkitOfflinePlayer().getPlayer();
	}

	public String getMojangAccount()
	{
		return mojangAccount;
	}

	public String getPlayerName()
	{
		return DemigodsPlayer.get(mojangAccount).getPlayerName();
	}

	public Integer getLevel()
	{
		return level;
	}

	public boolean isAlive()
	{
		return alive;
	}

	public Double getHealth()
	{
		return health;
	}

	public Double getMaxHealth()
	{
		return getDeity().getMaxHealth();
	}

	public Integer getHunger()
	{
		return hunger;
	}

	public Float getExperience()
	{
		return experience;
	}

	public boolean isDeity(String deityName)
	{
		return getDeity().getName().equalsIgnoreCase(deityName);
	}

	public Deity getDeity()
	{
		return Demigods.getMythos().getDeity(this.deity);
	}

	public Collection<Deity> getMinorDeities()
	{
		return Collections2.transform(minorDeities, new Function<String, Deity>()
		{
			@Override
			public Deity apply(String deity)
			{
				return Demigods.getMythos().getDeity(deity);
			}
		});
	}

	public Alliance getAlliance()
	{
		return getDeity().getAlliance();
	}

	public int getKillCount()
	{
		return killCount;
	}

	public void setKillCount(int amount)
	{
		killCount = amount;
		save();
	}

	public void addKill()
	{
		killCount += 1;
		save();
	}

	public int getDeathCount()
	{
		return deaths.size();
	}

	public void addDeath()
	{
		if(deaths == null) deaths = Sets.newHashSet();
		deaths.add(Death.create(this).getId().toString());
		save();
	}

	public void addDeath(DemigodsCharacter attacker)
	{
		deaths.add(Death.create(this, attacker).getId().toString());
		save();
	}

	public Collection<Death> getDeaths()
	{
		if(deaths == null) deaths = Sets.newHashSet();
		return Collections2.transform(deaths, new Function<String, Death>()
		{
			@Override
			public Death apply(String s)
			{
				try
				{
					return Death.get(UUID.fromString(s));
				}
				catch(Exception ignored)
				{
				}
				return null;
			}
		});
	}

	public Collection<DemigodsStructure> getOwnedStructures()
	{
		return DemigodsStructure.find(new Predicate<DemigodsStructure>()
		{
			@Override
			public boolean apply(DemigodsStructure data)
			{
				return data.getOwner().equals(getId());
			}
		});
	}

	public Collection<DemigodsStructure> getOwnedStructures(final String type)
	{
		return DemigodsStructure.find(new Predicate<DemigodsStructure>()
		{
			@Override
			public boolean apply(DemigodsStructure data)
			{
				return data.getTypeName().equals(type) && data.getOwner().equals(getId());
			}
		});
	}

	public int getFavorRegen()
	{
		int favorRegenSkill = getMeta().getSkill(Skill.Type.FAVOR_REGEN) != null ? 4 * getMeta().getSkill(Skill.Type.FAVOR_REGEN).getLevel() : 0;
		int regenRate = (int) Math.ceil(Configs.getSettingDouble("multipliers.favor") * (getDeity().getFavorRegen() + favorRegenSkill));
		if(regenRate < 30) regenRate = 30;
		return regenRate;
	}

	public void setCanPvp(boolean pvp)
	{
		DemigodsPlayer.of(getBukkitOfflinePlayer()).setCanPvp(pvp);
	}

	@Override
	public boolean canPvp()
	{
		return DemigodsPlayer.get(getMojangAccount()).canPvp();
	}

	public boolean isUsable()
	{
		return usable;
	}

	public void updateUseable()
	{
		usable = Demigods.getMythos().getDeity(this.deity) != null && Demigods.getMythos().getDeity(this.deity).getFlags().contains(Deity.Flag.PLAYABLE);
	}

	public UUID getId()
	{
		return id;
	}

	public boolean alliedTo(Participant participant)
	{
		return getAlliance().equals(participant.getRelatedCharacter().getAlliance());
	}

	public Collection<DemigodsTameable> getPets()
	{
		return DemigodsTameable.findByOwner(id);
	}

	@Override
	public void remove()
	{
		// Define the DemigodsPlayer
		DemigodsPlayer demigodsPlayer = DemigodsPlayer.of(getBukkitOfflinePlayer());

		// Switch the player to mortal
		if(getBukkitOfflinePlayer().isOnline() && demigodsPlayer.getCharacter().getName().equalsIgnoreCase(getName())) demigodsPlayer.setToMortal();

		// Remove the data
		if(demigodsPlayer.getCharacter() != null && demigodsPlayer.getCharacter().getName().equalsIgnoreCase(getName())) demigodsPlayer.resetCurrent();
		for(DemigodsStructure structure : DemigodsStructureType.Util.getStructuresWithFlag(DemigodsStructureType.Flag.DELETE_WITH_OWNER))
			if(structure.hasOwner() && structure.getOwner().equals(getId())) structure.remove();
		for(DemigodsPotionEffect potion : getRawPotionEffects())
			potion.remove();
		getInventory().remove();
		getDemigodsEnderInventory().remove();
		getMeta().remove();
		super.remove();
	}

	public List<Ability> getAbilities()
	{
		List<Ability> list = Lists.newArrayList();

		list.addAll(getDeity().getAbilities());

		for(Deity minorDeity : getMinorDeities())
			list.addAll(minorDeity.getAbilities());

		return list;
	}

	public void sendAllianceMessage(String message)
	{
		DemigodsChatEvent chatEvent = new DemigodsChatEvent(message, Demigods.getServer().getOnlineCharactersWithAlliance(getAlliance()));
		Bukkit.getPluginManager().callEvent(chatEvent);
		if(!chatEvent.isCancelled()) for(Player player : chatEvent.getRecipients())
			player.sendMessage(message);
	}

	public void chatWithAlliance(String message)
	{
		sendAllianceMessage(" " + ChatColor.GRAY + getAlliance() + "s " + ChatColor.DARK_GRAY + "" + Symbol.BLACK_FLAG + " " + getDeity().getColor() + name + ChatColor.GRAY + ": " + ChatColor.RESET + message);
		Messages.info("[" + getAlliance() + "]" + name + ": " + message);
	}

	public void applyToPlayer(final Player player)
	{
		// Define variables
		DemigodsPlayer playerSave = DemigodsPlayer.of(player);

		// Set character to active
		setActive(true);

		if(playerSave.getMortalInventory() != null)
		{
			playerSave.setMortalName(player.getDisplayName());
			playerSave.setMortalListName(player.getPlayerListName());
		}

		// Update their inventory
		if(playerSave.getCharacters().size() == 1) saveInventory();
		else player.getEnderChest().clear();
		getInventory().setToPlayer(player);
		getDemigodsEnderInventory().setToPlayer(player);

		// Update health, experience, and name
		player.setDisplayName(getDeity().getColor() + getName());
		player.setPlayerListName(getDeity().getColor() + getName());
		player.setMaxHealth(getMaxHealth());
		player.setHealth(getHealth() >= getMaxHealth() ? getMaxHealth() : getHealth());
		player.setFoodLevel(getHunger());
		player.setExp(getExperience());
		player.setLevel(getLevel());
		for(PotionEffect potion : player.getActivePotionEffects())
			player.removePotionEffect(potion.getType());
		Set<PotionEffect> potionEffects = getPotionEffects();
		if(!potionEffects.isEmpty()) player.addPotionEffects(potionEffects);
		Bukkit.getScheduler().scheduleSyncDelayedTask(DemigodsPlugin.getInst(), new BukkitRunnable()
		{
			@Override
			public void run()
			{
				if(getBedSpawn() != null) player.setBedSpawnLocation(getBedSpawn());
			}
		}, 1);
		if(gameMode != null) player.setGameMode(gameMode);

		// Set player display name
		player.setDisplayName(getDeity().getColor() + getName());
		player.setPlayerListName(getDeity().getColor() + getName());

		// Re-own pets
		DemigodsTameable.reownPets(player, this);
	}

	// -- STATIC GETTERS/SETTERS -- //

	private static final DataAccess<UUID, DemigodsCharacter> DATA_ACCESS = new DemigodsCharacter(null);

	public static DemigodsCharacter get(UUID id)
	{
		return DATA_ACCESS.getDirect(id);
	}

	public static Collection<DemigodsCharacter> all()
	{
		return DATA_ACCESS.allDirect();
	}

	// -- UTILITY METHODS -- //

	public static DemigodsCharacter of(OfflinePlayer player)
	{
		return DemigodsPlayer.of(player).getCharacter();
	}

	public static DemigodsCharacter create(DemigodsPlayer player, String chosenDeity, String chosenName, boolean switchCharacter)
	{
		// Switch to new character
		DemigodsCharacter character = create(player, chosenName, chosenDeity);
		if(switchCharacter) player.switchCharacter(character);
		return character;
	}

	public static DemigodsCharacter create(DemigodsPlayer player, String charName, String charDeity)
	{
		if(!charExists(charName))
		{
			// Create the DemigodsCharacter
			return create(player, charName, Demigods.getMythos().getDeity(charDeity));
		}
		return null;
	}

	private static DemigodsCharacter create(final DemigodsPlayer player, final String charName, final Deity deity)
	{
		DemigodsCharacter character = new DemigodsCharacter();
		character.generateId();
		character.setAlive(true);
		character.setMojangAccount(player);
		character.setName(charName);
		character.setDeity(deity);
		character.setMinorDeities(new HashSet<String>(0));
		character.setUsable(true);
		character.setHealth(deity.getMaxHealth());
		character.setHunger(20);
		character.setExperience(0);
		character.setLevel(0);
		character.setKillCount(0);
		character.setLocation(player.getBukkitOfflinePlayer().getPlayer().getLocation());
		character.setMeta(DemigodsCharacterMeta.create(character));
		character.save();

		// Log the creation
		Messages.info(English.LOG_CHARACTER_CREATED.getLine().replace("{character}", charName).replace("{id}", character.getId().toString()).replace("{deity}", deity.getName()));

		return character;
	}

	public static void updateUsableCharacters()
	{
		for(DemigodsCharacter character : all())
			character.updateUseable();
	}

	public static boolean charExists(String name)
	{
		return Demigods.getServer().getCharacter(name) != null;
	}

	public static boolean isCooledDown(DemigodsCharacter character, String abilityName)
	{
		return !TimedServerData.exists(character.getName(), abilityName + "_cooldown");
	}

	public static void setCooldown(DemigodsCharacter character, String abilityName, int cooldown)
	{
		TimedServerData.saveTimed(character.getName(), abilityName + "_cooldown", true, cooldown, TimeUnit.SECONDS);
	}

	public static Long getCooldown(DemigodsCharacter character, String abilityName)
	{
		return TimedServerData.getExpiration(character.getName(), abilityName + "_cooldown");
	}

	/**
	 * Updates favor for all online characters.
	 */
	public static void updateFavor()
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			DemigodsCharacter character = DemigodsCharacter.of(player);
			if(character != null) character.getMeta().addFavor(character.getFavorRegen());
		}
	}
}
