package com.censoredsoftware.demigods.greek.ability.passive;

import com.censoredsoftware.demigods.engine.mythos.Deity;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.censoredsoftware.demigods.greek.ability.GreekAbility;
import com.google.common.collect.Lists;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;

public class NoDamage extends GreekAbility.Passive
{
	private final static String name = "No Damage";
	private final static int repeat = 0;
	private final static List<String> details = Lists.newArrayList("Take no corruption, give no corruption.");

	public NoDamage(final String deity)
	{
		super(name, deity, repeat, details, new Listener()
		{
			@EventHandler(priority = EventPriority.HIGHEST)
			public void onEntityDamange(EntityDamageEvent damageEvent)
			{
				if(Zones.inNoDemigodsZone(damageEvent.getEntity().getLocation())) return;
				if(damageEvent.getEntity() instanceof Player)
				{
					Player player = (Player) damageEvent.getEntity();
					if(!Deity.Util.canUseDeitySilent(player, deity)) return;

					// If the player receives corrupt, cancel it
					damageEvent.setCancelled(true);
				}
			}

			@EventHandler(priority = EventPriority.HIGHEST)
			public void onEntityDamageByEntity(EntityDamageByEntityEvent damageEvent)
			{
				if(Zones.inNoDemigodsZone(damageEvent.getEntity().getLocation())) return;
				if(damageEvent.getDamager() instanceof Player)
				{
					Player player = (Player) damageEvent.getDamager();
					if(!Deity.Util.canUseDeitySilent(player, deity)) return;

					// If the player receives corrupt, cancel it
					damageEvent.setCancelled(true);
				}
			}
		}, null);
	}
}
