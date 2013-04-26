package com.censoredsoftware.Demigods.Objects;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

import com.censoredsoftware.Demigods.Listener.DPlayerListener;

public class DisconnectReasonFilter implements Filter
{
	public DisconnectReasonFilter()
	{}

	public boolean isLoggable(LogRecord arg0)
	{
		if(arg0.getMessage().toLowerCase().contains("disconnect"))
		{
			DPlayerListener.filterCheckGeneric = false;
			DPlayerListener.filterCheckStream = false;
			DPlayerListener.filterCheckOverflow = false;
			DPlayerListener.filterCheckTimeout = false;

			if(arg0.getMessage().toLowerCase().contains("genericreason"))
			{
				DPlayerListener.filterCheckGeneric = true;
				return true;
			}
			if(arg0.getMessage().toLowerCase().contains("endofstream"))
			{
				DPlayerListener.filterCheckStream = true;
				return true;
			}
			if(arg0.getMessage().toLowerCase().contains("overflow"))
			{
				DPlayerListener.filterCheckOverflow = true;
				return true;
			}
			if(arg0.getMessage().toLowerCase().contains("timeout"))
			{
				DPlayerListener.filterCheckTimeout = true;
				return true;
			}
			if(arg0.getMessage().toLowerCase().contains("quitting"))
			{
				DPlayerListener.filterCheckQuitting = true;
				return true;
			}
			return true;
		}
		return true;
	}
}
