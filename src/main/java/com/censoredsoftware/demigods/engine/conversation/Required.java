package com.censoredsoftware.demigods.engine.conversation;

import com.censoredsoftware.core.improve.ListedConversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

public enum Required implements ListedConversation.ConversationData
{
    // NoGrief
    PRAYER(new Prayer());

    private final ListedConversation conversationInfo;

    private Required(ListedConversation conversationInfo)
    {
        this.conversationInfo = conversationInfo;
    }

    public ListedConversation getConversation()
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