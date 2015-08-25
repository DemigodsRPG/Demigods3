package com.demigodsrpg.demigods.base;

import com.demigodsrpg.demigods.engine.conversation.Administration;
import com.demigodsrpg.demigods.engine.conversation.Prayer;
import com.demigodsrpg.demigods.engine.helper.ConversationManager;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

// Conversations
public enum DemigodsConversation {
    PRAYER(new Prayer()), ADMINSTRATION(new Administration());

    private final ConversationManager conversationInfo;

    private DemigodsConversation(ConversationManager conversationInfo) {
        this.conversationInfo = conversationInfo;
    }

    public ConversationManager getConversation() {
        return this.conversationInfo;
    }

    // Can't touch this. Naaaaaa na-na-na.. Ba-dum, ba-dum.
    public static interface Category extends Prompt {
        public String getChatName(ConversationContext context);

        public boolean canUse(ConversationContext context);
    }
}
