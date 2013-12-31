package com.censoredsoftware.demigods.greek.ability.offense;

import com.censoredsoftware.demigods.engine.data.Skill;
import com.censoredsoftware.demigods.engine.util.Abilities;
import com.censoredsoftware.demigods.greek.ability.GreekAbility;
import com.censoredsoftware.demigods.greek.ability.ultimate.Storm;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;

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

				Storm.strikeLightning(player, target, notify);

				return true;
			}
		}, null, null);
	}
}