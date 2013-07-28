package com.censoredsoftware.demigods.episodes.demo.ability.ultimate;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.battle.Battle;
import com.censoredsoftware.demigods.engine.element.Ability;
import com.censoredsoftware.demigods.engine.element.Deity;
import com.censoredsoftware.demigods.engine.player.DCharacter;
import com.censoredsoftware.demigods.engine.player.DPlayer;

public class Firestorm extends Ability
{
	private final static String name = "Firestorm", command = "firestorm";
	private final static int cost = 5500, delay = 15, repeat = 0;
	private static Info info;
	private final static List<String> details = new ArrayList<String>(1)
	{
		{
			add("Rain down fireballs from the sky.");
		}
	};
	private final static Devotion.Type type = Devotion.Type.ULTIMATE;

	public Firestorm(final String deity, String permission)
	{
		super(info = new Info(deity, name, command, permission, cost, delay, repeat, details, type), new Listener()
		{
			@EventHandler(priority = EventPriority.HIGH)
			public void onPlayerInteract(PlayerInteractEvent interactEvent)
			{
				if(!Ability.Util.isLeftClick(interactEvent)) return;

				// Set variables
				Player player = interactEvent.getPlayer();
				DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

				if(!Deity.Util.canUseDeitySilent(player, deity)) return;

				if(player.getItemInHand() != null && character.getMeta().checkBind(name, player.getItemInHand()))
				{
					if(!DCharacter.Util.isCooledDown(character, name, false)) return;

					Util.firestorm(player);
				}
			}
		}, null);
	}

	public static class ShootFireball extends Ability
	{
		private final static String name = "Fireball", command = "fireball";
		private final static int cost = 100, delay = 5, repeat = 0;
		private static Info info;
		private final static List<String> details = new ArrayList<String>(1)
		{
			{
				add("Shoot a fireball at the cursor's location.");
			}
		};
		private final static Devotion.Type type = Devotion.Type.OFFENSE;

		public ShootFireball(final String deity, String permission)
		{
			super(info = new Info(deity, name, command, permission, cost, delay, repeat, details, type), new Listener()
			{
				@EventHandler(priority = EventPriority.HIGH)
				public void onPlayerInteract(PlayerInteractEvent interactEvent)
				{
					if(!Util.isLeftClick(interactEvent)) return;

					// Set variables
					Player player = interactEvent.getPlayer();
					DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

					if(!Deity.Util.canUseDeitySilent(player, deity)) return;

					if(player.getItemInHand() != null && character.getMeta().checkBind(name, player.getItemInHand()))
					{
						if(!DCharacter.Util.isCooledDown(character, name, false)) return;

						Firestorm.Util.fireball(player);
					}
				}
			}, null);
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
			LivingEntity entity = Ability.Util.autoTarget(player);
			boolean notify;
			if(entity != null)
			{
				target = Ability.Util.autoTarget(player).getLocation();
				notify = true;
				if(!Ability.Util.doAbilityPreProcess(player, entity, "fireball", cost, info) || entity.getEntityId() == player.getEntityId()) return;
			}
			else
			{
				target = Ability.Util.directTarget(player);
				notify = false;
				if(!Ability.Util.doAbilityPreProcess(player, "fireball", cost, info)) return;
			}

			DCharacter.Util.setCoolDown(character, name, System.currentTimeMillis() + delay);
			character.getMeta().subtractFavor(cost);

			if(!Ability.Util.doTargeting(player, target, notify)) return;

			shootFireball(player.getEyeLocation(), target, player);
		}

		// The actual ability command
		public static void firestorm(final Player player)
		{
			// Define variables
			DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

			if(!Ability.Util.doAbilityPreProcess(player, name, cost, info)) return;

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
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Demigods.plugin, new Runnable()
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
