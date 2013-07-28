package com.censoredsoftware.why.engine.player;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import org.bukkit.Bukkit;

public class QuitReasonHandler extends Handler
{
	private QuitReason latestQuit = QuitReason.QUITTING;

	public QuitReasonHandler()
	{
		Bukkit.getServer().getLogger().addHandler(this);
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
	public void publish(LogRecord record)
	{
		if(!record.getMessage().toLowerCase().contains("disconnect")) return;
		latestQuit = QuitReason.QUITTING;
		if(record.getMessage().toLowerCase().contains("genericreason")) latestQuit = QuitReason.GENERIC_REASON;
		else if(record.getMessage().toLowerCase().contains("spam")) latestQuit = QuitReason.SPAM;
		else if(record.getMessage().toLowerCase().contains("endofstream")) latestQuit = QuitReason.END_OF_STREAM;
		else if(record.getMessage().toLowerCase().contains("overflow")) latestQuit = QuitReason.OVERFLOW;
		else if(record.getMessage().toLowerCase().contains("timeout")) latestQuit = QuitReason.TIMEOUT;
	}

	@Override
	public void flush()
	{}

	@Override
	public void close() throws SecurityException
	{}

	public static enum QuitReason
	{
		GENERIC_REASON, SPAM, END_OF_STREAM, OVERFLOW, TIMEOUT, QUITTING
	}
}
