package com.censoredsoftware.demigods.conversation;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.Elements;
import com.censoredsoftware.demigods.helper.ListedConversation;
import com.censoredsoftware.demigods.language.Translation;
import com.censoredsoftware.demigods.location.DLocation;
import com.censoredsoftware.demigods.player.DCharacter;
import com.censoredsoftware.demigods.player.DPlayer;
import com.censoredsoftware.demigods.structure.Structure;
import com.censoredsoftware.demigods.util.Structures;
import com.censoredsoftware.demigods.util.Titles;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Obelisk implements ListedConversation
{
	public static Obelisk obelisk;
	static
	{
		obelisk = new Obelisk();
	}

	@Override
	public org.bukkit.event.Listener getUniqueListener()
	{
		return new Listener();
	}

	@Override
	public Conversation startMenu(Player player)
	{
		Map<Object, Object> conversationContext = Maps.newHashMap();

		Conversation prayerConversation = Demigods.conversation.withEscapeSequence("/exit").withLocalEcho(false).withInitialSessionData(conversationContext).withFirstPrompt(new StartPrayer()).buildConversation(player);
		prayerConversation.begin();

		return prayerConversation;
	}

	static class StartPrayer extends ValidatingPrompt
	{
		@Override
		public String getPromptText(ConversationContext context)
		{
			// Define variables
			Player player = (Player) context.getForWhom();

			// Clear chat
			Demigods.message.clearRawChat(player);

			// Send NoGrief menu
			Demigods.message.clearRawChat(player);
			player.sendRawMessage(ChatColor.AQUA + " -- Obelisk Menu --------------------------------------");
			player.sendRawMessage(" ");
			for(String message : Demigods.language.getTextBlock(Translation.Text.PRAYER_INTRO))
				player.sendRawMessage(message);
			player.sendRawMessage(" ");
			player.sendRawMessage(ChatColor.GRAY + " To begin, choose an option by entering its number in the chat:");
			player.sendRawMessage(" ");

			// for(Menu menu : Menu.values())
			// if(menu.getCategory().canUse(context)) player.sendRawMessage(ChatColor.GRAY + "   [" + menu.getId() + ".] " + menu.getCategory().getChatName());

			return "";
		}

		@Override
		protected boolean isInputValid(ConversationContext context, String message)
		{
			try
			{
				// Menu menu = Menu.getFromId(Character.toUpperCase(message.charAt(0)));
				// return menu != null && menu.getCategory().canUse(context);
			}
			catch(Exception ignored)
			{}
			return false;
		}

		@Override
		protected Prompt acceptValidatedInput(ConversationContext context, String message)
		{
			return null; // Menu.getFromId(Character.toUpperCase(message.charAt(0))).getCategory();
		}
	}

	// Warps
	static class ViewWarps extends ValidatingPrompt implements Elements.Conversations.Category
	{
		@Override
		public String getChatName()
		{
			return ChatColor.LIGHT_PURPLE + "View Warps " + ChatColor.GRAY + "(& Invites)";
		}

		@Override
		public boolean canUse(ConversationContext context)
		{
			return DPlayer.Util.getPlayer((Player) context.getForWhom()).getCurrent() != null;
		}

		@Override
		public String getPromptText(ConversationContext context)
		{
			// Define variables
			Player player = (Player) context.getForWhom();
			DCharacter character = DPlayer.Util.getPlayer((Player) context.getForWhom()).getCurrent();

			Demigods.message.clearRawChat(player);
			player.sendRawMessage(ChatColor.YELLOW + Titles.chatTitle("Viewing Warps & Invites"));
			player.sendRawMessage(" ");

			if(character.getMeta().hasWarps() || character.getMeta().hasInvites())
			{
				player.sendRawMessage(ChatColor.LIGHT_PURPLE + "  Light purple" + ChatColor.GRAY + " represents the warp(s) at this location.");
				player.sendRawMessage(" ");

				for(Map.Entry<String, Object> entry : character.getMeta().getWarps().entrySet())
				{
					Location location = DLocation.Util.load(UUID.fromString(entry.getValue().toString())).toLocation();
					player.sendRawMessage((player.getLocation().distance(location) < 8 ? ChatColor.LIGHT_PURPLE : ChatColor.GRAY) + "    " + StringUtils.capitalize(entry.getKey().toLowerCase()) + ChatColor.GRAY + " (" + StringUtils.capitalize(location.getWorld().getName().toLowerCase()) + ": " + Math.round(location.getX()) + ", " + Math.round(location.getY()) + ", " + Math.round(location.getZ()) + ")");
				}
				for(Map.Entry<String, Object> entry : character.getMeta().getInvites().entrySet())
				{
					Location location = DLocation.Util.load(UUID.fromString(entry.getValue().toString())).toLocation();
					player.sendRawMessage((player.getLocation().distance(location) < 8 ? ChatColor.LIGHT_PURPLE : ChatColor.GRAY) + "    " + StringUtils.capitalize(entry.getKey().toLowerCase()) + ChatColor.GRAY + " (" + StringUtils.capitalize(location.getWorld().getName().toLowerCase()) + ": " + Math.round(location.getX()) + ", " + Math.round(location.getY()) + ", " + Math.round(location.getZ()) + ") " + ChatColor.GREEN + "Invited by [ALLAN!!]"); // TODO: Invited by
				}

				player.sendRawMessage(" ");
				player.sendRawMessage(ChatColor.GRAY + "  Type " + ChatColor.YELLOW + "new <warp name>" + ChatColor.GRAY + " to create a warp at this Altar,");
				player.sendRawMessage(ChatColor.YELLOW + "  warp <warp name>" + ChatColor.GRAY + " to teleport to a warp, or " + ChatColor.YELLOW + "delete");
				player.sendRawMessage(ChatColor.YELLOW + "  <warp name>" + ChatColor.GRAY + " remove a warp. You can also invite a player");
				player.sendRawMessage(ChatColor.GRAY + "  by using " + ChatColor.YELLOW + "invite <player/character> <warp name>" + ChatColor.GRAY + ".");
			}
			else
			{
				player.sendRawMessage(ChatColor.RED + "    You have no warps or invites!");
				player.sendRawMessage(" ");
				player.sendRawMessage(ChatColor.GRAY + "  Type " + ChatColor.YELLOW + "new <warp name>" + ChatColor.GRAY + " to create a warp at this Altar.");
			}

			// Display notifications if available
			if(context.getSessionData("warp_notifications") != null && !((List<Translation.Text>) context.getSessionData("warp_notifications")).isEmpty())
			{
				// Grab the notifications
				List<Translation.Text> notifications = (List<Translation.Text>) context.getSessionData("warp_notifications");

				player.sendRawMessage(" ");

				// List them
				for(Translation.Text notification : notifications)
					player.sendRawMessage("  " + Demigods.language.getText(notification));

				// Remove them
				notifications.clear();
			}

			return "";
		}

		@Override
		protected boolean isInputValid(ConversationContext context, String message)
		{
			// Define variables
			DCharacter character = DPlayer.Util.getPlayer((Player) context.getForWhom()).getCurrent();
			String arg0 = message.split(" ")[0];
			String arg1 = message.split(" ").length >= 2 ? message.split(" ")[1] : null;
			String arg2 = message.split(" ").length >= 3 ? message.split(" ")[2] : null;

			return message.equalsIgnoreCase("menu") || arg0.equalsIgnoreCase("new") && StringUtils.isAlphanumeric(arg1) && !character.getMeta().getWarps().containsKey(arg1.toLowerCase()) || ((arg0.equalsIgnoreCase("warp") || arg0.equalsIgnoreCase("delete")) && (character.getMeta().getWarps().containsKey(arg1.toLowerCase()) || character.getMeta().getInvites().containsKey(arg1.toLowerCase())) || (arg0.equalsIgnoreCase("invite") && (DCharacter.Util.charExists(arg1) || DPlayer.Util.getPlayer(Bukkit.getOfflinePlayer(arg1)).getCurrent() != null) && arg2 != null && character.getMeta().getWarps().containsKey(arg2.toLowerCase())));
		}

		@Override
		protected Prompt acceptValidatedInput(ConversationContext context, String message)
		{
			// Define variables
			Player player = (Player) context.getForWhom();
			DCharacter character = DPlayer.Util.getPlayer((Player) context.getForWhom()).getCurrent();
			String arg0 = message.split(" ")[0];
			String arg1 = message.split(" ").length >= 2 ? message.split(" ")[1] : null;
			String arg2 = message.split(" ").length >= 3 ? message.split(" ")[2] : null;

			// Create and save the notification list
			context.setSessionData("warp_notifications", Lists.newArrayList());
			List<Translation.Text> notifications = (List<Translation.Text>) context.getSessionData("warp_notifications");

			Demigods.message.clearRawChat(player);

			if(message.equalsIgnoreCase("menu"))
			{
				// THEY WANT THE MENU!? SOCK IT TO 'EM!
				return new StartPrayer();
			}
			if(arg0.equalsIgnoreCase("new"))
			{
				// Save notification
				notifications.add(Translation.Text.NOTIFICATION_WARP_CREATED);

				// Add the warp
				character.getMeta().addWarp(arg1, player.getLocation());

				// Return to view warps
				return new ViewWarps();
			}
			else if(arg0.equalsIgnoreCase("delete"))
			{
				// Save notification
				notifications.add(Translation.Text.NOTIFICATION_WARP_DELETED);

				// Remove the warp/invite
				if(character.getMeta().getWarps().containsKey(arg1.toLowerCase())) character.getMeta().removeWarp(arg1);
				else if(character.getMeta().getInvites().containsKey(arg1.toLowerCase())) character.getMeta().removeInvite(arg1);

				// Return to view warps
				return new ViewWarps();
			}
			else if(arg0.equalsIgnoreCase("invite"))
			{
				// Save notification
				notifications.add(Translation.Text.NOTIFICATION_INVITE_SENT);

				// Define variables
				DCharacter invitee = DCharacter.Util.charExists(arg1) ? DCharacter.Util.getCharacterByName(arg1) : DPlayer.Util.getPlayer(Bukkit.getOfflinePlayer(arg1)).getCurrent();
				Location warp = DLocation.Util.load(UUID.fromString(character.getMeta().getWarps().get(arg2).toString())).toLocation();

				// Add the invite
				invitee.getMeta().addInvite(character.getName(), warp);

				// Message the player if they're online
				if(invitee.getOfflinePlayer().isOnline())
				{
					invitee.getOfflinePlayer().getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "You've been invited to a warp by " + character.getName() + "!");
					invitee.getOfflinePlayer().getPlayer().sendMessage(ChatColor.GRAY + "Go to an Altar to accept this invite.");
				}

				// Return to warps menu
				return new ViewWarps();
			}
			else if(arg0.equalsIgnoreCase("warp"))
			{
				// Disable prayer
				DPlayer.Util.stopPrayingSilent(player);

				// Teleport and message
				if(character.getMeta().getWarps().containsKey(arg1.toLowerCase())) player.teleport(DLocation.Util.load(UUID.fromString(character.getMeta().getWarps().get(arg1.toLowerCase()).toString())).toLocation());
				else if(character.getMeta().getInvites().containsKey(arg1.toLowerCase()))
				{
					player.teleport(DLocation.Util.load(UUID.fromString(character.getMeta().getInvites().get(arg1.toLowerCase()).toString())).toLocation());
					character.getMeta().removeInvite(arg1.toLowerCase());
				}
				player.sendMessage(ChatColor.GRAY + "Teleported to " + ChatColor.LIGHT_PURPLE + StringUtils.capitalize(arg1.toLowerCase()) + ChatColor.GRAY + ".");
			}
			return null;
		}
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
				player.sendRawMessage(ChatColor.RED + "Obelisk.");
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
