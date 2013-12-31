package com.censoredsoftware.demigods.greek.ability.ultimate;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.censoredsoftware.demigods.engine.DemigodsPlugin;
import com.censoredsoftware.demigods.engine.data.Battle;
import com.censoredsoftware.demigods.engine.data.DCharacter;
import com.censoredsoftware.demigods.engine.data.DPlayer;
import com.censoredsoftware.demigods.engine.data.Skill;
import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.censoredsoftware.demigods.engine.mythos.Deity;
import com.censoredsoftware.demigods.engine.util.Abilities;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.google.common.collect.Lists;

public class Firestorm implements Ability
{
	private final static String name = "Firestorm", command = "firestorm";
	private final static int cost = 5500, delay = 15, repeat = 0;
	private final static List<String> details = Lists.newArrayList("Rain fireballs from the sky.");
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
		final Firestorm instance = this;

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

					// Process the cost and cooldown
					Abilities.processAbility(character, instance);

					// Use the ability
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

	public static class Util
	{
		// The actual ability command
		public static void firestorm(final Player player)
		{
			// Define variables
			DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

			if(!Abilities.doAbilityPreProcess(player, cost)) return;

            // Define variables
			int ultimateSkillLevel = character.getMeta().getSkill(Skill.Type.ULTIMATE).getLevel();
			int total = 3 * (int) Math.pow(ultimateSkillLevel, 0.25);
			int radius = (int) Math.log10(10 * ultimateSkillLevel) * 25;

			ArrayList<LivingEntity> entities = new ArrayList<>();

			for(final Entity entity : player.getNearbyEntities(radius, radius, radius))
			{
				// TODO: Abilities should work on mobs/animals too, just obviously not start a battle. Also simplify this (maybe with a util method?).

				// Validate them first
				if(!(entity instanceof LivingEntity)) continue;
				if(entity instanceof Player)
				{
					DCharacter opponent = DPlayer.Util.getPlayer((Player) entity).getCurrent();
					if(opponent != null && DCharacter.Util.areAllied(character, opponent)) continue;
				}
				if(!Battle.Util.canParticipate(entity) || !Battle.Util.canTarget(Battle.Util.defineParticipant(entity))) continue;

				entities.add((LivingEntity) entity);
			}

            final ArrayList<LivingEntity> targets = new ArrayList<>(entities);

			// Now shoot them
			for(int i = 0; i <= total; i++)
			{
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DemigodsPlugin.plugin(), new BukkitRunnable()
                {
					@Override
					public void run()
					{
						for(final LivingEntity target : targets)
						{
							// Skip if they died, reduces lag
							if(target.isDead()) continue;

							// Shoot 'em up!
							Location ground = target.getLocation();
							Location air = new Location(ground.getWorld(), ground.getX(), ground.getY() + 20, ground.getZ());

							org.bukkit.entity.Fireball fireball = (org.bukkit.entity.Fireball) target.getWorld().spawnEntity(air, EntityType.FIREBALL);
							fireball.setShooter(player);
							fireball.getDirection().zero();
							fireball.setIsIncendiary(true);
							fireball.setBounce(false);
							fireball.setYield(0F);
							Vector path = ground.toVector().subtract(air.toVector());
							fireball.setDirection(path.multiply(2));
							fireball.setVelocity(fireball.getDirection().multiply(3));
						}
					}
				}, i * 20);
			}
		}
	}
}
