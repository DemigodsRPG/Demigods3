package com.censoredsoftware.demigods.battle;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.data.DataManager;
import com.censoredsoftware.demigods.exception.SpigotNotFoundException;
import com.censoredsoftware.demigods.helper.ConfigFile;
import com.censoredsoftware.demigods.location.DLocation;
import com.censoredsoftware.demigods.player.DCharacter;
import com.censoredsoftware.demigods.player.DPlayer;
import com.censoredsoftware.demigods.player.Pet;
import com.censoredsoftware.demigods.structure.Structure;
import com.censoredsoftware.demigods.util.Randoms;
import com.censoredsoftware.demigods.util.Spigots;
import com.censoredsoftware.demigods.util.Structures;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Battle implements ConfigurationSerializable
{
	private UUID id;
	private UUID startLoc;
	private boolean active;
	private double range;
	private long duration;
	private int minKills;
	private int maxKills;
	private long startTime;
	private long deleteTime;
	private Set<String> involvedPlayers;
	private Set<String> involvedTameable;
	private int killCounter;
	private Map<String, Object> kills;
	private Map<String, Object> deaths;
	private UUID startedBy;

	public Battle()
	{}

	public Battle(UUID id, ConfigurationSection conf)
	{
		this.id = id;
		startLoc = UUID.fromString(conf.getString("startLoc"));
		active = conf.getBoolean("active");
		range = conf.getDouble("range");
		duration = conf.getLong("duration");
		minKills = conf.getInt("minKills");
		maxKills = conf.getInt("maxKills");
		startTime = conf.getLong("startTime");
		deleteTime = conf.getLong("deleteTime");
		involvedPlayers = Sets.newHashSet(conf.getStringList("involvedPlayers"));
		involvedTameable = Sets.newHashSet(conf.getStringList("involvedTameable"));
		killCounter = conf.getInt("killCounter");
		kills = conf.getConfigurationSection("kills").getValues(false);
		deaths = conf.getConfigurationSection("deaths").getValues(false);
		startedBy = UUID.fromString(conf.getString("startedBy"));
	}

	@Override
	public Map<String, Object> serialize()
	{
		return new HashMap<String, Object>()
		{
			{
				put("startLoc", startLoc.toString());
				put("active", active);
				put("range", range);
				put("duration", duration);
				put("minKills", minKills);
				put("maxKills", maxKills);
				put("startTime", startTime);
				put("deleteTime", deleteTime);
				put("involvedPlayers", Lists.newArrayList(involvedPlayers));
				put("involvedTameable", Lists.newArrayList(involvedTameable));
				put("killCounter", killCounter);
				put("kills", kills);
				put("deaths", deaths);
				put("startedBy", startedBy.toString());
			}
		};
	}

	public void generateId()
	{
		id = UUID.randomUUID();
	}

	public void setRange(double range)
	{
		this.range = range;
		Util.save(this);
	}

	public void setActive()
	{
		this.active = true;
		Util.save(this);
	}

	public void setInactive()
	{
		this.active = false;
		Util.save(this);
	}

	public void setDuration(long duration)
	{
		this.duration = duration;
		Util.save(this);
	}

	public void setMinKills(int kills)
	{
		this.minKills = kills;
		Util.save(this);
	}

	public void setMaxKills(int kills)
	{
		this.maxKills = kills;
		Util.save(this);
	}

	void setStartLocation(Location location)
	{
		this.startLoc = DLocation.Util.create(location).getId();
	}

	void setStartTime(long time)
	{
		this.startTime = time;
	}

	void setDeleteTime(long time)
	{
		this.deleteTime = time;
		Util.save(this);
	}

	public UUID getId()
	{
		return this.id;
	}

	public double getRange()
	{
		return this.range;
	}

	public boolean isActive()
	{
		return this.active;
	}

	public long getDuration()
	{
		return this.duration;
	}

	public int getMinKills()
	{
		return this.minKills;
	}

	public int getMaxKills()
	{
		return this.maxKills;
	}

	public Location getStartLocation()
	{
		return DLocation.Util.load(this.startLoc).toLocation();
	}

	public long getStartTime()
	{
		return this.startTime;
	}

	public long getDeleteTime()
	{
		return this.deleteTime;
	}

	void setStarter(DCharacter character)
	{
		this.startedBy = character.getId();
		addParticipant(character);
	}

	void initialize()
	{
		this.kills = Maps.newHashMap();
		this.deaths = Maps.newHashMap();
		this.involvedPlayers = Sets.newHashSet();
		this.involvedTameable = Sets.newHashSet();
		this.killCounter = 0;
	}

	public void addParticipant(Participant participant)
	{
		if(participant instanceof DCharacter) this.involvedPlayers.add((participant.getId().toString()));
		else this.involvedTameable.add(participant.getId().toString());
		Util.save(this);
	}

	public void addKill(Participant participant)
	{
		this.killCounter += 1;
		DCharacter character = participant.getRelatedCharacter();
		if(this.kills.containsKey(character.getId().toString())) this.kills.put(character.getId().toString(), Integer.parseInt(this.kills.get(character.getId().toString()).toString()) + 1);
		else this.kills.put(character.getId().toString(), 1);
		Util.save(this);
	}

	public void addDeath(Participant participant)
	{
		DCharacter character = participant.getRelatedCharacter();
		if(this.deaths.containsKey(character)) this.deaths.put(character.getId().toString(), Integer.parseInt(this.deaths.get(character.getId().toString()).toString()) + 1);
		else this.deaths.put(character.getId().toString(), 1);
		Util.save(this);
	}

	public DCharacter getStarter()
	{
		return DCharacter.Util.load(this.startedBy);
	}

	public Set<Participant> getParticipants()
	{
		return new HashSet<Participant>()
		{
			{
				for(String character : involvedPlayers)
					add(DCharacter.Util.load(UUID.fromString(character)));
				for(String tamable : involvedTameable)
					add(Pet.Util.load(UUID.fromString(tamable)));
			}
		};
	}

	public int getKillCounter()
	{
		return this.killCounter;
	}

	public void end()
	{
		// Prepare for graceful delete
		setDeleteTime(System.currentTimeMillis() + 3000L);
		setInactive();
	}

	public void delete()
	{
		DataManager.battles.remove(getId());
	}

	public static class File extends ConfigFile
	{
		private static String SAVE_PATH;
		private static final String SAVE_FILE = "battles.yml";

		public File()
		{
			super(Demigods.plugin);
			SAVE_PATH = Demigods.plugin.getDataFolder() + "/data/";
		}

		@Override
		public ConcurrentHashMap<UUID, Battle> loadFromFile()
		{
			final FileConfiguration data = getData(SAVE_PATH, SAVE_FILE);
			return new ConcurrentHashMap<UUID, Battle>()
			{
				{
					for(String stringId : data.getKeys(false))
						put(UUID.fromString(stringId), new Battle(UUID.fromString(stringId), data.getConfigurationSection(stringId)));
				}
			};
		}

		@Override
		public boolean saveToFile()
		{
			FileConfiguration saveFile = getData(SAVE_PATH, SAVE_FILE);
			Map<UUID, Battle> currentFile = loadFromFile();

			for(UUID id : DataManager.battles.keySet())
				if(!currentFile.keySet().contains(id) || !currentFile.get(id).equals(DataManager.battles.get(id))) saveFile.createSection(id.toString(), Util.get(id).serialize());

			for(UUID id : currentFile.keySet())
				if(!DataManager.battles.keySet().contains(id)) saveFile.set(id.toString(), null);

			return saveFile(SAVE_PATH, SAVE_FILE, saveFile);
		}
	}

	public static class Util
	{
		public static void save(Battle battle)
		{
			DataManager.battles.put(battle.getId(), battle);
		}

		public static Battle create(Participant damager, Participant damaged)
		{
			Battle battle = new Battle();
			battle.generateId();
			battle.setStartLocation(damager.getCurrentLocation().toVector().getMidpoint(damaged.getCurrentLocation().toVector()).toLocation(damager.getCurrentLocation().getWorld()));
			battle.setStartTime(System.currentTimeMillis());

			int default_range = Demigods.config.getSettingInt("battles.min_range");
			double range = damager.getCurrentLocation().distance(damaged.getCurrentLocation());
			if(range < default_range) battle.setRange(default_range);
			else battle.setRange(range);

			battle.setActive();

			battle.setDuration(Demigods.config.getSettingInt("battles.min_duration") * 1000);
			battle.setMinKills(Demigods.config.getSettingInt("battles.min_kills"));
			battle.setMaxKills(Demigods.config.getSettingInt("battles.max_kills"));

			battle.initialize();

			battle.setStarter(damager.getRelatedCharacter());
			battle.addParticipant(damager);
			battle.addParticipant(damaged);
			save(battle);
			return battle;
		}

		public static Battle get(UUID id)
		{
			return DataManager.battles.get(id);
		}

		public static Set<Battle> getAll()
		{
			return Sets.newHashSet(DataManager.battles.values());
		}

		public static List<Battle> getAllActive()
		{
			return Lists.newArrayList(Collections2.filter(getAll(), new Predicate<Battle>()
			{
				@Override
				public boolean apply(@Nullable Battle battle)
				{
					return battle.isActive();
				}
			}));
		}

		public static List<Battle> getAllInactive()
		{
			return Lists.newArrayList(Collections2.filter(getAll(), new Predicate<Battle>()
			{
				@Override
				public boolean apply(@Nullable Battle battle)
				{
					return !battle.isActive();
				}
			}));
		}

		public static boolean existsInRadius(Location location)
		{
			return getInRadius(location) != null;
		}

		public static Battle getInRadius(Location location)
		{
			for(Battle battle : getAllActive())
			{
				if(battle.getStartLocation().distance(location) <= battle.getRange()) return battle;
			}
			return null;
		}

		public static boolean isInBattle(Participant participant)
		{
			for(Battle battle : getAllActive())
			{
				if(battle.getParticipants().contains(participant)) return true;
			}
			return false;
		}

		public static Battle getBattle(Participant participant)
		{
			for(Battle battle : getAllActive())
				if(battle.getParticipants().contains(participant)) return battle;
			return null;
		}

		public static boolean existsNear(Location location)
		{
			return getNear(location) != null;
		}

		public static Battle getNear(Location location)
		{
			for(Battle battle : getAllActive())
			{
				double distance = battle.getStartLocation().distance(location);
				if(distance > battle.getRange() && distance <= Demigods.config.getSettingInt("battles.merge_range")) return battle;
			}
			return null;
		}

		public static Set<Location> battleBorder(final Battle battle)
		{
			if(!Demigods.isRunningSpigot()) throw new SpigotNotFoundException();
			return new HashSet<Location>()
			{
				{
					for(Location point : DLocation.Util.getCirclePoints(battle.getStartLocation(), battle.getRange(), 120))
						add(new Location(point.getWorld(), point.getBlockX(), point.getWorld().getHighestBlockYAt(point), point.getBlockZ()));
				}
			};
		}

		public static Location randomRespawnPoint(Battle battle)
		{
			List<Location> respawnPoints = getSafeRespawnPoints(battle);
			if(respawnPoints.size() == 0) return battle.getStartLocation();

			Location target = respawnPoints.get(Randoms.generateIntRange(0, respawnPoints.size() - 1));

			Vector direction = target.toVector().subtract(battle.getStartLocation().toVector()).normalize();
			double X = direction.getX();
			double Y = direction.getY();
			double Z = direction.getZ();

			// Now change the angle
			Location changed = target.clone();
			changed.setYaw(180 - DLocation.Util.toDegree(Math.atan2(Y, X)));
			changed.setPitch(90 - DLocation.Util.toDegree(Math.acos(Z)));
			return changed;
		}

		public static boolean isSafeLocation(Location reference, Location checking)
		{
			if(checking.getBlock().getType().isSolid() || checking.getBlock().getType().equals(Material.LAVA)) return false;
			double referenceY = reference.getY();
			double checkingY = checking.getY();
			return Math.abs(referenceY - checkingY) <= 5;
		}

		public static List<Location> getSafeRespawnPoints(final Battle battle)
		{
			return new ArrayList<Location>()
			{
				{
					for(Location location : DLocation.Util.getCirclePoints(battle.getStartLocation(), battle.getRange() - 1.5, 20))
						if(isSafeLocation(battle.getStartLocation(), location)) add(location);
				}
			};
		}

		public static boolean canParticipate(Entity entity)
		{
			if(!(entity instanceof Player) && !(entity instanceof Tameable)) return false;
			if(entity instanceof Player && DPlayer.Util.getPlayer((Player) entity).getCurrent() == null) return false;
			return !(entity instanceof Tameable && Pet.Util.getTameable((LivingEntity) entity) == null);
		}

		public static Participant defineParticipant(Entity entity)
		{
			if(!canParticipate(entity)) return null;
			if(entity instanceof Player) return DPlayer.Util.getPlayer((Player) entity).getCurrent();
			return Pet.Util.getTameable((LivingEntity) entity);
		}

		public static void battleDeath(Participant damager, Participant damagee, Battle battle)
		{
			if(damager instanceof DCharacter) ((DCharacter) damager).addKill();
			if(damager.getRelatedCharacter().getOfflinePlayer().isOnline()) damager.getRelatedCharacter().getOfflinePlayer().getPlayer().sendMessage(ChatColor.GREEN + "+1 Kill.");
			battle.addKill(damager);
			battleDeath(damagee, battle);
		}

		public static void battleDeath(Participant damagee, Battle battle)
		{
			damagee.getEntity().setHealth(damagee.getEntity().getMaxHealth());
			damagee.getEntity().teleport(randomRespawnPoint(battle));
			if(damagee instanceof DCharacter) ((DCharacter) damagee).addDeath();
			if(damagee.getRelatedCharacter().getOfflinePlayer().isOnline()) damagee.getRelatedCharacter().getOfflinePlayer().getPlayer().sendMessage(ChatColor.RED + "+1 Death.");
			battle.addDeath(damagee);
		}

		public static boolean canTarget(Entity entity)
		{
			return canParticipate(entity) && canTarget(defineParticipant(entity));
		}

		/**
		 * Returns true if doTargeting is allowed for <code>player</code>.
		 * 
		 * @param participant the player to check.
		 * @return true/false depending on if doTargeting is allowed.
		 */
		public static boolean canTarget(Participant participant)
		{
			return !(participant instanceof DCharacter || participant instanceof Pet) || participant.canPvp() || !Structures.isInRadiusWithFlag(participant.getCurrentLocation(), Structure.Flag.NO_PVP, true); // TODO Make this work with new PVP.
		}

		/**
		 * Updates all battle particles. Meant for use in a Runnable.
		 */
		public static void updateBattleParticles()
		{
			for(Battle battle : Battle.Util.getAllActive())
				for(Location point : Battle.Util.battleBorder(battle))
					Spigots.playParticle(point, Effect.MOBSPAWNER_FLAMES, 0, 6, 0, 1F, 10, (int) (battle.getRange() * 2.5));
		}

		/**
		 * Updates all battles.
		 */
		public static void updateBattles()
		{
			// Battle onTick logic
			for(Battle battle : Battle.Util.getAllActive())
				if(battle.getKillCounter() > battle.getMaxKills() || battle.getStartTime() + battle.getDuration() <= System.currentTimeMillis() && battle.getKillCounter() > battle.getMinKills()) battle.end();

			// Delete all inactive battles
			for(Battle remove : Battle.Util.getAllInactive())
				if(remove.getDeleteTime() >= System.currentTimeMillis()) remove.delete();
		}
	}
}
