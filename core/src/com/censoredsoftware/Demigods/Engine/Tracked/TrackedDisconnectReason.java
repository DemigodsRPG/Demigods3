package com.censoredsoftware.Demigods.Engine.Tracked;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

import com.censoredsoftware.Demigods.Engine.Listener.PlayerListener;

public class TrackedDisconnectReason implements Filter
{
	public TrackedDisconnectReason()
	{}

	public boolean isLoggable(LogRecord arg0)
	{
		if(arg0.getMessage().toLowerCase().contains("disconnect"))
		{
			PlayerListener.filterCheckGeneric = false;
			PlayerListener.filterCheckStream = false;
			PlayerListener.filterCheckOverflow = false;
			PlayerListener.filterCheckTimeout = false;

			if(arg0.getMessage().toLowerCase().contains("genericreason"))
			{
				PlayerListener.filterCheckGeneric = true;
				return true;
			}
			if(arg0.getMessage().toLowerCase().contains("endofstream"))
			{
				PlayerListener.filterCheckStream = true;
				return true;
			}
			if(arg0.getMessage().toLowerCase().contains("overflow"))
			{
				PlayerListener.filterCheckOverflow = true;
				return true;
			}
			if(arg0.getMessage().toLowerCase().contains("timeout"))
			{
				PlayerListener.filterCheckTimeout = true;
				return true;
			}
			if(arg0.getMessage().toLowerCase().contains("quitting"))
			{
				PlayerListener.filterCheckQuitting = true;
				return true;
			}
			return true;
		}
		return true;
	}
}
