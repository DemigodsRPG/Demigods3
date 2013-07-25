package com.censoredsoftware.Demigods.Engine.StructureFlags;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.censoredsoftware.Demigods.Engine.Object.Structure.Flag;
import com.censoredsoftware.Demigods.Engine.Object.Structure.Structure;

public class NoGrief implements Flag
{
	@Override
	public Listener getUniqueListener()
	{
		return new Listener()
		{
			@EventHandler(priority = EventPriority.HIGHEST)
			private void onBlockBreak(BlockBreakEvent event)
			{
				if(Structure.partOfStructureWithFlag(event.getBlock().getLocation(), StructureFlag.NO_GRIEFING)) event.setCancelled(true);
			}
		};
	}
}
