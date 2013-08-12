package com.censoredsoftware.demigods.listener;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.battle.Battle;
import com.censoredsoftware.demigods.battle.Participant;

public class BattleListener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public static void onDamageBy(EntityDamageByEntityEvent event)
	{
		if(Demigods.isDisabledWorld(event.getEntity().getLocation())) return;
		Entity damager = event.getDamager();
		if(damager instanceof Projectile) damager = ((Projectile) damager).getShooter();
		if(!Battle.Util.canParticipate(event.getEntity()) || !Battle.Util.canParticipate(damager)) return;

		// Define participants
		Participant damageeParticipant = Battle.Util.defineParticipant(event.getEntity());
		Participant damagerParticipant = Battle.Util.defineParticipant(damager);

		// Calculate midpoint location
		Location midpoint = damagerParticipant.getCurrentLocation().toVector().getMidpoint(event.getEntity().getLocation().toVector()).toLocation(damagerParticipant.getCurrentLocation().getWorld());

		if(!Battle.Util.existsNear(midpoint) && !Battle.Util.existsInRadius(midpoint))
		{
			// Create new battle
			Battle battle = Battle.Util.create(damagerParticipant, damageeParticipant);

			// Battle death
			if(event.getDamage() >= ((LivingEntity) event.getEntity()).getHealth())
			{
				event.setCancelled(true);
				Battle.Util.battleDeath(damagerParticipant, damageeParticipant, battle);
			}

			// Debug
			Demigods.message.broadcast(ChatColor.YELLOW + "Battle started involving " + damagerParticipant.getRelatedCharacter().getName() + " and " + damageeParticipant.getRelatedCharacter().getName() + "!");
		}
		else
		{
			// Add to existing battle
			Battle battle = Battle.Util.getNear(midpoint) != null ? Battle.Util.getNear(midpoint) : Battle.Util.getInRadius(midpoint);

			// Add participants from this event
			battle.addParticipant(damageeParticipant);
			battle.addParticipant(damagerParticipant);

			// Battle death
			if(event.getDamage() >= ((LivingEntity) event.getEntity()).getHealth())
			{
				event.setCancelled(true);
				Battle.Util.battleDeath(damagerParticipant, damageeParticipant, battle);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamage(EntityDamageEvent event)
	{
		if(Demigods.isDisabledWorld(event.getEntity().getLocation())) return;
		if(event instanceof EntityDamageByEntityEvent || !Battle.Util.canParticipate(event.getEntity())) return;

		Participant participant = Battle.Util.defineParticipant(event.getEntity());

		// Battle death
		if(Battle.Util.isInBattle(participant) && event.getDamage() >= ((LivingEntity) event.getEntity()).getHealth())
		{
			event.setCancelled(true);
			Battle.Util.battleDeath(participant, Battle.Util.getBattle(participant));
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	// TODO Doesn't work.
	public void onBattleMove(PlayerMoveEvent event)
	{
		if(Demigods.isDisabledWorld(event.getPlayer().getLocation())) return;
		// if(!Battle.Util.canParticipate(event.getPlayer())) return;
		// Participant participant = Battle.Util.defineParticipant(event.getPlayer());
		// TODO
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	// TODO Doesn't work.
	public void onBattleMove(PlayerTeleportEvent event)
	{
		if(Demigods.isDisabledWorld(event.getPlayer().getLocation())) return;
		// if(!Battle.Util.canParticipate(event.getPlayer())) return;
		// Participant participant = Battle.Util.defineParticipant(event.getPlayer());
		// TODO
	}
}
