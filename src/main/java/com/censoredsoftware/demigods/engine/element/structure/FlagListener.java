package com.censoredsoftware.demigods.engine.element.structure;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.data.DataManager;
import com.censoredsoftware.demigods.engine.language.TranslationManager;

public class FlagListener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if(Structure.Util.partOfStructureWithFlag(event.getBlock().getLocation(), Structure.Flag.PROTECTED_BLOCKS, true))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.YELLOW + Demigods.text.getText(TranslationManager.Text.PROTECTED_BLOCK));
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onBlockBreak(BlockBreakEvent event)
	{
		if(Structure.Util.partOfStructureWithFlag(event.getBlock().getLocation(), Structure.Flag.PROTECTED_BLOCKS, true))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.YELLOW + Demigods.text.getText(TranslationManager.Text.PROTECTED_BLOCK));
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockIgnite(BlockIgniteEvent event)
	{
		if(Structure.Util.partOfStructureWithFlag(event.getBlock().getLocation(), Structure.Flag.PROTECTED_BLOCKS, true)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockDamage(BlockDamageEvent event)
	{
		if(Structure.Util.partOfStructureWithFlag(event.getBlock().getLocation(), Structure.Flag.PROTECTED_BLOCKS, true)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPistonExtend(BlockPistonExtendEvent event)
	{
		for(Block block : event.getBlocks())
		{
			if(Structure.Util.partOfStructureWithFlag(block.getLocation(), Structure.Flag.PROTECTED_BLOCKS, true))
			{
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPistonRetract(BlockPistonRetractEvent event)
	{
		if(Structure.Util.partOfStructureWithFlag(event.getBlock().getRelative(event.getDirection(), 2).getLocation(), Structure.Flag.PROTECTED_BLOCKS, true) && event.isSticky()) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityExplode(final EntityExplodeEvent event)
	{
		final Structure.Save save = Structure.Util.getInRadiusWithFlag(event.getLocation(), Structure.Flag.PROTECTED_BLOCKS, true);
		if(save == null) return;

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Demigods.plugin, new Runnable()
		{
			@Override
			public void run()
			{
				// Remove all drops from explosion zone
				for(Item drop : event.getLocation().getWorld().getEntitiesByClass(Item.class))
				{
					drop.remove();
				}
			}
		}, 1);

		if(DataManager.hasTimed("explode", "structure")) return;
		DataManager.saveTimed("explode", "structure", true, 3);

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Demigods.plugin, new Runnable()
		{
			@Override
			public void run()
			{
				save.generate();
			}
		}, 30);
	}
}
