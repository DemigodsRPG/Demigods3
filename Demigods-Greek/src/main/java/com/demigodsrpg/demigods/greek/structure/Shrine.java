package com.demigodsrpg.demigods.greek.structure;

import com.censoredsoftware.library.schematic.Schematic;
import com.censoredsoftware.library.schematic.Selection;
import com.censoredsoftware.library.util.Colors;
import com.demigodsrpg.demigods.engine.conversation.Administration;
import com.demigodsrpg.demigods.engine.data.TimedServerData;
import com.demigodsrpg.demigods.engine.deity.Deity;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsPlayer;
import com.demigodsrpg.demigods.engine.structure.DemigodsStructure;
import com.demigodsrpg.demigods.engine.structure.DemigodsStructureType;
import com.demigodsrpg.demigods.engine.util.Configs;
import com.demigodsrpg.demigods.engine.util.Messages;
import com.demigodsrpg.demigods.engine.util.Zones;
import com.demigodsrpg.demigods.greek.language.English;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Shrine extends GreekStructureType
{
	private static final String name = "Shrine";
	private static final Function<Location, GreekStructureType.Design> getDesign = new Function<Location, GreekStructureType.Design>()
	{
		@Override
		public GreekStructureType.Design apply(Location reference)
		{
			switch(reference.getBlock().getBiome())
			{
				case HELL:
					return ShrineDesign.NETHER;
				default:
					return ShrineDesign.GENERAL;
			}
		}
	};
	private static final Function<GreekStructureType.Design, DemigodsStructure> createNew = new Function<GreekStructureType.Design, DemigodsStructure>()
	{
		@Override
		public DemigodsStructure apply(GreekStructureType.Design design)
		{
			DemigodsStructure save = new DemigodsStructure();
			save.setSanctifiers(new HashMap<String, Long>());
			save.setCorruptors(new HashMap<String, Long>());
			return save;
		}
	};
	private static final DemigodsStructureType.InteractFunction<Boolean> sanctify = new DemigodsStructureType.InteractFunction<Boolean>()
	{
		@Override
		public Boolean apply(DemigodsStructure data, DemigodsCharacter character)
		{
			if(!character.alliedTo(DemigodsCharacter.get(data.getOwner()))) return false;
			Location location = data.getBukkitLocation();
			location.getWorld().playSound(location, Sound.CAT_PURREOW, 0.7F, 0.9F);
			MaterialData colorData = Colors.getMaterial(character.getDeity().getColor());
			location.getWorld().playEffect(location.clone().add(0, 1, 0), Effect.STEP_SOUND, colorData.getItemTypeId(), colorData.getData());
			return true;
		}
	};
	private static final DemigodsStructureType.InteractFunction<Boolean> corrupt = new DemigodsStructureType.InteractFunction<Boolean>()
	{
		@Override
		public Boolean apply(DemigodsStructure data, DemigodsCharacter character)
		{
			if(character.alliedTo(DemigodsCharacter.get(data.getOwner()))) return false;
			Location location = data.getBukkitLocation();
			location.getWorld().playSound(location, Sound.WITHER_HURT, 0.4F, 1.5F);
			location.getWorld().playEffect(location.clone().add(0, 1, 0), Effect.STEP_SOUND, Material.REDSTONE_BLOCK.getId());
			character.getBukkitOfflinePlayer().getPlayer().sendMessage(ChatColor.RED + "This shrine has " + (data.getSanctity() - data.getCorruption()) + " sanctity left!");
			return true;
		}
	};
	private static final DemigodsStructureType.InteractFunction<Boolean> birth = new DemigodsStructureType.InteractFunction<Boolean>()
	{
		@Override
		public Boolean apply(DemigodsStructure data, DemigodsCharacter character)
		{
			Location location = data.getBukkitLocation();
			location.getWorld().strikeLightningEffect(location);
			location.getWorld().strikeLightningEffect(character.getLocation());
			return true;
		}
	};
	private static final DemigodsStructureType.InteractFunction<Boolean> kill = new DemigodsStructureType.InteractFunction<Boolean>()
	{
		@Override
		public Boolean apply(DemigodsStructure data, DemigodsCharacter character)
		{
			Location location = data.getBukkitLocation();
			location.getWorld().playSound(location, Sound.WITHER_DEATH, 1F, 1.2F);
			location.getWorld().createExplosion(location, 2F, false);
			character.addKill();
			return true;
		}
	};
	private static final Set<DemigodsStructureType.Flag> flags = new HashSet<DemigodsStructureType.Flag>()
	{
		{
			add(DemigodsStructureType.Flag.DELETE_WITH_OWNER);
			add(DemigodsStructureType.Flag.DESTRUCT_ON_BREAK);
			add(DemigodsStructureType.Flag.TRIBUTE_LOCATION);
		}
	};
	private static final Listener listener = new Listener()
	{
		@EventHandler(priority = EventPriority.HIGH)
		public void createAndRemove(PlayerInteractEvent event)
		{
			if(event.getClickedBlock() == null) return;

			if(Zones.inNoDemigodsZone(event.getPlayer().getLocation())) return;

			// Define variables
			Block clickedBlock = event.getClickedBlock();
			Location location = clickedBlock.getLocation();
			Player player = event.getPlayer();

			if(DemigodsPlayer.isImmortal(player))
			{
				DemigodsCharacter character = DemigodsCharacter.of(player);

				if(event.getAction() == Action.RIGHT_CLICK_BLOCK && !character.getDeity().getFlags().contains(Deity.Flag.NO_SHRINE) && character.getDeity().getClaimItems().keySet().contains(event.getPlayer().getItemInHand().getType()) && Util.validBlockConfiguration(event.getClickedBlock()))
				{
					try
					{
						// Shrine created!
						DemigodsStructure save = inst().createNew(true, null, location);
						save.setOwner(character.getId());
						inst().birth(save, character);

						// Log the generation
						Messages.info(com.demigodsrpg.demigods.engine.language.English.LOG_STRUCTURE_CREATED.getLine().replace("{structure}", name + " (" + character.getDeity() + ")").replace("{locX}", location.getX() + "").replace("{locY}", location.getY() + "").replace("{locZ}", location.getZ() + "").replace("{world}", location.getWorld().getName()).replace("{creator}", player.getName()));

						// Consume item in hand
						ItemStack item = player.getItemInHand();
						if(item.getAmount() > 1)
						{
							player.getItemInHand().setAmount(item.getAmount() - 1);
						}
						else
						{
							player.setItemInHand(new ItemStack(Material.AIR));
						}

						for(String string : English.NOTIFICATION_SHRINE_CREATED.getLines())
							player.sendMessage(string.replace("{alliance}", character.getAlliance() + "s").replace("{deity}", character.getDeity().getName()));
						event.setCancelled(true);
					}
					catch(Exception errored)
					{
						// Creation of shrine failed...
						Messages.warning(errored.getMessage());
					}
				}
			}

			if(Administration.Util.useWand(player) && DemigodsStructureType.Util.partOfStructureWithType(location, "Shrine"))
			{
				event.setCancelled(true);

				DemigodsStructure save = DemigodsStructureType.Util.getStructureRegional(location);
				DemigodsCharacter owner = DemigodsCharacter.get(save.getOwner());

				if(TimedServerData.exists(player.getName(), "destroy_shrine"))
				{
					// Remove the Shrine
					save.remove();
					TimedServerData.remove(player.getName(), "destroy_shrine");

					// Log the generation
					Messages.info(com.demigodsrpg.demigods.engine.language.English.LOG_STRUCTURE_REMOVED.getLine().replace("{structure}", name + " (" + owner.getDeity() + ")").replace("{locX}", location.getX() + "").replace("{locY}", location.getY() + "").replace("{locZ}", location.getZ() + "").replace("{world}", location.getWorld().getName()).replace("{creator}", player.getName()));

					// Tell the administrator
					player.sendMessage(ChatColor.GREEN + English.ADMIN_WAND_REMOVE_SHRINE_COMPLETE.getLine());
				}
				else
				{
					TimedServerData.saveTimed(player.getName(), "destroy_shrine", true, 5, TimeUnit.SECONDS);
					player.sendMessage(ChatColor.RED + English.ADMIN_WAND_REMOVE_SHRINE.getLine());
				}
			}
		}
	};
	private static final int radius = Configs.getSettingInt("zones.shrine_radius");
	private static final Predicate<Player> allow = new Predicate<Player>()
	{
		@Override
		public boolean apply(Player player)
		{
			return true;
		}
	};
	private static final float sanctity = 250F, sanctityRegen = 1F;

	private static final Schematic general = new Schematic("general", "_Alex", 2)
	{
		{
			// Create the main block
			add(new Selection(0, 1, 0, Material.GOLD_BLOCK));

			// Create the ender chest and the block below
			add(new Selection(0, 0, 0, Material.ENDER_CHEST));
			add(new Selection(0, -1, 0, Material.SMOOTH_BRICK));

			// Create the rest
			add(new Selection(-1, 0, 0, Material.SMOOTH_STAIRS));
			add(new Selection(1, 0, 0, Material.SMOOTH_STAIRS, (byte) 1));
			add(new Selection(0, 0, -1, Material.SMOOTH_STAIRS, (byte) 2));
			add(new Selection(0, 0, 1, Material.SMOOTH_STAIRS, (byte) 3));
		}
	};
	private static final Schematic nether = new Schematic("nether", "HmmmQuestionMark", 2)
	{
		{
			// Create the main block
			add(new Selection(0, 1, 0, Material.GOLD_BLOCK));

			// Create the ender chest and the block below
			add(new Selection(0, 0, 0, Material.ENDER_CHEST));
			add(new Selection(0, -1, 0, Material.NETHER_BRICK));

			// Create the rest
			add(new Selection(-1, 0, 0, Material.NETHER_BRICK_STAIRS));
			add(new Selection(1, 0, 0, Material.NETHER_BRICK_STAIRS, (byte) 1));
			add(new Selection(0, 0, -1, Material.NETHER_BRICK_STAIRS, (byte) 2));
			add(new Selection(0, 0, 1, Material.NETHER_BRICK_STAIRS, (byte) 3));
		}
	};

	private static final int generationPoints = 1;

	private Shrine()
	{
		super(name, ShrineDesign.values(), getDesign, createNew, sanctify, corrupt, birth, kill, flags, listener, radius, allow, sanctity, sanctityRegen, generationPoints);
	}

	public static enum ShrineDesign implements GreekStructureType.Design
	{
		GENERAL("general", general, new Selection(0, 1, 0)), NETHER("nether", nether, new Selection(0, 1, 0));

		private final String name;
		private final Schematic schematic;
		private final Selection clickableBlocks;

		private ShrineDesign(String name, Schematic schematic, Selection clickableBlocks)
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
		public Schematic getSchematic(DemigodsStructure unused)
		{
			return schematic;
		}
	}

	public static class Util
	{
		public static boolean validBlockConfiguration(Block block)
		{
			if(!block.getType().equals(Material.GOLD_BLOCK)) return false;
			if(!block.getRelative(1, 0, 0).getType().equals(Material.COBBLESTONE)) return false;
			if(!block.getRelative(-1, 0, 0).getType().equals(Material.COBBLESTONE)) return false;
			if(!block.getRelative(0, 0, 1).getType().equals(Material.COBBLESTONE)) return false;
			if(!block.getRelative(0, 0, -1).getType().equals(Material.COBBLESTONE)) return false;
			return !block.getRelative(1, 0, 1).getType().isSolid() && !block.getRelative(1, 0, -1).getType().isSolid() && !block.getRelative(-1, 0, 1).getType().isSolid() && !block.getRelative(-1, 0, -1).getType().isSolid();
		}
	}

	private static final DemigodsStructureType INST = new Shrine();

	public static DemigodsStructureType inst()
	{
		return INST;
	}
}
