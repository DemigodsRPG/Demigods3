package com.demigodsrpg.demigods.greek.item.armor;

import com.censoredsoftware.censoredlib.util.Items;
import com.demigodsrpg.demigods.engine.item.DivineItem;
import com.demigodsrpg.demigods.engine.util.Zones;
import com.demigodsrpg.demigods.greek.item.GreekItem;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BootsOfPagos extends GreekItem
{
	public static final String NAME = "Boots of Pagos";
	public static final String DESCRIPTION = "Boots this cold have their advantages... and disadvantages.";
	public static final Set<Flag> FLAGS = new HashSet<Flag>()
	{
		{
			add(Flag.UNENCHANTABLE);
		}
	};
	public static final DivineItem.Category CATEGORY = DivineItem.Category.ARMOR;
	public static final ItemStack ITEM = Items.create(Material.IRON_BOOTS, ChatColor.AQUA + NAME, new ArrayList<String>()
	{
		{
			add(ChatColor.BLUE + "" + ChatColor.ITALIC + DESCRIPTION);
		}
	}, null);
	public static final Recipe RECIPE = new ShapedRecipe(ITEM)
	{
		{
			shape("AAA", "ABA", "AAA");
			setIngredient('A', Material.PACKED_ICE);
			setIngredient('B', Material.IRON_BOOTS);
		}
	};
	public static final Listener LISTENER = new Listener()
	{
		@EventHandler(priority = EventPriority.NORMAL)
		private void onPlayerMove(PlayerMoveEvent event)
		{
			if(Zones.inNoDemigodsZone(event.getPlayer().getLocation()) || Zones.inNoBuildZone(event.getPlayer(), event.getPlayer().getLocation())) return;

			// Define variables
			Player player = event.getPlayer();

			if(player.getInventory().getBoots() != null && Items.areEqualIgnoreEnchantments(ITEM, player.getInventory().getBoots()))
			{
				Location location = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation();

				if(!location.getBlock().isLiquid() && location.getBlock().getType().isSolid() && location.getBlock().getType() != Material.ICE && location.getBlock().getType() != Material.PACKED_ICE && location.getBlock().getRelative(BlockFace.UP).getType().equals(Material.AIR))
				{
					if(System.currentTimeMillis() % 4000 < 1000)
					{
						player.sendBlockChange(location.clone().add(0, 1, 0), Material.SNOW, (byte) 0);

						for(Entity entity : player.getNearbyEntities(30, 30, 30))
							if(entity instanceof Player) ((Player) entity).sendBlockChange(location.clone().add(0, 1, 0), Material.SNOW, (byte) 0);
					}
				}
			}
		}
	};

	private BootsOfPagos()
	{
		super(NAME, DESCRIPTION, FLAGS, CATEGORY, ITEM, RECIPE, LISTENER);
	}

	private static final DivineItem INST = new BootsOfPagos();

	public static DivineItem inst()
	{
		return INST;
	}
}
