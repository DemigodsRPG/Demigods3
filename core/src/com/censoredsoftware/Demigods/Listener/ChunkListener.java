package com.censoredsoftware.Demigods.Listener;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;

import com.censoredsoftware.Demigods.API.BlockAPI;
import com.censoredsoftware.Demigods.API.ItemAPI;
import com.censoredsoftware.Demigods.API.MiscAPI;
import com.censoredsoftware.Demigods.Block.Altar;
import com.censoredsoftware.Demigods.Definition.SpecialItems;
import com.censoredsoftware.Demigods.Demigods;
import com.censoredsoftware.Demigods.DemigodsData;
import com.censoredsoftware.Demigods.Event.Altar.AltarCreateEvent;
import com.censoredsoftware.Demigods.Event.Altar.AltarCreateEvent.AltarCreateCause;
import com.censoredsoftware.Demigods.Event.Misc.ChestSpawnEvent;

public class ChunkListener implements Listener
{
	@EventHandler(priority = EventPriority.MONITOR)
	public void onChunkLoad(ChunkLoadEvent event)
	{
		// Define variables
		Location location = MiscAPI.randomChunkLocation(event.getChunk());

		// Let's randomly create chests
		if(DemigodsData.randomPercentBool(Demigods.config.getSettingDouble("chest_generation_chance")) && location.clone().subtract(0, 1, 0).getBlock().getType().isSolid())
		{
			ChestSpawnEvent chestSpawnEvent = new ChestSpawnEvent(location);
			Bukkit.getServer().getPluginManager().callEvent(chestSpawnEvent);
			if(chestSpawnEvent.isCancelled()) return;

			// Define variables
			ArrayList<ItemStack> items = new ArrayList<ItemStack>();

			for(Map.Entry<Double, ItemStack> item : SpecialItems.getBooks().entrySet())
			{
				if(DemigodsData.randomPercentBool(item.getKey())) items.add(item.getValue());
			}

			// Generate the chest
			ItemAPI.createChest(location, items);
		}

		// If it's a new chunk then we'll generate structures
		if(event.isNewChunk())
		{
			// Choose an arbitrary value and check the chance against it
			if(DemigodsData.randomPercentBool(Demigods.config.getSettingDouble("altar_generation_chance")))
			{
				if(BlockAPI.canGenerateSolid(location, 6))
				{
					// If another Altar doesn't exist nearby then make one
					if(!BlockAPI.altarNearby(location, Demigods.config.getSettingInt("minimum_blocks_between_altars")))
					{
						AltarCreateEvent altarCreateEvent = new AltarCreateEvent(location, AltarCreateCause.GENERATED);
						Bukkit.getServer().getPluginManager().callEvent(altarCreateEvent);
						if(altarCreateEvent.isCancelled()) return;

						new Altar(DemigodsData.generateInt(5), location);

						location.getWorld().strikeLightningEffect(location);
						location.getWorld().strikeLightningEffect(location);

						for(Entity entity : event.getWorld().getEntities())
						{
							if(entity instanceof Player)
							{
								if(entity.getLocation().distance(location) < 400)
								{
									((Player) entity).sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "An Altar has spawned near you...");
								}
							}
						}
					}
				}
			}
		}
	}
}
