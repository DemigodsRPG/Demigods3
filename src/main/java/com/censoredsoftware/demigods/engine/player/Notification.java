package com.censoredsoftware.demigods.engine.player;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.engine.data.DataManager;
import com.censoredsoftware.demigods.engine.language.Translation;
import com.google.common.collect.Sets;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Notification implements ConfigurationSerializable
{
	private UUID id;
	private long expiration;
	private UUID receiver;
	private UUID sender;
	private String senderType;
	private String danger;
	private String name;
	private String message;

	public Notification()
	{}

	public Notification(UUID id, ConfigurationSection conf)
	{
		this.id = id;
		expiration = conf.getLong("expiration");
		receiver = UUID.fromString(conf.getString("receiver"));
		sender = UUID.fromString(conf.getString("sender"));
		senderType = conf.getString("senderType");
		danger = conf.getString("danger");
		name = conf.getString("name");
		message = conf.getString("message");
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("expiration", expiration);
		map.put("receiver", receiver.toString());
		map.put("sender", sender.toString());
		map.put("senderType", senderType);
		map.put("danger", danger);
		map.put("name", name);
		map.put("message", message);
		return map;
	}

	public void generateId()
	{
		id = UUID.randomUUID();
	}

	void setExpiration(int minutes)
	{
		this.expiration = System.currentTimeMillis() + (minutes * 60000);
	}

	void setDanger(Danger danger)
	{
		this.danger = danger.name();
	}

	void setSenderType(Sender senderType)
	{
		this.senderType = senderType.name();
	}

	void setSender(DCharacter sender)
	{
		this.sender = sender.getId();
	}

	void setReceiver(DCharacter receiver)
	{
		this.receiver = receiver.getId();
	}

	void setName(String name)
	{
		this.name = name;
	}

	void setMessage(String message)
	{
		this.message = message;
	}

	public long getExpiration()
	{
		return this.expiration;
	}

	public Sender getSenderType()
	{
		return Sender.valueOf(this.senderType);
	}

	public Danger getDanger()
	{
		return Danger.valueOf(this.danger);
	}

	public DCharacter getReceiver()
	{
		return DCharacter.Util.load(this.receiver);
	}

	public DCharacter getSender()
	{
		return DCharacter.Util.load(this.sender);
	}

	public String getName()
	{
		return this.name;
	}

	public String getMessage()
	{
		return this.message;
	}

	public UUID getId()
	{
		return this.id;
	}

	public boolean hasExpiration()
	{
		return this.expiration != 0L;
	}

	public static void remove(Notification notification)
	{
		DataManager.notifications.remove(notification.getId());
	}

	public static Set<Notification> loadAll()
	{
		return Sets.newHashSet(DataManager.notifications.values());
	}

	public static class Util
	{
		public static void save(Notification notification)
		{
			DataManager.notifications.put(notification.getId(), notification);
		}

		public static Notification load(UUID id)
		{
			return DataManager.notifications.get(id);
		}

		public static Notification create(Sender sender, DCharacter receiver, Danger danger, String name, String message)
		{
			Notification notification = new Notification();
			notification.generateId();
			notification.setReceiver(receiver);
			notification.setDanger(danger);
			notification.setSenderType(sender);
			notification.setName(name);
			notification.setMessage(message);
			save(notification);
			return notification;
		}

		public static Notification create(Sender sender, DCharacter receiver, Danger danger, int minutes, String name, String message)
		{
			Notification notification = create(sender, receiver, danger, name, message);
			notification.generateId();
			notification.setExpiration(minutes);
			save(notification);
			return notification;
		}

		public static Notification create(DCharacter sender, DCharacter receiver, Danger danger, String name, String message)
		{
			Notification notification = create(Sender.CHARACTER, receiver, danger, name, message);
			notification.generateId();
			notification.setSender(sender);
			save(notification);
			return notification;
		}

		public static Notification create(DCharacter sender, DCharacter receiver, Danger danger, int minutes, String name, String message)
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
					Notification.remove(notification);
					notification.getReceiver().getMeta().removeNotification(notification);
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
				for(String message : Demigods.LANGUAGE.getTextBlock(Translation.Text.NOTIFICATION_RECEIVED))
				{
					player.sendMessage(message);
				}
			}
		}
	}

	public enum Sender
	{
		PLUGIN, QUEST, CHARACTER
	}

	public enum Danger
	{
		GOOD, NEUTRAL, BAD
	}
}
