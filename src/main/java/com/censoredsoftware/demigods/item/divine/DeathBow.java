package com.censoredsoftware.demigods.item.divine;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.item.DivineItem;
import com.censoredsoftware.demigods.util.Items;

public class DeathBow implements DivineItem.Item
{
	@Override
	public ItemStack getItem()
	{
		return Items.create(Material.BOW, ChatColor.BOLD + "" + ChatColor.DARK_RED + "The Bow of Five Arrows", new ArrayList<String>()
		{
			{
				add(ChatColor.ITALIC + "" + ChatColor.DARK_PURPLE + "Take your target out 5 times faster!");
			}
		}, null);
	}

	@Override
	public Recipe getRecipe()
	{
		ShapedRecipe recipe = new ShapedRecipe(getItem());
		recipe.shape("A  ", " A ", "  A");
		recipe.setIngredient('A', Material.BOW);
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
		private void onEntityShootBow(EntityShootBowEvent event)
		{
			// Return for disabled world
			if(Demigods.MiscUtil.isDisabledWorld(event.getEntity().getLocation())) return;

			// If they right clicked a block with the item in hand, do stuff
			if(Items.areEqual(event.getBow(), DeathBow.this.getItem()))
			{
				Arrow startArrow = (Arrow) event.getProjectile();

				for(int i = 0; i < 5; i++)
				{
					Arrow spawnedArrow = (Arrow) event.getEntity().getWorld().spawnEntity(startArrow.getLocation(), EntityType.ARROW);
					spawnedArrow.setVelocity(startArrow.getVelocity().multiply(i * .85));
				}
			}
		}
	}
}
