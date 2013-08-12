package com.censoredsoftware.demigods.conversation;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.censoredsoftware.demigods.Demigods;
import com.google.common.collect.Lists;

public class ChatRecorder implements Listener
{
	private boolean recording;
	private ArrayList<String> lines;
	private Player listener;

	public void start(Player player)
	{
		recording = true;
		listener = player;
		lines = Lists.newArrayList();
	}

	public ArrayList<String> stop()
	{
		recording = false;
		return lines;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	private void onChatEvent(AsyncPlayerChatEvent event)
	{
		if(recording && event.getRecipients().contains(listener))
		{
			lines.add(event.getFormat());
		}

		Demigods.message.broadcast("heard dat!"); // TODO
	}

	public static class Util
	{
		public static ChatRecorder startRecording(Player player)
		{
			ChatRecorder recorder = new ChatRecorder();
			recorder.start(player);
			return recorder;
		}
	}
}
