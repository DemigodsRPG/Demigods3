package com.censoredsoftware.demigods.greek.item.weapon;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import com.censoredsoftware.censoredlib.util.Items;
import com.censoredsoftware.demigods.engine.util.Messages;
import com.censoredsoftware.demigods.engine.util.Zones;

public class ButtSword
{
	public final static ItemStack buttSword = Items.create(Material.DIAMOND_SWORD, ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "The Butt Sword", null, null);
	public final static Recipe recipe = new ShapedRecipe(buttSword)
	{
		{
			shape("AAA", "AAA", "AAA");
			setIngredient('A', Material.DIAMOND_SWORD);
		}
	};
	public final static Listener listener = new Listener()
	{
		@EventHandler(priority = EventPriority.HIGH)
		private void onPlayerInteract(PlayerInteractEvent event)
		{
			if(Zones.inNoDemigodsZone(event.getPlayer().getLocation())) return;

			// Define variables
			Player player = event.getPlayer();

			// If they right clicked a block with the item in hand, do stuff
			if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && Items.areEqual(player.getItemInHand(), buttSword))
			{
				Location refLoc = event.getClickedBlock().getLocation();
				Location newLoc = refLoc.clone().add(0, 20, 0);

				// Make eggs fall... why? Just cause.
				for(int i = 0; i < 100; i++)
					refLoc.getWorld().spawnEntity(newLoc, EntityType.EGG);

				// Let everyone know what a perv the user is!
				Messages.broadcast(player.getDisplayName() + ChatColor.WHITE + " just smacked your butt!");
			}
		}
	};
}
