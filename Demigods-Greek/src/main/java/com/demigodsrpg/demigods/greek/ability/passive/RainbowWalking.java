package com.demigodsrpg.demigods.greek.ability.passive;

import com.demigodsrpg.demigods.engine.Demigods;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.demigodsrpg.demigods.engine.util.Zones;
import com.demigodsrpg.demigods.greek.ability.GreekAbility;
import com.demigodsrpg.demigods.greek.ability.ultimate.Discoball;
import com.google.common.collect.Lists;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class RainbowWalking extends GreekAbility.Passive
{
	private static final String NAME = "Rainbow Walking";
	private static final int REPEAT = 5;
	private static final List<String> DETAILS = Lists.newArrayList("Spread the disco while sneaking.");

	public RainbowWalking(final String deity)
	{
		super(NAME, deity, REPEAT, DETAILS, null, new Runnable()
		{
			@Override
			public void run()
			{
				for(DemigodsCharacter online : Demigods.getServer().getOnlineCharactersWithDeity(deity))
				{
					Player player = online.getBukkitOfflinePlayer().getPlayer();
					if(Zones.inNoDemigodsZone(player.getLocation()) || !player.isSneaking() || player.isFlying() || Zones.inNoPvpZone(player.getLocation()) || Zones.inNoBuildZone(player, player.getLocation())) continue;
					doEffect(player);
				}
			}

			private void doEffect(Player player)
			{
				for(Entity entity : player.getNearbyEntities(30, 30, 30))
					if(entity instanceof Player) Discoball.rainbow(player, (Player) entity);
				Discoball.rainbow(player, player);
				Discoball.playRandomNote(player.getLocation(), 0.5F);
			}
		});
	}
}
