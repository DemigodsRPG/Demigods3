package com.censoredsoftware.demigods.helper;

import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public interface ListedConversation
{
	public Listener getUniqueListener();

	public Conversation startMenu(Player player);

	public static interface ConversationData
	{
		public ListedConversation getConversation();
	}
}
