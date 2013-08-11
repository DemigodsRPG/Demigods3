package com.censoredsoftware.demigods.conversation;

import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Player;

import com.censoredsoftware.core.bukkit.ListedConversation;

public class Administration implements ListedConversation
{
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
		// TODO Everything.
	}
}
