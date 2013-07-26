package com.censoredsoftware.Demigods.Engine.Conversation;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.event.Listener;

import com.censoredsoftware.Demigods.Engine.Demigods;

public interface DConversation
{
	public Listener getUniqueListener();

	public enum Required implements Demigods.ListedConversation
	{
		// NoGrief
		PRAYER(new Prayer());

		private DConversation conversationInfo;

		private Required(DConversation conversationInfo)
		{
			this.conversationInfo = conversationInfo;
		}

		public DConversation getConversation()
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
}
