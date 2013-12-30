package com.censoredsoftware.demigods.greek.ability.ultimate;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.ability.Ability;
import com.censoredsoftware.demigods.engine.battle.Battle;
import com.censoredsoftware.demigods.engine.deity.Deity;
import com.censoredsoftware.demigods.engine.player.DCharacter;
import com.censoredsoftware.demigods.engine.player.DPlayer;
import com.censoredsoftware.demigods.engine.player.Skill;
import com.censoredsoftware.demigods.engine.util.Abilities;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.google.common.collect.Lists;

public class Firestorm implements Ability
{
	private final static String name = "Firestorm", command = "firestorm";
	private final static int cost = 5500, delay = 15, repeat = 0;
	private final static List<String> details = Lists.newArrayList("Rain down fireballs from the sky.");
	private String deity, permission;
	private final static Skill.Type type = Skill.Type.ULTIMATE;

	public Firestorm(String deity, String permission)
	{
		this.deity = deity;
		this.permission = permission;
	}

	@Override
	public String getDeity()
	{
		return deity;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getCommand()
	{
		return command;
	}

	@Override
	public String getPermission()
	{
		return permission;
	}

	@Override
	public int getCost()
	{
		return cost;
	}

	@Override
	public int getDelay()
	{
		return delay;
	}

	@Override
	public int getRepeat()
	{
		return repeat;
	}

	@Override
	public List<String> getDetails()
	{
		return details;
	}

	@Override
	public Skill.Type getType()
	{
		return type;
	}

	@Override
	public Material getWeapon()
	{
		return null;
	}

	@Override
	public boolean hasWeapon()
	{
		return getWeapon() != null;
	}

	@Override
	public Listener getListener()
	{
		return new Listener()
		{
			@EventHandler(priority = EventPriority.HIGH)
			public void onPlayerInteract(PlayerInteractEvent interactEvent)
			{
				if(Zones.inNoDemigodsZone(interactEvent.getPlayer().getLocation())) return;

				if(!Abilities.isLeftClick(interactEvent)) return;

				// Set variables
				Player player = interactEvent.getPlayer();
				DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

				if(!Deity.Util.canUseDeitySilent(character, deity)) return;

				if(player.getItemInHand() != null && character.getMeta().checkBound(name, player.getItemInHand().getType()))
				{
					if(!DCharacter.Util.isCooledDown(character, name, false)) return;

					Util.firestorm(player);
				}
			}
		};
	}

	@Override
	public BukkitRunnable getRunnable()
	{
		return null;
	}

	public static class ShootFireball implements Ability
	{
		private final static String name = "Fireball", command = "fireball";
		private final static int cost = 100, delay = 5, repeat = 0;
		private final static List<String> details = Lists.newArrayList("Shoot a fireball at the cursor's location.");
		private String deity, permission;
		private final static Skill.Type type = Skill.Type.OFFENSE;

		public ShootFireball(String deity, String permission)
		{
			this.deity = deity;
			this.permission = permission;
		}

		@Override
		public String getDeity()
		{
			return deity;
		}

		@Override
		public String getName()
		{
			return name;
		}

		@Override
		public String getCommand()
		{
			return command;
		}

		@Override
		public String getPermission()
		{
			return permission;
		}

		@Override
		public int getCost()
		{
			return cost;
		}

		@Override
		public int getDelay()
		{
			return delay;
		}

		@Override
		public int getRepeat()
		{
			return repeat;
		}

		@Override
		public List<String> getDetails()
		{
			return details;
		}

		@Override
		public Skill.Type getType()
		{
			return type;
		}

		@Override
		public Material getWeapon()
		{
			return null;
		}

		@Override
		public boolean hasWeapon()
		{
			return getWeapon() != null;
		}

		@Override
		public Listener getListener()
		{
			return new Listener()
			{
				@EventHandler(priority = EventPriority.HIGH)
				public void onPlayerInteract(PlayerInteractEvent interactEvent)
				{
					if(Zones.inNoDemigodsZone(interactEvent.getPlayer().getLocation())) return;

					if(!Abilities.isLeftClick(interactEvent)) return;

					// Set variables
					Player player = interactEvent.getPlayer();
					DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

					if(!Deity.Util.canUseDeitySilent(character, deity)) return;

					if(player.getItemInHand() != null && character.getMeta().checkBound(name, player.getItemInHand().getType()))
					{
						if(!DCharacter.Util.isCooledDown(character, name, false)) return;

						Util.fireball(player);
					}
				}
			};
		}

		@Override
		public BukkitRunnable getRunnable()
		{
			return null;
		}
	}

	public static class Util
	{
		public static void shootFireball(Location from, Location to, Player shooter)
		{
			Fireball fireball = (Fireball) shooter.getWorld().spawnEntity(from, EntityType.FIREBALL);
			to.setX(to.getX() + .5);
			to.setY(to.getY() + .5);
			to.setZ(to.getZ() + .5);
			Vector path = to.toVector().subtract(from.toVector());
			Vector victor = from.toVector().add(from.getDirection().multiply(2));
			fireball.teleport(new Location(shooter.getWorld(), victor.getX(), victor.getY(), victor.getZ()));
			fireball.setDirection(path);
			fireball.setShooter(shooter);
		}

		// The actual ability command
		public static void fireball(Player player)
		{
			// Define variables
			DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();
			Location target;
			LivingEntity entity = Abilities.autoTarget(player);
			boolean notify;
			if(entity != null)
			{
				target = Abilities.autoTarget(player).getLocation();
				notify = true;
				if(!Abilities.doAbilityPreProcess(player, entity, ShootFireball.cost) || entity.getEntityId() == player.getEntityId()) return;
			}
			else
			{
				target = Abilities.directTarget(player);
				notify = false;
				if(!Abilities.doAbilityPreProcess(player, ShootFireball.cost)) return;
			}

			DCharacter.Util.setCoolDown(character, ShootFireball.name, System.currentTimeMillis() + ShootFireball.delay);
			character.getMeta().subtractFavor(ShootFireball.cost);

			if(!Abilities.doTargeting(player, target, notify)) return;

			shootFireball(player.getEyeLocation(), target, player);
		}

		// The actual ability command
		public static void firestorm(final Player player)
		{
			// Define variables
			DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

			if(!Abilities.doAbilityPreProcess(player, cost)) return;

			int total = 3 * ((int) Math.pow(character.getMeta().getAscensions(), 0.35));
			Deque<LivingEntity> entities = new ArrayDeque<LivingEntity>();

			for(final Entity entity : player.getNearbyEntities(50, 50, 50)) // TODO: Make this dependent on levels.
			{
				// Validate them first
				if(!(entity instanceof LivingEntity)) continue;
				if(entity instanceof Player)
				{
					DCharacter otherCharacter = DPlayer.Util.getPlayer((Player) entity).getCurrent();
					if(otherCharacter != null && DCharacter.Util.areAllied(character, otherCharacter)) continue;
				}
				if(!Battle.Util.canParticipate(entity) || !Battle.Util.canTarget(Battle.Util.defineParticipant(entity))) continue;

				entities.add((LivingEntity) entity);
			}

			// Now shoot them
			for(int i = 0; i <= total; i++)
			{
				for(final LivingEntity entity : entities)
				{
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Demigods.PLUGIN, new Runnable()
					{
						@Override
						public void run()
						{
							Location entityLocation = entity.getLocation();
							Location air = new Location(entityLocation.getWorld(), entityLocation.getX(), entityLocation.getWorld().getHighestBlockAt(entityLocation).getLocation().getY() + 10.0, entityLocation.getZ());
							Location ground = new Location(entityLocation.getWorld(), entityLocation.getX(), entityLocation.getY(), entityLocation.getZ());
							Util.shootFireball(air, ground, player);
						}
					}, 40);
				}
			}
		}
	}
}
