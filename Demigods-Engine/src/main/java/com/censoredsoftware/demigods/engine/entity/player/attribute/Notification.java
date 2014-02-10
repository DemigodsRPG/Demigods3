package com.censoredsoftware.demigods.engine.entity.player.attribute;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;
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
}
