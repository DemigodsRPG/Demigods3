package com.legit2.Demigods.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import com.legit2.Demigods.DSouls;
import com.legit2.Demigods.Demigods;

public class DEntityListener implements Listener
{
	static Demigods plugin;
	
	public DEntityListener(Demigods instance)
	{
		plugin = instance;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public static void onEntityDeath(EntityDeathEvent event)
	{
		if(event.getEntityType().equals(EntityType.VILLAGER))
		{
			// Define villager
			LivingEntity villager = event.getEntity();
			EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent)villager.getLastDamageCause();
			
			// Define attacker and name
			Player attacker = (Player)damageEvent.getDamager();
			//String attackerName = attacker.getName();
		
			villager.getLocation().getWorld().dropItemNaturally(villager.getLocation(), DSouls.getSoul(villager));
			attacker.sendMessage(ChatColor.GRAY + "One weaker than you has been slain by your hand.");
		}
	}

}
