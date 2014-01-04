package com.censoredsoftware.demigods.greek.item.book;

import com.censoredsoftware.censoredlib.util.Items;
import com.censoredsoftware.demigods.engine.data.StructureData;
import com.censoredsoftware.demigods.engine.mythos.DivineItem;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.censoredsoftware.demigods.greek.item.GreekItem;
import com.censoredsoftware.demigods.greek.structure.Altar;
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
import java.util.HashSet;
import java.util.Set;

public class BookOfPrayer extends GreekItem
{
	public static final String NAME = "Book of Prayer";
	public static final String DESCRIPTION = "Teleport to the nearest Altar.";
	public static final Set<Flag> FLAGS = new HashSet<Flag>()
	{
		{
			add(Flag.UNENCHANTABLE);
		}
	};
	public static final DivineItem.Category CATEGORY = DivineItem.Category.BOOK;
	public static final ItemStack ITEM = Items.create(Material.BOOK, ChatColor.AQUA + "" + ChatColor.BOLD + NAME, new ArrayList<String>()
	{
		{
			add(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Right click to teleport to the nearest Altar.");
			add(" ");
			add(ChatColor.RED + "Consumed on use.");
		}
	}, null);
	public static final Recipe RECIPE = new ShapelessRecipe(ITEM)
	{
		{
			addIngredient(1, Material.NETHER_STAR);
			addIngredient(2, Material.BOOK);
		}
	};
	public static final Listener LISTENER = new Listener()
	{
		@EventHandler(priority = EventPriority.HIGH)
		private void onRightClick(PlayerInteractEvent event)
		{
			if(Zones.inNoDemigodsZone(event.getPlayer().getLocation())) return;

			// Define variables
			Player player = event.getPlayer();

			if(event.getAction().equals(Action.RIGHT_CLICK_AIR) && player.getItemInHand().equals(ITEM))
			{
				if(Altar.Util.isAltarNearby(player.getLocation()))
				{
					// Find the nearest Altar and teleport
					StructureData save = Altar.Util.getAltarNearby(player.getLocation());
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

	private BookOfPrayer()
	{
		super(NAME, DESCRIPTION, FLAGS, CATEGORY, ITEM, RECIPE, LISTENER);
	}

	private static final DivineItem INST = new BookOfPrayer();

	public static DivineItem inst()
	{
		return INST;
	}
}
