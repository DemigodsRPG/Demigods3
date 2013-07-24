package com.censoredsoftware.Demigods.Engine.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

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

		// if(event.getClickedInventory() instanceof PlayerInventory)
		// {
		// PlayerInventory inventory = (PlayerInventory) event.getClickedInventory();
		// Demigods.message.broadcast("Clicked slot: " + event.getSlot() + "/" + event.getRawSlot());
		// Demigods.message.broadcast("Item: " + inventory.getItem(event.getSlot()));
		// Demigods.message.broadcast("Size: " + inventory.getSize());
		// }

		// Set<Integer> hotBar = Ranges.closed(event.getClickedInventory().getSize() - 9, event.getClickedInventory().getSize()).asSet(DiscreteDomains.integers());

		// Return if no character exists
		if(character == null) return;

		for(AbilityBind bind : character.getMeta().getBinds())
		{
			if(event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasLore() && event.getCurrentItem().getItemMeta().getLore().toString().contains(bind.getIdentifier())) event.setCancelled(true);
		}
	}
}
