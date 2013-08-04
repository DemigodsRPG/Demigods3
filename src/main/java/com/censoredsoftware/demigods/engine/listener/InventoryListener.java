package com.censoredsoftware.demigods.engine.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.element.Ability;
import com.censoredsoftware.demigods.engine.player.DCharacter;
import com.censoredsoftware.demigods.engine.player.DPlayer;

public class InventoryListener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	private void onInventoryClickEvent(InventoryClickEvent event)
	{
		if(Demigods.isDisabledWorld(event.getWhoClicked().getLocation())) return;
		Player player = (Player) event.getWhoClicked();
		DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

		// Return if no character exists
		if(!DPlayer.Util.getPlayer(player).hasCurrent()) return;

		// Perform the check
		if(event.getSlotType().equals(InventoryType.SlotType.QUICKBAR) && event.getCurrentItem() != null && character.getMeta().isBound(event.getCurrentItem())) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerDropItemEvent(PlayerDropItemEvent event)
	{
		if(Demigods.isDisabledWorld(event.getPlayer().getLocation())) return;
		Player player = event.getPlayer();
		DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

		// Return if no character exists
		if(character == null) return;

		if(event.getItemDrop() != null && character.getMeta().isBound(event.getItemDrop().getItemStack())) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerDeathEvent(PlayerDeathEvent event)
	{
		if(Demigods.isDisabledWorld(event.getEntity().getLocation())) return;
		Player player = event.getEntity();
		DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

		// Return if no character exists
		if(character == null) return;

		// Remove the bound items from the drops
		for(Ability.Bind bind : character.getMeta().getBinds())
		{
			event.getDrops().remove(bind.getItem());
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerRespawn(PlayerRespawnEvent event)
	{
		if(Demigods.isDisabledWorld(event.getPlayer().getLocation())) return;
		Player player = event.getPlayer();
		final DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

		// Return if no character exists
		if(character == null) return;

		// Refresh the binds after a delay
		BukkitRunnable runnable = new BukkitRunnable()
		{
			@Override
			public void run()
			{
				character.refreshBinds();
			}
		};

		runnable.runTaskLater(Demigods.plugin, 20);
	}
}
