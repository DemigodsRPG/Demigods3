package com.censoredsoftware.demigods.engine.helper;

import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public interface WrappedConversation
{
	public Listener getUniqueListener();

	public Conversation startMenu(Player player);
}
