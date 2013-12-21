package com.censoredsoftware.demigods.greek.trigger;

import com.censoredsoftware.censoredlib.trigger.Trigger;
import com.censoredsoftware.demigods.greek.structure.Altar;

public class ProcessAltars implements Trigger
{
	@Override
	public void processSync()
	{
		// Update Atlars
		Altar.Util.generateAltars();
	}

	@Override
	public void processAsync()
	{
		// Process Atlars
		Altar.Util.processNewChunks();
	}
}
