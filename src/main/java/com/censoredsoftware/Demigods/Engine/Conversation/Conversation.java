package com.censoredsoftware.Demigods.Engine.Conversation;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.Conversation.ConversationInfo;

public enum Conversation implements Demigods.ListedConversation
{
	// Prayer
	PRAYER(new Prayer());

	private ConversationInfo conversationInfo;

	private Conversation(ConversationInfo conversationInfo)
	{
		this.conversationInfo = conversationInfo;
	}

	public ConversationInfo getConversation()
	{
		return this.conversationInfo;
	}
}
