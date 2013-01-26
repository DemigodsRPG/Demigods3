package com.legit2.Demigods.Listeners;

import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.legit2.Demigods.DConfig;
import com.legit2.Demigods.DDivineBlocks;
import com.legit2.Demigods.Demigods;
import com.legit2.Demigods.DTributeValue;
import com.legit2.Demigods.Utilities.DCharUtil;
import com.legit2.Demigods.Utilities.DDataUtil;
import com.legit2.Demigods.Utilities.DPlayerUtil;
import com.legit2.Demigods.Utilities.DMiscUtil;

public class DDivineBlockListener implements Listener
{
	static Demigods plugin;
	public static double FAVORMULTIPLIER = DConfig.getSettingDouble("globalfavormultiplier");
	public static int RADIUS = 8;
	
	public DDivineBlockListener(Demigods instance)
	{
		plugin = instance;
	}
	
	@EventHandler (priority = EventPriority.HIGH)
	public static void destroyDivineBlock(BlockBreakEvent event)
	{
		try
		{
			for(Location center : DDivineBlocks.getAllDivineBlocks())
			{
				if(event.getBlock().getLocation().equals(center))
				{
					event.getPlayer().sendMessage(ChatColor.YELLOW + "DivineBlocks cannot be broken by hand.");
					event.setCancelled(true);
					return;
				}
			}
		}
		catch (Exception e) {}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void stopDivineBlockDamage(BlockDamageEvent event)
	{
		try
		{
			for(Location center : DDivineBlocks.getAllDivineBlocks())
			{
				if(event.getBlock().getLocation().equals(center))
				{
					event.setCancelled(true);
				}
			}
		}
		catch (Exception e) {}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void stopDivineBlockIgnite(BlockIgniteEvent event)
	{
		try
		{
			for(Location center : DDivineBlocks.getAllDivineBlocks())
			{
				if(event.getBlock().getLocation().equals(center))
				{
					event.setCancelled(true);
				}
			}
		}
		catch (Exception e) {}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void stopDivineBlockBurn(BlockBurnEvent event)
	{
		try
		{
			for(Location center : DDivineBlocks.getAllDivineBlocks())
			{
				if(event.getBlock().getLocation().equals(center))
				{
					event.setCancelled(true);
				}
			}
		}
		catch (Exception e) {}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void stopDivineBlockPistonExtend(BlockPistonExtendEvent event)
	{
		List<Block> blocks = event.getBlocks();
		
		CHECKBLOCKS:
		for(Block block : blocks)
		{
			try
			{
				for(Location center : DDivineBlocks.getAllDivineBlocks())
				{
					if(block.getLocation().equals(center))
					{
						event.setCancelled(true);
						break CHECKBLOCKS;
					}
				}
			}
			catch (Exception e) {}
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void stopDivineBlockPistonRetract(BlockPistonRetractEvent event)
	{
		// Define variables
		final Block block = event.getBlock().getRelative(event.getDirection(), 2);
		
		try
		{
			for(Location divineBlock : DDivineBlocks.getAllDivineBlocks())
			{
				if(block.getLocation().equals((divineBlock)) && event.isSticky())
				{
					event.setCancelled(true);
				}
			}
		}
		catch (Exception e) {}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void divineBlockExplode(final EntityExplodeEvent event)
	{
		try
		{
			// Remove divineBlock blocks from explosions
			Iterator<Block> i = event.blockList().iterator();
			while(i.hasNext())
			{
				Block block = i.next();
				if(!DMiscUtil.canLocationPVP(block.getLocation())) i.remove();
				for(Location center : DDivineBlocks.getAllDivineBlocks())
				{
					if(block.getLocation().equals(center)) i.remove();
				}
			}
		} 
		catch (Exception er) {}
	}
	
	@EventHandler (priority = EventPriority.HIGH)
	public void divineBlockAlerts(PlayerMoveEvent event)
	{
		if(event.getFrom().distance(event.getTo()) < 0.1) return;
		for(int charID : DMiscUtil.getImmortalList())
		{
			try
			{
				if(DDivineBlocks.getShrines(charID) != null)
				{
					// Define variables
					OfflinePlayer charOwner = DCharUtil.getOwner(charID);
					for(Location center : DDivineBlocks.getShrines(charID))
					{
						// Check for world errors
						if(!center.getWorld().equals(event.getPlayer().getWorld())) return;
						if(event.getFrom().getWorld() != center.getWorld()) return;
						
						/*
						 * Outside coming in
						 */
						if(event.getFrom().distance(center) > RADIUS)
						{
							if(center.distance(event.getTo()) <= RADIUS)
							{
								event.getPlayer().sendMessage(ChatColor.GRAY + "You have entered " + charOwner.getName() + "'s divineBlock to " + ChatColor.YELLOW + DDivineBlocks.getDeityAtShrine(center) + ChatColor.GRAY + ".");
								return;
							}
						}
						
						/*
						 * Leaving
						 */
						else if(event.getFrom().distance(center) <= RADIUS)
						{
							if(center.distance(event.getTo()) > RADIUS)
							{
								event.getPlayer().sendMessage(ChatColor.GRAY + "You have left a divineBlock.");
								return;
							}
						}
					}
				}
			} catch(Exception e){}
		}
	}
	
	@EventHandler (priority = EventPriority.HIGH)
	public void playerTribute(PlayerInteractEvent event)
	{
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if(event.getClickedBlock().getType() != Material.GOLD_BLOCK) return;
		if(event.getPlayer().getItemInHand().getType() != Material.BOOK) return;
		else event.getPlayer().sendMessage("You right-clicked a block of gold with a book!");
		
		if(!DCharUtil.isImmortal(event.getPlayer())) return;
		
		try
		{
			// Check if block is divine
			String deityName = DDivineBlocks.getDeityAtShrine(event.getClickedBlock().getLocation());
			
			if(deityName == null) return;
			
			// Check if character has deity
			Player player = event.getPlayer();
			int charID = DPlayerUtil.getCurrentChar(player);
			
			if(DCharUtil.hasDeity(charID, deityName))
			{
				// Open the tribute inventory
				Inventory ii = DMiscUtil.getPlugin().getServer().createInventory(player, 27, "Tributes");
				player.openInventory(ii);
				DDataUtil.saveCharData(charID, "tributing_temp", DDivineBlocks.getOwnerOfShrine(event.getClickedBlock().getLocation()));
				event.setCancelled(true);
				return;
			}
			player.sendMessage(ChatColor.YELLOW + "You must be allied to " + deityName + " in order to tribute here.");
		}
		catch (Exception er) {}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void tributeSuccess(InventoryCloseEvent event)
	{
		try
		{
			if(!(event.getPlayer() instanceof Player)) return;
			Player player = (Player)event.getPlayer();
			int charID = DPlayerUtil.getCurrentChar(player);
			
			if(!DCharUtil.isImmortal(player)) return;
			
			// If it isn't a tribute chest then break the method
			if(!event.getInventory().getName().equals("Tributes")) return;
			
			// Get the creator of the shrine
			//int shrineCreator = DDivineBlocks.getOwnerOfShrine((Location) DDataUtil.getCharData(charID, "tributing_temp"));
			DDataUtil.removeCharData(charID, "temp_tributing"); 
			
			//calculate value of chest
			int tirbuteValue = 0;
			int items = 0;
			for(ItemStack ii : event.getInventory().getContents())
			{
				if(ii != null)
				{
					tirbuteValue += DTributeValue.getTributeValue(ii);
					items ++;
				}
			}
			
			tirbuteValue *= FAVORMULTIPLIER;
			
			// Give devotion
			int devotionBefore = DCharUtil.getDevotion(charID);
			DCharUtil.giveDevotion(charID, tirbuteValue);
			DCharUtil.giveDevotion(charID, tirbuteValue / 7);
			
			// Give favor
			int favorBefore = DCharUtil.getMaxFavor(charID);
			//DUtil.setFavorCap(player, DUtil.getFavorCap(username)+value/5); TODO
			
			// Devotion lock TODO
			String charName = DCharUtil.getName(charID);
			if(devotionBefore < DCharUtil.getDevotion(charID)) player.sendMessage(ChatColor.YELLOW + "Your devotion to " + charName + " has increased to " + DCharUtil.getDevotion(charID) + ".");
			if(favorBefore < DCharUtil.getMaxFavor(charID)) player.sendMessage(ChatColor.YELLOW + "Your favor cap has increased to " + DCharUtil.getMaxFavor(charID) + ".");
			if((favorBefore == DCharUtil.getMaxFavor(charID)) && (devotionBefore == DCharUtil.getDevotion(charID)) && (items > 0)) player.sendMessage(ChatColor.YELLOW + "Your tributes were insufficient for " + charName + "'s blessings.");
			
			// Clear the tribute case
			event.getInventory().clear();
		}
		catch (Exception er) {}
	}
}
