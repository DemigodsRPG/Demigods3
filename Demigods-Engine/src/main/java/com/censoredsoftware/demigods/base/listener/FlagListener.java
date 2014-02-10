package com.censoredsoftware.demigods.base.listener;

import com.censoredsoftware.demigods.engine.DemigodsPlugin;
import com.censoredsoftware.demigods.engine.data.Data;
import com.censoredsoftware.demigods.engine.data.serializable.StructureSave;
import com.censoredsoftware.demigods.engine.entity.player.DemigodsPlayer;
import com.censoredsoftware.demigods.engine.language.English;
import com.censoredsoftware.demigods.engine.mythos.DivineItem;
import com.censoredsoftware.demigods.engine.mythos.StructureType;
import com.censoredsoftware.demigods.engine.util.Zones;
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

public class FlagListener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEnchantEvent(EnchantItemEvent event)
	{
		if(Demigods.getMythos().itemHasFlag(event.getItem(), DivineItem.Flag.UNENCHANTABLE)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if(event.isCancelled() || Zones.inNoDemigodsZone(event.getBlock().getLocation())) return;
		if(StructureType.Util.partOfStructureWithFlag(event.getBlock().getLocation(), StructureType.Flag.PROTECTED_BLOCKS, StructureType.Flag.DESTRUCT_ON_BREAK))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.YELLOW + English.PROTECTED_BLOCK.getLine());
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onBlockBreak(BlockBreakEvent event)
	{
		Location location = event.getBlock().getLocation();
		if(event.isCancelled() || Zones.inNoDemigodsZone(location)) return;
		if(StructureType.Util.partOfStructureWithFlag(location, StructureType.Flag.PROTECTED_BLOCKS, StructureType.Flag.DESTRUCT_ON_BREAK))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.YELLOW + English.PROTECTED_BLOCK.getLine());
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockIgnite(BlockIgniteEvent event)
	{
		if(event.isCancelled() || Zones.inNoDemigodsZone(event.getBlock().getLocation())) return;
		if(StructureType.Util.partOfStructureWithFlag(event.getBlock().getLocation(), StructureType.Flag.PROTECTED_BLOCKS, StructureType.Flag.DESTRUCT_ON_BREAK)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockDamage(BlockDamageEvent event)
	{
		Location location = event.getBlock().getLocation();
		if(event.isCancelled() || Zones.inNoDemigodsZone(location)) return;
		if(StructureType.Util.partOfStructureWithFlag(location, StructureType.Flag.PROTECTED_BLOCKS, StructureType.Flag.DESTRUCT_ON_BREAK)) event.setCancelled(true);
		if(DemigodsPlayer.Util.getPlayer(event.getPlayer()).getCurrent() != null && StructureType.Util.partOfStructureWithFlag(location, StructureType.Flag.DESTRUCT_ON_BREAK)) StructureType.Util.getStructureRegional(location).corrupt(DemigodsPlayer.Util.getPlayer(event.getPlayer()).getCurrent(), 1F);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPistonExtend(BlockPistonExtendEvent event)
	{
		if(event.isCancelled() || Zones.inNoDemigodsZone(event.getBlock().getLocation())) return;
		for(Block block : event.getBlocks())
		{
			if(StructureType.Util.partOfStructureWithFlag(block.getLocation(), StructureType.Flag.PROTECTED_BLOCKS, StructureType.Flag.DESTRUCT_ON_BREAK))
			{
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPistonRetract(BlockPistonRetractEvent event)
	{
		if(event.isCancelled() || Zones.inNoDemigodsZone(event.getBlock().getLocation())) return;
		if(StructureType.Util.partOfStructureWithFlag(event.getBlock().getRelative(event.getDirection(), 2).getLocation(), StructureType.Flag.PROTECTED_BLOCKS, StructureType.Flag.DESTRUCT_ON_BREAK) && event.isSticky()) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityExplode(final EntityExplodeEvent event)
	{
		if(event.getEntity() == null || Zones.inNoDemigodsZone(event.getEntity().getLocation())) return;
		final List<StructureSave> saves = Lists.newArrayList(StructureType.Util.getInRadiusWithFlag(event.getLocation(), StructureType.Flag.PROTECTED_BLOCKS, StructureType.Flag.DESTRUCT_ON_BREAK));

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DemigodsPlugin.getInst(), new Runnable()
		{
			@Override
			public void run()
			{
				// Remove all drops from explosion zone
				for(final StructureSave save : saves)
					for(Item drop : event.getLocation().getWorld().getEntitiesByClass(Item.class))
						if(drop.getLocation().distance(save.getReferenceLocation()) <= save.getType().getRadius()) drop.remove();
			}
		}, 1);

		if(Data.TIMED.boolContainsKey("explode-structure")) return;
		Data.TIMED.setBool("explode-structure", true, 2, TimeUnit.SECONDS);

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(DemigodsPlugin.getInst(), new Runnable()
		{
			@Override
			public void run()
			{
				for(final StructureSave save : saves)
					save.generate();
			}
		}, 30);
	}
}
