package com.censoredsoftware.Demigods.Engine.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.Ability.AbilityBind;
import com.censoredsoftware.Demigods.Engine.Object.Player.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.Object.Player.PlayerWrapper;

public class InventoryListener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	private void onInventoryClickEvent(InventoryClickEvent event)
	{
		Player player = (Player) event.getWhoClicked();
		PlayerCharacter character = PlayerWrapper.getPlayer(player).getCurrent();

		// Return if no character exists
		if(character == null) return;

		for(AbilityBind bind : character.getMeta().getBinds())
		{
			// Set<Integer> hotBar = Ranges.closed(event.getClickedInventory().getSize(), event.getClickedInventory().getSize() + 9).asSet(DiscreteDomains.integers());

			Demigods.message.broadcast("Range: " + event.getInventory().getSize() + ", " + (event.getClickedInventory().getSize() + 9));
			Demigods.message.broadcast("Slot: " + (event.getSlot() - 9));
			Demigods.message.broadcast("Raw Slot: " + event.getRawSlot());

			if(bind.getSlot() == (event.getSlot() - 9)) event.setCancelled(true);
		}
	}
}
