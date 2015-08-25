package com.demigodsrpg.demigods.base.listener;

import com.demigodsrpg.demigods.engine.battle.Battle;
import com.demigodsrpg.demigods.engine.entity.DemigodsTameable;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsPlayer;
import com.demigodsrpg.demigods.engine.language.English;
import com.demigodsrpg.demigods.engine.util.Messages;
import com.demigodsrpg.demigods.engine.util.Zones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;

public class EntityListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public static void damageEvent(EntityDamageEvent event) {
        if (Zones.inNoDemigodsZone(event.getEntity().getLocation())) return;
        if (event.getEntity() instanceof Player && !Battle.canTarget(Battle.defineParticipant(event.getEntity())))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public static void damageByEntityEvent(EntityDamageByEntityEvent event) {
        if (Zones.inNoDemigodsZone(event.getEntity().getLocation())) return;

        Entity attacked = event.getEntity();
        Entity attacker = event.getDamager();

        // Allow killing things that don't need protection
        if (!Battle.canParticipate(attacked)) return;

        if (attacker instanceof Player) {
            Player hitting = (Player) attacker;

            // No PvP
            if (!DemigodsPlayer.of(hitting).canPvp() || !Battle.canTarget(Battle.defineParticipant(attacked))) {
                hitting.sendMessage(ChatColor.GRAY + English.NO_PVP_ZONE.getLine());
                event.setCancelled(true);
                return;
            }

            if (attacked instanceof Tameable && ((Tameable) attacked).isTamed() && DemigodsTameable.of((LivingEntity) attacked) != null && DemigodsCharacter.of(hitting) != null && DemigodsCharacter.of(hitting).alliedTo(DemigodsTameable.of((LivingEntity) attacked)))
                event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public static void entityDeath(EntityDeathEvent event) {
        if (Zones.inNoDemigodsZone(event.getEntity().getLocation())) return;

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            DemigodsCharacter playerChar = DemigodsCharacter.of(player);
            if (playerChar != null) playerChar.addDeath();
        } else if (event.getEntity() instanceof Tameable && ((Tameable) event.getEntity()).isTamed()) {
            LivingEntity entity = event.getEntity();
            DemigodsTameable wrapper = DemigodsTameable.of(entity);
            if (wrapper == null) return;
            DemigodsCharacter owner = wrapper.getOwner();
            if (owner == null) return;
            String damagerMessage = "";
            if (entity.getLastDamageCause() instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) entity.getLastDamageCause()).getDamager() instanceof Player) {
                DemigodsCharacter damager = DemigodsCharacter.of((Player) ((EntityDamageByEntityEvent) entity.getLastDamageCause()).getDamager());
                if (damager != null) damagerMessage = " by " + damager.getDeity().getColor() + damager.getName();
            }
            if (entity.getCustomName() != null)
                Messages.broadcast(owner.getDeity().getColor() + owner.getName() + "'s " + ChatColor.YELLOW + entity.getType().getName().replace("Entity", "").toLowerCase() + ", " + owner.getDeity().getColor() + entity.getCustomName() + ChatColor.YELLOW + ", was slain" + damagerMessage + ChatColor.YELLOW + ".");
            else
                Messages.broadcast(owner.getDeity().getColor() + owner.getName() + "'s " + ChatColor.YELLOW + entity.getType().getName().replace("Entity", "").toLowerCase() + " was slain" + damagerMessage + ChatColor.YELLOW + ".");
            wrapper.remove();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTame(EntityTameEvent event) {
        if (Zones.inNoDemigodsZone(event.getEntity().getLocation())) return;

        LivingEntity entity = event.getEntity();
        AnimalTamer owner = event.getOwner();
        DemigodsCharacter character = DemigodsCharacter.of(Bukkit.getOfflinePlayer(owner.getName()));
        if (character != null) DemigodsTameable.create((Tameable) entity, DemigodsCharacter.of((Player) owner));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityTarget(EntityTargetLivingEntityEvent event) {
        if (Zones.inNoDemigodsZone(event.getEntity().getLocation())) return;
        if (event.getTarget() instanceof Player && !DemigodsPlayer.of(((Player) event.getTarget())).canPvp())
            event.setCancelled(true);
    }
}
