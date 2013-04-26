package com.censoredsoftware.Demigods;

import com.censoredsoftware.Demigods.PlayerCharacter.PlayerCharacterClassFactory;

public class DemigodsFactory
{
	// Character
	public static PlayerCharacterClassFactory playerCharacterClassFactory;

	DemigodsFactory(DemigodsPlugin instance)
	{
		// Character
		playerCharacterClassFactory = new PlayerCharacterClassFactory(Demigods.config.getSettingInt("caps.favor"));
	}
}
