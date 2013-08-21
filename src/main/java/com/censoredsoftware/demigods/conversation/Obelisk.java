package com.censoredsoftware.demigods.conversation;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.helper.ListedConversation;
import com.censoredsoftware.demigods.language.Translation;
import com.censoredsoftware.demigods.player.DPlayer;
import com.censoredsoftware.demigods.structure.Structure;
import com.censoredsoftware.demigods.util.Structures;
import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Obelisk implements ListedConversation
{
	public static Obelisk obelisk;
	static
	{
		obelisk = new Obelisk();
	}

	// Define variables
	private static Conversation conversation;

	@Override
	public org.bukkit.event.Listener getUniqueListener()
	{
		return new Listener();
	}

	@Override
	public Conversation startMenu(Player player)
	{
		return null;
	}

	// TODO Everything.

	public static class Listener implements org.bukkit.event.Listener
	{
		@EventHandler(priority = EventPriority.HIGH)
		public void prayerInteract(PlayerInteractEvent event)
		{
			if(event.getClickedBlock() == null || event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

			// Define variables
			Player player = event.getPlayer();

			// First we check if the player is clicking a no grief block block
			if(Structures.isClickableBlockWithFlag(event.getClickedBlock().getLocation(), Structure.Flag.NO_GRIEFING))
			{
				if(!DPlayer.Util.isPraying(player))
				{
					// Toggle praying
					DPlayer.Util.startPraying(player, obelisk, true);

					// Tell nearby players that the user is praying
					for(Entity entity : player.getNearbyEntities(20, 20, 20))
						if(entity instanceof Player) ((Player) entity).sendMessage(ChatColor.AQUA + Demigods.language.getText(Translation.Text.KNELT_FOR_PRAYER).replace("{player}", ChatColor.stripColor(player.getDisplayName())));
				}
				else if(DPlayer.Util.isPraying(player))
				{
					// Toggle prayer to false
					DPlayer.Util.startPraying(player, obelisk, false);
				}

				event.setCancelled(true);
			}
		}
	}
}
