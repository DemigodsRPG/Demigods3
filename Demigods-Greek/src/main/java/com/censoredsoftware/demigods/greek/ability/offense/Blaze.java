package com.censoredsoftware.demigods.greek.ability.offense;

import com.censoredsoftware.demigods.engine.data.DCharacter;
import com.censoredsoftware.demigods.engine.data.DPlayer;
import com.censoredsoftware.demigods.engine.data.Skill;
import com.censoredsoftware.demigods.engine.util.Abilities;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.censoredsoftware.demigods.greek.ability.GreekAbility;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;

public class Blaze extends GreekAbility
{
	private final static String name = "Blaze", command = "blaze";
	private final static int cost = 400, delay = 15, repeat = 0;
	private final static List<String> details = Lists.newArrayList("Ignite the ground at the target location.");
	private final static Skill.Type type = Skill.Type.OFFENSE;

	public Blaze(String deity)
	{
		super(name, command, deity, cost, delay, repeat, details, type, null, new Predicate<Player>()
		{

			@Override
			public boolean apply(Player player)
			{
				// Define variables
				DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();
				Location target;
				LivingEntity entity = Abilities.autoTarget(player);
				boolean notify;
				if(entity != null)
				{
					target = Abilities.autoTarget(player).getLocation();
					notify = true;
					if(!Abilities.preProcessAbility(player, entity, cost) || entity.getEntityId() == player.getEntityId()) return false;
				}
				else
				{
					target = Abilities.directTarget(player);
					notify = false;
					if(!Abilities.preProcessAbility(player, cost)) return false;
				}
				int power = character.getMeta().getSkill(Skill.Type.OFFENSE).getLevel();
				int diameter = (int) Math.ceil(1.43 * Math.pow(power, 0.1527));
				if(diameter > 12) diameter = 12;

				if(!Abilities.target(player, target, notify)) return false;

				for(int X = -diameter / 2; X <= diameter / 2; X++)
				{
					for(int Y = -diameter / 2; Y <= diameter / 2; Y++)
					{
						for(int Z = -diameter / 2; Z <= diameter / 2; Z++)
						{
							Block block = target.getWorld().getBlockAt(target.getBlockX() + X, target.getBlockY() + Y, target.getBlockZ() + Z);
							if((block.getType() == Material.AIR) || (((block.getType() == Material.SNOW)) && !Zones.inNoBuildZone(player, block.getLocation()))) block.setType(Material.FIRE);
						}
					}
				}

				return true;
			}
		}, null, null);
	}
}