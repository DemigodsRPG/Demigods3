package com.censoredsoftware.demigods.engine.data.wrap;

import com.censoredsoftware.censoredlib.data.player.Notification;
import com.censoredsoftware.demigods.engine.data.Data;
import com.censoredsoftware.demigods.engine.data.serializable.DCharacter;
import com.censoredsoftware.demigods.engine.language.English;
import com.google.common.collect.Sets;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class NotificationManager
{
	public static void remove(Notification notification)
	{
		Data.NOTIFICATION.remove(notification.getId());
	}

	public static Set<Notification> loadAll()
	{
		return Sets.newHashSet(Data.NOTIFICATION.values());
	}

	public static void save(Notification notification)
	{
		Data.NOTIFICATION.put(notification.getId(), notification);
	}

	public static Notification load(UUID id)
	{
		return Data.NOTIFICATION.get(id);
	}

	public static Notification create(Notification.Sender sender, DCharacter receiver, Notification.Danger danger, String name, String message)
	{
		Notification notification = new Notification();
		notification.generateId();
		notification.setReceiver(receiver.getId());
		notification.setDanger(danger);
		notification.setSenderType(sender);
		notification.setName(name);
		notification.setMessage(message);
		save(notification);
		return notification;
	}

	public static Notification create(Notification.Sender sender, DCharacter receiver, Notification.Danger danger, int minutes, String name, String message)
	{
		Notification notification = create(sender, receiver, danger, name, message);
		notification.generateId();
		notification.setExpiration(minutes);
		save(notification);
		return notification;
	}

	public static Notification create(DCharacter sender, DCharacter receiver, Notification.Danger danger, String name, String message)
	{
		Notification notification = create(Notification.Sender.CHARACTER, receiver, danger, name, message);
		notification.generateId();
		notification.setSender(sender.getId());
		save(notification);
		return notification;
	}

	public static Notification create(DCharacter sender, DCharacter receiver, Notification.Danger danger, int minutes, String name, String message)
	{
		Notification notification = create(sender, receiver, danger, name, message);
		notification.generateId();
		notification.setExpiration(minutes);
		save(notification);
		return notification;
	}

	public static void updateNotifications()
	{
		for(Notification notification : loadAll())
		{
			if(notification.getExpiration() <= System.currentTimeMillis())
			{
				remove(notification);
				DCharacter.Util.load(notification.getReceiverId()).getMeta().removeNotification(notification);
			}
		}
	}

	public static void sendNotification(DCharacter character, Notification notification)
	{
		// Add the notification
		character.getMeta().addNotification(notification);

		// Message them if possible
		if(character.getOfflinePlayer().isOnline())
		{
			Player player = character.getOfflinePlayer().getPlayer();
			for(String message : English.NOTIFICATION_RECEIVED.getLines())
			{
				player.sendMessage(message);
			}
		}
	}
}
