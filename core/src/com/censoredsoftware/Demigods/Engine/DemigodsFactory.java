package com.censoredsoftware.Demigods.Engine;

import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacterFactory;

public class DemigodsFactory
{
	// Character
	public static PlayerCharacterFactory playerCharacterFactory;

	DemigodsFactory(DemigodsPlugin instance)
	{
		// Character
		playerCharacterFactory = new PlayerCharacterFactory(Demigods.config.getSettingInt("caps.favor"));
	}
}
