package com.demigodsrpg.demigods.greek.ability.passive;

import com.demigodsrpg.demigods.engine.Demigods;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.demigodsrpg.demigods.engine.util.Zones;
import com.demigodsrpg.demigods.greek.ability.GreekAbility;
import com.google.common.collect.Lists;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class NoDrown extends GreekAbility.Passive
{
	private static final String NAME = "No Drown Damage";
	private static final int REPEAT = 1;
	private static final List<String> DETAILS = Lists.newArrayList("Receive no corruption underwater.");

	public NoDrown(final String deity)
	{
		super(NAME, deity, REPEAT, DETAILS, null, new Runnable()
		{
			@Override
			public void run()
			{
				for(DemigodsCharacter character : Demigods.getServer().getOnlineCharactersWithAbility(NAME))
				{
					if(Zones.inNoDemigodsZone(character.getBukkitOfflinePlayer().getPlayer().getLocation())) continue;
					Player player = character.getBukkitOfflinePlayer().getPlayer();
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
