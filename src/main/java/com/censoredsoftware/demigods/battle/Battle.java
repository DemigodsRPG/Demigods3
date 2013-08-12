package com.censoredsoftware.demigods.battle;

import java.util.*;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.util.Vector;

import redis.clients.johm.*;

import com.censoredsoftware.core.exceptions.SpigotNotFoundException;
import com.censoredsoftware.core.util.Randoms;
import com.censoredsoftware.core.util.Spigots;
import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.location.DLocation;
import com.censoredsoftware.demigods.player.DCharacter;
import com.censoredsoftware.demigods.player.DPlayer;
import com.censoredsoftware.demigods.player.Pet;
import com.censoredsoftware.demigods.structure.Structure;
import com.censoredsoftware.demigods.util.Structures;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@Model
public class Battle
{
	@Id
	private Long Id;
	@Reference
	private Meta meta;
	@Reference
	private DLocation startLoc;
	@Attribute
	@Indexed
	private boolean active;
	@Attribute
	private double range;
	@Attribute
	private long duration;
	@Attribute
	private int minKills;
	@Attribute
	private int maxKills;
	@Attribute
	private long startTime;
	@Attribute
	private long deleteTime;

	void setMeta(Meta meta)
	{
		this.meta = meta;
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
		this.startLoc = DLocation.Util.create(location);
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

	public long getId()
	{
		return this.Id;
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

	public Meta getMeta()
	{
		return this.meta;
	}

	public Location getStartLocation()
	{
		return this.startLoc.toLocation();
	}

	public long getStartTime()
	{
		return this.startTime;
	}

	public long getDeleteTime()
	{
		return this.deleteTime;
	}

	public void end()
	{
		// Prepare for graceful delete
		setDeleteTime(System.currentTimeMillis() + 3000L);
		setInactive();
	}

	public void delete()
	{
		getMeta().delete();
		JOhm.delete(Battle.class, getId());
	}

	@Model
	public static class Meta
	{
		@Id
		private Long id;
		@CollectionSet(of = DCharacter.class)
		private Set<DCharacter> involvedPlayers;
		@CollectionSet(of = Pet.class)
		private Set<Pet> involvedTameable;
		@Attribute
		private int killCounter;
		@CollectionMap(key = DCharacter.class, value = Integer.class)
		private Map<DCharacter, Integer> kills;
		@CollectionMap(key = DCharacter.class, value = Integer.class)
		private Map<DCharacter, Integer> deaths;
		@Reference
		@Indexed
		private DCharacter startedBy;

		void setStarter(DCharacter character)
		{
			this.startedBy = character;
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

		public void addParticipant(Battle.Participant participant)
		{
			if(participant instanceof DCharacter) this.involvedPlayers.add((DCharacter) participant);
			else this.involvedTameable.add((Pet) participant);
			Util.save(this);
		}

		public void addKill(Battle.Participant participant)
		{
			this.killCounter += 1;
			DCharacter character = participant.getRelatedCharacter();
			if(this.kills.containsKey(character)) this.kills.put(character, this.kills.get(character) + 1);
			else this.kills.put(character, 1);
			Util.save(this);
		}

		public void addDeath(Battle.Participant participant)
		{
			DCharacter character = participant.getRelatedCharacter();
			if(this.deaths.containsKey(character)) this.deaths.put(character, this.deaths.get(character) + 1);
			else this.deaths.put(character, 1);
			Util.save(this);
		}

		public DCharacter getStarter()
		{
			return this.startedBy;
		}

		public Set<Battle.Participant> getParticipants()
		{
			return new HashSet<Battle.Participant>()
			{
				{
					for(DCharacter character : involvedPlayers)
						add(character);
					for(Pet tamable : involvedTameable)
						add(tamable);
				}
			};
		}

		public int getKillCounter()
		{
			return this.killCounter;
		}

		public long getId()
		{
			return this.id;
		}

		public void delete()
		{
			JOhm.delete(Meta.class, getId());
		}
	}

	public static class Util
	{

		public static Battle create(Participant damager, Participant damaged)
		{
			Battle battle = new Battle();
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

			Meta meta = createMeta(damager);
			meta.addParticipant(damager);
			meta.addParticipant(damaged);
			battle.setMeta(meta);
			save(battle);
			return battle;
		}

		public static Meta createMeta(Battle.Participant participant)
		{
			DCharacter character = participant.getRelatedCharacter();

			Meta meta = new Meta();
			meta.initialize();
			meta.setStarter(character);
			save(meta);
			return meta;
		}

		public static Meta loadMeta(Long id)
		{
			return JOhm.get(Meta.class, id);
		}

		public static Set<Meta> loadAllMeta()
		{
			return JOhm.getAll(Meta.class);
		}

		public static void save(Meta meta)
		{
			JOhm.save(meta);
		}

		public static Battle get(Long id)
		{
			return JOhm.get(Battle.class, id);
		}

		public static Set<Battle> getAll()
		{
			return JOhm.getAll(Battle.class);
		}

		public static List<Battle> getAllActive()
		{
			return JOhm.find(Battle.class, "active", true);
		}

		public static List<Battle> getAllInactive()
		{
			return JOhm.find(Battle.class, "active", false);
		}

		public static void save(Battle battle)
		{
			JOhm.save(battle);
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
				if(battle.getMeta().getParticipants().contains(participant)) return true;
			}
			return false;
		}

		public static Battle getBattle(Participant participant)
		{
			for(Battle battle : getAllActive())
			{
				if(battle.getMeta().getParticipants().contains(participant)) return battle;
			}
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
			// TODO THIS IS BROKEN
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
			// TODO THIS IS BROKEN
		}

		public static boolean isSafeLocation(Location reference, Location checking)
		{
			if(reference.getBlock().getType().isSolid() || reference.getBlock().getType().equals(Material.LAVA)) return false;
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
					{
						if(isSafeLocation(battle.getStartLocation(), location)) add(location);
					}
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
			battle.getMeta().addKill(damager);
			battleDeath(damagee, battle);
		}

		public static void battleDeath(Participant damagee, Battle battle)
		{
			damagee.getEntity().setHealth(damagee.getEntity().getMaxHealth());
			damagee.getEntity().teleport(randomRespawnPoint(battle));
			if(damagee instanceof DCharacter) ((DCharacter) damagee).addDeath();
			if(damagee.getRelatedCharacter().getOfflinePlayer().isOnline()) damagee.getRelatedCharacter().getOfflinePlayer().getPlayer().sendMessage(ChatColor.RED + "+1 Death.");
			battle.getMeta().addDeath(damagee);
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
		public static boolean canTarget(Battle.Participant participant)
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
				if(battle.getMeta().getKillCounter() > battle.getMaxKills() || battle.getStartTime() + battle.getDuration() <= System.currentTimeMillis() && battle.getMeta().getKillCounter() > battle.getMinKills()) battle.end();

			// Delete all inactive battles
			for(Battle remove : Battle.Util.getAllInactive())
				if(remove.getDeleteTime() >= System.currentTimeMillis()) remove.delete();
		}
	}

	public interface Participant
	{
		public Long getId();

		public void setCanPvp(boolean pvp);

		public Boolean canPvp();

		public Location getCurrentLocation();

		public DCharacter getRelatedCharacter();

		public LivingEntity getEntity();
	}
}
