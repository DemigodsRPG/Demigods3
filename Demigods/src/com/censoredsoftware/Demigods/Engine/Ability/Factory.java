package com.censoredsoftware.Demigods.Engine.Ability;

import com.censoredsoftware.Demigods.Engine.Demigods;

public class Factory
{
	public static Devotion createDevotion(Devotion.Type type)
	{
		Devotion devotion = new Devotion();
		devotion.setType(type);
		devotion.setLevel(Demigods.config.getSettingInt("character.defaults." + type.name().toLowerCase()));
		Devotion.save(devotion);
		return devotion;
	}
}
