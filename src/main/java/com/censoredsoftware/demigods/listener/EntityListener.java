package com.censoredsoftware.demigods.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.battle.Battle;
import com.censoredsoftware.demigods.language.Translation;
import com.censoredsoftware.demigods.player.Character;
import com.censoredsoftware.demigods.player.Pet;
import com.censoredsoftware.demigods.player.PlayerSave;
import com.censoredsoftware.demigods.util.Messages;

public class EntityListener implements Listener
{
	@EventHandler(priority = EventPriority.LOWEST)
	public static void damageEvent(EntityDamageEvent event)
	{
		if(Demigods.MiscUtil.isDisabledWorld(event.getEntity().getLocation())) return;
		if(event.getEntity() instanceof Player && !Battle.Util.canTarget(Battle.Util.defineParticipant(event.getEntity()))) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public static void damageByEntityEvent(EntityDamageByEntityEvent event)
	{
		if(Demigods.MiscUtil.isDisabledWorld(event.getEntity().getLocation())) return;

		Entity attacked = event.getEntity();
		Entity attacker = event.getDamager();

		if(attacker instanceof Player)
		{
			Player hitting = (Player) attacker;

			// No PvP
			if(!PlayerSave.Util.getPlayer(hitting).canPvp() || !Battle.Util.canTarget(Battle.Util.defineParticipant(attacked)))
			{
				hitting.sendMessage(ChatColor.GRAY + Demigods.LANGUAGE.getText(Translation.Text.NO_PVP_ZONE));
				event.setCancelled(true);
				return;
			}

			if(attacked instanceof Tameable && ((Tameable) attacked).isTamed() && Pet.Util.getPet((LivingEntity) attacked) != null && PlayerSave.Util.getPlayer(hitting).getCurrent() != null && Character.Util.areAllied(PlayerSave.Util.getPlayer(hitting).getCurrent(), Pet.Util.getPet((LivingEntity) attacked).getOwner())) event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public static void entityDeath(EntityDeathEvent event)
	{
		if(Demigods.MiscUtil.isDisabledWorld(event.getEntity().getLocation())) return;

		if(event.getEntity() instanceof Player)
		{
			Player player = (Player) event.getEntity();
			Character playerChar = PlayerSave.Util.getPlayer(player).getCurrent();
			if(playerChar != null) playerChar.addDeath();
		}
		else if(event.getEntity() instanceof Tameable && ((Tameable) event.getEntity()).isTamed())
		{
			LivingEntity entity = event.getEntity();
			Pet wrapper = Pet.Util.getPet(entity);
			if(wrapper == null) return;
			Character owner = wrapper.getOwner();
			if(owner == null) return;
			String damagerMessage = "";
			if(entity.getLastDamageCause() instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) entity.getLastDamageCause()).getDamager() instanceof Player)
			{
				Character damager = PlayerSave.Util.getPlayer((Player) ((EntityDamageByEntityEvent) entity.getLastDamageCause()).getDamager()).getCurrent();
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
		if(Demigods.MiscUtil.isDisabledWorld(event.getEntity().getLocation())) return;

		LivingEntity entity = event.getEntity();
		AnimalTamer owner = event.getOwner();
		com.censoredsoftware.demigods.player.Character character = PlayerSave.Util.getPlayer(Bukkit.getOfflinePlayer(owner.getName())).getCurrent();
		if(character != null) Pet.Util.create(entity, PlayerSave.Util.getPlayer(Bukkit.getOfflinePlayer(owner.getName())).getCurrent());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityTarget(EntityTargetLivingEntityEvent event)
	{
		if(Demigods.MiscUtil.isDisabledWorld(event.getEntity().getLocation())) return;
		if(event.getTarget() instanceof Player && !PlayerSave.Util.getPlayer(((Player) event.getTarget())).canPvp()) event.setCancelled(true);
	}
}
