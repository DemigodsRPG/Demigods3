package com.censoredsoftware.demigods.episodes.demo.ability.passive;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.element.Ability;
import com.censoredsoftware.demigods.engine.element.Deity;

public class NoFall extends Ability
{
	private final static String name = "No Fall Damage", command = null;
	private final static int cost = 0, delay = 0, repeat = 0;
	private static Info info;
	private final static Devotion.Type type = Devotion.Type.PASSIVE;
	private final static List<String> details = new ArrayList<String>(1)
	{
		{
			add("Take no damage from falling.");
		}
	};

	public NoFall(final String deity, String permission)
	{
		super(info = new Info(deity, name, command, permission, cost, delay, repeat, details, type), new Listener()
		{
			@EventHandler(priority = EventPriority.MONITOR)
			public void onEntityDamange(EntityDamageEvent damageEvent)
			{
				if(Demigods.isDisabledWorld(damageEvent.getEntity().getWorld())) return;
				if(damageEvent.getEntity() instanceof Player)
				{
					Player player = (Player) damageEvent.getEntity();
					if(!Deity.Util.canUseDeitySilent(player, deity)) return;

					// If the player receives falling damage, cancel it
					if(damageEvent.getCause() == EntityDamageEvent.DamageCause.FALL) damageEvent.setCancelled(true);
				}
			}
		}, null);
	}
}
