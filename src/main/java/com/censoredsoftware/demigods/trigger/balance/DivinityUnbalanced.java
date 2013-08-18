package com.censoredsoftware.demigods.trigger.balance;

import com.censoredsoftware.demigods.trigger.Trigger;

public class DivinityUnbalanced implements Trigger
{
	public static DivinityUnbalanced trigger;

	static
	{
		trigger = new DivinityUnbalanced();
	}

	@Override
	public boolean evaluate()
	{
		// Condition here.
		return false;
	}

	public class Process implements Trigger.Process
	{
		@Override
		public void sync()
		{
			if(!evaluate()) return;
			// Sync methods here.
		}

		@Override
		public void async()
		{
			if(!evaluate()) return;
			// Thread-safe methods here.
		}
	}

	@Override
	public Process process()
	{
		return new Process();
	}
}
