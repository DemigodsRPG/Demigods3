package com.legit2.Demigods.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import com.legit2.Demigods.Demigods;
import com.legit2.Demigods.Utilities.DCharUtil;
import com.legit2.Demigods.Utilities.DMiscUtil;
import com.legit2.Demigods.Utilities.DObjUtil;
import com.legit2.Demigods.Utilities.DPlayerUtil;

public class DEntityListener implements Listener
{
	static Demigods plugin;
	
	public DEntityListener(Demigods instance)
	{
		plugin = instance;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public static void damageEvent(EntityDamageEvent event)
	{
		// Define variables
		LivingEntity entity;
		if(event.getEntityType().equals(EntityType.PLAYER)) // If it's a player
		{
			// Define entity as player and other variables
			entity = (LivingEntity) event.getEntity();
			
			// NO DAMAGE IN NO PVP ZONES FOR PLAYERS
			if(!DMiscUtil.canTarget(entity))
			{
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public static void damageByEntityEvent(EntityDamageByEntityEvent event)
	{
		Entity attacked = event.getEntity();
		Entity attacker = event.getDamager();
		
		if(attacker instanceof Player)
		{	
			Player attackingPlayer = (Player) attacker;
			
			// NO PVP
			if(!DMiscUtil.canTarget(attacked))
			{
				attackingPlayer.sendMessage(ChatColor.GRAY + "No-PVP in this zone.");
				event.setCancelled(true);
				return;
			}
			
			if(attacked instanceof Villager) // If it's a villager
			{
				// Define villager
				Villager villager = (Villager) event.getEntity();
				
				// Define attacker and name
				if(attacker instanceof Player && event.getDamage() > villager.getHealth()) attackingPlayer.sendMessage(ChatColor.GRAY + "One weaker than you has been slain by your hand.");
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public static void entityDeath(EntityDeathEvent event)
	{
		if(event.getEntityType().equals(EntityType.PLAYER))
		{
			// Define variables
			Player player = (Player) event.getEntity();
			int charID = DPlayerUtil.getCurrentChar(player);
			String deity = DObjUtil.capitalize(DCharUtil.getDeity(charID));
			int devotion = DCharUtil.getDevotion(charID);
			int devotionRemoved = (int) Math.ceil(devotion * .19);
			
			// Set their devotion and add a death
			DCharUtil.subtractDevotion(charID, devotionRemoved);
			DPlayerUtil.addDeath(player);
			
			// Let 'em know
			player.sendMessage(ChatColor.RED + "You have failed " + deity + "!");
			player.sendMessage(ChatColor.RED + "You have been stripped of " + devotionRemoved + " devotion!");
		}
	}

}
