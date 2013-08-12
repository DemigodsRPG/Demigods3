package com.censoredsoftware.demigods.ability.passive;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.scheduler.BukkitRunnable;

import com.censoredsoftware.core.util.Randoms;
import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.player.Pet;
import com.google.common.collect.Lists;

public class RainbowHorse extends Ability
{
	public static RainbowHorse ability;
	private final static String name = "Horse of a Different Color", command = null;
	private final static int cost = 0, delay = 0, repeat = 100;

	private final static Devotion.Type type = Devotion.Type.PASSIVE;
	private final static List<String> details = new ArrayList<String>(1)
	{
		{
			add("All of you horse are belong to us.");
		}
	};

	public RainbowHorse(String deity, String permission)
	{
		super(null, new BukkitRunnable()
		{
			@Override
			public void run()
			{
				for(Pet horse : Pet.Util.findByType(EntityType.HORSE))
				{
					if(Demigods.isDisabledWorld(horse.getCurrentLocation().getWorld())) return;
					if(horse.getDeity().getName().equals("DrD1sco") && horse.getEntity() != null && !horse.getEntity().isDead())
					{
						((Horse) horse.getEntity()).setColor(getRandomHorseColor());
					}
				}
			}

			private Horse.Color getRandomHorseColor()
			{
				return Lists.newArrayList(Horse.Color.values()).get(Randoms.generateIntRange(0, 5));
			}
		}, deity, name, command, permission, cost, delay, repeat, details, type);
		ability = this;
	}
}