package com.censoredsoftware.Demigods.Engine.Listener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.censoredsoftware.Demigods.API.AdminAPI;
import com.censoredsoftware.Demigods.API.BlockAPI;
import com.censoredsoftware.Demigods.API.LocationAPI;
import com.censoredsoftware.Demigods.API.ZoneAPI;
import com.censoredsoftware.Demigods.Engine.Block.BlockFactory;
import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Event.Altar.AltarCreateEvent;
import com.censoredsoftware.Demigods.Engine.Event.Altar.AltarCreateEvent.AltarCreateCause;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedPlayer;

public class BlockListener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event)
	{
		DecimalFormat shorten = new DecimalFormat("#.##");
		long bCheck = System.currentTimeMillis();

		Location location = event.getBlock().getLocation();
		if(BlockAPI.isProtected(location))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.YELLOW + "That block is protected by a Deity!");

			double check = (System.currentTimeMillis() - bCheck) / 1000.0;
			Demigods.message.broadcast("It took " + shorten.format(check) + " seconds to execute this test.");
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockIgnite(BlockIgniteEvent event)
	{
		Location location = event.getBlock().getLocation();
		if(BlockAPI.isProtected(location)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockDamage(BlockDamageEvent event)
	{
		Location location = event.getBlock().getLocation();
		if(BlockAPI.isProtected(location)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPistonExtend(BlockPistonExtendEvent event)
	{
		List<Block> blocks = event.getBlocks();

		for(Block block : blocks)
		{
			Location location = block.getLocation();

			if(BlockAPI.isProtected(location))
			{
				event.setCancelled(true);
				break;
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPistonRetract(BlockPistonRetractEvent event)
	{
		final Block block = event.getBlock().getRelative(event.getDirection(), 2);

		if(BlockAPI.isProtected(block.getLocation()) && event.isSticky())
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void divineBlockExplode(final EntityExplodeEvent event) // TODO: Clean up and make it generic to Protected Blocks
	{
		final List<Block> savedBlocks = new ArrayList<Block>();
		final List<Material> savedMaterials = new ArrayList<Material>();
		final List<Byte> savedBytes = new ArrayList<Byte>();

		List<Block> blocks = event.blockList();
		for(Block block : blocks)
		{
			if(block.getType() == Material.TNT) continue;
			if(BlockAPI.isProtected(block.getLocation()))
			{
				savedBlocks.add(block);
				savedMaterials.add(block.getType());
				savedBytes.add(block.getData());
			}
		}

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Demigods.plugin, new Runnable()
		{
			@Override
			public void run()
			{
				// Regenerate blocks
				int i = 0;
				for(Block block : savedBlocks)
				{
					block.setTypeIdAndData(savedMaterials.get(i).getId(), savedBytes.get(i), true);
					i++;
				}

				// Remove all drops from explosion zone
				for(Item drop : event.getLocation().getWorld().getEntitiesByClass(Item.class))
				{
					Location location = drop.getLocation();
					if(ZoneAPI.zoneAltar(location) != null)
					{
						drop.remove();
						continue;
					}

					if(ZoneAPI.zoneShrine(location) != null)
					{
						drop.remove();
					}
				}
			}
		}, 1);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void stopDestroyEnderCrystal(EntityDamageEvent event)
	{
		if(BlockAPI.isProtected(event.getEntity().getLocation().subtract(0.5, 1.0, 0.5)))
		{
			event.setDamage(0);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void demigodsAdminWand(PlayerInteractEvent event)
	{
		if(event.getClickedBlock() == null) return;

		// Define variables
		Block clickedBlock = event.getClickedBlock();
		Location location = clickedBlock.getLocation();
		Player player = event.getPlayer();

		// Return if the player does not qualify for use of the admin wand
		if(!AdminAPI.useWand(player)) return;

		if(clickedBlock.getType().equals(Material.EMERALD_BLOCK))
		{
			AltarCreateEvent altarEvent = new AltarCreateEvent(location, AltarCreateCause.ADMIN_WAND);
			Bukkit.getServer().getPluginManager().callEvent(altarEvent);

			player.sendMessage(ChatColor.GRAY + "Generating new Altar...");
			BlockFactory.createAltar(location); // TODO Should this be here?
			player.sendMessage(ChatColor.GREEN + "Altar created!");
		}

		if(BlockAPI.isAltar(location))
		{
			// TODO TIMED DATA
			// if(DemigodsData.timedAltarData.contains(player))
			// {
			// AltarRemoveEvent altarRemoveEvent = new AltarRemoveEvent(location, AltarRemoveEvent.AltarRemoveCause.ADMIN_WAND);
			// Bukkit.getServer().getPluginManager().callEvent(altarRemoveEvent);
			// if(altarRemoveEvent.isCancelled()) return;
			//
			// // We can destroy the Altar
			// BlockAPI.getAltar(location).remove();
			// DemigodsData.timedAltarData.remove(player);
			//
			// // Save Protected Blocks
			// DemigodsData.altarFile.save(DemigodsData.altarData);
			//
			// player.sendMessage(ChatColor.GREEN + "Altar removed!");
			// }
			// else
			// {
			// DemigodsData.timedAltarData.add(player, System.currentTimeMillis() + 5000);
			// player.sendMessage(ChatColor.RED + "Right-click this Altar again to remove it.");
			// }
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void divineBlockAlerts(PlayerMoveEvent event)
	{
		if(event.getFrom().distance(event.getTo()) < 0.1) return;

		// Define variables
		Player player = event.getPlayer();
		Location to = event.getTo();
		Location from = event.getFrom();

		/**
		 * Entering Altar
		 */
		if(ZoneAPI.enterZoneAltar(to, from) && !LocationAPI.hasWarp(ZoneAPI.zoneAltar(to), TrackedPlayer.getTracked(player).getCurrent())) // TODO This is an annoying message.
		{
			player.sendMessage(ChatColor.GRAY + "You've never set a warp at this Altar.");
			return;
		}
	}
}
