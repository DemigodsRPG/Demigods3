package com.censoredsoftware.Demigods.Engine.Conversation;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.Conversation.ConversationInfo;

public enum Conversation implements Demigods.ListedConversation
{
	// NoGrief
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

	// Can't touch this. Naaaaaa na-na-na.. Ba-dum, ba-dum.
	public static interface Category extends Prompt
	{
		public String getChatName();

		public boolean canUse(ConversationContext context);
	}
}
