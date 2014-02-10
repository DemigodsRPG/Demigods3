package com.censoredsoftware.demigods.engine.entity.player;

import com.censoredsoftware.censoredlib.language.Symbol;
import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.DemigodsPlugin;
import com.censoredsoftware.demigods.engine.battle.Participant;
import com.censoredsoftware.demigods.engine.data.DataAccess;
import com.censoredsoftware.demigods.engine.data.serializable.StructureSave;
import com.censoredsoftware.demigods.engine.entity.player.attribute.Death;
import com.censoredsoftware.demigods.engine.entity.player.attribute.DemigodsCharacterMeta;
import com.censoredsoftware.demigods.engine.entity.player.attribute.Skill;
import com.censoredsoftware.demigods.engine.event.DemigodsChatEvent;
import com.censoredsoftware.demigods.engine.inventory.DemigodsEnderInventory;
import com.censoredsoftware.demigods.engine.inventory.DemigodsPlayerInventory;
import com.censoredsoftware.demigods.engine.language.English;
import com.censoredsoftware.demigods.engine.location.DemigodsLocation;
import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.censoredsoftware.demigods.engine.mythos.Alliance;
import com.censoredsoftware.demigods.engine.mythos.Deity;
import com.censoredsoftware.demigods.engine.mythos.StructureType;
import com.censoredsoftware.demigods.engine.util.Configs;
import com.censoredsoftware.demigods.engine.util.Messages;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.*;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
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
    {}

	public DemigodsCharacter()
	{
		deaths = Sets.newHashSet();
		potionEffects = Sets.newHashSet();
		minorDeities = Sets.newHashSet();
	}

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
				CLocationManager.load(location);
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
				CLocationManager.load(bedSpawn);
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
		{}
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

	public void setMeta(Meta meta)
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
				potionEffects.add((new DSavedPotion(potion)).getId().toString());
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
				PotionEffect potion = Util.getSavedPotion(UUID.fromString(stringId)).toPotionEffect();
				if(potion != null)
				{
					DSavedPotion.remove(UUID.fromString(stringId));
					set.add(potion);
				}
			}
			catch(Exception ignored)
			{}
		}

		potionEffects.clear(); // METHOD MUST BE PRIVATE IF WE DO THIS HERE
		return set;
	}

	public Collection<DSavedPotion> getRawPotionEffects()
	{
		if(potionEffects == null) potionEffects = Sets.newHashSet();
		return Collections2.transform(potionEffects, new Function<String, DSavedPotion>()
		{
			@Override
			public DSavedPotion apply(String s)
			{
				try
				{
					return DSavedPotion.get(UUID.fromString(s));
				}
				catch(Exception ignored)
				{}
				return null;
			}
		});
	}

	public CInventory getInventory()
	{
		if(Util.getInventory(inventory) == null) inventory = Util.createEmptyInventory().getId();
		return Util.getInventory(inventory);
	}

	public CEnderInventory getEnderInventory()
	{
		if(Util.getEnderInventory(enderInventory) == null) enderInventory = Util.createEnderInventory(this).getId();
		return Util.getEnderInventory(enderInventory);
	}

	public Meta getMeta()
	{
		return Util.loadMeta(meta);
	}

	public OfflinePlayer getOfflinePlayer()
	{
		return Bukkit.getOfflinePlayer(getPlayerName());
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
		return CLocationManager.load(location).toLocation();
	}

	public Location getBedSpawn()
	{
		if(bedSpawn == null) return null;
		return CLocationManager.load(bedSpawn).toLocation();
	}

	public GameMode getGameMode()
	{
		return gameMode;
	}

	public Location getCurrentLocation()
	{
		if(getOfflinePlayer().isOnline()) return getOfflinePlayer().getPlayer().getLocation();
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
		return getOfflinePlayer().getPlayer();
	}

	public String getMojangAccount()
	{
		return mojangAccount;
	}

	public String getPlayerName()
	{
		return DemigodsPlayer.Util.getPlayer(mojangAccount).getPlayerName();
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
		deaths.add(new Death(this).getId().toString());
		save();
	}

	public void addDeath(DemigodsCharacter attacker)
	{
		deaths.add(new Death(this, attacker).getId().toString());
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
				{}
				return null;
			}
		});
	}

	public Collection<StructureSave> getOwnedStructures()
	{
		return StructureSave.Util.findAll(new Predicate<StructureSave>()
		{
			@Override
			public boolean apply(StructureSave data)
			{
				return data.getOwner().equals(getId());
			}
		});
	}

	public Collection<StructureSave> getOwnedStructures(final String type)
	{
		return StructureSave.Util.findAll(new Predicate<StructureSave>()
		{
			@Override
			public boolean apply(StructureSave data)
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
		DemigodsPlayer.Util.getPlayer(getOfflinePlayer()).setCanPvp(pvp);
	}

	@Override
	public boolean canPvp()
	{
		return DemigodsPlayer.Util.getPlayer(getMojangAccount()).canPvp();
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

	public Collection<DPet> getPets()
	{
		return DPet.Util.findByOwner(id);
	}

	public void remove()
	{
		// Define the DemigodsPlayer
		DemigodsPlayer demigodsPlayer = DemigodsPlayer.Util.getPlayer(getOfflinePlayer());

		// Switch the player to mortal
		if(getOfflinePlayer().isOnline() && demigodsPlayer.getCurrent().getName().equalsIgnoreCase(getName())) demigodsPlayer.setToMortal();

		// Remove the data
		if(demigodsPlayer.getCurrent() != null && demigodsPlayer.getCurrent().getName().equalsIgnoreCase(getName())) demigodsPlayer.resetCurrent();
		for(StructureSave structureSave : StructureType.Util.getStructuresWithFlag(StructureType.Flag.DELETE_WITH_OWNER))
			if(structureSave.hasOwner() && structureSave.getOwner().equals(getId())) structureSave.remove();
		for(DSavedPotion potion : getRawPotionEffects())
            DSavedPotion.remove(potion.getId());
		Util.deleteInventory(getInventory().getId());
		Util.deleteEnderInventory(getEnderInventory().getId());
		Util.deleteMeta(getMeta().getId());
		Util.delete(getId());
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
		DemigodsChatEvent chatEvent = new DemigodsChatEvent(message, DemigodsCharacter.Util.getOnlineCharactersWithAlliance(getAlliance()));
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
		DemigodsPlayer playerSave = DemigodsPlayer.Util.getPlayer(player);

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
		getEnderInventory().setToPlayer(player);

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
		DPet.Util.reownPets(player, this);
	}

    // -- STATIC GETTERS/SETTERS -- //

    private static final DataAccess<UUID, DemigodsCharacter> DATA_ACCESS = new DemigodsCharacter(null);

    public static DemigodsCharacter get(UUID id)
    {
        return DATA_ACCESS.get(id);
    }

    public static Collection<DemigodsCharacter> all()
    {
        return DATA_ACCESS.getAll();
    }

    // -- UTILITY METHODS -- //

    public static DemigodsCharacter create(DemigodsPlayer player, String chosenDeity, String chosenName, boolean switchCharacter)
    {
        // Switch to new character
        DemigodsCharacter character = create(player, chosenName, chosenDeity);
        if(switchCharacter) player.switchCharacter( character);
        return character;
    }

    public static DemigodsCharacter create(DemigodsPlayer player, String charName, String charDeity)
    {
        if(getCharacterByName(charName) == null)
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
        character.setLocation(player.getOfflinePlayer().getPlayer().getLocation());
        character.setMeta(DemigodsCharacterMeta.create(character));
        character.save();

        // Log the creation
        Messages.info(English.LOG_CHARACTER_CREATED.getLine().replace("{character}", charName).replace("{id}", character.getId().toString()).replace("{deity}", deity.getName()));

        return character;
    }

		public static CInventory createInventory(DemigodsCharacter character)
		{
			PlayerInventory inventory = character.getOfflinePlayer().getPlayer().getInventory();
			Inventory charInventory = new Inventory();
			charInventory.generateId();
			if(inventory.getHelmet() != null) charInventory.setHelmet(inventory.getHelmet());
			if(inventory.getChestplate() != null) charInventory.setChestplate(inventory.getChestplate());
			if(inventory.getLeggings() != null) charInventory.setLeggings(inventory.getLeggings());
			if(inventory.getBoots() != null) charInventory.setBoots(inventory.getBoots());
			charInventory.setItems(inventory);
			saveInventory(charInventory);
			return charInventory;
		}

		public static CInventory createEmptyInventory()
		{
			Inventory charInventory = new Inventory();
			charInventory.generateId();
			charInventory.setHelmet(new ItemStack(Material.AIR));
			charInventory.setChestplate(new ItemStack(Material.AIR));
			charInventory.setLeggings(new ItemStack(Material.AIR));
			charInventory.setBoots(new ItemStack(Material.AIR));
			saveInventory(charInventory);
			return charInventory;
		}

		public static CEnderInventory createEnderInventory(DemigodsCharacter character)
		{
			org.bukkit.inventory.Inventory inventory = character.getOfflinePlayer().getPlayer().getEnderChest();
			EnderInventory enderInventory = new EnderInventory();
			enderInventory.generateId();
			enderInventory.setItems(inventory);
			saveInventory(enderInventory);
			return enderInventory;
		}

		public static CEnderInventory createEmptyEnderInventory()
		{
			EnderInventory enderInventory = new EnderInventory();
			enderInventory.generateId();
			saveInventory(enderInventory);
			return enderInventory;
		}

		public static Meta createMeta(DemigodsCharacter character)
		{
			Meta charMeta = new Meta();
			charMeta.initialize();
			charMeta.setCharacter(character);
			charMeta.generateId();
			charMeta.setFavor(Configs.getSettingInt("character.defaults.favor"));
			charMeta.setMaxFavor(Configs.getSettingInt("character.defaults.max_favor"));
			charMeta.resetSkills();
			saveMeta(charMeta);
			return charMeta;
		}

		public static Set<DemigodsCharacter> loadAll()
		{
			return Sets.newHashSet(Data.CHARACTER.values());
		}

		public static DemigodsCharacter load(UUID id)
		{
			return Data.CHARACTER.get(id);
		}

		public static Meta loadMeta(UUID id)
		{
			return Data.CHARACTER_META.get(id);
		}

		public static Inventory getInventory(UUID id)
		{
			try
			{
				return Data.CHARACTER_INVENTORY.get(id);
			}
			catch(Exception ignored)
			{}
			return null;
		}

		public static EnderInventory getEnderInventory(UUID id)
		{
			try
			{
				return Data.CHARACTER_ENDER_INVENTORY.get(id);
			}
			catch(Exception ignored)
			{}
			return null;
		}

		public static DSavedPotion getSavedPotion(UUID id)
		{
			try
			{
				return Data.SAVED_POTION.get(id);
			}
			catch(Exception ignored)
			{}
			return null;
		}

		public static void updateUsableCharacters()
		{
			for(DemigodsCharacter character : loadAll())
				character.updateUseable();
		}

		public static DemigodsCharacter getCharacterByName(final String name)
		{
			try
			{
				return Iterators.find(loadAll().iterator(), new Predicate<DemigodsCharacter>()
				{
					@Override
					public boolean apply(DemigodsCharacter loaded)
					{
						return loaded.getName().equalsIgnoreCase(name);
					}
				});
			}
			catch(Exception ignored)
			{}
			return null;
		}

		public static boolean charExists(String name)
		{
			return getCharacterByName(name) != null;
		}

		public static boolean isCooledDown(DemigodsCharacter character, String abilityName)
		{
			return !Data.TIMED.boolContainsKey(character.getName() + abilityName + "_cooldown");
		}

		public static void setCooldown(DemigodsCharacter character, String abilityName, int cooldown)
		{
			Data.TIMED.setBool(character.getName() + abilityName + "_cooldown", true, cooldown, TimeUnit.SECONDS);
		}

		public static Long getCooldown(DemigodsCharacter character, String abilityName)
		{
			return Data.TIMED.boolExpireInMilli(character.getName() + abilityName + "_cooldown");
		}

		public static Set<DemigodsCharacter> getAllActive()
		{
			return Sets.filter(loadAll(), new Predicate<DemigodsCharacter>()
			{
				@Override
				public boolean apply(DemigodsCharacter character)
				{
					return character.isUsable() && character.isActive();
				}
			});
		}

		public static Set<DemigodsCharacter> getAllUsable()
		{
			return Sets.filter(loadAll(), new Predicate<DemigodsCharacter>()
			{
				@Override
				public boolean apply(DemigodsCharacter character)
				{
					return character.isUsable();
				}
			});
		}

		/**
		 * Returns true if <code>char1</code> is allied with <code>char2</code> based
		 * on their current alliances.
		 * 
		 * @param char1 the first character to check.
		 * @param char2 the second character to check.
		 * @return boolean
		 */
		public static boolean areAllied(DemigodsCharacter char1, DemigodsCharacter char2)
		{
			return char1.getAlliance().getName().equalsIgnoreCase(char2.getAlliance().getName());
		}

		public static Collection<DemigodsCharacter> getOnlineCharactersWithDeity(final String deity)
		{
			return getCharactersWithPredicate(new Predicate<DemigodsCharacter>()
			{
				@Override
				public boolean apply(DemigodsCharacter character)
				{
					return character.isActive() && character.getOfflinePlayer().isOnline() && character.getDeity().getName().equalsIgnoreCase(deity);
				}
			});
		}

		public static Collection<DemigodsCharacter> getOnlineCharactersWithAbility(final String abilityName)
		{
			return getCharactersWithPredicate(new Predicate<DemigodsCharacter>()
			{
				@Override
				public boolean apply(DemigodsCharacter character)
				{
					if(character.isActive() && character.getOfflinePlayer().isOnline())
					{
						for(Ability abilityToCheck : character.getDeity().getAbilities())
							if(abilityToCheck.getName().equalsIgnoreCase(abilityName)) return true;
					}
					return false;
				}
			});
		}

		public static Collection<DemigodsCharacter> getOnlineCharactersWithAlliance(final Alliance alliance)
		{
			return getCharactersWithPredicate(new Predicate<DemigodsCharacter>()
			{
				@Override
				public boolean apply(DemigodsCharacter character)
				{
					return character.isActive() && character.getOfflinePlayer().isOnline() && character.getAlliance().equals(alliance);
				}
			});
		}

		public static Collection<DemigodsCharacter> getOnlineCharactersWithoutAlliance(final Alliance alliance)
		{
			return getCharactersWithPredicate(new Predicate<DemigodsCharacter>()
			{
				@Override
				public boolean apply(DemigodsCharacter character)
				{
					return character.isActive() && character.getOfflinePlayer().isOnline() && !character.getAlliance().equals(alliance);
				}
			});
		}

		public static Collection<DemigodsCharacter> getOnlineCharactersBelowAscension(final int ascension)
		{
			return getCharactersWithPredicate(new Predicate<DemigodsCharacter>()
			{
				@Override
				public boolean apply(DemigodsCharacter character)
				{
					return character.isActive() && character.getOfflinePlayer().isOnline() && character.getMeta().getAscensions() < ascension;
				}
			});
		}

		public static Collection<DemigodsCharacter> getOnlineCharacters()
		{
			return getCharactersWithPredicate(new Predicate<DemigodsCharacter>()
			{
				@Override
				public boolean apply(DemigodsCharacter character)
				{
					return character.isActive() && character.getOfflinePlayer().isOnline();
				}
			});
		}

		public static Collection<DemigodsCharacter> getAllCharactersWithDeity(final String deity)
		{
			return getCharactersWithPredicate(new Predicate<DemigodsCharacter>()
			{
				@Override
				public boolean apply(DemigodsCharacter character)
				{
					return character.isActive() && character.getDeity().getName().equalsIgnoreCase(deity);
				}
			});
		}

		public static Collection<DemigodsCharacter> getAllCharactersWithAlliance(final Alliance alliance)
		{
			return getCharactersWithPredicate(new Predicate<DemigodsCharacter>()
			{
				@Override
				public boolean apply(DemigodsCharacter character)
				{
					return character.isActive() && character.getAlliance().equals(alliance);
				}
			});
		}

		public static Collection<DemigodsCharacter> getCharactersWithPredicate(Predicate<DemigodsCharacter> predicate)
		{
			return Collections2.filter(getAllUsable(), predicate);
		}

		/**
		 * Updates favor for all online characters.
		 */
		public static void updateFavor()
		{
			for(Player player : Bukkit.getOnlinePlayers())
			{
				DemigodsCharacter character = DemigodsPlayer.Util.getPlayer(player).getCurrent();
				if(character != null) character.getMeta().addFavor(character.getFavorRegen());
			}
		}
	}
}
