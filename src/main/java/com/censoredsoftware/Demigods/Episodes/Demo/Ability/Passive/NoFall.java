package com.censoredsoftware.Demigods.Episodes.Demo.Ability.Passive;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.censoredsoftware.Demigods.Engine.Object.Ability;
import com.censoredsoftware.Demigods.Engine.Object.Deity;

public class NoFall extends Ability
{
	private final static String name = "No Fall Damage", command = null;
	private final static int cost = 0, delay = 0, repeat = 0;
	private final static List<String> details = new ArrayList<String>(1)
	{
		{
			add("Take no damage from falling.");
		}
	};
	private final static Devotion.Type type = Devotion.Type.PASSIVE;

	public NoFall(final String deity, String permission)
	{
		super(new Info(deity, name, command, permission, cost, delay, repeat, details, type), new Listener()
		{
			@EventHandler(priority = EventPriority.MONITOR)
			public void onEntityDamange(EntityDamageEvent damageEvent)
			{
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
