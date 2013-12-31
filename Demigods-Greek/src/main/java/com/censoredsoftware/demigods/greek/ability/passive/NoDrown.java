package com.censoredsoftware.demigods.greek.ability.passive;

import com.censoredsoftware.demigods.engine.data.DCharacter;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.censoredsoftware.demigods.greek.ability.GreekAbility;
import com.google.common.collect.Lists;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class NoDrown extends GreekAbility.Passive
{
	private final static String name = "No Drown Damage";
	private final static int repeat = 1;
	private final static List<String> details = Lists.newArrayList("Receive no corruption underwater.");

	public NoDrown(final String deity)
	{
		super(name, deity, repeat, details, null, new Runnable()
		{
			@Override
			public void run()
			{
				for(DCharacter character : DCharacter.Util.getOnlineCharactersWithAbility(name))
				{
					if(Zones.inNoDemigodsZone(character.getOfflinePlayer().getPlayer().getLocation())) continue;
					Player player = character.getOfflinePlayer().getPlayer();
					potionEffect(player);
				}
			}

			private void potionEffect(LivingEntity entity)
			{
				entity.removePotionEffect(PotionEffectType.WATER_BREATHING);
				entity.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 120, 1));
			}
		});
	}
}