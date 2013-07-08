package com.censoredsoftware.Demigods.Engine.Conversation;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
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
		CREATE_CHARACTER(1, new CreateCharacter());

		private Integer id;
		private MenuCategory category;

		private Category(int id, MenuCategory category)
		{
			this.id = id;
			this.category = category;
		}

		public Integer getId()
		{
			return this.id;
		}

		public MenuCategory getCategory()
		{
			return this.category;
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
		prayerConversation = Demigods.conversation.withEscapeSequence("/exit").withFirstPrompt(new StartPrayer()).buildConversation(player);
		prayerConversation.begin();
	}

	class StartPrayer extends ValidatingPrompt
	{
		@Override
		public String getPromptText(ConversationContext context)
		{
			// Define variables
			Player player = (Player) context.getForWhom();
			PlayerCharacter currentCharacter = PlayerWrapper.getPlayer(player).getCurrent();

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
				if(category.getCategory().canUse(player)) player.sendRawMessage(ChatColor.GRAY + "   [" + category.getId() + ".] " + category.getCategory().getChatName());
			}

			return "";
		}

		@Override
		protected boolean isInputValid(ConversationContext context, String arg)
		{
			for(Category category : Category.values())
			{
				return category.getFromId(Integer.parseInt(arg)) != null;
			}
			return false;
		}

		@Override
		protected StartPrayer acceptValidatedInput(ConversationContext context, String arg)
		{
			for(Category category : Category.values())
			{
				if(category.getFromId(Integer.parseInt(arg)) != null) ;
			}
			return new StartPrayer();
		}
	}

	// Character Creation
	class CreateCharacter extends ValidatingPrompt implements MenuCategory
	{
		@Override
		public String getChatName()
		{
			return ChatColor.GREEN + "Create Character";
		}

		@Override
		public boolean canUse(Player player)
		{
			return true;
		}

		@Override
		public String getPromptText(ConversationContext context)
		{
			return ChatColor.AQUA + "You are now creating a character. Are you sure you want to continue? " + ChatColor.GRAY + "(y/n)";
		}

		@Override
		protected boolean isInputValid(ConversationContext context, String message)
		{
			return message.equalsIgnoreCase("y") || message.equalsIgnoreCase("n");
		}

		@Override
		protected Prompt acceptValidatedInput(ConversationContext context, String message)
		{
			if(message.equalsIgnoreCase("y"))
			{
				return new ChooseName();
			}
			else
			{
				return new StartPrayer();
			}
		}

		class ChooseName extends ValidatingPrompt
		{
			@Override
			public String getPromptText(ConversationContext context)
			{
				return null;
			}

			@Override
			protected boolean isInputValid(ConversationContext context, String name)
			{
				if(name.length() <= 15 && StringUtils.isAlphanumeric(name) && !PlayerWrapper.hasCharName((Player) context.getSessionData("player"), name) && !MiscUtility.hasCapitalLetters(name, Demigods.config.getSettingInt("character.max_caps_in_name")))
				{
					return true;
				}
				else return false;
			}

			@Override
			protected CreateCharacter acceptValidatedInput(ConversationContext context, String arg)
			{
				return new CreateCharacter();
			}

		}
	}

	// Don't touch these things
	public interface MenuCategory extends Prompt
	{
		public String getChatName();

		public boolean canUse(Player player);
	}
}
