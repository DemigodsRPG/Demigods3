package com.censoredsoftware.Demigods.Engine.Listener;

// TODO Fix for lag.

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.censoredsoftware.Demigods.Engine.Object.DPlayer;
import com.censoredsoftware.Demigods.Engine.Object.Structure;
import com.censoredsoftware.Demigods.Engine.Utility.LocationUtility;

public class GriefListener implements Listener
{
	private final static Set<Material> blockInventories = new HashSet<Material>()
	{
		{
			add(Material.CHEST);
			add(Material.ENDER_CHEST);
			add(Material.FURNACE);
			add(Material.BURNING_FURNACE);
			add(Material.DISPENSER);
			add(Material.DROPPER);
			add(Material.BREWING_STAND);
			add(Material.BEACON);
			add(Material.HOPPER);
			add(Material.HOPPER_MINECART);
			add(Material.STORAGE_MINECART);
		}
	};

	@EventHandler(priority = EventPriority.HIGHEST)
	// TODO MINOR LAG - NOT SURE
	public void onBlockPlace(BlockPlaceEvent event)
	{
		Structure.Save save = Structure.Util.getInRadiusWithFlag(event.getBlock().getLocation(), Structure.Flag.NO_GRIEFING);
		if(save != null)
		{
			DPlayer.Character character = DPlayer.Util.getPlayer(event.getPlayer()).getCurrent();
			DPlayer.Character owner = save.getOwner();
			if(character != null && owner != null && character.getId().equals(owner.getId())) return;
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event)
	{
		Structure.Save save = Structure.Util.getInRadiusWithFlag(event.getBlock().getLocation(), Structure.Flag.NO_GRIEFING);
		if(save != null)
		{
			DPlayer.Character character = DPlayer.Util.getPlayer(event.getPlayer()).getCurrent();
			DPlayer.Character owner = save.getOwner();
			if(character != null && owner != null && character.getId().equals(owner.getId())) return;
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockIgnite(BlockIgniteEvent event)
	{
		Structure.Save save = Structure.Util.getInRadiusWithFlag(event.getBlock().getLocation(), Structure.Flag.NO_GRIEFING);
		if(save != null)
		{
			DPlayer.Character character = DPlayer.Util.getPlayer(event.getPlayer()).getCurrent();
			DPlayer.Character owner = save.getOwner();
			if(character != null && owner != null && character.getId().equals(owner.getId())) return;
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBurn(BlockBurnEvent event)
	{
		if(Structure.Util.isInRadiusWithFlag(event.getBlock().getLocation(), Structure.Flag.NO_GRIEFING)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	// TODO MINOR LAG
	public void onBlockFall(EntityChangeBlockEvent event)
	{
		if(event.getEntityType() != EntityType.FALLING_BLOCK) return;
		FallingBlock block = (FallingBlock) event.getEntity();
		Location blockLocation = block.getLocation();
		if(Structure.Util.isInRadiusWithFlag(LocationUtility.getFloorBelowLocation(block.getLocation()), Structure.Flag.NO_GRIEFING))
		{
			// Break the block
			event.setCancelled(true);
			event.getBlock().setType(Material.AIR);
			blockLocation.getWorld().dropItemNaturally(blockLocation, new ItemStack(block.getMaterial()));
			block.remove();
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLiquidMove(BlockFromToEvent event)
	{
		if(Structure.Util.isInRadiusWithFlag(event.getToBlock().getLocation(), Structure.Flag.NO_GRIEFING)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPistonExtend(BlockPistonExtendEvent event)
	{
		boolean in = false;
		boolean out = false;
		for(Block block : event.getBlocks())
		{
			if(Structure.Util.isInRadiusWithFlag(block.getLocation(), Structure.Flag.NO_GRIEFING)) in = true;
			else out = true;
		}
		if(in != out) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPistonRetract(BlockPistonRetractEvent event)
	{
		boolean block = Structure.Util.isInRadiusWithFlag(event.getBlock().getLocation(), Structure.Flag.NO_GRIEFING);
		boolean retract = Structure.Util.isInRadiusWithFlag(event.getRetractLocation(), Structure.Flag.NO_GRIEFING);
		if(block != retract) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	// TODO MINOR LAG - NOT SURE
	public void onBlockDamage(BlockDamageEvent event)
	{
		Structure.Save save = Structure.Util.getInRadiusWithFlag(event.getBlock().getLocation(), Structure.Flag.NO_GRIEFING);
		if(save != null)
		{
			DPlayer.Character character = DPlayer.Util.getPlayer(event.getPlayer()).getCurrent();
			DPlayer.Character owner = save.getOwner();
			if(character != null && owner != null && character.getId().equals(owner.getId())) return;
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityExplode(final EntityExplodeEvent event)
	{
		if(Structure.Util.isInRadiusWithFlag(event.getLocation(), Structure.Flag.NO_GRIEFING)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onAttemptInventoryOpen(PlayerInteractEvent event) // TODO Fix horse inventories.
	{
		if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		Block block = event.getClickedBlock();
		Structure.Save save = Structure.Util.getInRadiusWithFlag(block.getLocation(), Structure.Flag.NO_GRIEFING);
		if(save == null) return;
		if(blockInventories.contains(block.getType()))
		{
			DPlayer.Character character = DPlayer.Util.getPlayer(event.getPlayer()).getCurrent();
			DPlayer.Character owner = save.getOwner();
			if(character != null && owner != null && character.getId().equals(owner.getId())) return;
			event.setCancelled(true);
		}
	}
}
