package com.legit2.Demigods.Listeners;

import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
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
import com.legit2.Demigods.DSave;
import com.legit2.Demigods.DUtil;
import com.legit2.Demigods.Demigods;
import com.legit2.Demigods.DTributeValue;

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
		for(Location center : DDivineBlocks.getAllDivineBlocks())
		{
			if(event.getBlock().getLocation().equals(center))
			{
				event.getPlayer().sendMessage(ChatColor.YELLOW+"DivineBlocks cannot be broken by hand.");
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void stopDivineBlockDamage(BlockDamageEvent event)
	{
		for(Location center : DDivineBlocks.getAllDivineBlocks())
		{
			if(event.getBlock().getLocation().equals(center))
			{
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void stopDivineBlockIgnite(BlockIgniteEvent event)
	{
		for(Location center : DDivineBlocks.getAllDivineBlocks())
		{
			if(event.getBlock().getLocation().equals(center))
			{
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void stopDivineBlockBurn(BlockBurnEvent event)
	{
		for(Location center : DDivineBlocks.getAllDivineBlocks())
		{
			if(event.getBlock().getLocation().equals(center))
			{
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void stopDivineBlockPistonExtend(BlockPistonExtendEvent event)
	{
		List<Block> blocks = event.getBlocks();
		
		CHECKBLOCKS:
		for(Block block : blocks)
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
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void stopDivineBlockPistonRetract(BlockPistonRetractEvent event)
	{
		// Define variables
		final Block block = event.getBlock().getRelative(event.getDirection(), 2);
		
		for(Location divineBlock : DDivineBlocks.getAllDivineBlocks())
		{
			if(block.getLocation().equals((divineBlock)) && event.isSticky())
			{
				event.setCancelled(true);
			}
		}
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
				if(!DUtil.canPVP(block.getLocation())) i.remove();
				for(Location center : DDivineBlocks.getAllDivineBlocks())
				{
					if(block.getLocation().equals(center)) i.remove();
				}
			}
		} 
		catch (Exception er) {}
	}
	
	@EventHandler (priority = EventPriority.HIGH)
	public void playerTribute(PlayerInteractEvent event)
	{
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if(event.getClickedBlock().getType() != Material.GOLD_BLOCK) return;
		if(!DUtil.isImmortal(event.getPlayer().getName())) return;
		
		//check if block is divineBlock
		String deityName = DDivineBlocks.getDeityAtShrine(event.getClickedBlock().getLocation());
		
		if(deityName == null) return;
		
		//check if player has deity
		Player player = event.getPlayer();
		if(DUtil.hasDeity(player.getName(), deityName))
		{
			//open the tribute inventory
			Inventory ii = DUtil.getPlugin().getServer().createInventory(player, 27, "Tributes");
			player.openInventory(ii);
			DSave.saveDeityData(player.getName(), deityName, "tributing_temp", DDivineBlocks.getOwnerOfShrine(event.getClickedBlock().getLocation()));
			event.setCancelled(true);
			return;
		}
		player.sendMessage(ChatColor.YELLOW+"You must be allianced to " + deityName + " in order to tribute here.");
	}
	
	@EventHandler (priority = EventPriority.HIGH)
	public void divineBlockAlerts(PlayerMoveEvent event)
	{
		if(event.getFrom().distance(event.getTo()) < 0.1) return;
		for(String player : DUtil.getImmortalList())
		{
			if(DDivineBlocks.getShrines(player) != null)
				for(Location center : DDivineBlocks.getShrines(player))
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
							event.getPlayer().sendMessage(ChatColor.GRAY+"You have entered "+player+"'s divineBlock to "+ChatColor.YELLOW+DDivineBlocks.getDeityAtShrine(center)+ChatColor.GRAY+".");
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
							event.getPlayer().sendMessage(ChatColor.GRAY+"You have left a divineBlock.");
							return;
						}
					}
				}
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void tributeSuccess(InventoryCloseEvent event)
	{
		if(!(event.getPlayer() instanceof Player)) return;
		Player player = (Player)event.getPlayer();
		String username = player.getName();
		
		if(!DUtil.isImmortal(username)) return;
		
		//continue if tribute chest
		if(!event.getInventory().getName().equals("Tributes")) return;
		
		//get which deity tribute goes to
		String toGive = null;
		for(String immortalPlayer : DUtil.getImmortalList())
		{
			for(String deity : DUtil.getDeities(immortalPlayer))
			{
				if(DSave.hasDeityData(immortalPlayer, deity, "tributing_temp"))
				{
					toGive = deity;
					break;
				}
			}
		}
		
		if(toGive == null) return;
		
		String creator = DDivineBlocks.getOwnerOfShrine((Location) DUtil.getDeityData(username, toGive, "tributing_temp")); //get the creator of the shrine
		DSave.removeDeityData(username, toGive, "tributing_temp"); 
		
		//calculate value of chest
		int value = 0;
		int items = 0;
		for(ItemStack ii : event.getInventory().getContents())
		{
			if(ii != null)
			{
				value += DTributeValue.getTributeValue(ii);
				items ++;
			}
		}
		
		value *= FAVORMULTIPLIER;
		
		//give devotion
		int devotionBefore = DUtil.getDevotion(username, toGive);
		DUtil.setDevotion(username, toGive, DUtil.getDevotion(username, toGive) + value);
		DUtil.setDevotion(creator, toGive, DUtil.getDevotion(creator, toGive) + value / 7);
		
		//give favor
		int favorBefore = DUtil.getFavorCap(username);
		//DUtil.setFavorCap(player, DUtil.getFavorCap(username)+value/5); TODO
		
		//devotion lock TODO
		if(devotionBefore < DUtil.getDevotion(username, toGive)) player.sendMessage(ChatColor.YELLOW + "Your Devotion for " + toGive + " has increased to " + DUtil.getDevotion(username, toGive) + ".");
		if(favorBefore < DUtil.getFavorCap(username)) player.sendMessage(ChatColor.YELLOW + "Your Favor Cap has increased to " + DUtil.getFavorCap(username) + ".");
		if((favorBefore == DUtil.getFavorCap(username)) && (devotionBefore == DUtil.getDevotion(username, toGive)) && (items > 0)) player.sendMessage(ChatColor.YELLOW + "Your tributes were insufficient for " + toGive + "'s blessings.");
		
		//clear inventory
		event.getInventory().clear();
	}
}
