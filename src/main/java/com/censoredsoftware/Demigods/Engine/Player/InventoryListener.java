package com.censoredsoftware.Demigods.Engine.Player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;

public class InventoryListener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	private void onInventoryClickEvent(InventoryClickEvent event)
	{
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
		Player player = event.getPlayer();
		DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();

		// Return if no character exists
		if(character == null) return;

		if(event.getItemDrop() != null && character.getMeta().isBound(event.getItemDrop().getItemStack())) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	private void onPlayerDeathEvent(PlayerDeathEvent event)
	{
		// TODO: Save hot bar
	}
}
