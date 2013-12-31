package com.censoredsoftware.demigods.greek.ability.offense;

import com.censoredsoftware.demigods.engine.data.DCharacter;
import com.censoredsoftware.demigods.engine.data.DPlayer;
import com.censoredsoftware.demigods.engine.data.Skill;
import com.censoredsoftware.demigods.engine.util.Abilities;
import com.censoredsoftware.demigods.greek.ability.GreekAbility;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.material.MaterialData;

import java.util.List;

public class Reel extends GreekAbility
{
	private final static String name = "Reel", command = "reel";
	private final static int cost = 120, delay = 1100, repeat = 0;
	private final static List<String> details = Lists.newArrayList("Use a fishing rod for a stronger attack.");
	private final static Skill.Type type = Skill.Type.OFFENSE;
	private final static MaterialData weapon = new MaterialData(Material.FISHING_ROD);

	public Reel(String deity)
	{
		super(name, command, deity, cost, delay, repeat, details, type, weapon, new Predicate<Player>()
		{

			@Override
			public boolean apply(Player player)
			{
				// Set variables
				DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();
				int damage = (int) Math.ceil(0.37286 * Math.pow(character.getMeta().getAscensions() * 100, 0.371238)); // TODO
				LivingEntity target = Abilities.autoTarget(player);

				if(!Abilities.preProcessAbility(player, target, cost)) return false;

				if(!Abilities.target(player, target.getLocation(), true)) return false;

				Abilities.dealDamage(player, target, damage, EntityDamageEvent.DamageCause.CUSTOM);

				if(target.getLocation().getBlock().getType() == Material.AIR)
				{
					target.getLocation().getBlock().setType(Material.WATER);
					target.getLocation().getBlock().setData((byte) 0x8);
				}

				return true;
			}
		}, null, null);
	}
}