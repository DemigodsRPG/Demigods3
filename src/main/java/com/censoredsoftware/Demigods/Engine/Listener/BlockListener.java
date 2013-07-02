package com.censoredsoftware.Demigods.Engine.Listener;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.General.DemigodsBlock;
import com.censoredsoftware.Demigods.Engine.Object.General.DemigodsLocation;
import com.censoredsoftware.Demigods.Engine.Object.General.DemigodsPlayer;
import com.censoredsoftware.Demigods.Engine.Object.PlayerCharacter.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.Object.Structure.Old.Altar;
import com.censoredsoftware.Demigods.Engine.Object.Structure.Old.Shrine;
import com.censoredsoftware.Demigods.Engine.Object.Structure.Old.StructureFactory;
import com.censoredsoftware.Demigods.Engine.Utility.AdminUtility;
import com.censoredsoftware.Demigods.Engine.Utility.DataUtility;
import com.censoredsoftware.Demigods.Engine.Utility.TextUtility;
import com.censoredsoftware.Demigods.Engine.Utility.ZoneUtility;
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
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

public class BlockListener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event)
	{
		Location location = event.getBlock().getLocation();
		if(DemigodsBlock.isProtected(location))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.YELLOW + Demigods.text.getText(TextUtility.Text.PROTECTED_BLOCK));
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockIgnite(BlockIgniteEvent event)
	{
		Location location = event.getBlock().getLocation();
		if(DemigodsBlock.isProtected(location)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockDamage(BlockDamageEvent event)
	{
		Location location = event.getBlock().getLocation();
		if(DemigodsBlock.isProtected(location)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPistonExtend(BlockPistonExtendEvent event)
	{
		List<Block> blocks = event.getBlocks();

		for(Block block : blocks)
		{
			Location location = block.getLocation();

			if(DemigodsBlock.isProtected(location))
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

		if(DemigodsBlock.isProtected(block.getLocation()) && event.isSticky())
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityExplode(final EntityExplodeEvent event)
	{
		final Location location = event.getLocation();
		if(ZoneUtility.zoneAltar(location) == null && ZoneUtility.zoneShrine(location) == null) return;

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Demigods.plugin, new Runnable()
		{
			@Override
			public void run()
			{
				// Remove all drops from explosion zone
				for(Item drop : event.getLocation().getWorld().getEntitiesByClass(Item.class))
				{
					Location dropLocation = drop.getLocation();
					if(ZoneUtility.zoneAltar(dropLocation) != null)
					{
						drop.remove();
						continue;
					}
					if(ZoneUtility.zoneShrine(dropLocation) != null) drop.remove();
				}
			}
		}, 1);

		if(DataUtility.hasTimed("explode", "structure")) return;
		DataUtility.saveTimed("explode", "structure", true, 3);

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Demigods.plugin, new Runnable()
		{
			@Override
			public void run()
			{
				if(ZoneUtility.zoneAltar(location) != null) Altar.generate(ZoneUtility.zoneAltar(location), ZoneUtility.zoneAltar(location).getLocation());
				if(ZoneUtility.zoneShrine(location) != null) Shrine.generate(ZoneUtility.zoneShrine(location), ZoneUtility.zoneShrine(location).getLocation());
			}
		}, 30);
	}

	/*
	 * @EventHandler(priority = EventPriority.HIGHEST)
	 * public void stopDestroyEnderCrystal(EntityDamageEvent callAbilityEvent)
	 * {
	 * try
	 * {
	 * if(DemigodsBlock.isProtected(callAbilityEvent.getEntity().getLocation().subtract(0.5, 1.0, 0.5)))
	 * {
	 * callAbilityEvent.setDamage(0);
	 * callAbilityEvent.setCancelled(true);
	 * }
	 * }
	 * catch(Exception ignored)
	 * {}
	 * }
	 */

	@EventHandler(priority = EventPriority.HIGH)
	public void demigodsAdminWand(PlayerInteractEvent event)
	{
		if(event.getClickedBlock() == null) return;

		// Define variables
		Block clickedBlock = event.getClickedBlock();
		Location location = clickedBlock.getLocation();
		Player player = event.getPlayer();

		/**
		 * Handle Altars
		 */
		if(AdminUtility.useWand(player) && clickedBlock.getType().equals(Material.EMERALD_BLOCK))
		{
			event.setCancelled(true);

			AdminUtility.sendDebug(ChatColor.RED + "Altar generated by " + "ADMIN WAND" + " at " + ChatColor.GRAY + "(" + location.getWorld().getName() + ") " + location.getX() + ", " + location.getY() + ", " + location.getZ());

			player.sendMessage(ChatColor.GRAY + Demigods.text.getText(TextUtility.Text.ADMIN_WAND_GENERATE_ALTAR));
			StructureFactory.createAltar(location);
			player.sendMessage(ChatColor.GREEN + Demigods.text.getText(TextUtility.Text.ADMIN_WAND_GENERATE_ALTAR_COMPLETE));
		}

		if(AdminUtility.useWand(player) && Altar.isAltar(location))
		{
			event.setCancelled(true);

			Altar altar = Altar.getAltar(location);

			if(DataUtility.hasTimed(player.getName(), "destroy_altar"))
			{
				AdminUtility.sendDebug(ChatColor.RED + "Altar at " + ChatColor.GRAY + "(" + location.getWorld().getName() + ") " + location.getX() + ", " + location.getY() + ", " + location.getZ() + " removed by " + "ADMIN WAND" + ".");

				// Remove the Altar
				altar.remove();
				DataUtility.removeTimed(player.getName(), "destroy_altar");

				player.sendMessage(ChatColor.GREEN + Demigods.text.getText(TextUtility.Text.ADMIN_WAND_REMOVE_ALTAR_COMPLETE));
			}
			else
			{
				DataUtility.saveTimed(player.getName(), "destroy_altar", true, 5);
				player.sendMessage(ChatColor.RED + Demigods.text.getText(TextUtility.Text.ADMIN_WAND_REMOVE_ALTAR));
			}
		}

		/**
		 * Handle Shrines
		 */
		if(DemigodsPlayer.isImmortal(player))
		{
			PlayerCharacter character = DemigodsPlayer.getPlayer(player).getCurrent();

			if(event.getAction() == Action.RIGHT_CLICK_BLOCK && character.getDeity().getInfo().getClaimItems().contains(event.getPlayer().getItemInHand().getType()) && Shrine.validBlockConfiguration(event.getClickedBlock()))
			{
				try
				{
					// Shrine created!
					AdminUtility.sendDebug(ChatColor.RED + "Shrine created by " + character.getName() + " (" + character.getDeity() + ") at: " + ChatColor.GRAY + "(" + location.getWorld().getName() + ") " + location.getX() + ", " + location.getY() + ", " + location.getZ());

					StructureFactory.createShrine(character, location);
					location.getWorld().strikeLightningEffect(location);

					player.sendMessage(ChatColor.GRAY + Demigods.text.getText(TextUtility.Text.CREATE_SHRINE_1).replace("{alliance}", "" + ChatColor.YELLOW + character.getAlliance() + "s" + ChatColor.GRAY));
					player.sendMessage(ChatColor.GRAY + Demigods.text.getText(TextUtility.Text.CREATE_SHRINE_2).replace("{deity}", "" + ChatColor.YELLOW + character.getDeity().getInfo().getName() + ChatColor.GRAY));
				}
				catch(Exception e)
				{
					// Creation of shrine failed...
					e.printStackTrace();
				}
			}
		}

		if(AdminUtility.useWand(player) && Shrine.isShrine(location))
		{
			event.setCancelled(true);

			Shrine shrine = Shrine.getShrine(location);
			PlayerCharacter owner = shrine.getCharacter();

			if(DataUtility.hasTimed(player.getName(), "destroy_shrine"))
			{
				// Remove the Shrine
				shrine.remove();
				DataUtility.removeTimed(player.getName(), "destroy_shrine");

				AdminUtility.sendDebug(ChatColor.RED + "Shrine of (" + owner.getDeity() + ") at: " + ChatColor.GRAY + "(" + location.getWorld().getName() + ") " + location.getX() + ", " + location.getY() + ", " + location.getZ() + " removed.");

				player.sendMessage(ChatColor.GREEN + Demigods.text.getText(TextUtility.Text.ADMIN_WAND_REMOVE_SHRINE_COMPLETE));
			}
			else
			{
				DataUtility.saveTimed(player.getName(), "destroy_shrine", true, 5);
				player.sendMessage(ChatColor.RED + Demigods.text.getText(TextUtility.Text.ADMIN_WAND_REMOVE_SHRINE));
			}
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
		if(ZoneUtility.enterZoneAltar(to, from) && !DemigodsLocation.hasWarp(ZoneUtility.zoneAltar(to), DemigodsPlayer.getPlayer(player).getCurrent())) // TODO This is an annoying message.
		{
			// player.sendMessage(ChatColor.GRAY + Demigods.text.getText(TextUtility.Text.NO_WARP_ALTAR));
		}
	}
}
