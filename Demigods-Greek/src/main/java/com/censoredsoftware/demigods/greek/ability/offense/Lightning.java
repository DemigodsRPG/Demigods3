package com.censoredsoftware.demigods.greek.ability.offense;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import com.censoredsoftware.demigods.engine.data.Battle;
import com.censoredsoftware.demigods.engine.data.DCharacter;
import com.censoredsoftware.demigods.engine.data.DPlayer;
import com.censoredsoftware.demigods.engine.data.Skill;
import com.censoredsoftware.demigods.engine.util.Abilities;
import com.censoredsoftware.demigods.greek.ability.GreekAbility;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

public class Lightning extends GreekAbility
{
	private final static String name = "Lighting", command = "lightning";
	private final static int cost = 140, delay = 1000, repeat = 0;
	private final static List<String> details = Lists.newArrayList("Strike lightning upon your enemies.");
	private final static Skill.Type type = Skill.Type.OFFENSE;

	public Lightning(String deity)
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
					target = entity.getLocation();
					notify = true;
					if(!Abilities.preProcessAbility(player, entity, cost)) return false;
				}
				else
				{
					target = Abilities.directTarget(player);
					notify = false;
					if(!Abilities.preProcessAbility(player, cost)) return false;
				}

				strikeLightning(player, target, notify);

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
