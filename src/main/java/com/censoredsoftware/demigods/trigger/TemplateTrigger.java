package com.censoredsoftware.demigods.trigger;

public class TemplateTrigger implements Trigger
{
	public static TemplateTrigger trigger;

	static
	{
		trigger = new TemplateTrigger();
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
