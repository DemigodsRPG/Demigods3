package com.demigodsrpg.demigods.engine.entity.player.attribute;

import com.demigodsrpg.demigods.engine.data.DataAccess;
import com.demigodsrpg.demigods.engine.data.IdType;
import com.demigodsrpg.demigods.engine.data.Register;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.demigodsrpg.demigods.engine.language.English;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Notification extends DataAccess<UUID, Notification>
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
	{
	}

	@Register(idType = IdType.UUID)
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
		Map<String, Object> map = new HashMap<>();
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

	public void setExpiration(int minutes)
	{
		this.expiration = System.currentTimeMillis() + (minutes * 60000);
	}

	public void setDanger(Danger danger)
	{
		this.danger = danger.name();
	}

	public void setSenderType(Sender senderType)
	{
		this.senderType = senderType.name();
	}

	public void setSender(UUID sender)
	{
		this.sender = getId();
	}

	public void setReceiver(UUID receiver)
	{
		this.receiver = receiver;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setMessage(String message)
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

	public UUID getReceiverId()
	{
		return this.receiver;
	}

	public UUID getSenderId()
	{
		return this.sender;
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

	public enum Sender
	{
		PLUGIN, QUEST, CHARACTER
	}

	public enum Danger
	{
		GOOD, NEUTRAL, BAD
	}

	private static final DataAccess<UUID, Notification> DATA_ACCESS = new Notification();

	public static Notification get(UUID id)
	{
		return DATA_ACCESS.getDirect(id);
	}

	public static Collection<Notification> all()
	{
		return DATA_ACCESS.allDirect();
	}

	public static Notification create(Notification.Sender sender, DemigodsCharacter receiver, Notification.Danger danger, String name, String message)
	{
		Notification notification = new Notification();
		notification.generateId();
		notification.setReceiver(receiver.getId());
		notification.setDanger(danger);
		notification.setSenderType(sender);
		notification.setName(name);
		notification.setMessage(message);
		notification.save();
		return notification;
	}

	public static Notification create(Notification.Sender sender, DemigodsCharacter receiver, Notification.Danger danger, int minutes, String name, String message)
	{
		Notification notification = create(sender, receiver, danger, name, message);
		notification.generateId();
		notification.setExpiration(minutes);
		notification.save();
		return notification;
	}

	public static Notification create(DemigodsCharacter sender, DemigodsCharacter receiver, Notification.Danger danger, String name, String message)
	{
		Notification notification = create(Notification.Sender.CHARACTER, receiver, danger, name, message);
		notification.generateId();
		notification.setSender(sender.getId());
		notification.save();
		return notification;
	}

	public static Notification create(DemigodsCharacter sender, DemigodsCharacter receiver, Notification.Danger danger, int minutes, String name, String message)
	{
		Notification notification = create(sender, receiver, danger, name, message);
		notification.generateId();
		notification.setExpiration(minutes);
		notification.save();
		return notification;
	}

	public static void updateNotifications()
	{
		for(Notification notification : all())
		{
			if(notification.getExpiration() <= System.currentTimeMillis())
			{
				notification.remove();
				DemigodsCharacter.get(notification.getReceiverId()).getMeta().removeNotification(notification);
			}
		}
	}

	public static void sendNotification(DemigodsCharacter character, Notification notification)
	{
		// Add the notification
		character.getMeta().addNotification(notification);

		// Message them if possible
		if(character.getBukkitOfflinePlayer().isOnline())
		{
			Player player = character.getBukkitOfflinePlayer().getPlayer();
			for(String message : English.NOTIFICATION_RECEIVED.getLines())
			{
				player.sendMessage(message);
			}
		}
	}
}
