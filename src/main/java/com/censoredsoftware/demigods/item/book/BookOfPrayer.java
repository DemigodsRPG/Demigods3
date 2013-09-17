package com.censoredsoftware.demigods.item.book;

import com.censoredsoftware.demigods.structure.Structure;
import com.censoredsoftware.demigods.structure.global.Altar;
import com.censoredsoftware.demigods.util.Items;
import com.censoredsoftware.demigods.util.Zones;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.ArrayList;

public class BookOfPrayer
{
	public final static ItemStack book = Items.create(Material.BOOK, ChatColor.AQUA + "" + ChatColor.BOLD + "Book of Prayer", new ArrayList<String>()
	{
		{
			add(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Right click to teleport to the nearest Altar.");
			add(" ");
			add(ChatColor.RED + "Consumed on use.");
		}
	}, null);
	public final static Recipe recipe = new ShapelessRecipe(book)
	{
		{
			addIngredient(1, Material.NETHER_STAR);
			addIngredient(2, Material.BOOK);
		}
	};
	public final static Listener listener = new Listener()
	{
		@EventHandler(priority = EventPriority.HIGH)
		private void onRightClick(PlayerInteractEvent event)
		{
			if(Zones.inNoDemigodsZone(event.getPlayer().getLocation())) return;

			// Define variables
			Player player = event.getPlayer();

			if(event.getAction().equals(Action.RIGHT_CLICK_AIR) && player.getItemInHand().equals(book))
			{
				if(Altar.Util.isAltarNearby(player.getLocation()))
				{
					// Find the nearest Altar and teleport
					Structure save = Altar.Util.getAltarNearby(player.getLocation());
					player.teleport(save.getReferenceLocation().clone().add(2.0, 1.5, 0));
					player.sendMessage(ChatColor.YELLOW + "Teleporting to the nearest Altar...");
					player.getWorld().strikeLightningEffect(player.getLocation());

					// Consume the book
					player.getInventory().setItemInHand(new ItemStack(Material.AIR));
				}
				else player.sendMessage(ChatColor.YELLOW + "No Altar found!");
			}
		}
	};
}
