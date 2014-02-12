package com.demigodsrpg.demigods.base.listener;

import com.demigodsrpg.demigods.engine.battle.Battle;
import com.demigodsrpg.demigods.engine.battle.Participant;
import com.demigodsrpg.demigods.engine.data.TimedData;
import com.demigodsrpg.demigods.engine.entity.DemigodsPet;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.demigodsrpg.demigods.engine.location.DemigodsLocation;
import com.demigodsrpg.demigods.engine.util.Configs;
import com.demigodsrpg.demigods.engine.util.Zones;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTeleportEvent;

public class BattleListener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public static void onDamageBy(EntityDamageByEntityEvent event)
	{
		if(event.isCancelled()) return;
		if(Zones.inNoDemigodsZone(event.getEntity().getLocation())) return;
		Entity damager = event.getDamager();
		if(damager instanceof Projectile) damager = ((Projectile) damager).getShooter();
		if(!Battle.canParticipate(event.getEntity()))
		{
			if(Zones.inNoPvpZone(damager.getLocation()) || Zones.inNoPvpZone(damager.getLocation())) event.setCancelled(true);
			return;
		}

		Participant damageeParticipant = Battle.defineParticipant(event.getEntity());

		if(!Configs.getSettingBoolean("battles.enabled"))
		{
			if(!Battle.canParticipate(damager) && ((LivingEntity) event.getEntity()).getHealth() <= event.getDamage())
			{
				Participant damagerParticipant = Battle.defineParticipant(damager);

				damageeParticipant.getRelatedCharacter().addDeath(damagerParticipant.getRelatedCharacter());
				damagerParticipant.getRelatedCharacter().addKill();
			}
			return;
		}

		// Early battle death (prevents being killed by mobs)
		if(!Battle.canParticipate(damager) && Battle.isInBattle(damageeParticipant) && event.getDamage() >= ((LivingEntity) event.getEntity()).getHealth())
		{
			event.setCancelled(true);
			Battle.battleDeath(damageeParticipant, Battle.getBattle(damageeParticipant));
			return;
		}
		else if(!Battle.canParticipate(damager)) return;

		Participant damagerParticipant = Battle.defineParticipant(damager);

		// Various things that should cancel the event
		if(damageeParticipant.equals(damagerParticipant) || damageeParticipant.getRelatedCharacter().alliedTo(damagerParticipant.getRelatedCharacter()) || !damageeParticipant.getRelatedCharacter().canPvp() || !damagerParticipant.getRelatedCharacter().canPvp())
		{
			event.setCancelled(true);
			return;
		}
		if(damageeParticipant instanceof DemigodsCharacter && TimedData.exists(damageeParticipant.getId().toString(), "just_finished_battle"))
		{
			((Player) damager).sendMessage(ChatColor.YELLOW + "That player is in cooldown from a recent battle.");
			event.setCancelled(true);
			return;
		}
		if(damagerParticipant instanceof DemigodsCharacter && TimedData.exists(damagerParticipant.getId().toString(), "just_finished_battle"))
		{
			((Player) damager).sendMessage(ChatColor.YELLOW + "You are still in cooldown from a recent battle.");
			event.setCancelled(true);
			return;
		}

		// Calculate midpoint location
		Location midpoint = damagerParticipant.getCurrentLocation().toVector().getMidpoint(event.getEntity().getLocation().toVector()).toLocation(damagerParticipant.getCurrentLocation().getWorld());

		if(Battle.isInBattle(damageeParticipant) || Battle.isInBattle(damagerParticipant))
		{
			// Add to existing battle
			Battle battle = Battle.isInBattle(damageeParticipant) ? Battle.getBattle(damageeParticipant) : Battle.getBattle(damagerParticipant);

			// Add participants from this event
			battle.addParticipant(damageeParticipant);
			battle.addParticipant(damagerParticipant);

			// Battle death
			if(event.getDamage() >= ((LivingEntity) event.getEntity()).getHealth())
			{
				event.setCancelled(true);
				Battle.battleDeath(damagerParticipant, damageeParticipant, battle);
			}
			return;
		}

		if(!Battle.existsNear(midpoint) && !Battle.existsInRadius(midpoint))
		{
			// Create new battle
			Battle battle = Battle.create(damagerParticipant, damageeParticipant);

			// Battle death
			if(event.getDamage() >= ((LivingEntity) event.getEntity()).getHealth())
			{
				event.setCancelled(true);
				Battle.battleDeath(damagerParticipant, damageeParticipant, battle);
			}

			battle.sendMessage(ChatColor.YELLOW + "You are now in battle!");
		}
		else
		{
			// Add to existing battle
			Battle battle = Battle.getNear(midpoint) != null ? Battle.getNear(midpoint) : Battle.getInRadius(midpoint);

			// Add participants from this event
			battle.addParticipant(damageeParticipant);
			battle.addParticipant(damagerParticipant);

			// Battle death
			if(event.getDamage() >= ((LivingEntity) event.getEntity()).getHealth())
			{
				event.setCancelled(true);
				Battle.battleDeath(damagerParticipant, damageeParticipant, battle);
			}
		}

		// Pets
		if(damager instanceof LivingEntity) for(DemigodsPet pet : damageeParticipant.getRelatedCharacter().getPets())
		{
			LivingEntity entity = pet.getEntity();
			if(entity != null && entity instanceof Monster) ((Monster) entity).setTarget((LivingEntity) damager);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onDamage(EntityDamageEvent event)
	{
		if(!Configs.getSettingBoolean("battles.enabled") || Zones.inNoDemigodsZone(event.getEntity().getLocation())) return;
		if(event instanceof EntityDamageByEntityEvent || !Battle.canParticipate(event.getEntity())) return;

		Participant participant = Battle.defineParticipant(event.getEntity());

		if(participant instanceof DemigodsCharacter && TimedData.exists(participant.getId().toString(), "just_finished_battle"))
		{
			event.setCancelled(true);
			return;
		}

		// Battle death
		if(Battle.isInBattle(participant) && event.getDamage() >= ((LivingEntity) event.getEntity()).getHealth())
		{
			event.setCancelled(true);
			Battle.battleDeath(participant, Battle.getBattle(participant));
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBattleTeleport(EntityTeleportEvent event)
	{
		if(!Configs.getSettingBoolean("battles.enabled") || Zones.inNoDemigodsZone(event.getEntity().getLocation())) return;
		if(!Battle.canParticipate(event.getEntity())) return;
		Participant participant = Battle.defineParticipant(event.getEntity());
		if(Battle.isInBattle(participant))
		{
			Battle battle = Battle.getBattle(participant);
			if(!event.getTo().getWorld().equals(battle.getStartLocation().getWorld()) || DemigodsLocation.distanceFlat(event.getTo(), battle.getStartLocation().getBukkitLocation()) > battle.getRadius()) event.setCancelled(true);
		}
	}
}
