package com.censoredsoftware.demigods.greek.item.armor;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import com.censoredsoftware.censoredlib.util.Items;
import com.censoredsoftware.demigods.engine.util.Zones;

public class BootsOfPagos
{
	public final static ItemStack boots = Items.create(Material.IRON_BOOTS, ChatColor.AQUA + "Boots of Pagos", new ArrayList<String>()
	{
		{
			add(ChatColor.BLUE + "" + ChatColor.ITALIC + "Boots this cold have their advantages... and disadvantages.");
		}
	}, null);
	public final static Recipe recipe = new ShapedRecipe(boots)
	{
		{
			shape("AAA", "ABA", "AAA");
			setIngredient('A', Material.PACKED_ICE);
			setIngredient('B', Material.IRON_BOOTS);
		}
	};
	public final static Listener listener = new Listener()
	{
		@EventHandler(priority = EventPriority.HIGH)
		private void onPlayerMove(PlayerMoveEvent event)
		{
			if(Zones.inNoDemigodsZone(event.getPlayer().getLocation())) return;

			// Define variables
			Player player = event.getPlayer();

			if(player.getInventory().getBoots() != null && player.getInventory().getBoots().isSimilar(boots))
			{
				Location location = player.getLocation().subtract(0, 1, 0);

				if(location.getBlock().isLiquid())
				{
					location.getBlock().setType(Material.ICE);
				}
				else if(!location.getBlock().isLiquid() && location.getBlock().getType() != Material.AIR && location.getBlock().getType() != Material.ICE && location.getBlock().getType() != Material.PACKED_ICE)
				{
					player.getLocation().getBlock().setType(Material.SNOW);
				}
			}
		}
	};
}
