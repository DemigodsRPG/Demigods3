package com.censoredsoftware.demigods.greek.item.weapon;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import com.censoredsoftware.censoredlib.util.Items;
import com.censoredsoftware.demigods.engine.util.Zones;

public class DeathBow
{
	public final static ItemStack deathBow = Items.create(Material.BOW, ChatColor.DARK_RED + "" + ChatColor.BOLD + "The Bow of Five Arrows", new ArrayList<String>()
	{
		{
			add(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Take your target out 5 times faster!");
		}
	}, null);
	public final static Recipe recipe = new ShapedRecipe(deathBow)
	{
		{
			shape("A  ", " A ", "  A");
			setIngredient('A', Material.BOW);
		}
	};
	public final static Listener listener = new Listener()
	{
		@EventHandler(priority = EventPriority.HIGH)
		private void onEntityShootBow(EntityShootBowEvent event)
		{
			// Return for disabled world
			if(Zones.inNoDemigodsZone(event.getEntity().getLocation())) return;

			// If they right clicked a block with the item in hand, do stuff
			if(Items.areEqual(event.getBow(), deathBow))
			{
				PlayerInventory inventory = null;
				Arrow startArrow = (Arrow) event.getProjectile();
				startArrow.setVelocity(startArrow.getVelocity().multiply(.8));

				if(event.getEntity() instanceof Player) inventory = ((Player) event.getEntity()).getInventory();

				for(int i = 1; i < 5; i++)
				{
					if(inventory != null)
					{
						if(!inventory.contains(Material.ARROW, 1)) break;
						inventory.remove(new ItemStack(Material.ARROW, 1));
					}

					Arrow spawnedArrow = (Arrow) event.getEntity().getWorld().spawnEntity(startArrow.getLocation(), EntityType.ARROW);
					spawnedArrow.setShooter(event.getEntity());
					spawnedArrow.setVelocity(startArrow.getVelocity().multiply(.9 / i));
				}
			}
		}
	};
}
