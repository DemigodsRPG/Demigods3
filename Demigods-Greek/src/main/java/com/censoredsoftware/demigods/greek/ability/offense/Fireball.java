package com.censoredsoftware.demigods.greek.ability.offense;

import com.censoredsoftware.demigods.engine.data.Skill;
import com.censoredsoftware.demigods.engine.util.Abilities;
import com.censoredsoftware.demigods.greek.ability.GreekAbility;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

public class Fireball extends GreekAbility
{
	private final static String name = "Fireball", command = "fireball";
	private final static int cost = 100, delay = 5, repeat = 0;
	private final static List<String> details = Lists.newArrayList("Send a fireball flying at your enemy.");
	private final static Skill.Type type = Skill.Type.OFFENSE;

	public Fireball(String deity)
	{
		super(name, command, deity, cost, delay, repeat, details, type, null, new Predicate<Player>()
		{
			@Override
			public boolean apply(Player player)
			{
				// Define variables
				Location target;
				LivingEntity entity = Abilities.autoTarget(player);
				boolean notify;

				if(entity != null)
				{
					target = Abilities.autoTarget(player).getLocation();
					notify = true;
					if(!Abilities.preProcessAbility(player, entity, Fireball.cost) || entity.getEntityId() == player.getEntityId()) return false;
				}
				else
				{
					target = Abilities.directTarget(player);
					notify = false;
					if(!Abilities.preProcessAbility(player, Fireball.cost)) return false;
				}

				if(!Abilities.target(player, target, notify)) return false;

				shootFireball(player.getEyeLocation(), target, player);

				return true;
			}
		}, null, null);
	}

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
}