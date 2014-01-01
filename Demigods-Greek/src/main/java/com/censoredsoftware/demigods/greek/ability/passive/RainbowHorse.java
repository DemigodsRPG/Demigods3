package com.censoredsoftware.demigods.greek.ability.passive;

import java.util.List;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;

import com.censoredsoftware.censoredlib.util.Randoms;
import com.censoredsoftware.demigods.engine.data.DPet;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.censoredsoftware.demigods.greek.ability.GreekAbility;
import com.google.common.collect.Lists;

public class RainbowHorse extends GreekAbility.Passive
{
	private static final String NAME = "Horse of a Different Color";
	private static final int REPEAT = 100;
	private static final List<String> DETAILS = Lists.newArrayList("All of you horse are belong to us.");

	public RainbowHorse(final String deity)
	{
		super(NAME, deity, REPEAT, DETAILS, null, new Runnable()
		{
			@Override
			public void run()
			{
				for(DPet horse : DPet.Util.findByType(EntityType.HORSE))
				{
					if(horse.getCurrentLocation() == null || Zones.inNoDemigodsZone(horse.getCurrentLocation())) return;
					if(horse.getDeity().getName().equals("DrD1sco") && horse.getEntity() != null && !horse.getEntity().isDead()) ((Horse) horse.getEntity()).setColor(getRandomHorseColor());
				}
			}

			private Horse.Color getRandomHorseColor()
			{
				return Lists.newArrayList(Horse.Color.values()).get(Randoms.generateIntRange(0, 5));
			}
		});
	}
}
