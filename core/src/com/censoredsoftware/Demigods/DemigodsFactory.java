package com.censoredsoftware.Demigods;

import com.censoredsoftware.Objects.Character.Factories.PlayerCharacterClassFactory;
import com.censoredsoftware.Objects.Character.Factories.PlayerCharacterFactory;
import com.censoredsoftware.Objects.Character.Factories.PlayerCharacterInventoryFactory;
import com.censoredsoftware.Objects.Special.Factories.SpecialItemStackFactory;
import com.censoredsoftware.Objects.Special.Factories.SpecialLocationFactory;

// TODO The reason I'm using factories and passing in the instance into each character class is because
// TODO I'm going to make the DataModules have a .save() and .load() function inside of them that lets
// TODO them be saved and loaded on the fly.  This way we can save each player separately.

public class DemigodsFactory
{
	// Special
	public static SpecialLocationFactory specialLocationFactory;
	public static SpecialItemStackFactory specialItemStackFactory;

	// Character
	public static PlayerCharacterFactory playerCharacterFactory;
	public static PlayerCharacterClassFactory playerCharacterClassFactory;
	public static PlayerCharacterInventoryFactory playerCharacterInventoryFactory;

	DemigodsFactory(DemigodsPlugin instance)
	{
		// Special
		specialLocationFactory = new SpecialLocationFactory(instance);
		specialItemStackFactory = new SpecialItemStackFactory(instance);

		// Character
		playerCharacterFactory = new PlayerCharacterFactory(instance);
		playerCharacterInventoryFactory = new PlayerCharacterInventoryFactory(instance);
		playerCharacterClassFactory = new PlayerCharacterClassFactory(instance, Demigods.config.getSettingInt("caps.favor"));
	}
}
