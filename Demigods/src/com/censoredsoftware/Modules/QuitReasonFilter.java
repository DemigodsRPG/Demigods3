package com.censoredsoftware.Modules;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

import org.bukkit.Bukkit;

public class QuitReasonFilter implements Filter
{
	private QuitReason latestQuit = QuitReason.QUITTING;

	public QuitReasonFilter()
	{
		Bukkit.getServer().getLogger().setFilter(this);
	}

	/**
	 * Get the QuitReason of the last player to quit.
	 * 
	 * @return The last QuitReason.
	 */
	public QuitReason getLatestQuitReason()
	{
		return latestQuit;
	}

	@Override
	public boolean isLoggable(LogRecord record)
	{
		if(!record.getMessage().toLowerCase().contains("disconnect")) return true;
		latestQuit = QuitReason.QUITTING;
		if(record.getMessage().toLowerCase().contains("genericreason")) latestQuit = QuitReason.GENERIC_REASON;
		else if(record.getMessage().toLowerCase().contains("spam")) latestQuit = QuitReason.SPAM;
		else if(record.getMessage().toLowerCase().contains("endofstream")) latestQuit = QuitReason.END_OF_STREAM;
		else if(record.getMessage().toLowerCase().contains("overflow")) latestQuit = QuitReason.OVERFLOW;
		else if(record.getMessage().toLowerCase().contains("timeout")) latestQuit = QuitReason.TIMEOUT;
		return true;
	}

	public static enum QuitReason
	{
		GENERIC_REASON, SPAM, END_OF_STREAM, OVERFLOW, TIMEOUT, QUITTING
	}
}
