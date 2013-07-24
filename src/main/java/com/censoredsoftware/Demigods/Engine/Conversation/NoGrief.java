package com.censoredsoftware.Demigods.Engine.Conversation;

import org.bukkit.event.Listener;

import com.censoredsoftware.Demigods.Engine.Object.Conversation.ConversationInfo;

public class NoGrief implements ConversationInfo
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
