package com.censoredsoftware.demigods.engine.conversation;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.censoredsoftware.censoredlib.helper.WrappedConversation;
import com.censoredsoftware.censoredlib.util.Titles;
import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.base.DemigodsConversation;
import com.censoredsoftware.demigods.engine.data.DPlayer;
import com.censoredsoftware.demigods.engine.data.Data;
import com.censoredsoftware.demigods.engine.language.English;
import com.censoredsoftware.demigods.engine.util.Admins;
import com.censoredsoftware.demigods.engine.util.Configs;
import com.censoredsoftware.demigods.engine.util.Messages;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@SuppressWarnings("unchecked")
public class Administration implements WrappedConversation
{
	// Define constants
	private static final String CONTEXT_NAME = "admin_context";

	@Override
	public org.bukkit.event.Listener getUniqueListener()
	{
		return new Listener();
	}

	/**
	 * Defines categories that can be used during administration.
	 */
	public enum Menu
	{
		STRUCTURE_WAND('1', new StructureWand());

		private final char id;
		private final DemigodsConversation.Category category;

		private Menu(char id, DemigodsConversation.Category category)
		{
			this.id = id;
			this.category = category;
		}

		public char getId()
		{
			return this.id;
		}

		public DemigodsConversation.Category getCategory()
		{
			return this.category;
		}

		public static Menu getFromId(char id)
		{
			for(Menu menu : Menu.values())
				if(menu.getId() == id) return menu;
			return null;
		}
	}

	@Override
	public Conversation startMenu(Player player)
	{
		return startAdministration(player);
	}

	public static Map<Object, Object> grabRawContext(Player player)
	{
		Map<Object, Object> context = Maps.newHashMap();

		try
		{
			if(!Demigods.Util.isRunningSpigot())
			{
				// Compatibility with vanilla Bukkit
				Field sessionDataField = ConversationContext.class.getDeclaredField("sessionData");
				sessionDataField.setAccessible(true);
				if(Data.hasKeyTemp(player.getName(), CONTEXT_NAME)) context = (Map<Object, Object>) sessionDataField.get(Data.getValueTemp(player.getName(), CONTEXT_NAME));
			}
			else
			{
				// Grab the context Map
				if(Data.hasKeyTemp(player.getName(), CONTEXT_NAME)) context.putAll(((ConversationContext) Data.getValueTemp(player.getName(), CONTEXT_NAME)).getAllSessionData());
			}
		}
		catch(IllegalAccessException | NoSuchFieldException ignored)
		{
			// ignored
		}

		return context;
	}

	public static void saveNotification(ConversationContext context, String menuName, String notification)
	{
		// Create the data
		if(context.getSessionData(menuName + "_notifications") == null)
		{
			context.setSessionData(menuName + "_notifications", Lists.newArrayList());
		}

		// Save the notification
		((List<String>) context.getSessionData(menuName + "_notifications")).add(notification);
	}

	public static void displayNotifications(ConversationContext context, Player player, String menuName)
	{
		if(context.getSessionData(menuName + "_notifications") != null && !((List<String>) context.getSessionData(menuName + "_notifications")).isEmpty())
		{
			// Grab the notifications
			List<String> notifications = (List<String>) context.getSessionData(menuName + "_notifications");

			player.sendRawMessage(" ");

			// List them
			for(String notification : notifications)
				player.sendRawMessage("  " + notification);

			// Remove them
			notifications.clear();
		}
	}

	public static Conversation startAdministration(Player player)
	{
		// Build the conversation and begin
		Conversation conversation = Demigods.CONVERSATION_FACTORY.withEscapeSequence("/exit").withLocalEcho(false).withInitialSessionData(grabRawContext(player)).withFirstPrompt(new StartAdministration()).buildConversation(player);

		// Save the context
		DPlayer.Util.saveAdministrationContext(player, conversation.getContext());

		// Begin
		conversation.begin();

		return conversation;
	}

	// Main menu
	static class StartAdministration extends ValidatingPrompt
	{
		@Override
		public String getPromptText(ConversationContext context)
		{
			// Define variables
			Player player = (Player) context.getForWhom();

			// Clear chat
			Messages.clearRawChat(player);

			// Send menu
			Messages.clearRawChat(player);
			player.sendRawMessage(ChatColor.RED + " -- Administration -------------------------------------");
			player.sendRawMessage(" ");
			for(String message : English.ADMINISTRATION_INTRO.getLines())
				player.sendRawMessage(message);
			player.sendRawMessage(" ");
			player.sendRawMessage(ChatColor.GRAY + " To begin, choose an option by entering its number in the chat:");
			player.sendRawMessage(" ");

			// Send menu options
			for(Menu menu : Menu.values())
				if(menu.getCategory().canUse(context)) player.sendRawMessage(ChatColor.GRAY + "   [" + menu.getId() + ".] " + menu.getCategory().getChatName(context));

			// Send menu intro footer
			player.sendRawMessage(" ");
			player.sendRawMessage(English.ADMINISTRATION_INTRO_FOOTER.getLine());

			return "";
		}

		@Override
		protected boolean isInputValid(ConversationContext context, String message)
		{
			try
			{
				Menu menu = Menu.getFromId(Character.toUpperCase(message.charAt(0)));
				return menu != null && menu.getCategory().canUse(context) || "leave".equalsIgnoreCase(message);
			}
			catch(Exception ignored)
			{
				// ignored
			}
			return false;
		}

		@Override
		protected Prompt acceptValidatedInput(ConversationContext context, String message)
		{
			// Handle leaving
			if("leave".equalsIgnoreCase(message))
			{
				// Toggle everything off
				DPlayer.Util.toggleAdministration((Player) context.getForWhom(), false, true);
				return null;
			}

			// Otherwise return the chosen prompt
			return Menu.getFromId(Character.toUpperCase(message.charAt(0))).getCategory();
		}
	}

	// Structure wand
	static class StructureWand extends ValidatingPrompt implements DemigodsConversation.Category
	{
		private static final String DATA_NAME = "structuredata";

		@Override
		public String getChatName(ConversationContext context)
		{
			return ChatColor.GREEN + "Use Structure Wand";
		}

		@Override
		public boolean canUse(ConversationContext context)
		{
			return ((Player) context.getForWhom()).hasPermission("demigods.admin.structurewand") && !Admins.structureWandEnabled((Player) context.getForWhom());
		}

		@Override
		public String getPromptText(ConversationContext context)
		{
			// Define variables
			Player player = (Player) context.getForWhom();

			// Enable the wand
			Admins.toggleStructureWand(player, true);

			// Send the messages
			Messages.clearRawChat(player);
			player.sendRawMessage(ChatColor.YELLOW + Titles.chatTitle("Structure Wand"));
			player.sendRawMessage(" ");
			for(String string : English.ADMINISTRATION_STRUCTURE_WAND_ENABLED.getLines())
			{
				player.sendRawMessage(string.replace("{item}", Material.getMaterial(Configs.getSettingInt("admin.structure_wand_tool")).name()));
			}

			// Display notifications if available
			displayNotifications(context, player, DATA_NAME);

			return "";
		}

		@Override
		protected boolean isInputValid(ConversationContext context, String message)
		{
			return "menu".equalsIgnoreCase(message) || "continue".equalsIgnoreCase(message);
		}

		@Override
		protected Prompt acceptValidatedInput(ConversationContext context, String message)
		{
			// Define variables
			Player player = (Player) context.getForWhom();
			String[] splitMsg = message.split(" ");

			if("menu".equalsIgnoreCase(message))
			{
				// Disable wand
				Admins.toggleStructureWand(player, false);

				// Return to main menu
				return new StartAdministration();
			}
			else if("continue".equalsIgnoreCase(splitMsg[0]))
			{
				// Get locations from context
				Object locObj1 = context.getSessionData("structurewand_loc1");
				Object locObj2 = context.getSessionData("structurewand_loc2");

				// Ensure that selections have been made
				if(locObj1 != null && locObj2 != null)
				{
					// All good, toggle wand off
					Admins.toggleStructureWand(player, false);

					// Continue
					return new StructureChooser();
				}
				else
				{
					// Add notification
					saveNotification(context, DATA_NAME, English.ADMINISTRATION_LOCATIONS_NOT_SELECTED.getLine());
				}
			}

			return new StructureWand();
		}
	}

	// Structure wand
	static class StructureChooser extends ValidatingPrompt
	{
		@Override
		public String getPromptText(ConversationContext context)
		{
			// Define variables
			Player player = (Player) context.getForWhom();
			Location loc1 = (Location) context.getSessionData("structurewand_loc1");
			Location loc2 = (Location) context.getSessionData("structurewand_loc2");

			// Send the messages
			Messages.clearRawChat(player);
			player.sendRawMessage(ChatColor.YELLOW + Titles.chatTitle("Structure Chooser"));
			player.sendRawMessage(" ");
			for(String string : English.ADMINISTRATION_STRUCTURE_CHOOSER_INTRO.getLines())
			{
				player.sendRawMessage(string.replace("{loc1X}", "" + loc1.getX()).replace("{loc1Y}", "" + loc1.getY()).replace("{loc1Z}", "" + loc1.getZ()).replace("{loc2X}", "" + loc2.getX()).replace("{loc2Y}", "" + loc2.getY()).replace("{loc2Z}", "" + loc1.getZ()));
			}
			player.sendRawMessage(" ");

			// List the generable structures FIXME: MAKE THIS DYNAMIC (!!!)
			player.sendRawMessage(ChatColor.GRAY + "   [1.] " + ChatColor.GOLD + "Invisible Wall");

			// Send intro footer
			player.sendRawMessage(" ");
			for(String string : English.ADMINISTRATION_STRUCTURE_CHOOSER_FOOTER.getLines())
			{
				player.sendRawMessage(string);
			}

			return "";
		}

		@Override
		protected boolean isInputValid(ConversationContext context, String message)
		{
			return "menu".equalsIgnoreCase(message) || "1".equalsIgnoreCase(message);
		}

		@Override
		protected Prompt acceptValidatedInput(ConversationContext context, String message)
		{
			// Define variables
			Player player = (Player) context.getForWhom();

			if("menu".equalsIgnoreCase(message))
			{
				// Return to main menu
				return new StartAdministration();
			}
			else if("1".equalsIgnoreCase(message))
			{
				// FIXME: This is where a wall would be generated, however I'm sleepy and am confused on how to access the InvisibleWall to even make a new one... So it's like 99% done.

				// Save success notification
				saveNotification(context, StructureWand.DATA_NAME, English.ADMINISTRATION_STRUCTURE_GENERATED.getLine());

				// Clear saved data
				context.setSessionData("structurewand_loc1", null);
				context.setSessionData("structurewand_loc2", null);

				// Return to main structure wand menu
				return new StructureWand();
			}

			// In case something goes wrong, go back to choice menu
			return new StructureChooser();
		}
	}

	public static class Listener implements org.bukkit.event.Listener
	{
		@EventHandler(priority = EventPriority.MONITOR)
		private void onStructureWand(PlayerInteractEvent event)
		{
			// Check some requirements
			if(event.getClickedBlock() == null || Zones.inNoDemigodsZone(event.getPlayer().getLocation()) || !Admins.structureWandEnabled(event.getPlayer())) return;

			// All good, handle the wand
			Player player = event.getPlayer();

			// Grab the context
			ConversationContext context = DPlayer.Util.getAdministrationContext(player);

			// Save the blocks
			if(event.getAction() == Action.LEFT_CLICK_BLOCK)
			{
				Location location = event.getClickedBlock().getLocation();

				context.setSessionData("structurewand_loc1", location);
				player.sendRawMessage(English.ADMINISTRATION_STRUCTUREWAND_LEFT.getLine().replace("{x}", "" + location.getX()).replace("{y}", "" + location.getY()).replace("{z}", "" + location.getZ()));
				player.sendRawMessage(" ");
			}
			else if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				Location location = event.getClickedBlock().getLocation();

				context.setSessionData("structurewand_loc2", location);
				player.sendRawMessage(English.ADMINISTRATION_STRUCTUREWAND_RIGHT.getLine().replace("{x}", "" + location.getX()).replace("{y}", "" + location.getY()).replace("{z}", "" + location.getZ()));
				player.sendRawMessage(" ");
			}

			// Cancel the event
			event.setCancelled(true);
		}

		@EventHandler(priority = EventPriority.MONITOR)
		private void onPlayerMove(PlayerMoveEvent event)
		{
			// Define variables
			Player player = event.getPlayer();

			// Remind them that they're administrating
			if(DPlayer.Util.isAdministrating(player) && System.currentTimeMillis() % 4000 < 1000)
			{
				player.sendMessage(English.ADMINISTRATION_STILL_IN_MENU.getLine());
			}
		}
	}
}
