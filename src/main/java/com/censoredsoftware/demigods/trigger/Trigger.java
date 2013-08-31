package com.censoredsoftware.demigods.trigger;

import com.censoredsoftware.demigods.trigger.balance.DivinityUnbalanced;
import com.censoredsoftware.demigods.trigger.balance.NewPlayerNeedsHelp;

public interface Trigger
{
	public boolean evaluate();

	public void processSync();

	public void processAsync();

	public static class Util
	{
		/**
		 * List of all triggers.
		 */
		public static Trigger[] getAll()
		{
			return new Trigger[] { DivinityUnbalanced.trigger, NewPlayerNeedsHelp.trigger };
		}
	}
}
