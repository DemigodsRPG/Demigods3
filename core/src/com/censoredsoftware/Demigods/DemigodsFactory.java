package com.censoredsoftware.Demigods;

import com.censoredsoftware.Demigods.PlayerCharacter.PlayerCharacterFactory;

public class DemigodsFactory
{
	// Character
	public static PlayerCharacterFactory playerCharacterClassFactory;

	DemigodsFactory(DemigodsPlugin instance)
	{
		// Character
		playerCharacterClassFactory = new PlayerCharacterFactory(Demigods.config.getSettingInt("caps.favor"));
	}
}
