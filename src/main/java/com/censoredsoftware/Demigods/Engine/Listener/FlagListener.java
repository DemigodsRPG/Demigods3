package com.censoredsoftware.Demigods.Engine.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.censoredsoftware.Demigods.Engine.Object.Structure.Structure;

public class FlagListener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	private void onBlockBreak(BlockBreakEvent event)
	{
		if(Structure.partOfStructureWithFlag(event.getBlock().getLocation(), Structure.Flag.PROTECTED_BLOCKS)) event.setCancelled(true);
	}
}
