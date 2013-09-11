package com.censoredsoftware.demigods.item.divine;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.item.DivineItem;
import com.censoredsoftware.demigods.util.Items;
import com.censoredsoftware.demigods.util.Messages;

public class ButtSword implements DivineItem.Item
{
	@Override
	public ItemStack getItem()
	{
		return Items.create(Material.DIAMOND_SWORD, ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "The Butt Sword", null, null);
	}

	@Override
	public Recipe getRecipe()
	{
		ShapedRecipe recipe = new ShapedRecipe(getItem());
		recipe.shape("AAA", "AAA", "AAA");
		recipe.setIngredient('A', Material.DIAMOND_SWORD);
		return recipe;
	}

	@Override
	public Listener getUniqueListener()
	{
		return new Listener();
	}

	class Listener implements org.bukkit.event.Listener
	{
		@EventHandler(priority = EventPriority.HIGH)
		private void onPlayerInteract(PlayerInteractEvent event)
		{
			if(Demigods.MiscUtil.isDisabledWorld(event.getPlayer().getLocation())) return;

			// Define variables
			Player player = event.getPlayer();

			// If they right clicked a block with the item in hand, do stuff
			if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && Items.areEqual(player.getItemInHand(), ButtSword.this.getItem()))
			{
				Location refLoc = event.getClickedBlock().getLocation();
				Location newLoc = refLoc.clone().add(0, 20, 0);

				// Make eggs fall... why? Just cause.
				for(int i = 0; i < 100; i++)
					refLoc.getWorld().spawnEntity(newLoc, EntityType.EGG);

				// Let everyone know what a perve the user is!
				Messages.broadcast(player.getDisplayName() + ChatColor.WHITE + " just smacked your butt!");
			}
		}
	}
}
