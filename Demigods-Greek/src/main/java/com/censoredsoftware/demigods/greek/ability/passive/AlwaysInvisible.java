package com.censoredsoftware.demigods.greek.ability.passive;

import com.censoredsoftware.demigods.engine.data.DCharacter;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.censoredsoftware.demigods.greek.ability.GreekAbility;
import com.google.common.collect.Lists;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class AlwaysInvisible extends GreekAbility.Passive
{
	private final static String name = "Invisible";
	private final static int repeat = 20;
	private final static List<String> details = Lists.newArrayList("They'll never see you coming.");

	public AlwaysInvisible(final String deity)
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
					if(player.isInsideVehicle() && player.getVehicle().getType().equals(EntityType.HORSE)) potionEffect((LivingEntity) player.getVehicle());
				}
			}

			private void potionEffect(LivingEntity entity)
			{
				entity.removePotionEffect(PotionEffectType.INVISIBILITY);
				entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 120, 1));
			}
		});
	}
}
