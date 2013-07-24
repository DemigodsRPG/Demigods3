package com.censoredsoftware.Demigods.Engine.Listener;

import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.Ability.AbilityBind;
import com.censoredsoftware.Demigods.Engine.Object.Player.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.Object.Player.PlayerWrapper;
import com.censoredsoftware.Demigods.Engine.Utility.MiscUtility;
import com.google.common.collect.DiscreteDomains;
import com.google.common.collect.Ranges;

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
			Demigods.message.broadcast("Debug: " + (event.getInventory().getSize() - 9) + ", " + event.getInventory().getSize()); // TODO: Debug

			Set<Integer> hotBar = Ranges.closed(event.getInventory().getSize() - 9, event.getInventory().getSize()).asSet(DiscreteDomains.integers());
			Demigods.message.broadcast("Debug: " + MiscUtility.getIndex(hotBar, bind.getSlot())); // TODO: Debug
			if(bind.getSlot() == MiscUtility.getIndex(hotBar, bind.getSlot())) event.setCancelled(true);
		}
	}
}
