package com.demigodsrpg.demigods.base.listener;

import com.demigodsrpg.demigods.engine.Demigods;
import com.demigodsrpg.demigods.engine.DemigodsPlugin;
import com.demigodsrpg.demigods.engine.data.TimedServerData;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsPlayer;
import com.demigodsrpg.demigods.engine.item.DivineItem;
import com.demigodsrpg.demigods.engine.language.English;
import com.demigodsrpg.demigods.engine.structure.DemigodsStructure;
import com.demigodsrpg.demigods.engine.structure.DemigodsStructureType;
import com.demigodsrpg.demigods.engine.util.Zones;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class FlagListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEnchantEvent(EnchantItemEvent event) {
        if (Demigods.getMythos().itemHasFlag(event.getItem(), DivineItem.Flag.UNENCHANTABLE)) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled() || Zones.inNoDemigodsZone(event.getBlock().getLocation())) return;
        if (DemigodsStructureType.Util.partOfStructureWithFlag(event.getBlock().getLocation(), DemigodsStructureType.Flag.PROTECTED_BLOCKS, DemigodsStructureType.Flag.DESTRUCT_ON_BREAK)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.YELLOW + English.PROTECTED_BLOCK.getLine());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onBlockBreak(BlockBreakEvent event) {
        Location location = event.getBlock().getLocation();
        if (event.isCancelled() || Zones.inNoDemigodsZone(location)) return;
        if (DemigodsStructureType.Util.partOfStructureWithFlag(location, DemigodsStructureType.Flag.PROTECTED_BLOCKS, DemigodsStructureType.Flag.DESTRUCT_ON_BREAK)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.YELLOW + English.PROTECTED_BLOCK.getLine());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.isCancelled() || Zones.inNoDemigodsZone(event.getBlock().getLocation())) return;
        if (DemigodsStructureType.Util.partOfStructureWithFlag(event.getBlock().getLocation(), DemigodsStructureType.Flag.PROTECTED_BLOCKS, DemigodsStructureType.Flag.DESTRUCT_ON_BREAK))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockDamage(BlockDamageEvent event) {
        Location location = event.getBlock().getLocation();
        if (event.isCancelled() || Zones.inNoDemigodsZone(location)) return;
        if (DemigodsStructureType.Util.partOfStructureWithFlag(location, DemigodsStructureType.Flag.PROTECTED_BLOCKS, DemigodsStructureType.Flag.DESTRUCT_ON_BREAK))
            event.setCancelled(true);
        if (DemigodsPlayer.of(event.getPlayer()).isACharacter() && DemigodsStructureType.Util.partOfStructureWithFlag(location, DemigodsStructureType.Flag.DESTRUCT_ON_BREAK))
            DemigodsStructureType.Util.getStructureRegional(location).corrupt(DemigodsCharacter.of(event.getPlayer()), 1F);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        if (event.isCancelled() || Zones.inNoDemigodsZone(event.getBlock().getLocation())) return;
        for (Block block : event.getBlocks()) {
            if (DemigodsStructureType.Util.partOfStructureWithFlag(block.getLocation(), DemigodsStructureType.Flag.PROTECTED_BLOCKS, DemigodsStructureType.Flag.DESTRUCT_ON_BREAK)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        if (event.isCancelled() || Zones.inNoDemigodsZone(event.getBlock().getLocation())) return;
        if (DemigodsStructureType.Util.partOfStructureWithFlag(event.getBlock().getRelative(event.getDirection(), 2).getLocation(), DemigodsStructureType.Flag.PROTECTED_BLOCKS, DemigodsStructureType.Flag.DESTRUCT_ON_BREAK) && event.isSticky())
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(final EntityExplodeEvent event) {
        if (event.getEntity() == null || Zones.inNoDemigodsZone(event.getEntity().getLocation())) return;
        final List<DemigodsStructure> saves = Lists.newArrayList(DemigodsStructureType.Util.getInRadiusWithFlag(event.getLocation(), DemigodsStructureType.Flag.PROTECTED_BLOCKS, DemigodsStructureType.Flag.DESTRUCT_ON_BREAK));

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DemigodsPlugin.getInst(), new Runnable() {
            @Override
            public void run() {
                // Remove all drops from explosion zone
                for (final DemigodsStructure save : saves)
                    for (Item drop : event.getLocation().getWorld().getEntitiesByClass(Item.class))
                        if (drop.getLocation().distance(save.getBukkitLocation()) <= save.getType().getRadius())
                            drop.remove();
            }
        }, 1);

        if (TimedServerData.exists("explode-structure", "blaam")) return;
        TimedServerData.saveTimed("explode-structure", "blaam", true, 2, TimeUnit.SECONDS);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DemigodsPlugin.getInst(), new Runnable() {
            @Override
            public void run() {
                for (final DemigodsStructure save : saves)
                    save.generate();
            }
        }, 30);
    }
}
