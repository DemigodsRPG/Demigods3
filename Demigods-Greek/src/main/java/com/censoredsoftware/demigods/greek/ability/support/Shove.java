package com.censoredsoftware.demigods.greek.ability.support;

import com.censoredsoftware.demigods.engine.data.DCharacter;
import com.censoredsoftware.demigods.engine.data.DPlayer;
import com.censoredsoftware.demigods.engine.data.Skill;
import com.censoredsoftware.demigods.engine.util.Abilities;
import com.censoredsoftware.demigods.greek.ability.GreekAbility;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.util.List;

public class Shove extends GreekAbility
{
	private final static String name = "Shove", command = "shove";
	private final static int cost = 170, delay = 15, repeat = 0;
	private final static List<String> details = Lists.newArrayList("Shove your target away from you.");
	private final static Skill.Type type = Skill.Type.SUPPORT;

	public Shove(String deity)
	{
		super(name, command, deity, cost, delay, repeat, details, type, null, new Predicate<Player>()
		{
			@Override
			public boolean apply(Player player)
			{
				LivingEntity target = Abilities.autoTarget(player);

				if(!Abilities.preProcessAbility(player, target, cost) || !Abilities.target(player, target.getLocation(), true)) return false;

				// Define variables
				DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();
				int ascensions = character.getMeta().getAscensions();
				double multiply = 0.1753 * Math.pow(ascensions, 0.322917);

				Vector vector = player.getLocation().toVector();
				Vector victor = target.getLocation().toVector().subtract(vector);
				victor.multiply(multiply);
				target.setVelocity(victor);
				Abilities.dealDamage(player, target, 0, EntityDamageEvent.DamageCause.FALL);

				return true;
			}
		}, null, null);
	}
}