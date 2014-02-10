package com.censoredsoftware.demigods.base.listener;

import com.censoredsoftware.demigods.engine.battle.Battle;
import com.censoredsoftware.demigods.engine.entity.player.DemigodsCharacter;
import com.censoredsoftware.demigods.engine.entity.player.DemigodsPlayer;
import com.censoredsoftware.demigods.engine.language.English;
import com.censoredsoftware.demigods.engine.util.Messages;
import com.censoredsoftware.demigods.engine.util.Zones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;

public class EntityListener implements Listener
{
	@EventHandler(priority = EventPriority.LOWEST)
	public static void damageEvent(EntityDamageEvent event)
	{
		if(Zones.inNoDemigodsZone(event.getEntity().getLocation())) return;
		if(event.getEntity() instanceof Player && !Battle.Util.canTarget(Battle.Util.defineParticipant(event.getEntity()))) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public static void damageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if(Zones.inNoDemigodsZone(event.getEntity().getLocation())) return;

		Entity attacked = event.getEntity();
		Entity attacker = event.getDamager();

		// Allow killing things that don't need protection
		if(!Battle.Util.canParticipate(attacked)) return;

		if(attacker instanceof Player)
		{
			Player hitting = (Player) attacker;

			// No PvP
			if(!DemigodsPlayer.Util.getPlayer(hitting).canPvp() || !Battle.Util.canTarget(Battle.Util.defineParticipant(attacked)))
			{
				hitting.sendMessage(ChatColor.GRAY + English.NO_PVP_ZONE.getLine());
				event.setCancelled(true);
				return;
			}

			if(attacked instanceof Tameable && ((Tameable) attacked).isTamed() && DPet.Util.getPet((LivingEntity) attacked) != null && DemigodsPlayer.Util.getPlayer(hitting).getCurrent() != null && DemigodsCharacter.Util.areAllied(DemigodsPlayer.Util.getPlayer(hitting).getCurrent(), DPet.Util.getPet((LivingEntity) attacked).getOwner())) event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public static void entityDeath(EntityDeathEvent event)
	{
		if(Zones.inNoDemigodsZone(event.getEntity().getLocation())) return;

		if(event.getEntity() instanceof Player)
		{
			Player player = (Player) event.getEntity();
			DemigodsCharacter playerChar = DemigodsPlayer.Util.getPlayer(player).getCurrent();
			if(playerChar != null) playerChar.addDeath();
		}
		else if(event.getEntity() instanceof Tameable && ((Tameable) event.getEntity()).isTamed())
		{
			LivingEntity entity = event.getEntity();
			DPet wrapper = DPet.Util.getPet(entity);
			if(wrapper == null) return;
			DemigodsCharacter owner = wrapper.getOwner();
			if(owner == null) return;
			String damagerMessage = "";
			if(entity.getLastDamageCause() instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) entity.getLastDamageCause()).getDamager() instanceof Player)
			{
				DemigodsCharacter damager = DemigodsPlayer.Util.getPlayer((Player) ((EntityDamageByEntityEvent) entity.getLastDamageCause()).getDamager()).getCurrent();
				if(damager != null) damagerMessage = " by " + damager.getDeity().getColor() + damager.getName();
			}
			if(entity.getCustomName() != null) Messages.broadcast(owner.getDeity().getColor() + owner.getName() + "'s " + ChatColor.YELLOW + entity.getType().getName().replace("Entity", "").toLowerCase() + ", " + owner.getDeity().getColor() + entity.getCustomName() + ChatColor.YELLOW + ", was slain" + damagerMessage + ChatColor.YELLOW + ".");
			else Messages.broadcast(owner.getDeity().getColor() + owner.getName() + "'s " + ChatColor.YELLOW + entity.getType().getName().replace("Entity", "").toLowerCase() + " was slain" + damagerMessage + ChatColor.YELLOW + ".");
			wrapper.delete();
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onTame(EntityTameEvent event)
	{
		if(Zones.inNoDemigodsZone(event.getEntity().getLocation())) return;

		LivingEntity entity = event.getEntity();
		AnimalTamer owner = event.getOwner();
		DemigodsCharacter character = DemigodsPlayer.Util.getPlayer(Bukkit.getOfflinePlayer(owner.getName())).getCurrent();
		if(character != null) DPet.Util.create((Tameable) entity, DemigodsPlayer.Util.getPlayer((Player) owner).getCurrent());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityTarget(EntityTargetLivingEntityEvent event)
	{
		if(Zones.inNoDemigodsZone(event.getEntity().getLocation())) return;
		if(event.getTarget() instanceof Player && !DemigodsPlayer.Util.getPlayer(((Player) event.getTarget())).canPvp()) event.setCancelled(true);
	}
}
