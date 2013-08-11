package com.censoredsoftware.demigods.episodes.demo.structure;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.data.DataManager;
import com.censoredsoftware.demigods.engine.element.Structure.StandaloneStructure;
import com.censoredsoftware.demigods.engine.element.Structure.Structure;
import com.censoredsoftware.demigods.engine.language.Translation;
import com.censoredsoftware.demigods.engine.player.DCharacter;
import com.censoredsoftware.demigods.engine.player.DPlayer;
import com.censoredsoftware.demigods.engine.util.Admins;
import com.censoredsoftware.demigods.engine.util.Structures;
import com.censoredsoftware.demigods.episodes.demo.EpisodeDemo;

public class Shrine implements StandaloneStructure
{
	private final static Schematic general = new Schematic("general", "_Alex", 2)
	{
		{
			// Create the main block
			add(new Selection(0, 1, 0, Selection.BuildingBlock.goldClickBlock));

			// Create the ender chest and the block below
			add(new Selection(0, 0, 0, Selection.BuildingBlock.enderChest));
			add(new Selection(0, -1, 0, Selection.BuildingBlock.stoneBrick));

			// Create the rest
			add(new Selection(-1, 0, 0, Selection.BuildingBlock.stoneBrickStairs));
			add(new Selection(1, 0, 0, Selection.BuildingBlock.stoneBrickStairs1));
			add(new Selection(0, 0, -1, Selection.BuildingBlock.stoneBrickStairs2));
			add(new Selection(0, 0, 1, Selection.BuildingBlock.stoneBrickStairs3));
		}
	};

	public static enum ShrineDesign implements Design
	{
		GENERAL("general", general, new Selection(0, 1, 0));

		private final String name;
		private final Structure.Schematic schematic;
		private final Selection clickableBlocks;

		private ShrineDesign(String name, Structure.Schematic schematic, Selection clickableBlocks)
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
	}

	@Override
	public Set<Flag> getFlags()
	{
		return new HashSet<Flag>()
		{
			{
				add(Flag.DELETE_WITH_OWNER);
				add(Flag.PROTECTED_BLOCKS);
				add(Flag.TRIBUTE_LOCATION);
			}
		};
	}

	@Override
	public String getStructureType()
	{
		return "Shrine";
	}

	@Override
	public Design getDesign(String name)
	{
		return ShrineDesign.GENERAL;
	}

	@Override
	public int getRadius()
	{
		return Demigods.config.getSettingInt("zones.shrine_radius");
	}

	@Override
	public org.bukkit.event.Listener getUniqueListener()
	{
		return new Listener();
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
		save.setDesign("general");
		save.addFlags(getFlags());
		save.setActive(true);
		save.save();
		if(generate && !save.generate(true)) save.remove();
		return save;
	}

	public static boolean validBlockConfiguration(Block block)
	{
		if(!block.getType().equals(Material.IRON_BLOCK)) return false;
		if(!block.getRelative(1, 0, 0).getType().equals(Material.COBBLESTONE)) return false;
		if(!block.getRelative(-1, 0, 0).getType().equals(Material.COBBLESTONE)) return false;
		if(!block.getRelative(0, 0, 1).getType().equals(Material.COBBLESTONE)) return false;
		if(!block.getRelative(0, 0, -1).getType().equals(Material.COBBLESTONE)) return false;
		if(block.getRelative(1, 0, 1).getType().isSolid()) return false;
		return !block.getRelative(1, 0, -1).getType().isSolid() && !block.getRelative(-1, 0, 1).getType().isSolid() && !block.getRelative(-1, 0, -1).getType().isSolid();
	}

	public static class Listener implements org.bukkit.event.Listener
	{
		@EventHandler(priority = EventPriority.HIGH)
		public void createAndRemove(PlayerInteractEvent event)
		{
			if(event.getClickedBlock() == null) return;

			if(Demigods.isDisabledWorld(event.getPlayer().getWorld())) return;

			// Define variables
			Block clickedBlock = event.getClickedBlock();
			Location location = clickedBlock.getLocation();
			Player player = event.getPlayer();

			if(DPlayer.Util.isImmortal(player))
			{
				DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

				if(event.getAction() == Action.RIGHT_CLICK_BLOCK && character.getDeity().getInfo().getClaimItems().contains(event.getPlayer().getItemInHand().getType()) && Shrine.validBlockConfiguration(event.getClickedBlock()))
				{
					try
					{
						// Shrine created!
						Admins.sendDebug(ChatColor.RED + "Shrine created by " + character.getName() + " (" + character.getDeity() + ") at: " + ChatColor.GRAY + "(" + location.getWorld().getName() + ") " + location.getX() + ", " + location.getY() + ", " + location.getZ());
						Structure.Save save = EpisodeDemo.Structures.SHRINE.getStructure().createNew(location, true);
						save.setOwner(character);
						location.getWorld().strikeLightningEffect(location);

						player.sendMessage(ChatColor.GRAY + Demigods.language.getText(Translation.Text.CREATE_SHRINE_1).replace("{alliance}", "" + ChatColor.YELLOW + character.getAlliance() + "s" + ChatColor.GRAY));
						player.sendMessage(ChatColor.GRAY + Demigods.language.getText(Translation.Text.CREATE_SHRINE_2).replace("{deity}", "" + ChatColor.YELLOW + character.getDeity().getInfo().getName() + ChatColor.GRAY));
					}
					catch(Exception e)
					{
						// Creation of shrine failed...
						e.printStackTrace();
					}
				}
			}

			if(Admins.useWand(player) && Structures.partOfStructureWithType(location, "Shrine", true))
			{
				event.setCancelled(true);

				Structure.Save save = Structures.getStructureSave(location, true);
				DCharacter owner = save.getOwner();

				if(DataManager.hasTimed(player.getName(), "destroy_shrine"))
				{
					// Remove the Shrine
					save.remove();
					DataManager.removeTimed(player.getName(), "destroy_shrine");

					Admins.sendDebug(ChatColor.RED + "Shrine of (" + owner.getDeity() + ") at: " + ChatColor.GRAY + "(" + location.getWorld().getName() + ") " + location.getX() + ", " + location.getY() + ", " + location.getZ() + " removed.");

					player.sendMessage(ChatColor.GREEN + Demigods.language.getText(Translation.Text.ADMIN_WAND_REMOVE_SHRINE_COMPLETE));
				}
				else
				{
					DataManager.saveTimed(player.getName(), "destroy_shrine", true, 5);
					player.sendMessage(ChatColor.RED + Demigods.language.getText(Translation.Text.ADMIN_WAND_REMOVE_SHRINE));
				}
			}
		}
	}
}
