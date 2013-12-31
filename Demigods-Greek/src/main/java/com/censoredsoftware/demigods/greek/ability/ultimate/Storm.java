package com.censoredsoftware.demigods.greek.ability.ultimate;

import com.censoredsoftware.demigods.engine.data.Battle;
import com.censoredsoftware.demigods.engine.data.DCharacter;
import com.censoredsoftware.demigods.engine.data.DPlayer;
import com.censoredsoftware.demigods.engine.data.Skill;
import com.censoredsoftware.demigods.engine.util.Abilities;
import com.censoredsoftware.demigods.greek.ability.GreekAbility;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Set;

public class Storm extends GreekAbility
{
	private final static String name = "Storm", command = "storm";
	private final static int cost = 3700, delay = 600 * 20, repeat = 0;
	private final static List<String> details = Lists.newArrayList("Send your enemies flying as lightning fills the heavens.");
	private final static Skill.Type type = Skill.Type.ULTIMATE;

	public Storm(String deity)
	{
		super(name, command, deity, cost, delay, repeat, details, type, null, new Predicate<Player>()
		{
			@Override
			public boolean apply(Player player)
			{
				// Define variables
				DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();
				Set<Entity> entitySet = Sets.newHashSet();
				Vector playerLocation = player.getLocation().toVector();

				if(!Abilities.preProcessAbility(player, cost)) return false;

				for(Entity anEntity : player.getWorld().getEntities())
					if(anEntity.getLocation().toVector().isInSphere(playerLocation, 50.0)) entitySet.add(anEntity);

				for(Entity entity : entitySet)
				{
					if(entity instanceof Player)
					{
						Player otherPlayer = (Player) entity;
						DCharacter otherChar = DPlayer.Util.getPlayer(otherPlayer).getCurrent();
						if(otherPlayer.equals(player)) continue;
						if(otherChar != null && !DCharacter.Util.areAllied(character, otherChar) && !otherPlayer.equals(player))
						{
							strikeLightning(player, otherPlayer);
							strikeLightning(player, otherPlayer);
							continue;
						}
					}
					if(entity instanceof LivingEntity)
					{
						LivingEntity livingEntity = (LivingEntity) entity;
						strikeLightning(player, livingEntity);
						strikeLightning(player, livingEntity);
					}
				}

				return true;
			}
		}, null, null);
	}

	public static boolean strikeLightning(Player player, LivingEntity target)
	{
		return Battle.Util.canTarget(target) && strikeLightning(player, target.getLocation(), true);
	}

	public static boolean strikeLightning(Player player, Location target, boolean notify)
	{
		// Set variables
		DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

		if(!player.getWorld().equals(target.getWorld())) return false;
		Location toHit = Abilities.adjustedAimLocation(character, target);

		player.getWorld().strikeLightningEffect(toHit);

		for(Entity entity : toHit.getBlock().getChunk().getEntities())
		{
			if(entity instanceof LivingEntity)
			{
				if(!Battle.Util.canTarget(entity)) continue;
				LivingEntity livingEntity = (LivingEntity) entity;
				if(livingEntity.equals(player)) continue;
				if((toHit.getBlock().getType().equals(Material.WATER) || toHit.getBlock().getType().equals(Material.STATIONARY_WATER)) && livingEntity.getLocation().distance(toHit) < 8) Abilities.dealDamage(player, livingEntity, character.getMeta().getAscensions() * 6, EntityDamageEvent.DamageCause.LIGHTNING);
				else if(livingEntity.getLocation().distance(toHit) < 2) Abilities.dealDamage(player, livingEntity, character.getMeta().getAscensions() * 4, EntityDamageEvent.DamageCause.LIGHTNING);
			}
		}

		if(!Abilities.isHit(target, toHit) && notify) player.sendMessage(ChatColor.RED + "Missed...");

		return true;
	}
}