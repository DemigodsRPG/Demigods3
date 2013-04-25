package com.censoredsoftware.Demigods;

import com.censoredsoftware.Objects.Character.Factories.PlayerCharacterClassFactory;

// TODO The reason I'm using factories and passing in the instance into each character class is because
// TODO I'm going to make the DataModules have a .save() and .load() function inside of them that lets
// TODO them be saved and loaded on the fly.  This way we can save each player separately.

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
