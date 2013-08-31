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
