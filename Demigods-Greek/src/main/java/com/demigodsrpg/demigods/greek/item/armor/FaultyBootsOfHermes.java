package com.demigodsrpg.demigods.greek.item.armor;

import com.censoredsoftware.library.util.Items;
import com.demigodsrpg.demigods.engine.item.DivineItem;
import com.demigodsrpg.demigods.engine.util.Zones;
import com.demigodsrpg.demigods.greek.item.GreekItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FaultyBootsOfHermes extends GreekItem
{
	public static final String NAME = "Faulty Boots of Hermes";
	public static final String DESCRIPTION = "Walk as fast as Hermes!";
	public static final Set<Flag> FLAGS = new HashSet<Flag>()
	{
		{
			add(Flag.UNENCHANTABLE);
		}
	};
	public static final DivineItem.Category CATEGORY = DivineItem.Category.ARMOR;
	public static final ItemStack ITEM = Items.create(Material.LEATHER_BOOTS, ChatColor.DARK_GREEN + NAME, new ArrayList<String>()
	{
		{
			add(ChatColor.BLUE + "" + ChatColor.ITALIC + DESCRIPTION);
		}
	}, null);
	public static final Recipe RECIPE = new ShapedRecipe(ITEM)
	{
		{
			shape("AAA", "ABA", "AAA");
			setIngredient('A', Material.FEATHER);
			setIngredient('B', Material.LEATHER_BOOTS);
		}
	};
	public static final Listener LISTENER = new Listener()
	{
		@EventHandler
		public void onPlayerExpChange(PlayerExpChangeEvent event)
		{
			if(Zones.inNoDemigodsZone(event.getPlayer().getLocation())) return;

			// Define variables
			Player player = event.getPlayer();

			if(player.getInventory().getBoots() != null && Items.areEqualIgnoreEnchantments(ITEM, player.getInventory().getBoots()))
			{
				event.getPlayer().setVelocity(new Vector(0.1F, 3, 0));
			}
		}

		@EventHandler(priority = EventPriority.NORMAL)
		public void onPlayerVelocity(PlayerVelocityEvent event)
		{
			if(Zones.inNoDemigodsZone(event.getPlayer().getLocation()) || Zones.inNoBuildZone(event.getPlayer(), event.getPlayer().getLocation())) return;
			// Define variables
			Player player = event.getPlayer();

			if(player.getInventory().getBoots() != null && Items.areEqualIgnoreEnchantments(ITEM, player.getInventory().getBoots()))
			{
				Vector victor = event.getVelocity();

				event.setVelocity(new Vector(victor.getX() + 1, victor.getX() + 1, victor.getX() + 1));

				player.setVelocity(new Vector(7, 7, 7));
			}
		}
	};

	private FaultyBootsOfHermes()
	{
		super(NAME, DESCRIPTION, FLAGS, CATEGORY, ITEM, RECIPE, LISTENER);
	}

	private static final DivineItem INST = new FaultyBootsOfHermes();

	public static DivineItem inst()
	{
		return INST;
	}
}
