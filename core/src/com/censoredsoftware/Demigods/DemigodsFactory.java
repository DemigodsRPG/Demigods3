package com.censoredsoftware.Demigods;

import com.censoredsoftware.Demigods.PlayerCharacter.PlayerCharacterFactory;

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
