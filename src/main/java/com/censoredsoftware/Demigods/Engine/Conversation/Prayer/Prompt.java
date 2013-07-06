package com.censoredsoftware.Demigods.Engine.Conversation.Prayer;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ValidatingPrompt;

public class Prompt extends ValidatingPrompt
{
	@Override
	public String getPromptText(ConversationContext context)
	{
		return "Here's what you said: " + context.getSessionData("poop");
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String arg)
	{
		context.setSessionData("poop", arg);
		// TODO return new Test();
		return this;
	}

	@Override
	protected boolean isInputValid(ConversationContext context, String arg)
	{
		// TODO
		return true;
	}
}
