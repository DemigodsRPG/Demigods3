package com.censoredsoftware.demigods.greek.ability.offense;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.censoredsoftware.demigods.engine.data.Skill;
import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.censoredsoftware.demigods.greek.ability.GreekAbility;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

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
				LivingEntity entity = Ability.Util.autoTarget(player);
				boolean notify;

				if(entity != null)
				{
					target = Ability.Util.autoTarget(player).getLocation();
					notify = true;
					if(!Ability.Util.preProcessAbility(player, entity, Fireball.cost) || entity.getEntityId() == player.getEntityId()) return false;
				}
				else
				{
					target = Ability.Util.directTarget(player);
					notify = false;
					if(!Ability.Util.preProcessAbility(player, Fireball.cost)) return false;
				}

				if(!Ability.Util.target(player, target, notify)) return false;

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
