package com.censoredsoftware.Demigods.Engine.Object.General;

import java.util.List;
import java.util.Set;

import redis.clients.johm.*;

import com.google.common.base.Objects;

@Model
public class TimedData
{
	@Id
	private Long id;
	@Attribute
	@Indexed
	private String key;
	@Attribute
	@Indexed
	private String subKey;
	@Attribute
	@Indexed
	private String data;
	@Attribute
	@Indexed
	private String type;
	@Attribute
	@Indexed
	private long expiration;

	public void setKey(String key)
	{
		this.key = key;
	}

	public void setSubKey(String subKey)
	{
		this.subKey = subKey;
	}

	public void setData(Object data)
	{
		this.data = data.toString();
		this.type = data.getClass().getName();
	}

	public void setSeconds(Integer seconds)
	{
		this.expiration = System.currentTimeMillis() + (seconds * 1000);
	}

	public void setMinutes(Integer minutes)
	{
		this.expiration = System.currentTimeMillis() + (minutes * 60000);
	}

	public void setHours(Integer hours)
	{
		this.expiration = System.currentTimeMillis() + (hours * 3600000);
	}

	public Long getId()
	{
		return this.id;
	}

	public String getKey()
	{
		return this.key;
	}

	public String getSubKey()
	{
		return this.subKey;
	}

	public Object getData()
	{
		if(this.type.equalsIgnoreCase("string"))
		{
			return this.data;
		}
		if(this.type.equalsIgnoreCase("integer"))
		{
			return Integer.parseInt(this.data);
		}
		if(this.type.equalsIgnoreCase("boolean"))
		{
			return Boolean.parseBoolean(this.data);
		}
		return this.data;
	}

	public long getExpiration()
	{
		return this.expiration;
	}

	public static void save(TimedData data)
	{
		JOhm.save(data);
	}

	public void delete()
	{
		JOhm.delete(TimedData.class, getId());
	}

	public static TimedData get(Long id)
	{
		return JOhm.get(TimedData.class, id);
	}

	public static Set<TimedData> getAll()
	{
		return JOhm.getAll(TimedData.class);
	}

	public static TimedData find(String key, String subKey)
	{
		if(TimedData.findByKey(key) == null) return null;

		for(TimedData data : TimedData.findByKey(key))
		{
			if(data.getSubKey().equals(subKey)) return data;
		}

		return null;
	}

	public static List<TimedData> findByKey(String key)
	{
		return JOhm.find(TimedData.class, "key", key);
	}

	public static List<TimedData> findBySubKey(String key, String subKey)
	{
		return JOhm.find(TimedData.class, "subKey", subKey);
	}

	public static List<TimedData> findByExpiration(Long expiration)
	{
		return JOhm.find(TimedData.class, "expiration", expiration);
	}

	public static void remove(String key, String subKey)
	{
		if(find(key, subKey) != null) find(key, subKey).delete();
	}

	@Override
	public boolean equals(final Object obj)
	{
		if(this == obj) return true;
		if(obj == null || getClass() != obj.getClass()) return false;
		final TimedData other = (TimedData) obj;
		return Objects.equal(this.id, other.id) && Objects.equal(this.key, other.key) && Objects.equal(this.subKey, other.subKey) && Objects.equal(this.data, other.data);
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.id, this.key, this.subKey, this.data);
	}

	@Override
	public String toString()
	{
		return Objects.toStringHelper(this).add("id", this.id).add("key", this.key).add("subkey", this.subKey).add("data", this.data).toString();
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}
