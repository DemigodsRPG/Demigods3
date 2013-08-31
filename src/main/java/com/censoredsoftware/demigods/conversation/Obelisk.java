package com.censoredsoftware.demigods.conversation;

import com.censoredsoftware.demigods.helper.WrappedConversation;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Player;

public class Obelisk implements WrappedConversation
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
