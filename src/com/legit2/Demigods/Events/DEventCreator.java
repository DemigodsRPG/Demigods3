package com.legit2.Demigods.Events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import com.legit2.Demigods.Events.Player.PlayerBetrayPlayerEvent;
import com.legit2.Demigods.Events.Player.PlayerKillPlayerEvent;
import com.legit2.Demigods.Utilities.DMiscUtil;
import com.legit2.Demigods.Utilities.DPlayerUtil;

public class DEventCreator implements Listener
{
	@EventHandler(priority = EventPriority.MONITOR)
	public static void onEntityDeath(EntityDeathEvent event)
	{
		DMiscUtil.serverMsg("1");
		Entity entity = event.getEntity();
		if(entity instanceof Player)
		{
			DMiscUtil.serverMsg("2");
			Player player = (Player) entity;
			EntityDamageEvent damageEvent = player.getLastDamageCause();
			
			if(damageEvent instanceof EntityDamageByEntityEvent)
			{
				DMiscUtil.serverMsg("3");
				EntityDamageByEntityEvent damageByEvent = (EntityDamageByEntityEvent) damageEvent;
				Entity damager = damageByEvent.getDamager();
				
				if(damager instanceof Player)
				{
					DMiscUtil.serverMsg("4");
					Player attacker = (Player) damager;
					if(DMiscUtil.areAllied(attacker, player)) new PlayerBetrayPlayerEvent(attacker, player, DPlayerUtil.getCurrentAlliance(player));
					else new PlayerKillPlayerEvent(attacker, player);
				}
			}
		}
	}
}