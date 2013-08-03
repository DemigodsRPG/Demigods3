package com.censoredsoftware.demigods.episodes.demo.structure;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import com.censoredsoftware.core.util.Randoms;
import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.data.DataManager;
import com.censoredsoftware.demigods.engine.element.Structure.StandaloneStructure;
import com.censoredsoftware.demigods.engine.element.Structure.Structure;
import com.censoredsoftware.demigods.engine.language.TranslationManager;
import com.censoredsoftware.demigods.engine.location.DLocation;
import com.censoredsoftware.demigods.engine.util.Admins;
import com.censoredsoftware.demigods.engine.util.Structures;
import com.censoredsoftware.demigods.episodes.demo.EpisodeDemo;

// TODO Optimize and generalize methods.

public class Altar implements StandaloneStructure
{
	private final static Schematic general = new Schematic("general", "_Alex", 3)
	{
		{
			// Create roof
			add(new Selection().include(2, 3, 2).setBlockData(Selection.BuildingBlock.stoneBrickSlabTop));
			add(new Selection().include(-2, 3, -2).setBlockData(Selection.BuildingBlock.stoneBrickSlabTop));
			add(new Selection().include(2, 3, -2).setBlockData(Selection.BuildingBlock.stoneBrickSlabTop));
			add(new Selection().include(-2, 3, 2).setBlockData(Selection.BuildingBlock.stoneBrickSlabTop));
			add(new Selection().include(2, 4, 2).setBlockData(Selection.BuildingBlock.stoneBrick));
			add(new Selection().include(-2, 4, -2).setBlockData(Selection.BuildingBlock.stoneBrick));
			add(new Selection().include(2, 4, -2).setBlockData(Selection.BuildingBlock.stoneBrick));
			add(new Selection().include(-2, 4, 2).setBlockData(Selection.BuildingBlock.stoneBrick));
			add(new Selection().include(2, 5, 2).setBlockData(Selection.BuildingBlock.spruceSlab));
			add(new Selection().include(-2, 5, -2).setBlockData(Selection.BuildingBlock.spruceSlab));
			add(new Selection().include(2, 5, -2).setBlockData(Selection.BuildingBlock.spruceSlab));
			add(new Selection().include(-2, 5, 2).setBlockData(Selection.BuildingBlock.spruceSlab));
			add(new Selection().include(0, 6, 0).setBlockData(Selection.BuildingBlock.spruceSlab));
			add(new Selection().include(-1, 5, -1, 1, 5, 1).setBlockData(Selection.BuildingBlock.spruceWood));

			// Create the enchantment table
			add(new Selection().include(0, 2, 0).setBlockData(Selection.BuildingBlock.enchantTable));

			// Create magical table stand
			add(new Selection().include(0, 1, 0).setBlockData(Selection.BuildingBlock.stoneBrick));

			// Create outer steps
			add(new Selection().include(3, 0, 3).setBlockData(Selection.BuildingBlock.stoneBrickSlabBottom));
			add(new Selection().include(-3, 0, -3).setBlockData(Selection.BuildingBlock.stoneBrickSlabBottom));
			add(new Selection().include(3, 0, -3).setBlockData(Selection.BuildingBlock.stoneBrickSlabBottom));
			add(new Selection().include(-3, 0, 3).setBlockData(Selection.BuildingBlock.stoneBrickSlabBottom));
			add(new Selection().include(4, 0, -2, 4, 0, 2).setBlockData(Selection.BuildingBlock.stoneBrickSlabBottom));
			add(new Selection().include(-4, 0, -2, -4, 0, 2).setBlockData(Selection.BuildingBlock.stoneBrickSlabBottom));
			add(new Selection().include(-2, 0, -4, 2, 0, -4).setBlockData(Selection.BuildingBlock.stoneBrickSlabBottom));
			add(new Selection().include(-2, 0, 4, 2, 0, 4).setBlockData(Selection.BuildingBlock.stoneBrickSlabBottom));

			// Create inner steps
			add(new Selection().include(3, 0, -1, 3, 0, 1).setBlockData(Selection.BuildingBlock.stoneBrick));
			add(new Selection().include(-1, 0, 3, 1, 0, 3).setBlockData(Selection.BuildingBlock.stoneBrick));
			add(new Selection().include(-3, 0, -1, -3, 0, 1).setBlockData(Selection.BuildingBlock.stoneBrick));
			add(new Selection().include(-1, 0, -3, 1, 0, -3).setBlockData(Selection.BuildingBlock.stoneBrick));

			// Create pillars
			add(new Selection().include(3, 4, 2).setBlockData(Selection.BuildingBlock.spruceSlab));
			add(new Selection().include(3, 4, -2).setBlockData(Selection.BuildingBlock.spruceSlab));
			add(new Selection().include(2, 4, 3).setBlockData(Selection.BuildingBlock.spruceSlab));
			add(new Selection().include(-2, 4, 3).setBlockData(Selection.BuildingBlock.spruceSlab));
			add(new Selection().include(-3, 4, 2).setBlockData(Selection.BuildingBlock.spruceSlab));
			add(new Selection().include(-3, 4, -2).setBlockData(Selection.BuildingBlock.spruceSlab));
			add(new Selection().include(2, 4, -3).setBlockData(Selection.BuildingBlock.spruceSlab));
			add(new Selection().include(-2, 4, -3).setBlockData(Selection.BuildingBlock.spruceSlab));
			add(new Selection().include(3, 0, 2, 3, 3, 2).setBlockData(Selection.BuildingBlock.stoneBrick));
			add(new Selection().include(3, 0, -2, 3, 3, -2).setBlockData(Selection.BuildingBlock.stoneBrick));
			add(new Selection().include(2, 0, 3, 2, 3, 3).setBlockData(Selection.BuildingBlock.stoneBrick));
			add(new Selection().include(-2, 0, 3, -2, 3, 3).setBlockData(Selection.BuildingBlock.stoneBrick));
			add(new Selection().include(-3, 0, 2, -3, 3, 2).setBlockData(Selection.BuildingBlock.stoneBrick));
			add(new Selection().include(-3, 0, -2, -3, 3, -2).setBlockData(Selection.BuildingBlock.stoneBrick));
			add(new Selection().include(2, 0, -3, 2, 3, -3).setBlockData(Selection.BuildingBlock.stoneBrick));
			add(new Selection().include(-2, 0, -3, -2, 3, -3).setBlockData(Selection.BuildingBlock.stoneBrick));

			// Left beam
			add(new Selection().include(1, 4, -2, -1, 4, -2).exclude(0, 4, -2).setBlockData(Selection.BuildingBlock.stoneBrick));
			add(new Selection().include(0, 4, -2).setBlockData(Selection.BuildingBlock.stoneBrickSpecial));
			add(new Selection().include(-1, 5, -2, 1, 5, -2).setBlockData(Selection.BuildingBlock.spruceSlab));

			// Right beam
			add(new Selection().include(1, 4, 2, -1, 4, 2).exclude(0, 4, 2).setBlockData(Selection.BuildingBlock.stoneBrick));
			add(new Selection().include(0, 4, 2).setBlockData(Selection.BuildingBlock.stoneBrickSpecial));
			add(new Selection().include(-1, 5, 2, 1, 5, 2).setBlockData(Selection.BuildingBlock.spruceSlab));

			// Top beam
			add(new Selection().include(2, 4, 1, 2, 4, -1).exclude(2, 4, 0).setBlockData(Selection.BuildingBlock.stoneBrick));
			add(new Selection().include(2, 4, 0).setBlockData(Selection.BuildingBlock.stoneBrickSpecial));
			add(new Selection().include(2, 5, -1, 2, 5, 1).setBlockData(Selection.BuildingBlock.spruceSlab));

			// Bottom beam
			add(new Selection().include(-2, 4, 1, -2, 4, -1).exclude(-2, 4, 0).setBlockData(Selection.BuildingBlock.stoneBrick));
			add(new Selection().include(-2, 4, 0).setBlockData(Selection.BuildingBlock.stoneBrickSpecial));
			add(new Selection().include(-2, 5, -1, -2, 5, 1).setBlockData(Selection.BuildingBlock.spruceSlab));

			// Create main platform
			add(new Selection().include(-2, 1, -2, 2, 1, 2).exclude(0, 1, 0).setBlockData(Selection.BuildingBlock.stoneBrickSlabBottom));
		}
	};

	private final static Schematic holy = new Schematic("holy", "HmmmQuestionMark", 3)
	{
		{
			// Create roof
			add(new Selection().include(2, 3, 2).setBlockData(Selection.BuildingBlock.quartzSlabTop));
			add(new Selection().include(-2, 3, -2).setBlockData(Selection.BuildingBlock.quartzSlabTop));
			add(new Selection().include(2, 3, -2).setBlockData(Selection.BuildingBlock.quartzSlabTop));
			add(new Selection().include(-2, 3, 2).setBlockData(Selection.BuildingBlock.quartzSlabTop));
			add(new Selection().include(2, 4, 2).setBlockData(Selection.BuildingBlock.quartz));
			add(new Selection().include(-2, 4, -2).setBlockData(Selection.BuildingBlock.quartz));
			add(new Selection().include(2, 4, -2).setBlockData(Selection.BuildingBlock.quartz));
			add(new Selection().include(-2, 4, 2).setBlockData(Selection.BuildingBlock.quartz));
			add(new Selection().include(2, 5, 2).setBlockData(Selection.BuildingBlock.birchSlab));
			add(new Selection().include(-2, 5, -2).setBlockData(Selection.BuildingBlock.birchSlab));
			add(new Selection().include(2, 5, -2).setBlockData(Selection.BuildingBlock.birchSlab));
			add(new Selection().include(-2, 5, 2).setBlockData(Selection.BuildingBlock.birchSlab));
			add(new Selection().include(0, 6, 0).setBlockData(Selection.BuildingBlock.birchSlab));
			add(new Selection().include(-1, 5, -1, 1, 5, 1).setBlockData(Selection.BuildingBlock.birchWood));

			// Create the enchantment table
			add(new Selection().include(0, 2, 0).setBlockData(Selection.BuildingBlock.enchantTable));

			// Create magical table stand
			add(new Selection().include(0, 1, 0).setBlockData(Selection.BuildingBlock.quartzSpecial));

			// Create outer steps
			add(new Selection().include(3, 0, 3).setBlockData(Selection.BuildingBlock.quartzSlabBottom));
			add(new Selection().include(-3, 0, -3).setBlockData(Selection.BuildingBlock.quartzSlabBottom));
			add(new Selection().include(3, 0, -3).setBlockData(Selection.BuildingBlock.quartzSlabBottom));
			add(new Selection().include(-3, 0, 3).setBlockData(Selection.BuildingBlock.quartzSlabBottom));
			add(new Selection().include(4, 0, -2, 4, 0, 2).setBlockData(Selection.BuildingBlock.quartzSlabBottom));
			add(new Selection().include(-4, 0, -2, -4, 0, 2).setBlockData(Selection.BuildingBlock.quartzSlabBottom));
			add(new Selection().include(-2, 0, -4, 2, 0, -4).setBlockData(Selection.BuildingBlock.quartzSlabBottom));
			add(new Selection().include(-2, 0, 4, 2, 0, 4).setBlockData(Selection.BuildingBlock.quartzSlabBottom));

			// Create inner steps
			add(new Selection().include(3, 0, -1, 3, 0, 1).exclude(3, 0, 0).setBlockData(Selection.BuildingBlock.quartz));
			add(new Selection().include(-1, 0, 3, 1, 0, 3).exclude(0, 0, 3).setBlockData(Selection.BuildingBlock.quartz));
			add(new Selection().include(-3, 0, -1, -3, 0, 1).exclude(-3, 0, 0).setBlockData(Selection.BuildingBlock.quartz));
			add(new Selection().include(-1, 0, -3, 1, 0, -3).exclude(0, 0, -3).setBlockData(Selection.BuildingBlock.quartz));
			add(new Selection().include(3, 0, 0).setBlockData(Selection.BuildingBlock.quartzSpecial));
			add(new Selection().include(0, 0, 3).setBlockData(Selection.BuildingBlock.quartzSpecial));
			add(new Selection().include(-3, 0, 0).setBlockData(Selection.BuildingBlock.quartzSpecial));
			add(new Selection().include(0, 0, -3).setBlockData(Selection.BuildingBlock.quartzSpecial));

			// Create pillars
			add(new Selection().include(3, 4, 2).setBlockData(Selection.BuildingBlock.birchSlab));
			add(new Selection().include(3, 4, -2).setBlockData(Selection.BuildingBlock.birchSlab));
			add(new Selection().include(2, 4, 3).setBlockData(Selection.BuildingBlock.birchSlab));
			add(new Selection().include(-2, 4, 3).setBlockData(Selection.BuildingBlock.birchSlab));
			add(new Selection().include(-3, 4, 2).setBlockData(Selection.BuildingBlock.birchSlab));
			add(new Selection().include(-3, 4, -2).setBlockData(Selection.BuildingBlock.birchSlab));
			add(new Selection().include(2, 4, -3).setBlockData(Selection.BuildingBlock.birchSlab));
			add(new Selection().include(-2, 4, -3).setBlockData(Selection.BuildingBlock.birchSlab));
			add(new Selection().include(3, 0, 2, 3, 3, 2).setBlockData(Selection.BuildingBlock.pillarQuartz));
			add(new Selection().include(3, 0, -2, 3, 3, -2).setBlockData(Selection.BuildingBlock.pillarQuartz));
			add(new Selection().include(2, 0, 3, 2, 3, 3).setBlockData(Selection.BuildingBlock.pillarQuartz));
			add(new Selection().include(-2, 0, 3, -2, 3, 3).setBlockData(Selection.BuildingBlock.pillarQuartz));
			add(new Selection().include(-3, 0, 2, -3, 3, 2).setBlockData(Selection.BuildingBlock.pillarQuartz));
			add(new Selection().include(-3, 0, -2, -3, 3, -2).setBlockData(Selection.BuildingBlock.pillarQuartz));
			add(new Selection().include(2, 0, -3, 2, 3, -3).setBlockData(Selection.BuildingBlock.pillarQuartz));
			add(new Selection().include(-2, 0, -3, -2, 3, -3).setBlockData(Selection.BuildingBlock.pillarQuartz));

			// Left beam
			add(new Selection().include(1, 4, -2, -1, 4, -2).exclude(0, 4, -2).setBlockData(Selection.BuildingBlock.quartz));
			add(new Selection().include(0, 4, -2).setBlockData(Selection.BuildingBlock.quartzSpecial));
			add(new Selection().include(-1, 5, -2, 1, 5, -2).setBlockData(Selection.BuildingBlock.birchSlab));

			// Right beam
			add(new Selection().include(1, 4, 2, -1, 4, 2).exclude(0, 4, 2).setBlockData(Selection.BuildingBlock.quartz));
			add(new Selection().include(0, 4, 2).setBlockData(Selection.BuildingBlock.quartzSpecial));
			add(new Selection().include(-1, 5, 2, 1, 5, 2).setBlockData(Selection.BuildingBlock.birchSlab));

			// Top beam
			add(new Selection().include(2, 4, 1, 2, 4, -1).exclude(2, 4, 0).setBlockData(Selection.BuildingBlock.quartz));
			add(new Selection().include(2, 4, 0).setBlockData(Selection.BuildingBlock.quartzSpecial));
			add(new Selection().include(2, 5, -1, 2, 5, 1).setBlockData(Selection.BuildingBlock.birchSlab));

			// Bottom beam
			add(new Selection().include(-2, 4, 1, -2, 4, -1).exclude(-2, 4, 0).setBlockData(Selection.BuildingBlock.quartz));
			add(new Selection().include(-2, 4, 0).setBlockData(Selection.BuildingBlock.quartzSpecial));
			add(new Selection().include(-2, 5, -1, -2, 5, 1).setBlockData(Selection.BuildingBlock.birchSlab));

			// Create main platform
			add(new Selection().include(-2, 1, -2, 2, 1, 2).exclude(0, 1, 0).setBlockData(Selection.BuildingBlock.quartzSlabBottom));
		}
	};

	private final static Schematic oasis = new Schematic("oasis", "_Alex", 4)
	{
		{
			// Enchantment Table
			add(new Selection().include(0, 0, 0).setBlockData(Selection.BuildingBlock.smoothSandStone));
			add(new Selection().include(0, 1, 0).setBlockData(Selection.BuildingBlock.enchantTable));

			// PWETTY FLOWAS AND GWASS!
			add(new Selection().include(-3, 0, -3, 3, 0, 3).exclude(-1, 0, -1, 1, 0, 1).setBlockData(Selection.BuildingBlock.prettyFlowersAndGrass));

			// Ground
			add(new Selection().include(-3, -1, -3, 3, -1, 3).exclude(-2, -1, -2, 2, -1, 2).setBlockData(Selection.BuildingBlock.sandyGrass));
			add(new Selection().include(-2, -1, -2, 2, -1, 2).exclude(-1, -1, -1, 1, -1, 1).setBlockData(Selection.BuildingBlock.grass));
			add(new Selection().include(-1, -1, -1, 1, -1, 1).exclude(0, -1, 0).setBlockData(Selection.BuildingBlock.water));
			add(new Selection().include(0, -1, 0).setBlockData(Selection.BuildingBlock.smoothSandStone));
			add(new Selection().include(-3, -2, -3, 3, -2, 3).setBlockData(Selection.BuildingBlock.sandStone));

			// Table
			add(new Selection().include(-1, 0, 0).setBlockData(Selection.BuildingBlock.sandStairWest));
			add(new Selection().include(1, 0, 0).setBlockData(Selection.BuildingBlock.sandStairEast));
			add(new Selection().include(0, 0, -1).setBlockData(Selection.BuildingBlock.sandStairNorth));
			add(new Selection().include(0, 0, 1).setBlockData(Selection.BuildingBlock.sandStairSouth));

			// Tiki Torch
			int rand1 = Randoms.generateIntRange(-3, -2);
			int rand2 = Randoms.generateIntRange(-3, 3);
			add(new Selection().include(-3, 1, -3, 3, 2, 3).exclude(0, 1, 0).setBlockData(Material.AIR));
			add(new Selection().include(rand1, 0, rand2, rand1, 1, rand2).setBlockData(Material.FENCE)); // Fence
			add(new Selection().include(rand1, 2, rand2).setBlockData(Material.TORCH)); // Torch
		}
	};

	public static enum AltarDesign implements Design
	{
		GENERAL("general", general, new Selection().include(0, 2, 0)), HOLY("holy", holy, new Selection().include(0, 2, 0)), OASIS("oasis", oasis, new Selection().include(0, 1, 0));

		private final String name;
		private final Schematic schematic;
		private final Selection clickableBlocks;

		private AltarDesign(String name, Schematic schematic, Selection clickableBlocks)
		{
			this.name = name;
			this.schematic = schematic;
			this.clickableBlocks = clickableBlocks;
		}

		@Override
		public String getName()
		{
			return name;
		}

		@Override
		public Set<Location> getClickableBlocks(Location reference)
		{
			return clickableBlocks.getBlockLocations(reference);
		}

		@Override
		public Schematic getSchematic()
		{
			return schematic;
		}

		public static AltarDesign getByName(String name)
		{
			for(AltarDesign design : AltarDesign.values())
			{
				if(design.getName().equalsIgnoreCase(name)) return design;
			}
			return null;
		}
	}

	@Override
	public Set<Flag> getFlags()
	{
		return new HashSet<Flag>()
		{
			{
				add(Flag.NO_PVP);
				add(Flag.PRAYER_LOCATION);
				add(Flag.PROTECTED_BLOCKS);
			}
		};
	}

	@Override
	public String getStructureType()
	{
		return "Altar";
	}

	@Override
	public Design getDesign(String name)
	{
		if(name.equals(general.toString())) return AltarDesign.GENERAL;
		if(name.equals(oasis.toString())) return AltarDesign.OASIS;
		return AltarDesign.HOLY;
	}

	@Override
	public int getRadius()
	{
		return Demigods.config.getSettingInt("zones.altar_radius");
	}

	@Override
	public Listener getUniqueListener()
	{
		return new AltarListener();
	}

	@Override
	public Set<Save> getAll()
	{
		return Structures.findAll("type", getStructureType());
	}

	@Override
	public Save createNew(Location reference, boolean generate)
	{
		Save save = new Save();
		save.setReferenceLocation(reference);
		save.setType(getStructureType());
		save.setDesign(getDesign(reference).getName());
		save.addFlags(getFlags());
		save.setActive(true);
		save.save();
		if(generate && !save.generate(true)) save.remove();
		return save;
	}

	public Design getDesign(Location reference)
	{
		switch(reference.getBlock().getBiome())
		{
			case ICE_PLAINS:
				return AltarDesign.HOLY;
			case DESERT:
			case DESERT_HILLS:
				return AltarDesign.OASIS;
			default:
				return AltarDesign.GENERAL;
		}
	}

	public static boolean altarNearby(Location location)
	{
		int distance = Demigods.config.getSettingInt("generation.min_blocks_between_altars");
		for(Save structureSave : Structures.findAll("type", "Altar"))
		{
			if(structureSave.getReferenceLocation().distance(location) <= distance) return true;
		}
		return false;
	}
}

class AltarListener implements Listener
{
	@EventHandler(priority = EventPriority.MONITOR)
	public void onChunkLoad(final ChunkLoadEvent event)
	{
		if(!event.isNewChunk() || Demigods.isDisabledWorld(event.getWorld())) return;

		// Define variables
		final Location location = DLocation.Util.randomChunkLocation(event.getChunk());

		// Check if it can generate
		if(Structures.canGenerateStrict(location, 3))
		{
			// Return a random boolean based on the chance of Altar generation
			if(Randoms.randomPercentBool(Demigods.config.getSettingDouble("generation.altar_chance")))
			{
				// If another Altar doesn't exist nearby then make one
				if(!Altar.altarNearby(location))
				{
					Admins.sendDebug(ChatColor.RED + "Altar generated by SERVER at " + ChatColor.GRAY + "(" + location.getWorld().getName() + ") " + location.getX() + ", " + location.getY() + ", " + location.getZ());

					EpisodeDemo.Structures.ALTAR.getStructure().createNew(location, true);

					location.getWorld().strikeLightningEffect(location);
					location.getWorld().strikeLightningEffect(location);

					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Demigods.plugin, new Runnable()
					{
						@Override
						public void run()
						{
							for(Entity entity : event.getWorld().getEntities())
							{
								if(entity instanceof Player)
								{
									if(entity.getLocation().distance(location) < 400)
									{
										((Player) entity).sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + Demigods.text.getText(TranslationManager.Text.ALTAR_SPAWNED_NEAR));
									}
								}
							}
						}
					}, 1);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void demigodsAdminWand(PlayerInteractEvent event)
	{
		if(event.getClickedBlock() == null) return;

		if(Demigods.isDisabledWorld(event.getPlayer().getWorld())) return;

		// Define variables
		Block clickedBlock = event.getClickedBlock();
		Location location = clickedBlock.getLocation();
		Player player = event.getPlayer();

		/**
		 * Handle Altars
		 */
		String design = clickedBlock.getType().equals(Material.EMERALD_BLOCK) ? "general" : clickedBlock.getType().equals(Material.GOLD_BLOCK) ? "holy" : clickedBlock.getType().equals(Material.DIAMOND_BLOCK) ? "oasis" : "";
		if(Admins.useWand(player) && Altar.AltarDesign.getByName(design) != null)
		{
			event.setCancelled(true);

			// Remove clicked block
			clickedBlock.setType(Material.AIR);

			Admins.sendDebug(ChatColor.RED + "Altar generated by ADMIN WAND at " + ChatColor.GRAY + "(" + location.getWorld().getName() + ") " + location.getX() + ", " + location.getY() + ", " + location.getZ());

			player.sendMessage(ChatColor.GRAY + Demigods.text.getText(TranslationManager.Text.ADMIN_WAND_GENERATE_ALTAR));

			// Generate the Altar based on the block given.
			Structure.Save save = EpisodeDemo.Structures.ALTAR.getStructure().createNew(location, false);
			save.setDesign(design);
			// if(!save.generate(true))
			if(!save.slowGenerate(true))
			{
				player.sendMessage(ChatColor.RED + "Could not generate.");
				save.remove();
			}

			player.sendMessage(ChatColor.GREEN + Demigods.text.getText(TranslationManager.Text.ADMIN_WAND_GENERATE_ALTAR_COMPLETE));
			return;
		}

		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && Admins.useWand(player) && Structures.partOfStructureWithType(location, "Altar", true))
		{
			event.setCancelled(true);

			Structure.Save altar = Structures.getStructureSave(location, true);

			if(DataManager.hasTimed(player.getName(), "destroy_altar"))
			{
				Admins.sendDebug(ChatColor.RED + "Altar at " + ChatColor.GRAY + "(" + location.getWorld().getName() + ") " + location.getX() + ", " + location.getY() + ", " + location.getZ() + " removed by " + "ADMIN WAND" + ".");

				// Remove the Altar

				altar.remove();

				DataManager.removeTimed(player.getName(), "destroy_altar");

				player.sendMessage(ChatColor.GREEN + Demigods.text.getText(TranslationManager.Text.ADMIN_WAND_REMOVE_ALTAR_COMPLETE));
			}
			else
			{
				DataManager.saveTimed(player.getName(), "destroy_altar", true, 5);
				player.sendMessage(ChatColor.RED + Demigods.text.getText(TranslationManager.Text.ADMIN_WAND_REMOVE_ALTAR));
			}
		}
	}
}
