package com.censoredsoftware.Demigods.Engine.Conversation;

import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

import com.censoredsoftware.Demigods.Engine.Demigods;

public class Prayer
{
	public Prayer(Player player)
	{
		Conversation prayer = Demigods.conversation.withFirstPrompt(new StartPrayer()).withEscapeSequence("/exit").buildConversation(player);
		prayer.begin();
	}
}

class StartPrayer extends ValidatingPrompt
{
	@Override
	public String getPromptText(ConversationContext context)
	{
		return "Here's what you said: " + context.getSessionData("poop");
	}

	@Override
	protected StartPrayer acceptValidatedInput(ConversationContext context, String arg)
	{
		context.setSessionData("poop", arg);
		// TODO return new Test();
		return new StartPrayer();
	}

	@Override
	protected boolean isInputValid(ConversationContext context, String arg)
	{
		// TODO
		return true;
	}
}
