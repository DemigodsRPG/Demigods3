package com.censoredsoftware.Demigods.Engine.Listener;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.censoredsoftware.Demigods.Engine.Object.Structure.StructureInfo;
import com.censoredsoftware.Demigods.Engine.Object.Structure.StructureSave;
import com.censoredsoftware.Demigods.Engine.Utility.StructureUtility;

public class GriefListener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		Location location = event.getBlock().getLocation();
		if(StructureUtility.isInRadiusWithFlag(location, StructureInfo.Flag.NO_GRIEFING_ZONE))
		{
			StructureSave save = StructureUtility.getInRadiusWithFlag(location, StructureInfo.Flag.NO_GRIEFING_ZONE);
			if(save.getStructureInfo().getFlags().contains(StructureInfo.Flag.HAS_OWNER))
			{
				if(event.getPlayer().getName().equals(save.getOwner().getOfflinePlayer().getName())) return;
			}
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event)
	{
		Location location = event.getBlock().getLocation();
		if(StructureUtility.isInRadiusWithFlag(location, StructureInfo.Flag.NO_GRIEFING_ZONE))
		{
			StructureSave save = StructureUtility.getInRadiusWithFlag(location, StructureInfo.Flag.NO_GRIEFING_ZONE);
			if(save.getStructureInfo().getFlags().contains(StructureInfo.Flag.HAS_OWNER))
			{
				if(event.getPlayer().getName().equals(save.getOwner().getOfflinePlayer().getName())) return;
			}
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockIgnite(BlockIgniteEvent event)
	{
		Location location = event.getBlock().getLocation();
		if(StructureUtility.isInRadiusWithFlag(location, StructureInfo.Flag.NO_GRIEFING_ZONE))
		{
			StructureSave save = StructureUtility.getInRadiusWithFlag(location, StructureInfo.Flag.NO_GRIEFING_ZONE);
			if(save.getStructureInfo().getFlags().contains(StructureInfo.Flag.HAS_OWNER))
			{
				if(event.getPlayer().getName().equals(save.getOwner().getOfflinePlayer().getName())) return;
			}
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockDamage(BlockDamageEvent event)
	{
		Location location = event.getBlock().getLocation();
		if(StructureUtility.isInRadiusWithFlag(location, StructureInfo.Flag.NO_GRIEFING_ZONE))
		{
			StructureSave save = StructureUtility.getInRadiusWithFlag(location, StructureInfo.Flag.NO_GRIEFING_ZONE);
			if(save.getStructureInfo().getFlags().contains(StructureInfo.Flag.HAS_OWNER))
			{
				if(event.getPlayer().getName().equals(save.getOwner().getOfflinePlayer().getName())) return;
			}
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityExplode(final EntityExplodeEvent event)
	{
		event.setCancelled(StructureUtility.isInRadiusWithFlag(event.getLocation(), StructureInfo.Flag.NO_GRIEFING_ZONE));
	}
}
