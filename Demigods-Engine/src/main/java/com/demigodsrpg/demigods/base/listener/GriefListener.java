package com.demigodsrpg.demigods.base.listener;

import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.demigodsrpg.demigods.engine.location.DemigodsLocation;
import com.demigodsrpg.demigods.engine.structure.DemigodsStructure;
import com.demigodsrpg.demigods.engine.structure.DemigodsStructureType;
import com.demigodsrpg.demigods.engine.util.Zones;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GriefListener implements Listener
{
	private static final Set<Material> BLOCK_INVENTORIES = new HashSet<Material>()
	{
		{
			add(Material.CHEST);
			add(Material.ENDER_CHEST);
			add(Material.FURNACE);
			add(Material.BURNING_FURNACE);
			add(Material.DISPENSER);
			add(Material.DROPPER);
			add(Material.BREWING_STAND);
			add(Material.BEACON);
			add(Material.HOPPER);
			add(Material.HOPPER_MINECART);
			add(Material.STORAGE_MINECART);
		}
	};

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if(Zones.inNoDemigodsZone(event.getBlock().getLocation())) return;
		DemigodsStructure save = Iterables.getFirst(DemigodsStructureType.Util.getInRadiusWithFlag(event.getBlock().getLocation(), DemigodsStructureType.Flag.NO_GRIEFING), null);
		if(save != null && save.hasMembers())
		{
			DemigodsCharacter character = DemigodsCharacter.of(event.getPlayer());
			Collection<UUID> members = save.getSanctifiers();
			if(character != null && members.contains(character.getId())) return;
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent event)
	{
		Location location = event.getBlock().getLocation();
		if(Zones.inNoDemigodsZone(location) || DemigodsStructureType.Util.partOfStructureWithFlag(location, DemigodsStructureType.Flag.DESTRUCT_ON_BREAK)) return;
		DemigodsStructure save = Iterables.getFirst(DemigodsStructureType.Util.getInRadiusWithFlag(location, DemigodsStructureType.Flag.NO_GRIEFING), null);
		if(save != null && save.hasMembers())
		{
			DemigodsCharacter character = DemigodsCharacter.of(event.getPlayer());
			Collection<UUID> members = save.getSanctifiers();
			if(character != null && members.contains(character.getId())) return;
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockIgnite(BlockIgniteEvent event)
	{
		if(Zones.inNoDemigodsZone(event.getBlock().getLocation())) return;
		if(event.getPlayer() == null)
		{
			event.setCancelled(true);
			return;
		}
		DemigodsStructure save = Iterables.getFirst(DemigodsStructureType.Util.getInRadiusWithFlag(event.getBlock().getLocation(), DemigodsStructureType.Flag.NO_GRIEFING), null);
		if(save != null && save.hasMembers())
		{
			DemigodsCharacter character = DemigodsCharacter.of(event.getPlayer());
			Collection<UUID> members = save.getSanctifiers();
			if(character != null && members.contains(character.getId())) return;
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBurn(BlockBurnEvent event)
	{
		if(Zones.inNoDemigodsZone(event.getBlock().getLocation())) return;
		if(DemigodsStructureType.Util.isInRadiusWithFlag(event.getBlock().getLocation(), DemigodsStructureType.Flag.NO_GRIEFING)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockFall(EntityChangeBlockEvent event)
	{
		if(Zones.inNoDemigodsZone(event.getBlock().getLocation())) return;
		if(event.getEntityType() != EntityType.FALLING_BLOCK || event.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.AIR)) return;
		FallingBlock block = (FallingBlock) event.getEntity();
		Location blockLocation = block.getLocation();
		if(DemigodsStructureType.Util.isInRadiusWithFlag(DemigodsLocation.getFloorBelowLocation(block.getLocation()), DemigodsStructureType.Flag.NO_GRIEFING))
		{
			// Break the block
			event.setCancelled(true);
			event.getBlock().setType(Material.AIR);
			blockLocation.getWorld().dropItemNaturally(blockLocation, new ItemStack(block.getMaterial()));
			block.remove();
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPistonExtend(BlockPistonExtendEvent event)
	{
		if(Zones.inNoDemigodsZone(event.getBlock().getLocation())) return;
		boolean piston = DemigodsStructureType.Util.isInRadiusWithFlag(event.getBlock().getLocation(), DemigodsStructureType.Flag.NO_GRIEFING);
		boolean blocks = Iterables.any(event.getBlocks(), piston ? new Predicate<Block>()
		{
			@Override
			public boolean apply(Block block)
			{
				return !DemigodsStructureType.Util.isInRadiusWithFlag(block.getLocation(), DemigodsStructureType.Flag.NO_GRIEFING);
			}
		} : new Predicate<Block>()
		{
			@Override
			public boolean apply(Block block)
			{
				return DemigodsStructureType.Util.isInRadiusWithFlag(block.getLocation(), DemigodsStructureType.Flag.NO_GRIEFING);
			}
		});
		if(piston == (piston ? blocks : !blocks)) event.setCancelled(true); // Am I overthinking this?
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPistonRetract(BlockPistonRetractEvent event)
	{
		if(Zones.inNoDemigodsZone(event.getBlock().getLocation())) return;
		boolean block = DemigodsStructureType.Util.isInRadiusWithFlag(event.getBlock().getLocation(), DemigodsStructureType.Flag.NO_GRIEFING);
		boolean retract = DemigodsStructureType.Util.isInRadiusWithFlag(event.getRetractLocation(), DemigodsStructureType.Flag.NO_GRIEFING);
		if(block != retract) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockDamage(BlockDamageEvent event)
	{
		Location location = event.getBlock().getLocation();
		if(Zones.inNoDemigodsZone(location)) return;
		DemigodsStructure save = Iterables.getFirst(DemigodsStructureType.Util.getInRadiusWithFlag(location, DemigodsStructureType.Flag.NO_GRIEFING), null);
		if(save != null && save.hasMembers())
		{
			if(DemigodsStructureType.Util.partOfStructureWithAllFlags(location, DemigodsStructureType.Flag.NO_GRIEFING, DemigodsStructureType.Flag.DESTRUCT_ON_BREAK)) return;
			DemigodsCharacter character = DemigodsCharacter.of(event.getPlayer());
			Collection<UUID> members = save.getSanctifiers();
			if(character != null && members.contains(character.getId())) return;
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteract(final PlayerInteractEvent event)
	{
		if(Zones.inNoDemigodsZone(event.getPlayer().getLocation())) return;
		DemigodsStructure save = event.getAction().equals(Action.LEFT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK) ? Iterables.getFirst(DemigodsStructureType.Util.getInRadiusWithFlag(event.getClickedBlock().getLocation(), DemigodsStructureType.Flag.NO_GRIEFING), null) : Iterables.getFirst(DemigodsStructureType.Util.getInRadiusWithFlag(event.getPlayer().getLocation(), DemigodsStructureType.Flag.NO_GRIEFING), null);
		if(save != null && save.hasMembers())
		{
			if(DemigodsStructureType.Util.partOfStructureWithAllFlags(event.getPlayer().getLocation(), DemigodsStructureType.Flag.NO_GRIEFING, DemigodsStructureType.Flag.DESTRUCT_ON_BREAK)) return;
			DemigodsCharacter character = DemigodsCharacter.of(event.getPlayer());
			Collection<UUID> members = save.getSanctifiers();
			if(character != null && members.contains(character.getId())) return;
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityInteract(final EntityInteractEvent event)
	{
		if(Zones.inNoDemigodsZone(event.getEntity().getLocation()) || !(event.getEntity() instanceof Projectile) || DemigodsStructureType.Util.getInRadiusWithFlag(event.getBlock().getLocation(), DemigodsStructureType.Flag.NO_GRIEFING) == null) return;
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityExplode(EntityExplodeEvent event)
	{
		if(event.getEntity() == null || Zones.inNoDemigodsZone(event.getEntity().getLocation())) return;
		if(Iterables.any(event.blockList(), new Predicate<Block>()
		{
			@Override
			public boolean apply(Block block)
			{
				return DemigodsStructureType.Util.isInRadiusWithFlag(block.getLocation(), DemigodsStructureType.Flag.NO_GRIEFING);
			}
		})) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onItemDropEvent(PlayerDropItemEvent event)
	{
		if(Zones.inNoDemigodsZone(event.getPlayer().getLocation())) return;
		DemigodsStructure save = Iterables.getFirst(DemigodsStructureType.Util.getInRadiusWithFlag(event.getPlayer().getLocation(), DemigodsStructureType.Flag.NO_GRIEFING), null);
		if(save != null && save.hasMembers())
		{
			DemigodsCharacter character = DemigodsCharacter.of(event.getPlayer());
			Collection<UUID> members = save.getSanctifiers();
			if(character != null && members.contains(character.getId())) return;
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onAttemptInteractEntity(PlayerInteractEntityEvent event)
	{
		if(Zones.inNoDemigodsZone(event.getPlayer().getLocation())) return;
		Entity entity = event.getRightClicked();
		DemigodsStructure save = Iterables.getFirst(DemigodsStructureType.Util.getInRadiusWithFlag(entity.getLocation(), DemigodsStructureType.Flag.NO_GRIEFING), null);
		if(save != null && save.hasMembers())
		{
			DemigodsCharacter character = DemigodsCharacter.of(event.getPlayer());
			Collection<UUID> members = save.getSanctifiers();
			if(character != null && members.contains(character.getId())) return;
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onAttemptInventoryOpen(PlayerInteractEvent event)
	{
		if(Zones.inNoDemigodsZone(event.getPlayer().getLocation())) return;
		if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
		Block block = event.getClickedBlock();
		DemigodsStructure save = Iterables.getFirst(DemigodsStructureType.Util.getInRadiusWithFlag(block.getLocation(), DemigodsStructureType.Flag.NO_GRIEFING), null);
		if(save == null || !save.hasMembers()) return;
		if(BLOCK_INVENTORIES.contains(block.getType()))
		{
			DemigodsCharacter character = DemigodsCharacter.of(event.getPlayer());
			Collection<UUID> members = save.getSanctifiers();
			if(character != null && members.contains(character.getId())) return;
			event.setCancelled(true);
		}
	}
}
