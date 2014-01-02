package com.censoredsoftware.demigods.base;

import com.censoredsoftware.censoredlib.helper.WrappedConversation;
import com.censoredsoftware.demigods.engine.conversation.Administration;
import com.censoredsoftware.demigods.engine.conversation.Prayer;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

// Conversations
public enum DemigodsConversation
{
	PRAYER(new Prayer()), ADMINSTRATION(new Administration());

	private final WrappedConversation conversationInfo;

	private DemigodsConversation(WrappedConversation conversationInfo)
	{
		this.conversationInfo = conversationInfo;
	}

	public WrappedConversation getConversation()
	{
		return this.conversationInfo;
	}

	// Can't touch this. Naaaaaa na-na-na.. Ba-dum, ba-dum.
	public static interface Category extends Prompt
	{
		public String getChatName(ConversationContext context);

		public boolean canUse(ConversationContext context);
	}
}