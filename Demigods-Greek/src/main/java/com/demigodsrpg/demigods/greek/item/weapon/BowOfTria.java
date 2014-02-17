package com.demigodsrpg.demigods.greek.item.weapon;

import com.censoredsoftware.library.util.Items;
import com.demigodsrpg.demigods.engine.item.DivineItem;
import com.demigodsrpg.demigods.engine.util.Zones;
import com.demigodsrpg.demigods.greek.item.GreekItem;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BowOfTria extends GreekItem
{
	public static final String NAME = "Bow of Tría";
	public static final String DESCRIPTION = "Take your target out 3 times faster!";
	public static final Set<Flag> FLAGS = new HashSet<Flag>()
	{
		{
			add(Flag.UNENCHANTABLE);
		}
	};
	public static final DivineItem.Category CATEGORY = DivineItem.Category.WEAPON;
	public static final ItemStack ITEM = Items.create(Material.BOW, ChatColor.DARK_RED + "" + ChatColor.BOLD + NAME, new ArrayList<String>()
	{
		{
			add(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + DESCRIPTION);
		}
	}, null);
	public static final Recipe RECIPE = new ShapedRecipe(ITEM)
	{
		{
			shape("ABB", " AB", "C A");
			setIngredient('A', Material.BOW);
			setIngredient('B', Material.ARROW);
			setIngredient('C', Material.DIAMOND);
		}
	};
	public static final Listener LISTENER = new Listener()
	{
		@EventHandler(priority = EventPriority.HIGH)
		private void onEntityShootBow(EntityShootBowEvent event)
		{
			// Return for disabled world
			if(Zones.inNoDemigodsZone(event.getEntity().getLocation())) return;

			// If they right clicked a block with the ITEM in hand, do stuff
			if(Items.areEqual(event.getBow(), ITEM))
			{
				PlayerInventory inventory = null;
				Arrow startArrow = (Arrow) event.getProjectile();
				startArrow.setVelocity(startArrow.getVelocity().multiply(.8));

				if(event.getEntity() instanceof Player) inventory = ((Player) event.getEntity()).getInventory();

				for(int i = 1; i < 3; i++)
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

	private BowOfTria()
	{
		super(NAME, DESCRIPTION, FLAGS, CATEGORY, ITEM, RECIPE, LISTENER);
	}

	private static final DivineItem INST = new BowOfTria();

	public static DivineItem inst()
	{
		return INST;
	}
}
