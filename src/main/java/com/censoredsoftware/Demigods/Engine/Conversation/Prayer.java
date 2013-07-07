package com.censoredsoftware.Demigods.Engine.Conversation;

import org.bukkit.ChatColor;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.Player.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.Object.Player.PlayerWrapper;
import com.censoredsoftware.Demigods.Engine.Utility.MiscUtility;

public class Prayer
{
	// Define variables
	public static Conversation prayerConversation;

	/**
	 * Defines categories that can be used during prayer.
	 */
	public enum Category
	{
		CREATE_CHARACTER(1, ChatColor.GREEN + "Create Character", new CreateCharacter()), VIEW_CHARACTERS(2, ChatColor.YELLOW + "View Characters", null), VIEW_WARPS(3, ChatColor.BLUE + "View Warps", null), VIEW_INVITES(4, "View Invites", null);

		private Integer id;
		private String name;
		private Prompt prompt;

		private Category(int id, String name, Prompt prompt)
		{
			this.id = id;
			this.name = name;
			this.prompt = prompt;
		}

		public Integer getId()
		{
			return this.id;
		}

		public String getName()
		{
			return this.name;
		}

		public Prompt getPrompt()
		{
			return this.prompt;
		}

		public Category getFromId(int id)
		{
			for(Category category : Category.values())
			{
				if(category.getId().equals(id)) return category;
			}
			return null;
		}
	}

	public Prayer(Player player)
	{
		prayerConversation = Demigods.conversation.withFirstPrompt(new StartPrayer()).withEscapeSequence("/exit").buildConversation(player);
		prayerConversation.begin();
	}

	class StartPrayer extends ValidatingPrompt
	{
		@Override
		public String getPromptText(ConversationContext context)
		{
			// Define main variables
			Player player = (Player) context.getForWhom();
			PlayerCharacter currentCharacter = PlayerWrapper.getPlayer(player).getCurrent();

			// Save variables for later
			context.setSessionData("chosen_deity", null);
			context.setSessionData("chosen_name", null);

			// Send Prayer menu
			MiscUtility.clearRawChat(player);
			player.sendRawMessage(ChatColor.AQUA + " -- Prayer Menu --------------------------------------");
			player.sendRawMessage(" ");
			player.sendRawMessage(ChatColor.GRAY + " While praying you are unable chat with players.");
			player.sendRawMessage(ChatColor.GRAY + " You can return to the main menu at anytime by typing \"menu\".");
			player.sendRawMessage(ChatColor.GRAY + " Walk away to stop praying.");
			player.sendRawMessage(" ");
			player.sendRawMessage(ChatColor.GRAY + " To begin, choose an option by entering it's number in the chat:");
			player.sendRawMessage(" ");

			for(Category category : Category.values())
			{
				player.sendRawMessage(ChatColor.GRAY + "   [" + category.getId() + ".] " + category.getName());
			}

			/*
			 * if(currentCharacter != null)
			 * {
			 * player.sendRawMessage(ChatColor.GRAY + "   [3.] " + ChatColor.BLUE + "View Warps");
			 * if(DemigodsLocation.hasInvites(currentCharacter)) player.sendRawMessage(ChatColor.GRAY + "   [4.] " + ChatColor.DARK_PURPLE + "View Invites");
			 * player.sendRawMessage(" ");
			 * player.sendRawMessage(ChatColor.GRAY + " Type" + ChatColor.YELLOW + " invite <character name> " + ChatColor.GRAY + "to invite another player here.");
			 * }
			 */
			player.sendRawMessage(" ");

			return "";
		}

		@Override
		protected boolean isInputValid(ConversationContext context, String arg)
		{
			// Make sure a valid category is selected

			return true;
		}

		@Override
		protected StartPrayer acceptValidatedInput(ConversationContext context, String arg)
		{
			// Determine which option they chose and redirect them

			context.setSessionData("poop", arg);

			return new StartPrayer();
		}
	}

	// Character Creation
	static class CreateCharacter extends StringPrompt
	{
		@Override
		public String getPromptText(ConversationContext context)
		{
			return null;
		}

		@Override
		public Prompt acceptInput(ConversationContext context, String s)
		{
			return null;
		}
	}
}
