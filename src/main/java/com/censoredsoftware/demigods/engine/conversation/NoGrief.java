package com.censoredsoftware.demigods.engine.conversation;

import com.censoredsoftware.core.improve.ListedConversation;
import org.bukkit.event.Listener;

public class NoGrief implements ListedConversation
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
