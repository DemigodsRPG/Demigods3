package com.censoredsoftware.demigods.trigger.balance;

import com.censoredsoftware.demigods.trigger.Trigger;

public class DivinityUnbalanced implements Trigger
{
	public static final DivinityUnbalanced trigger = new DivinityUnbalanced();

	// Example
	public boolean evaluate()
	{
		// Condition here.
		return false;
	}

	@Override
	public void processSync()
	{
		if(!evaluate()) return;
		// Sync methods here.
	}

	@Override
	public void processAsync()
	{
		if(!evaluate()) return;
		// Thread-safe methods here.
	}
}
