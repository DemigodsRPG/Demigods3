package com.censoredsoftware.demigods.item.divine;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
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

public class ButtSword implements DivineItem.Item
{
	@Override
	public ItemStack getItem()
	{
		return Items.create(Material.DIAMOND_SWORD, ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "The Butt Sword", null, null);
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

			if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getPlayer().getItemInHand().equals(ButtSword.this.getItem()))
			{
				// They right clicked a block with the item in hand, do stuff
				Location location = event.getClickedBlock().getLocation();
				location.getWorld().spawnEntity(location, EntityType.EGG);
			}
		}
	}
}
