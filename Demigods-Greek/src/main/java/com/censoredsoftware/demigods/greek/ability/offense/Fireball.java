package com.censoredsoftware.demigods.greek.ability.offense;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.censoredsoftware.demigods.engine.data.DCharacter;
import com.censoredsoftware.demigods.engine.data.DPlayer;
import com.censoredsoftware.demigods.engine.data.Skill;
import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.censoredsoftware.demigods.engine.mythos.Deity;
import com.censoredsoftware.demigods.engine.util.Abilities;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.google.common.collect.Lists;

public class Fireball implements Ability
{
	private final static String name = "Fireball", command = "fireball";
	private final static int cost = 100, delay = 5, repeat = 0;
	private final static List<String> details = Lists.newArrayList("Send a fireball flying at your enemy.");
	private String deity, permission;
	private final static Skill.Type type = Skill.Type.OFFENSE;

	public Fireball(String deity, String permission)
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
		final Fireball instance = this;

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

					// Shoot the fireball
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

	public static class Util
	{
		public static void shootFireball(Location from, Location to, Player shooter)
		{
			org.bukkit.entity.Fireball fireball = (org.bukkit.entity.Fireball) shooter.getWorld().spawnEntity(from, EntityType.FIREBALL);
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
			Location target;
			LivingEntity entity = Abilities.autoTarget(player);
			boolean notify;

			if(entity != null)
			{
				target = Abilities.autoTarget(player).getLocation();
				notify = true;
				if(!Abilities.doAbilityPreProcess(player, entity, Fireball.cost) || entity.getEntityId() == player.getEntityId()) return;
			}
			else
			{
				target = Abilities.directTarget(player);
				notify = false;
				if(!Abilities.doAbilityPreProcess(player, Fireball.cost)) return;
			}

			if(!Abilities.doTargeting(player, target, notify)) return;

			shootFireball(player.getEyeLocation(), target, player);
		}
	}
}
