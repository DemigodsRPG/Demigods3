package com.censoredsoftware.why.engine.conversation;

import org.bukkit.event.Listener;

public class NoGrief implements DConversation
{
	// Define variables
	private static org.bukkit.conversations.Conversation noGriefConversation;

	@Override
	public Listener getUniqueListener()
	{
		return new NoGriefListener();
	}

	// TODO Everything.
}

class NoGriefListener implements Listener
{
	// TODO Everything.
}
