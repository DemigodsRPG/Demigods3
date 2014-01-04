package com.censoredsoftware.demigods.greek.ability.passive;

import com.censoredsoftware.demigods.engine.mythos.Deity;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.censoredsoftware.demigods.greek.ability.GreekAbility;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.List;

public class Swim extends GreekAbility.Passive
{
	private static final String NAME = "Swim";
	private static final int REPEAT = 20;
	private static final List<String> DETAILS = Lists.newArrayList("Crouch while in water to swim quickly.");

	public Swim(final String deity)
	{
		super(NAME, deity, REPEAT, DETAILS, new Listener()
		{
			@EventHandler(priority = EventPriority.HIGHEST)
			private void onPlayerMoveEvent(PlayerMoveEvent event)
			{
				if(Zones.inNoDemigodsZone(event.getPlayer().getLocation())) return;

				Player player = event.getPlayer();

				if(!Deity.Util.canUseDeitySilent(player, deity)) return;

				Material locationMaterial = player.getLocation().getBlock().getType();
				if((locationMaterial.equals(Material.STATIONARY_WATER) || locationMaterial.equals(Material.WATER)) && player.isSneaking())
				{
					Vector victor = (player.getPassenger() != null && player.getLocation().getDirection().getY() > 0 ? player.getLocation().getDirection().clone().setY(0) : player.getLocation().getDirection()).normalize().multiply(1.3D);
					player.setVelocity(new Vector(victor.getX(), victor.getY(), victor.getZ()));
				}
			}
		}, null);
	}
}
