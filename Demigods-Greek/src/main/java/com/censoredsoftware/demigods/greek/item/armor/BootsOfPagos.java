package com.censoredsoftware.demigods.greek.item.armor;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import com.censoredsoftware.censoredlib.util.Items;
import com.censoredsoftware.demigods.engine.item.DivineItem;
import com.censoredsoftware.demigods.engine.util.Zones;

public class BootsOfPagos
{
	public final static String name = "Boots of Pagos";
	public final static String description = "Boots this cold have their advantages... and disadvantages.";
	public final static DivineItem.Category category = DivineItem.Category.ARMOR;
	public final static ItemStack item = Items.create(Material.IRON_BOOTS, ChatColor.AQUA + name, new ArrayList<String>()
	{
		{
			add(ChatColor.BLUE + "" + ChatColor.ITALIC + description);
		}
	}, null);
	public final static Recipe recipe = new ShapedRecipe(item)
	{
		{
			shape("AAA", "ABA", "AAA");
			setIngredient('A', Material.PACKED_ICE);
			setIngredient('B', Material.IRON_BOOTS);
		}
	};
	public final static Listener listener = new Listener()
	{
		@EventHandler(priority = EventPriority.NORMAL)
		private void onPlayerMove(PlayerMoveEvent event)
		{
			if(Zones.inNoDemigodsZone(event.getPlayer().getLocation())) return;

			// Define variables
			Player player = event.getPlayer();

			if(player.getInventory().getBoots() != null && Items.areEqualIgnoreEnchantments(item, player.getInventory().getBoots()))
			{
				Location location = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation();

				if(location.getBlock().getType().equals(Material.WATER) || location.getBlock().getType().equals(Material.STATIONARY_WATER) && player.getLocation().getBlock().getRelative(BlockFace.NORTH).getType().equals(Material.AIR) && player.getLocation().getBlock().getRelative(BlockFace.SOUTH).getType().equals(Material.AIR) && player.getLocation().getBlock().getRelative(BlockFace.EAST).getType().equals(Material.AIR) && player.getLocation().getBlock().getRelative(BlockFace.WEST).getType().equals(Material.AIR) && player.getLocation().getBlock().getRelative(BlockFace.NORTH_EAST).getType().equals(Material.AIR) && player.getLocation().getBlock().getRelative(BlockFace.NORTH_WEST).getType().equals(Material.AIR) && player.getLocation().getBlock().getRelative(BlockFace.SOUTH_EAST).getType().equals(Material.AIR) && player.getLocation().getBlock().getRelative(BlockFace.SOUTH_WEST).getType().equals(Material.AIR))
				{
					Location loc1 = location.getBlock().getRelative(BlockFace.NORTH).getLocation();
					Location loc2 = location.getBlock().getRelative(BlockFace.SOUTH).getLocation();
					Location loc3 = location.getBlock().getRelative(BlockFace.EAST).getLocation();
					Location loc4 = location.getBlock().getRelative(BlockFace.WEST).getLocation();

					player.sendBlockChange(location, Material.ICE, (byte) 0);
					if(loc1.getBlock().isLiquid()) player.sendBlockChange(loc1, Material.ICE, (byte) 0);
					if(loc2.getBlock().isLiquid()) player.sendBlockChange(loc2, Material.ICE, (byte) 0);
					if(loc3.getBlock().isLiquid()) player.sendBlockChange(loc3, Material.ICE, (byte) 0);
					if(loc4.getBlock().isLiquid()) player.sendBlockChange(loc4, Material.ICE, (byte) 0);

					for(Entity entity : player.getNearbyEntities(30, 30, 30))
					{
						if(entity instanceof Player)
						{
							((Player) entity).sendBlockChange(location, Material.ICE, (byte) 0);
							if(loc1.getBlock().isLiquid()) ((Player) entity).sendBlockChange(loc1, Material.ICE, (byte) 0);
							if(loc2.getBlock().isLiquid()) ((Player) entity).sendBlockChange(loc2, Material.ICE, (byte) 0);
							if(loc3.getBlock().isLiquid()) ((Player) entity).sendBlockChange(loc3, Material.ICE, (byte) 0);
							if(loc4.getBlock().isLiquid()) ((Player) entity).sendBlockChange(loc4, Material.ICE, (byte) 0);
						}
					}
				}
				else if(!location.getBlock().isLiquid() && location.getBlock().getType().isSolid() && location.getBlock().getType() != Material.ICE && location.getBlock().getType() != Material.PACKED_ICE && location.getBlock().getRelative(BlockFace.UP).getType().equals(Material.AIR))
				{
					player.sendBlockChange(location.getBlock().getRelative(BlockFace.UP).getLocation(), Material.SNOW, (byte) 0);

					for(Entity entity : player.getNearbyEntities(30, 30, 30))
					{
						if(entity instanceof Player)
						{
							((Player) entity).sendBlockChange(location.getBlock().getRelative(BlockFace.UP).getLocation(), Material.SNOW, (byte) 0);
						}
					}
				}
			}
		}
	};
}
