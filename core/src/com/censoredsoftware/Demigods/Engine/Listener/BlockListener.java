package com.censoredsoftware.Demigods.Engine.Listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.*;
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
import org.bukkit.inventory.ItemStack;

import com.censoredsoftware.Demigods.Engine.Block.Altar;
import com.censoredsoftware.Demigods.Engine.Block.BlockFactory;
import com.censoredsoftware.Demigods.Engine.Block.Shrine;
import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.DemigodsData;
import com.censoredsoftware.Demigods.Engine.Event.Altar.AltarCreateEvent;
import com.censoredsoftware.Demigods.Engine.Event.Altar.AltarCreateEvent.AltarCreateCause;
import com.censoredsoftware.Demigods.Engine.Event.Altar.AltarRemoveEvent;
import com.censoredsoftware.Demigods.Engine.Event.Shrine.ShrineCreateEvent;
import com.censoredsoftware.Demigods.Engine.Event.Shrine.ShrineRemoveEvent;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedBlock;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedLocation;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedPlayer;
import com.censoredsoftware.Demigods.Engine.Utility.AdminUlility;
import com.censoredsoftware.Demigods.Engine.Utility.ZoneUtility;

public class BlockListener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event)
	{
		Location location = event.getBlock().getLocation();
		if(TrackedBlock.isProtected(location))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.YELLOW + "That block is protected by a Deity!");
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockIgnite(BlockIgniteEvent event)
	{
		Location location = event.getBlock().getLocation();
		if(TrackedBlock.isProtected(location)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockDamage(BlockDamageEvent event)
	{
		Location location = event.getBlock().getLocation();
		if(TrackedBlock.isProtected(location)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPistonExtend(BlockPistonExtendEvent event)
	{
		List<Block> blocks = event.getBlocks();

		for(Block block : blocks)
		{
			Location location = block.getLocation();

			if(TrackedBlock.isProtected(location))
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

		if(TrackedBlock.isProtected(block.getLocation()) && event.isSticky())
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void divineBlockExplode(final EntityExplodeEvent event) // TODO: Clean up and make it generic to Protected Blocks
	{
		final List<Location> savedLocations = new ArrayList<Location>();
		final List<Material> savedMaterials = new ArrayList<Material>();
		final List<Byte> savedBytes = new ArrayList<Byte>();

		List<Block> blocks = event.blockList();
		for(Block block : blocks)
		{
			if(block.getType() == Material.TNT) continue;
			if(TrackedBlock.isProtected(block.getLocation()))
			{
				savedLocations.add(block.getLocation());
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
				for(Location location : savedLocations)
				{
					location.getBlock().setTypeIdAndData(savedMaterials.get(i).getId(), savedBytes.get(i), true);
					i++;
				}

				// Remove all drops from explosion zone
				for(Item drop : event.getLocation().getWorld().getEntitiesByClass(Item.class))
				{
					Location location = drop.getLocation();
					if(ZoneUtility.zoneAltar(location) != null)
					{
						drop.remove();
						continue;
					}

					if(ZoneUtility.zoneShrine(location) != null)
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
		try
		{
			if(TrackedBlock.isProtected(event.getEntity().getLocation().subtract(0.5, 1.0, 0.5)))
			{
				event.setDamage(0);
				event.setCancelled(true);
			}
		}
		catch(Exception ignored)
		{}
	}

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
		if(AdminUlility.useWand(player) && clickedBlock.getType().equals(Material.EMERALD_BLOCK))
		{
			event.setCancelled(true);

			AltarCreateEvent altarEvent = new AltarCreateEvent(location, AltarCreateCause.ADMIN_WAND);
			Bukkit.getServer().getPluginManager().callEvent(altarEvent);

			player.sendMessage(ChatColor.GRAY + "Generating new Altar...");
			BlockFactory.createAltar(location);
			player.sendMessage(ChatColor.GREEN + "Altar created!");
		}

		if(AdminUlility.useWand(player) && Altar.isAltar(location))
		{
			event.setCancelled(true);

			Altar altar = Altar.getAltar(location);

			if(DemigodsData.hasTimed(player.getName(), "destroy_altar"))
			{
				AltarRemoveEvent altarRemoveEvent = new AltarRemoveEvent(location, AltarRemoveEvent.AltarRemoveCause.ADMIN_WAND);
				Bukkit.getServer().getPluginManager().callEvent(altarRemoveEvent);
				if(altarRemoveEvent.isCancelled()) return;

				// Remove the Altar
				altar.remove();
				DemigodsData.removeTimed(player.getName(), "destroy_altar");

				player.sendMessage(ChatColor.GREEN + "Altar removed!");
			}
			else
			{
				DemigodsData.saveTimed(player.getName(), "destroy_altar", true, 5);
				player.sendMessage(ChatColor.RED + "Right-click this Altar again to remove it.");
			}
		}

		/**
		 * Handle Shrines
		 */
		if(TrackedPlayer.isImmortal(player))
		{
			PlayerCharacter character = TrackedPlayer.getTracked(player).getCurrent();

			if(event.getAction() == Action.RIGHT_CLICK_BLOCK && character.getDeity().getInfo().getClaimItems().contains(event.getPlayer().getItemInHand().getType()) && Shrine.validBlockConfiguration(event.getClickedBlock()))
			{
				try
				{
					// Shrine created!
					ShrineCreateEvent shrineCreateEvent = new ShrineCreateEvent(character, location);
					Bukkit.getServer().getPluginManager().callEvent(shrineCreateEvent);
					if(shrineCreateEvent.isCancelled()) return;

					BlockFactory.createShrine(character, location);
					location.getWorld().strikeLightningEffect(location);

					if(!player.getGameMode().equals(GameMode.CREATIVE))
					{
						if(player.getItemInHand().getAmount() > 1)
						{
							ItemStack books = new ItemStack(player.getItemInHand().getType(), player.getInventory().getItemInHand().getAmount() - 1);
							player.setItemInHand(books);
						}
						else
						{
							player.getInventory().remove(Material.BOOK);
						}
					}

					player.sendMessage(ChatColor.GRAY + "The " + ChatColor.YELLOW + character.getAlliance() + "s" + ChatColor.GRAY + " are pleased...");
					player.sendMessage(ChatColor.GRAY + "You have created a Shrine in the name of " + ChatColor.YELLOW + character.getDeity().getInfo().getName() + ChatColor.GRAY + "!");
				}
				catch(Exception e)
				{
					// Creation of shrine failed...
					e.printStackTrace();
				}
			}
		}

		if(AdminUlility.useWand(player) && Shrine.isShrine(location))
		{
			event.setCancelled(true);

			Shrine shrine = Shrine.getShrine(location);

			if(DemigodsData.hasTimed(player.getName(), "destroy_shrine"))
			{
				ShrineRemoveEvent shrineRemoveEvent = new ShrineRemoveEvent(shrine.getCharacter(), location);
				Bukkit.getServer().getPluginManager().callEvent(shrineRemoveEvent);
				if(shrineRemoveEvent.isCancelled()) return;

				// Remove the Shrine
				shrine.remove();
				DemigodsData.removeTimed(player.getName(), "destroy_shrine");

				player.sendMessage(ChatColor.GREEN + "Shrine removed!");
				return;
			}
			else
			{
				DemigodsData.saveTimed(player.getName(), "destroy_shrine", true, 5);
				player.sendMessage(ChatColor.RED + "Right-click this Shrine again to remove it.");
				return;
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
		if(ZoneUtility.enterZoneAltar(to, from) && !TrackedLocation.hasWarp(ZoneUtility.zoneAltar(to), TrackedPlayer.getTracked(player).getCurrent())) // TODO This is an annoying message.
		{
			// player.sendMessage(ChatColor.GRAY + "You've never set a warp at this Altar.");
			return;
		}
	}
}
