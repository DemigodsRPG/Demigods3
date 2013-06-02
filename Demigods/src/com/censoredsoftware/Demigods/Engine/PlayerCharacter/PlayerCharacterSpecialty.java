package com.censoredsoftware.Demigods.Engine.PlayerCharacter;

import javax.persistence.Id;

import redis.clients.johm.Attribute;
import redis.clients.johm.Indexed;
import redis.clients.johm.Model;

import com.censoredsoftware.Demigods.Engine.Ability.Ability;

@Model
public class PlayerCharacterSpecialty
{
	@Id
	private Long id;
	@Attribute
	@Indexed
	private Ability.Type type;
	@Attribute
	private Integer exp;
	@Attribute
	private Integer level;

	void setType(Ability.Type type)
	{
		this.type = type;
	}

	void setExp(Integer exp)
	{
		this.exp = exp;
	}

	void setLevel(Integer level)
	{
		this.level = level;
	}

	public Ability.Type getType()
	{
		return this.type;
	}

	public Integer getExp()
	{
		return this.exp;
	}

	public Integer getLevel()
	{
		return this.level;
	}
}
