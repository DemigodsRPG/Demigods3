package com.censoredsoftware.Demigods;

import com.censoredsoftware.Demigods.Demigod.DemigodFactory;

public class DemigodsFactory
{
	// Character
	public static DemigodFactory playerCharacterClassFactory;

	DemigodsFactory(DemigodsPlugin instance)
	{
		// Character
		playerCharacterClassFactory = new DemigodFactory(Demigods.config.getSettingInt("caps.favor"));
	}
}
